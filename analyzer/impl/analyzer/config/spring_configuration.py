import logging
import os
from typing import List

import yaml
from pyhocon import ConfigFactory, ConfigTree
from spring_config import ClientConfigurationBuilder
from spring_config.client import SpringConfigClient

log = logging.getLogger(__name__)


def load_profiles() -> List[str]:
    profiles = os.environ.get('spring_profiles_active') or 'default'
    log.info("Active profiles: %s", profiles)
    profiles = list(profiles.split(','))
    return profiles


def fetch_bootstrap(bootstrap_path: str,
                    profiles: List[str]) -> ConfigTree:
    configuration = ConfigFactory.from_dict({})

    log.info("Fetching config from bootstrap.")
    with open(bootstrap_path, "r") as stream:
        try:
            loaded = yaml.safe_load_all(stream)
            configs = list(map(lambda l: ConfigFactory.from_dict(l), loaded))
            for config in configs:
                profile = config.get_string(
                    'spring.config.activate.on-profile', None)

                if profile is None or profile in profiles:
                    configuration = configuration.merge_configs(configuration,
                                                                config)
        except yaml.YAMLError as exc:
            log.error(exc)
    return configuration


def fetch_service(uri: str,
                  name: str,
                  profiles: List[str]) -> ConfigTree:
    log.info(f"Fetching config from server at: %s", uri)
    client_configuration = ClientConfigurationBuilder() \
        .app_name(name) \
        .address(uri) \
        .profile(','.join(profiles)) \
        .build()

    config_client = SpringConfigClient(client_configuration)
    configuration = ConfigFactory.from_dict(config_client.get_config())
    return configuration
