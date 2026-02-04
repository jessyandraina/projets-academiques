# models/booking.py
class Booking:
    def __init__(self, id_user, product, guests, check_in, check_out, price, city):
        self.id_user = id_user
        self.product = product  # Ce produit peut être un Hotel, Car, ou autre produit
        self.booking = {
            "guests": guests,
            "check_in": check_in,
            "check_out": check_out,
            "price": price,
            "city": city
        }

    def to_dict(self):
        return {
            "id_user": self.id_user,
            "product": self.product.to_dict(),  # Convertir le produit en dictionnaire
            "booking": self.booking,
        }
