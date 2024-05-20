/**
 * Copyright (c) 2023 Osparks AMG Inc. All rights reserved.
 * <p>
 * All information and code contained herein is the property of Osparks AMG Inc.
 * <p>
 * Permission is granted to view this material for personal use only.
 * <p>
 * Any unauthorized modification, publication, distribution, sublicensing, or sale of this
 * material without written permission from Osparks AMG Inc. is strictly prohibited.
 * <p>
 * Employers who have received this software through a job application process
 * are granted full access to view, modify, distribute, and use the software for
 * evaluation purposes only.
 */

package com.osparks.vpin.bot.service;

import com.osparks.vpin.bot.jobs.BotJob;
import com.osparks.vpin.bot.model.BotFunction;
import com.osparks.vpin.bot.model.BotModel;
import com.osparks.vpin.bot.model.IntervalModel;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for scheduling bot actions.
 * 
 * @Author Christopher Leu (
 *         <a href= "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@Service
public class BotSchedulerService {
    private StdSchedulerFactory factory;
    private Scheduler scheduler;

    @PostConstruct
    public void init() throws SchedulerException {
        factory = new StdSchedulerFactory();
        scheduler = factory.getScheduler();
        scheduler.start();
    }

    @PreDestroy
    public void shutdown() throws SchedulerException {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    /**
     * Reschedules bot actions based on the provided bot model.
     *
     * @param botModel the bot model
     * @throws SchedulerException if an error occurs during scheduling
     */
    public void rescheduleBot(BotModel botModel) throws SchedulerException {
        for (IntervalModel interval : botModel.getIntervals()) {
            LocalDateTime nextActivationTime = calculateNextActivationTime(botModel, interval);
            LocalDateTime nextDeactivationTime = calculateNextDeactivationTime(botModel, interval);

            List<LocalTime> actionTimes = calculateDelaysForInterval(interval, interval.getActionsPerHour());
            for (LocalTime actionTime : actionTimes) {
                LocalDateTime dateTimeAction = LocalDateTime.of(nextActivationTime.toLocalDate(), actionTime);
                if (dateTimeAction.isAfter(LocalDateTime.now()) && dateTimeAction.isBefore(nextDeactivationTime)) {
                    scheduleBotAction(botModel, dateTimeAction);
                }
            }
        }
    }

    private LocalDateTime calculateNextActivationTime(BotModel botModel, IntervalModel interval) {
        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();

        if (now.isBefore(interval.getEndTime()) && now.isAfter(interval.getStartTime())) {
            return LocalDateTime.of(today, now);
        }

        List<IntervalModel> sortedIntervals = botModel.getIntervals().stream()
                .filter(i -> i.getStartTime().isAfter(now))
                .sorted(Comparator.comparing(IntervalModel::getStartTime))
                .collect(Collectors.toList());

        if (sortedIntervals.isEmpty()) {
            return LocalDateTime.of(today.plusDays(1), botModel.getIntervals().get(0).getStartTime());
        } else {
            return LocalDateTime.of(today, sortedIntervals.get(0).getStartTime());
        }
    }

    private LocalDateTime calculateNextDeactivationTime(BotModel botModel, IntervalModel interval) {
        return LocalDateTime.of(LocalDate.now(), interval.getEndTime());
    }

    private void scheduleBotAction(BotModel botModel, LocalDateTime actionTime) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(BotJob.class)
                .withIdentity("botJob_" + botModel.getId() + "_" + actionTime)
                .usingJobData("botId", botModel.getId())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("botTrigger_" + botModel.getId() + "_" + actionTime)
                .startAt(Date.from(actionTime.atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * Schedules bot actions based on the provided bot model.
     *
     * @param botModel the bot model
     * @throws SchedulerException if an error occurs during scheduling
     */
    public void scheduleBot(BotModel botModel) throws SchedulerException {
        for (IntervalModel interval : botModel.getIntervals()) {
            for (BotFunction function : botModel.getFunctions()) {
                List<LocalTime> actionTimes = calculateDelaysForInterval(interval, interval.getActionsPerHour());
                for (LocalTime actionTime : actionTimes) {
                    LocalDateTime dateTimeAction = LocalDateTime.of(LocalDate.now(), actionTime);
                    scheduleBotAction(botModel, dateTimeAction);
                }
            }
        }
    }

    /**
     * Unschedules bot actions based on the provided bot model.
     *
     * @param botModel the bot model
     * @throws SchedulerException if an error occurs during unscheduling
     */
    public void unscheduleBot(BotModel botModel) throws SchedulerException {
        for (IntervalModel interval : botModel.getIntervals()) {
            for (BotFunction function : botModel.getFunctions()) {
                scheduler.deleteJob(new JobKey("botJob_" + botModel.getId() + "_" + interval + "_" + function));
            }
        }
    }

    private List<LocalTime> calculateDelaysForInterval(IntervalModel interval, int actionsPerHour) {
        Random rand = new Random();
        int totalMinutes = calcIntervalDuration(interval);
        int averageDelay = totalMinutes / actionsPerHour;

        List<LocalTime> actionTimes = new ArrayList<>();
        LocalTime start = interval.getStartTime();

        for (int i = 0; i < actionsPerHour; i++) {
            int fluctuation = rand.nextInt(averageDelay);
            LocalTime actionTime = start.plusMinutes(((long) i * averageDelay) + fluctuation);

            if (actionTime.isBefore(interval.getEndTime()) || actionTime.equals(interval.getEndTime())) {
                actionTimes.add(actionTime);
            }
        }
        return actionTimes;
    }

    private int calcIntervalDuration(IntervalModel interval) {
        Duration duration = Duration.between(interval.getStartTime(), interval.getEndTime());
        return (int) duration.toMinutes();
    }
}