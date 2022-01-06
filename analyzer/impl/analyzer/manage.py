import logging

from config.rabbitmq_configuration import create_connection, \
    consumer_declaration, producer_declaration
from config.spring_configuration import load_profiles, fetch_bootstrap, \
    fetch_service
from message.listener.raw_post_listener import get_listener
from message.producer.post_producer import get_publisher

log = logging.getLogger(__name__)

profiles = load_profiles()
print(profiles)

bootstrap = fetch_bootstrap(bootstrap_path="resources/bootstrap.yml",
                            profiles=profiles)

remote_config = fetch_service(uri=bootstrap['spring.cloud.config.uri'],
                              name=bootstrap["spring.application.name"],
                              profiles=profiles)

config = bootstrap.merge_configs(bootstrap, remote_config)

connection = create_connection(host=config['rabbitmq.host'],
                               port=config['rabbitmq.port'],
                               username=config['rabbitmq.username'],
                               password=config['rabbitmq.password'])
channel = connection.channel()

consumer_declaration(channel=channel,
                     exchange=config['rabbitmq.consumer.exchange'],
                     queue=config['rabbitmq.consumer.queue'])

producer_declaration(channel=channel,
                     exchange=config['rabbitmq.producer.exchange'])

publisher = get_publisher(config['rabbitmq.producer.exchange'])

listener = get_listener(publisher)

channel.basic_consume(queue=config['rabbitmq.consumer.queue'],
                      on_message_callback=listener,
                      auto_ack=True)

channel.start_consuming()
