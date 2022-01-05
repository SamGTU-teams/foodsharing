import pika
from pika.exchange_type import ExchangeType

from .spring_configuration import config

rabbitmq_config = config["rabbitmq"]

_credentials = pika.PlainCredentials(rabbitmq_config["username"],
                                     rabbitmq_config["password"])
_connection_parameters = pika.ConnectionParameters(host=rabbitmq_config["host"],
                                                   port=rabbitmq_config["port"],
                                                   credentials=_credentials)

connection = pika.BlockingConnection(_connection_parameters)
channel = connection.channel()

# Consumer declaration
consumer_config = rabbitmq_config["consumer"]

channel.exchange_declare(exchange=consumer_config["exchange"],
                         exchange_type=ExchangeType.fanout,
                         durable=True)

channel.queue_declare(queue=consumer_config["queue"],
                      durable=True)

channel.queue_bind(queue=consumer_config["queue"],
                   exchange=consumer_config["exchange"])

# Producer declaration
producer_config = rabbitmq_config["producer"]
channel.exchange_declare(exchange=producer_config["exchange"],
                         exchange_type=ExchangeType.fanout,
                         durable=True)
