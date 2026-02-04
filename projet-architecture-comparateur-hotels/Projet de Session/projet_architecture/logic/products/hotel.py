# models/hotel.py
class Hotel:
    def __init__(self, name, price, image_url, country=None, description=None,city=None, star_rating=None, amenities=None):
        self.name = name
        self.country = country
        self.description = description
        self.price = price
        self.image_url = image_url
        self.city = city  # Optionnel
        self.star_rating = star_rating  # Optionnel, par défaut None
        self.amenities = amenities  # Optionnel, par défaut None

    def to_dict(self):
        return {
            "name": self.name,
            "country": self.country,
            "description": self.description,
            "price": self.price,
            "image_url": self.image_url,
            "city": self.city,
            "star_rating": self.star_rating,
            "amenities": self.amenities
        }
