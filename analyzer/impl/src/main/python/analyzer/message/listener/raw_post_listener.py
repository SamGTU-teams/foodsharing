import json
import logging

log = logging.getLogger(__name__)


def get_listener(publish):
    def listen(channel, method, properties, body):
        log.info(f"Incoming message: {body}")
        data = json.loads(body)
        publish(channel, data)

    return listen
