import os
from dotenv import load_dotenv

from logic.repositories.bookingRepository import BookingRepository
from logic.repositories.mongoUserRepository import MongoUserRepository

# Charger le fichier .env
load_dotenv()

def provide_repositories():
    db_url = os.getenv("MONGO_URI")
    db_name = "comparator_hotel"
    if not db_url:
        raise ValueError("MONGO_URI n'est pas défini dans le fichier .env")
    return [MongoUserRepository(db_url, db_name), BookingRepository(db_url, db_name)]
