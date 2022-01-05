import yaml
import os

from spring_config import ClientConfigurationBuilder
from spring_config.client import SpringConfigClient

config = {}

with open(f"{os.path.dirname(__file__)}/../../resources/bootstrap.yml",
          "r") as stream:
    try:
        config.update(yaml.safe_load(stream))
    except yaml.YAMLError as exc:
        print(exc)

_spring = config["spring"]

_client_configuration = ClientConfigurationBuilder() \
    .app_name(_spring["application"]["name"]) \
    .address(_spring["cloud"]["config"]["uri"]) \
    .build()

_config_client = SpringConfigClient(_client_configuration)

config.update(_config_client.get_config())
