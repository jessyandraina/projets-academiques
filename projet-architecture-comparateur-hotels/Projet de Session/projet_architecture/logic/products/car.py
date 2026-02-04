# models/car.py

from logic.products.product import Product


class Car(Product):
    def __init__(self, name, country, description, price, image_url, car_type, brand):
        super().__init__(name, country, description, price, image_url)
        self.car_type = car_type
        self.brand = brand

    def to_dict(self):
        data = super().to_dict()
        data["car_type"] = self.car_type
        data["brand"] = self.brand
        return data
