import logging.config


def load_logging() -> None:
    logging_config = {
        'version': 1,
        'disable_existing_loggers': False,

        'formatters': {
            'console': {
                'format': '%(asctime)s.%(msecs)03d %(levelname)5s %(process)d '
                          '--- [%(threadName)15s] %(name)-40s: %(message)s',
                'datefmt': '%Y-%m-%d,%H:%M:%S'
            },
        },

        'handlers': {
            'console': {
                'class': 'logging.StreamHandler',
                'formatter': 'console',
            },
        },

        'loggers': {
            '': {
                'handlers': ['console'],
                'level': 'INFO',
                'propagate': False
            }
        }
    }

    logging.config.dictConfig(logging_config)
