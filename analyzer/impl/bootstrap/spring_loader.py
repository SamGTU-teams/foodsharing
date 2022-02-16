import logging
from typing import List

from pyhocon import ConfigFactory, ConfigTree
from spring_config import ClientConfigurationBuilder
from spring_config.client import SpringConfigClient

log = logging.getLogger(__name__)


def fetch_config(uri: str,
                 name: str,
                 profiles: List[str],
                 delimiter: str = ",") -> ConfigTree:
    log.info(f"Fetching config from server at: %s", uri)
    client_configuration = ClientConfigurationBuilder() \
        .app_name(name) \
        .address(uri) \
        .profile(delimiter.join(profiles)) \
        .build()

    config_client = SpringConfigClient(client_configuration)
    configuration = ConfigFactory.from_dict(config_client.get_config())
    return configuration
