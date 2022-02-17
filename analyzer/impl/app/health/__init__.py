from flask import Blueprint
from healthcheck import HealthCheck

bp = Blueprint('health', __name__)

health = HealthCheck()

from app.health import routes
