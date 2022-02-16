from app import db
import logging

log = logging.getLogger(__name__)


class Product(db.Model):
    __slot__ = ['id', 'name', 'category_id']

    __table_args__ = {'extend_existing': True}
    id = db.Column(db.Integer, primary_key=True, nullable=False)
    name = db.Column(db.String(63), unique=True, nullable=False)
    category_id = db.Column(db.Integer, db.ForeignKey('category.id'), nullable=True)

    def __repr__(self):
        return '<Product id={} name={} category={}>'.format(self.id, self.name, self.category_id)

    def to_dict(self, include_email=False):
        data = {
            'id': self.id,
            'name': self.name,
            'category_id': self.category_id
        }
        return data

    def from_dict(self, data):
        for field in self.__slot__:
            if field in data:
                setattr(self, field, data[field])


class Category(db.Model):
    __slot__ = ['id', 'name']
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(63))
    products = db.relationship('Product', backref='category', lazy='dynamic')

    def __repr__(self):
        return '<Category id={} name={}>'.format(self.id, self.name)

    def to_dict(self):
        data = {
            'id': self.id,
            'name': self.name
        }
        return data

    def from_dict(self, data):
        for field in self.__slot__:
            if field in data:
                setattr(self, field, data[field])


