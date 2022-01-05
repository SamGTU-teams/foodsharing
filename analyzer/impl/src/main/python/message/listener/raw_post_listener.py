def callback(ch, method, properties, body):
    print(f"Incoming message: {body}")
