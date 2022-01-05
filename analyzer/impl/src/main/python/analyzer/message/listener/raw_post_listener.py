import json
import logging

from ..producer.post_producer import publish

log = logging.getLogger(__name__)


def callback(channel, method, properties, body):
    log.info(f"Incoming message: {body}")
    data = json.loads(body)
    publish(channel, data)
