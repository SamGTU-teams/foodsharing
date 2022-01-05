from config.rabbitmq_config import channel, consumer_config
from message.listener.raw_post_listener import callback

channel.basic_consume(queue=consumer_config["queue"],
                      on_message_callback=callback,
                      auto_ack=True)

channel.start_consuming()
