import logging

from config.logger_configuration import load_logging
from config.rabbitmq_configuration import create_connection, \
    consumer_declaration, producer_declaration
from config.spring_configuration import load_profiles, fetch_bootstrap, \
    fetch_service
from message.listener.raw_post_listener import get_listener
from message.producer.post_producer import get_publisher

load_logging()

log = logging.getLogger(__name__)

profiles = load_profiles()

bootstrap = fetch_bootstrap(bootstrap_path="resources/bootstrap.yml",
                            profiles=profiles)

remote_config = fetch_service(uri=bootstrap['spring.cloud.config.uri'],
                              name=bootstrap["spring.application.name"],
                              profiles=profiles)

config = bootstrap.merge_configs(bootstrap, remote_config)

connection = create_connection(host=config['spring.rabbitmq.host'],
                               port=config['spring.rabbitmq.port'],
                               username=config['spring.rabbitmq.username'],
                               password=config['spring.rabbitmq.password'])
channel = connection.channel()

consumer_declaration(channel=channel,
                     exchange=config['spring.rabbitmq.consumer.exchange'],
                     queue=config['spring.rabbitmq.consumer.queue'])

producer_declaration(channel=channel,
                     exchange=config['spring.rabbitmq.producer.exchange'])

publisher = get_publisher(config['spring.rabbitmq.producer.exchange'])

listener = get_listener(publisher)

log.info("Binding consumer to queue: %s.",
         config['spring.rabbitmq.consumer.queue'])
channel.basic_consume(queue=config['spring.rabbitmq.consumer.queue'],
                      on_message_callback=listener,
                      auto_ack=True)

log.info("Start consuming.")
channel.start_consuming()
