from flask import jsonify
from app import app
from app.models import Product
import logging

log = logging.getLogger(__name__)


@app.route('/')
@app.route('/index')
def index():
    return "Hello, World!"


@app.route('/products', methods=["GET"])
def get_products():
    data = Product.query.all()
    log.debug("DB result: %s", data)
    response = jsonify([val.to_dict() for val in data])
    response.status_code = 200
    return response
