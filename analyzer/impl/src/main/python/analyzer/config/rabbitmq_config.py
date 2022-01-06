import logging

import pika
from pika.exchange_type import ExchangeType

log = logging.getLogger(__name__)


def create_connection(username, password, host, port):
    credentials = pika.PlainCredentials(username,
                                        password)

    connection_parameters = pika.ConnectionParameters(
        host=host,
        port=port,
        credentials=credentials)

    log.info(f"Attempting to connect to: [%s:%s]", host, port)
    connection = pika.BlockingConnection(connection_parameters)
    return connection


def consumer_declaration(channel, exchange, queue):
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


def producer_declaration(channel, exchange):
    log.info("Attempt to declare producer exchange.")
    channel.exchange_declare(exchange=exchange,
                             exchange_type=ExchangeType.fanout,
                             durable=True)
