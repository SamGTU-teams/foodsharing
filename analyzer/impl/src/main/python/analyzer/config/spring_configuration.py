import logging
import os

import yaml
from spring_config import ClientConfigurationBuilder
from spring_config.client import SpringConfigClient

log = logging.getLogger(__name__)


def fetch_bootstrap():
    config = {}
    log.info("Fetching config from bootstrap.")
    with open(f"{os.path.dirname(__file__)}/../../resources/bootstrap.yml",
              "r") as stream:
        try:
            config.update(yaml.safe_load(stream))
        except yaml.YAMLError as exc:
            log.error(exc)
    return config


def fetch_service(uri, application_name):
    log.info(f"Fetching config from server at: %s", uri)
    client_configuration = ClientConfigurationBuilder() \
        .app_name(application_name) \
        .address(uri) \
        .build()

    config_client = SpringConfigClient(client_configuration)

    return {**config_client.get_config()}
