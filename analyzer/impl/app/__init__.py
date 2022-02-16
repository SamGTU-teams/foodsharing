from config import Config

from flask import Flask
from flask_sqlalchemy import SQLAlchemy
import logging

log = logging.getLogger(__name__)

app = Flask(__name__)

app.config.from_object(Config)

db = SQLAlchemy(app)

from app import models, routes

if __name__ == "__main__":
    app.run()
