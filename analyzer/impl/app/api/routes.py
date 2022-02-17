from flask import jsonify
from app.api import bp
from app.models import Product
import logging

log = logging.getLogger(__name__)


@bp.route('/')
@bp.route('/index')
def index():
    return "Hello, World!"


@bp.route('/products', methods=["GET"])
def get_products():
    data = Product.query.all()
    log.debug("DB result: %s", data)
    response = jsonify([val.to_dict() for val in data])
    response.status_code = 200
    return response
