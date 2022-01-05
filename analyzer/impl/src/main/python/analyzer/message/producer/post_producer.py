import json
import logging

from analyzer.config.rabbitmq_config import producer_config

log = logging.getLogger(__name__)


def publish(channel, body):
    channel.basic_publish(exchange=producer_config["exchange"],
                          routing_key="",
                          body=json.dumps(body))
    log.info(f"Outgoing message: {body}")
