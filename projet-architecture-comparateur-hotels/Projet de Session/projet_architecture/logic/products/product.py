# models/product.py
class Product:
    def __init__(self, name, country, description, price, image_url):
        self.name = name
        self.country = country
        self.description = description
        self.price = price
        self.image_url = image_url

    def to_dict(self):
        return {
            "name": self.name,
            "country": self.country,
            "description": self.description,
            "price": self.price,
            "image_url": self.image_url
        }
