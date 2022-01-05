import logging

import pika
from pika.exchange_type import ExchangeType

from .spring_configuration import config

log = logging.getLogger(__name__)

rabbitmq_config = config["rabbitmq"]

_credentials = pika.PlainCredentials(rabbitmq_config["username"],
                                     rabbitmq_config["password"])

_connection_parameters = pika.ConnectionParameters(host=rabbitmq_config["host"],
                                                   port=rabbitmq_config["port"],
                                                   credentials=_credentials)

log.info(f"Attempting to connect to: ["
         f"{rabbitmq_config['host']}:"
         f"{rabbitmq_config['port']}]")
connection = pika.BlockingConnection(_connection_parameters)
channel = connection.channel()

# Consumer declaration
consumer_config = rabbitmq_config["consumer"]

log.info("Attempt to declare consumer exchange.")
channel.exchange_declare(exchange=consumer_config["exchange"],
                         exchange_type=ExchangeType.fanout,
                         durable=True)

log.info("Attempt to declare queue.")
channel.queue_declare(queue=consumer_config["queue"],
                      durable=True)

log.info("Attempt to bind queue.")
channel.queue_bind(queue=consumer_config["queue"],
                   exchange=consumer_config["exchange"])

# Producer declaration
producer_config = rabbitmq_config["producer"]

log.info("Attempt to declare producer exchange.")
channel.exchange_declare(exchange=producer_config["exchange"],
                         exchange_type=ExchangeType.fanout,
                         durable=True)
