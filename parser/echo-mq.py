import pika


def on_connection_open(connection):
    print("Connection open")
    channel = connection.channel(on_channel_open)
    channel.queue_declare(queue='echo-parser')


def on_channel_open(channel):
    print("Channel open")
    message = 'Testing echo'
    channel.basic_publish(exchange='',
                          routing_key='echo-parser',
                          body=message)
    print(f" [x] Sent '{message}'")


params = pika.ConnectionParameters(host='rabbitmq',
                                   retry_delay=5,
                                   connection_attempts=5)
connection = pika.SelectConnection(params, on_open_callback=on_connection_open)

try:
    connection.ioloop.start()
except KeyboardInterrupt:
    connection.close()
