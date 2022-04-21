import csv
import itertools
import os
import urllib.request as request


def read_data(url):
    reader = request.urlopen(url)
    reader = map(lambda l: l.decode('utf-8'), reader)
    return csv.DictReader(list(reader), delimiter=',')


insert_category = "-- '{1}' category\n" + \
                  "INSERT INTO category (id, name) VALUES ({0}, '{1}');\n"
insert_product = "INSERT INTO products" + \
                 " (id, name, category) VALUES ({0}, '{1}', {2});\n"

product_header = 'product'
category_header = 'category'

start_category_index = 1
start_product_index = 1

key = "1Pj6DnRFcn5VdOI4mkNNIv9JPBQeZ_I6q"
sheet_name = "products_v2"
sheet_url = f"https://docs.google.com/spreadsheets/d/{key}/gviz/tq?tqx=out:csv&sheet={sheet_name}"

current_dir = os.path.dirname(os.path.realpath(__file__))
output_file_name = "script.sql"
output_file_path = os.path.join(*[current_dir, output_file_name])

reader = read_data(sheet_url)
categories = map(lambda row: row[category_header], reader)
categories = list(dict.fromkeys(categories))

with open(output_file_path, 'w') as writer:
    product_id = itertools.count(start=start_product_index)

    for category_i in range(len(categories)):
        category_id = start_category_index + category_i
        category = categories[category_i]
        writer.write(insert_category.format(category_id, category))

        reader = read_data(sheet_url)
        data = filter(lambda row: category == row[category_header], reader)
        data = map(lambda row: row[product_header], data)
        data = map(lambda p: [next(product_id), p, category_id], data)
        data = map(lambda p: insert_product.format(*p), data)
        writer.writelines(data)
        writer.write('\n')
