import json
import logging
from typing import Callable, Any

from pika.adapters.blocking_connection import BlockingChannel
from pika.spec import BasicProperties, Basic

log = logging.getLogger(__name__)


def get_listener(publish: Callable[[BlockingChannel, Any], None]
                 ) -> Callable[[BlockingChannel, Any, Any, Any], None]:
    def listen(channel: BlockingChannel,
               method: Basic.Deliver,
               properties: BasicProperties,
               body: Any) -> None:
        log.info(f"Incoming message: {body}")
        data = json.loads(body)
        publish(channel, data)

    return listen
