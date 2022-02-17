from config import Config

from flask import Flask
from flask_sqlalchemy import SQLAlchemy
import logging

log = logging.getLogger(__name__)

db = SQLAlchemy()


def create_app(config_class=Config):
    app = Flask(__name__)
    app.config.from_object(config_class)

    db.init_app(app)

    app.elasticsearch = Elasticsearch([app.config['ELASTICSEARCH_URL']]) \
        if app.config['ELASTICSEARCH_URL'] else None

    from app.api import bp as api_bp
    app.register_blueprint(api_bp, url_prefix="/api")

    from app.health import bp as health_bp
    app.register_blueprint(health_bp, url_prefix="/actuator")

    return app


from app import models
