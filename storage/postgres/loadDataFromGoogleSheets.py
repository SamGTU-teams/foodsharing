import csv
import os
import urllib.request as request
from itertools import count

key = "1Pj6DnRFcn5VdOI4mkNNIv9JPBQeZ_I6q"
sheet_name = "products_v2"
sheet_url = f"https://docs.google.com/spreadsheets/d/{key}/gviz/tq?tqx=out:csv&sheet={sheet_name}"

current_dir = os.path.dirname(os.path.realpath(__file__))

[categories_path, products_path] = [os.path.join(
    *[current_dir, "liquibase", "data", f]) for f in
    ["categories.csv", "products.csv"]]

categories = dict()
products = set()
category_index, product_index = count(start=1), count(start=1)

with open(categories_path, 'w', newline='') as writer_cat, \
    open(products_path, 'w', newline='') as writer_prod:
    csv_cat = csv.DictWriter(writer_cat, fieldnames=['id', 'name'])
    csv_prod = csv.DictWriter(writer_prod, fieldnames=[
        'id', 'name', 'category_id'])
    [f.writeheader() for f in [csv_cat, csv_prod]]

    reader = request.urlopen(sheet_url)
    reader = map(lambda l: l.decode('utf-8'), reader)
    for row in csv.DictReader(reader, delimiter=','):
        category, product = row['category'], row['product']
        if product in products:
            continue
        products.add(product)
        category_id = categories.get(category)
        if category_id is None:
            category_id = next(category_index)
            categories[category] = category_id
            csv_cat.writerow({'id': category_id, 'name': category})
        csv_prod.writerow({'id': next(product_index),
                           'name': product, 'category_id': category_id})
