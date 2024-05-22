# Vpin Automated Content System

This repository contains selected files from a Java-based microservice developed as a proof of concept (POC) for Vpin, an AI-powered multimedia collection, organization, and sharing platform.

These files are part of a larger proof-of-concept project, and some files have been modified to remove sensitive information. This repository is not intended to be a fully compilable project but rather a showcase of my work during my time at OSparks AMG Inc.

## Overview

### Initial Proof of Concept
The initial proof of concept (POC) involved generating bots with diverse characteristics and interests using OpenAI. These bots interacted with and posted content, simulating various user behaviors based on randomly generated profiles. This POC could have been used for load testing or other purposes. The project later shifted focus to developing an automated system to curate and post content at scheduled times.

### Features
This project showcases the integration of various technologies and services, including:
- **RESTful APIs**: Developed APIs using Spring Boot to handle bot operations and user interactions.
- **Integration**: Integrated OpenAI for content generation and YouTube APIs for multimedia procurement.
- **Core Services**: Implemented core services following MVC architecture and OOP principles.
- **Command Patterns**: Utilized command patterns for modular and streamlined bot creation and recommendation behaviors.
- **Database Design and Implementation**: Designed and implemented the initial database schema, including the determination of nodes, relationships, interactions to track, and more.

## Key Files and Their Roles

### Behavior
- **BrowseVpinBehavior.java**: Implements the behavior for browsing Vpins.

### Command
- **BotCommand.java**: Base class for bot commands.
- **CommentVpinCommand.java**: Command for commenting on a Vpin.
- **CreateVpinCommand.java**: Command for creating a Vpin.
- **LikeCommand.java**: Command for liking a Vpin.
- **ReplyCommentCommand.java**: Command for replying to a comment.

### Controller
- **BotController.java**: Handles bot-related operations.
- **VpinController.java**: Manages operations related to Vpins.

### DTO (Data Transfer Objects)
- **BotDashFormData.java**: DTO for bot dashboard form data.
- **CreateVpinRequest.java**: DTO for creating a Vpin request.
- **VpinCreationResponse.java**: DTO for Vpin creation response.

### Model
- **BotFunction.java**: Represents a function of a bot.
- **BotModel.java**: Represents the bot entity.
- **CommentModel.java**: Represents a comment entity.
- **RepliedToRelationship.java**: Represents the relationship of a bot replying to a comment.
- **TagModel.java**: Represents a tag.
- **VpinModel.java**: Represents a Vpin entity.

### Repository
- **BotRepository.java**: Repository for bot entities.
- **VpinRepository.java**: Repository for Vpin entities.

### Service
- **BotActionExecutor.java**: Executes bot actions.
- **BotCommandService.java**: Manages bot commands.
- **BotCreateService.java**: Handles bot creation.
- **BotSchedulerService.java**: Schedules bot actions.
- **OpenAIService.java**: Integrates with OpenAI for content generation.
- **RemoteExchangeService.java**: Manages remote data exchanges.
- **VpinAuthenticationService.java**: Handles authentication for Vpins.
- **VpinService.java**: Manages Vpin-related operations.

### Other
- **VpinBotApplication.java**: Main application class.
- **pom.xml**: Maven project configuration file.
- **.gitignore**: Git ignore file to exclude certain files from the repository.
- **LICENSE**: License file for the project.
- **README.md**: Project documentation.

## License

This project is licensed under a custom license. See the [LICENSE](LICENSE) file for details.

## Contact

For any inquiries or access requests, please contact cl3880@nyu.edu.
