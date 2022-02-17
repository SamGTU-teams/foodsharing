from logging.config import dictConfig

dictConfig({
    'version': 1,
    'disable_existing_loggers': True,

    'formatters': {
        'console': {
            'format': '%(asctime)s.%(msecs)03d %(levelname)5.5s %(process)d '
                      '--- [%(threadName)15s] %(name)-40.40s: %(message)s',
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
            'level': 'DEBUG',
            'propagate': False
        },
        'sqlalchemy': {
            'handlers': ['console'],
            'level': 'INFO',
            'propagate': False
        },
        'sqlalchemy.engine.Engine': {
            'handlers': ['console'],
            'level': 'INFO',
            'propagate': False
        }
    }
})


from app import create_app

app = create_app()
