import logging

from analyzer.config.rabbitmq_config import create_connection, \
    consumer_declaration, producer_declaration
from analyzer.config.spring_configuration import fetch_bootstrap, fetch_service
from analyzer.message.listener.raw_post_listener import get_listener
from analyzer.message.producer.post_producer import get_publisher

log = logging.getLogger(__name__)

bootstrap = fetch_bootstrap()
spring_config = bootstrap['spring']
remote_config = fetch_service(uri=spring_config['cloud']['config']['uri'],
                              application_name=spring_config["application"][
                                  "name"])

config = {**bootstrap, **remote_config}

rabbitmq_config = config['rabbitmq']
connection = create_connection(host=rabbitmq_config['host'],
                               port=rabbitmq_config['port'],
                               username=rabbitmq_config['username'],
                               password=rabbitmq_config['password'])
channel = connection.channel()

consumer_declaration(channel=channel,
                     exchange=rabbitmq_config['consumer']['exchange'],
                     queue=rabbitmq_config['consumer']['queue'])

producer_declaration(channel=channel,
                     exchange=rabbitmq_config['producer']['exchange'])

publisher = get_publisher(rabbitmq_config['producer']['exchange'])

listener = get_listener(publisher)

channel.basic_consume(queue=rabbitmq_config['consumer']['queue'],
                      on_message_callback=listener,
                      auto_ack=True)

channel.start_consuming()
