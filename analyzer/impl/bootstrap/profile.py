import logging
import os

log = logging.getLogger(__name__)


def load_profiles(env_name: str = "SPR_PROFILE",
                  default: str = "default",
                  delimiter: str = ","):
    profiles = os.getenv(env_name)
    if not profiles:
        profiles = default
        log.info("No active profiles installed. Use default profile: %s",
                 profiles)
    profiles = profiles.split(delimiter)
    profiles = list(filter(lambda profile: profile, profiles))
    log.info("Active profiles: %s", profiles)
    return profiles
