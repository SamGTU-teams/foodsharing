import json

from config.rabbitmq_config import producer_config

def publish(channel, body):
    channel.basic_publish(exchange=producer_config["exchange"],
                          routing_key="",
                          body=json.dumps(body))
    print(f"Outgoing message: {body}")
