import pika, sys, os

def main():
    params = pika.ConnectionParameters(virtual_host='rabbitmq', retry_delay=5, connection_attempts=5)
    connection = pika.BlockingConnection(params)
    channel = connection.channel()

    channel.queue_declare(queue='echo-parser')
    channel.queue_declare(queue='echo-analyzer')

    def receive(ch, mehod, properties, body):
        print(f" [x] Received '{body}'")
        channel.basic_publish(exchange='',
                        routing_key='echo-analyzer',
                        body=body)

    channel.basic_consume(queue='echo-parser', on_message_callback=receive, auto_ack=True)

    print(' [*] Waiting for messages. To exit press CTRL+C')
    channel.start_consuming()

if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        print('Interrupted')
        try:
            sys.exit(0)
        except SystemExit:
            os._exit(0)
