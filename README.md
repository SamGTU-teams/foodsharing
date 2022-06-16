# Foodsharing

## Requirements

- JDK 11
- Maven 3
- PostgreSQL 10+
- PostGis
- RabbitMQ
- Docker
- Docker-compose

## Preparements

Copy the file `.env.example` and rename it to `.env`.

Replace the properties in it with sensitive data such as:

- RABBITMQ_USER - string
- RABBITMQ_PASS - string
- POSTGRES_USER - string
- POSTGRES_PASS - string
- TG_BOT_TOKEN - string
- VK_BOT_ACCESS_TOKEN - string
- VK_CONFIRM_CODE - may be empty string

Other properties to customize the environment.

Telegram bot needs SSL certificate to work.

### Access tokens

- [Telegram](https://core.telegram.org/bots#creating-a-new-bot)
- [ВКонтакте](https://vk.com/dev.php?method=access_token) - only group access token

## Modules

- [Config Service](config-service) -  distributed configuration system
- [Parsers](parsers)
    - [API](parsers/api)
    - [VK-Parser](parsers/vk-parser/webhook-impl) - register ВКонтакте groups, parse wall on registered ВКонтакте groups
- [Analyzer](analyzer):
    - [API](analyzer/api)
    - [Impl](analyzer/impl) - gather products from request text and from wallposts
- [Bots](bots):
    - [IBot](bots/ibot)
        - [API](bots/ibot/api)
        - [Impl](bots/ibot/impl) - save the meta and received information from posts, return posts by criteria
    - [Session-starter](bots/session-starter) - spring module with dialogue engine
    - [TG-Bot](bots/tg-bot) - bot implementation for Telegram
    - [VK-Bot](bots/vk-bot) - bot implementation for ВКонтакте
- [Nginx](nginx) - Nginx configuration for routing request
- [Ad](ad):
    - [API](ad/api)
    - [Impl](ad/impl) - sends advertising integrations

## Build application

run shell script `build`

## Run application

### In development mode

run shell script `dev`

### In production mode

run shell script `prod`
