import json
import logging
import os

from bootstrap import *

log = logging.getLogger(__name__)

basedir = os.path.abspath(os.path.dirname(__file__))
resources_dir = os.path.join(basedir, "resources")

profiles = load_profiles()

bootstrap = load_yml_profiles(resources_dir, profiles)

configuration = fetch_config(
    uri=bootstrap['spring.cloud.config.uri'],
    name=bootstrap["spring.application.name"],
    profiles=profiles)

config = bootstrap.merge_configs(bootstrap, configuration)

log.debug("Full configuration: \n %s", json.dumps(config, indent=2))


class Config(object):
    SECRET_KEY = os.environ.get('SECRET_KEY') or 'secret_key'

    SPRING_APPLICATION_NAME = config["spring.application.name"]
    SPRING_CLOUD_CONFIG_URI = config["spring.cloud.config.uri"]

    DATASOURCE_HOST = config.get_string("spring.datasource.host", "localhost")
    DATASOURCE_PORT = config.get_int("spring.datasource.port", 5432)
    DATASOURCE_NAME = config.get_string("spring.datasource.name", "foodsharing")
    DATASOURCE_USER = config.get_string("spring.datasource.username",
                                        "postgres")
    DATASOURCE_PASS = config.get_string("spring.datasource.password",
                                        "postgres")
    DATASOURCE_DRIVER = config.get_string("spring.datasource.driver",
                                          "postgresql")

    RABBITMQ_HOST = config.get_string("spring.rabbitmq.host", "localhost")
    RABBITMQ_PORT = config.get_int("spring.datasource.port", 5432)
    RABBITMQ_PASS = config.get_string("spring.rabbitmq.host", "guest")
    RABBITMQ_USER = config.get_string("spring.rabbitmq.host", "guest")

    RABBITMQ_CONSUMER = {
        "EXCHANGE": config.get_string("spring.rabbitmq.consumer.exchange",
                                      "post-raw-exchange"),
        "QUEQUE": config.get_string("spring.rabbitmq.consumer.exchange",
                                    "post.raw.analyzer")
    }
    RABBITMQ_PRODUCER = {
        "EXCHANGE": config.get_string("spring.rabbitmq.producer.exchange",
                                      "post-ready-exchange")
    }

    SQLALCHEMY_DATABASE_URI = f"{DATASOURCE_DRIVER}://{DATASOURCE_USER}:{DATASOURCE_PASS}" + \
                              f"@{DATASOURCE_HOST}:{DATASOURCE_PORT}/{DATASOURCE_NAME}"
    SQLALCHEMY_TRACK_MODIFICATIONS = False
    SQLALCHEMY_ECHO = True

    OPENAPI_VERSION = '3.0.2'
