import json
import logging

log = logging.getLogger(__name__)


def get_publisher(exchange):
    def publish(channel, body):
        channel.basic_publish(exchange=exchange,
                              routing_key="",
                              body=json.dumps(body))
        log.info(f"Outgoing message: {body}")

    return publish
