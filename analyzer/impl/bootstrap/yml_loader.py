import logging
import os
from typing import List

import yaml
from pyhocon import ConfigFactory

log = logging.getLogger(__name__)


def load_yml_profiles(path: str = "",
                      profiles: List[str] = None,
                      prefix: str ="bootstrap",
                      delimiter: str ="-",
                      fmt: str ="{prefix}{delimiter}{profile}.yml"):
    if profiles is None:
        profiles = list()
    if path:
        if not path.endswith("/"):
            path = f"{path}/"
        fmt = f"{path}{fmt}"

    paths = list(
        map(
            lambda profile: fmt.format(prefix=prefix, delimiter=delimiter,
                                       profile=profile),
            filter(lambda profile: profile != "default", profiles)
        )
    )
    paths.insert(0, fmt.format(prefix=prefix, delimiter="", profile=""))

    main_config = ConfigFactory.from_dict({})
    for config_path in paths:
        try:
            config = load_yml(config_path)
            main_config.merge_configs(main_config, config)
        except RuntimeError:
            pass

    return main_config


def load_yml(path: str):
    log.debug("Start loading: %s", path)
    if not os.path.exists(path):
        log.warning("File not exists: %s", path)
        raise RuntimeError(f"File not exists: ${path}")
    with open(path, 'r') as config_file:
        try:
            loaded = yaml.safe_load(config_file)
            config = ConfigFactory.from_dict(loaded)
            log.debug("Finish loading: %s", path)
        except yaml.YAMLError as exc:
            log.warning(exc_info=True)
            config = ConfigFactory.from_dict({})
    return config


if __name__ == "__main__":
    import json

    conf = load_yml_profiles(path="./resources",
                             profiles=["default", "docker", "test"])
    print(json.dumps(conf.as_plain_ordered_dict(), indent=2))
