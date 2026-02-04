# controllers/booking_controller.py
from flask import Flask, request, jsonify
from logic.models.booking import Booking
from logic.products.car import Car
from logic.products.hotel import Hotel
from logic.repositories.bookingRepository import BookingRepository


class BookingController:
    def __init__(self, booking_repository: BookingRepository):
        self.booking_repository = booking_repository

    def register_routes(self, app: Flask):
        @app.route('/bookings', methods=['POST'])
        def create_booking():
            data = request.get_json()

            # Récupérer les paramètres nécessaires
            id_user = data.get("idUser")
            product_type = data.get("productType")
            product_data = data.get("product")
            city = data.get("city")  # 'city' est maintenant optionnel
            guests = data.get("guests")
            check_in = data.get("checkIn")
            check_out = data.get("checkOut")
            price = data.get("total")
            name = data.get("name")

            # Vérifier que les champs obligatoires sont présents
            if not all([id_user, product_type, product_data, guests, check_in, check_out, price]):
                return jsonify({"error": "Missing required fields"}), 400

            # Assurez-vous que `guests` et `price` sont du bon type
            try:
                guests = int(guests)  # Convertir en entier
                price = float(price)  # Convertir en flottant
            except ValueError:
                return jsonify({"error": "'guests' must be an integer and 'total' must be a float"}), 400

            # Si le type de produit est "hotel", vérifier la présence des clés nécessaires
            if product_type == "hotel":
                required_keys = ["name", "city", "price"]
                for key in required_keys:
                    if key not in product_data:
                        return jsonify({"error": f"Missing key '{key}' in product data"}), 400
                
                # Créer l'objet Hotel avec des valeurs optionnelles
                hotel = Hotel(
                    name=product_data["name"],
                    city=product_data["city"],
                    description=product_data.get("description", None),
                    price=product_data["price"],
                    image_url=product_data.get("image_url", None),
                    star_rating=product_data.get("star_rating", None),  # Par défaut None si absent
                    amenities=product_data.get("amenities", None)  # Par défaut None si absent
                )
            elif product_type == "car":
                required_keys = ["name", "city", "price", "image_url"]
                for key in required_keys:
                    if key not in product_data:
                        return jsonify({"error": f"Missing key '{key}' in product data"}), 400

                # Créer l'objet Car avec des valeurs optionnelles
                car = Car(
                    name=product_data["name"],
                    city=product_data["city"],
                    description=product_data["description"],
                    price=product_data["price"],
                    image_url=product_data["image_url"],
                    car_type=product_data.get("car_type", None),  # Optionnel
                    brand=product_data.get("brand", None)  # Optionnel
                )
            else:
                return jsonify({"error": "Invalid product type"}), 400

            # Créer l'objet Booking
            booking = Booking(
                id_user=id_user,
                product=hotel if product_type == "hotel" else car,
                city=city,  # 'city' est optionnel, mais si présent il est ajouté
                guests=guests,
                check_in=check_in,
                check_out=check_out,
                price=price
            )

            # Enregistrer le booking dans la base de données MongoDB
            booking_id = self.booking_repository.create_booking(booking)

            return jsonify({"booking_id": booking_id}), 201
