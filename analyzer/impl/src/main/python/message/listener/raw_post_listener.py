import json

from ..producer.post_producer import publish

def callback(channel, method, properties, body):
    print(f"Incoming message: {body}")
    data = json.loads(body)
    publish(channel, data)
