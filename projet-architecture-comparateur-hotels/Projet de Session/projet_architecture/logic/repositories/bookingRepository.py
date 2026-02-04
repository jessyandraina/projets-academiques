# repositories/booking_repository.py
from bson import ObjectId
from pymongo import MongoClient

class BookingRepository:
    def __init__(self, db_url: str, db_name: str):
        self.client = MongoClient(db_url)
        self.collection = self.client[db_name]["bookings"]
        print(self.collection)
        
    def create_booking(self, booking):
        # Convertir l'objet Booking en dictionnaire
        booking_data = booking.to_dict()

        # Insérer la réservation dans la collection MongoDB
        result = self.collection.insert_one(booking_data)
        
        # Retourner l'ID de la réservation insérée (généré automatiquement par MongoDB)
        return str(result.inserted_id)
