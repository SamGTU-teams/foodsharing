import json
import logging
from typing import Any, Callable

from pika.adapters.blocking_connection import BlockingChannel

log = logging.getLogger(__name__)


def get_publisher(exchange: str
                  ) -> Callable[[BlockingChannel, Any], None]:
    def publish(channel: BlockingChannel,
                body: Any) -> None:
        channel.basic_publish(exchange=exchange,
                              routing_key="",
                              body=json.dumps(body))
        log.info(f"Outgoing message: {body}")

    return publish
