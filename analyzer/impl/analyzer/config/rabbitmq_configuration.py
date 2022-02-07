import logging

from pika import PlainCredentials, ConnectionParameters
from pika.adapters.blocking_connection import BlockingChannel, \
    BlockingConnection
from pika.exchange_type import ExchangeType

log = logging.getLogger(__name__)


def create_connection(username: str,
                      password: str,
                      host: str,
                      port: int) -> BlockingConnection:
    credentials = PlainCredentials(username=username,
                                   password=password)

    connection_parameters = ConnectionParameters(host=host,
                                                 port=port,
                                                 credentials=credentials)

    log.info(f"Attempting to connect to: [%s:%s]", host, port)
    connection = BlockingConnection(connection_parameters)
    return connection


def consumer_declaration(channel: BlockingChannel,
                         exchange: str,
                         queue: str) -> None:
    log.info("Attempt to declare consumer exchange.")
    channel.exchange_declare(exchange=exchange,
                             exchange_type=ExchangeType.fanout,
                             durable=True)

    log.info("Attempt to declare queue.")
    channel.queue_declare(queue=queue,
                          durable=True)

    log.info("Attempt to bind queue.")
    channel.queue_bind(queue=queue,
                       exchange=exchange)


def producer_declaration(channel: BlockingChannel,
                         exchange: str) -> None:
    log.info("Attempt to declare producer exchange.")
    channel.exchange_declare(exchange=exchange,
                             exchange_type=ExchangeType.fanout,
                             durable=True)
