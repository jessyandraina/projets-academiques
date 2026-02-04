from pymongo import MongoClient
from logic.models.user import User
from logic.repositories.userRepository import IUserRepository
from werkzeug.security import check_password_hash, generate_password_hash


class MongoUserRepository(IUserRepository):
    def __init__(self, db_url: str, db_name: str):
        self.client = MongoClient(db_url)
        self.collection = self.client[db_name]["users"]
        print(self.collection)

    def register(self, user: User):
        # Hachage du mot de passe
        user.password = generate_password_hash(user.password)
        # user.password = user.password
        result = self.collection.insert_one(user.to_dict())
        user.id = str(result.inserted_id)
        return user

    def login(self, mail: str, password: str):
        # Recherche par email
        user_data = self.collection.find_one({"mail": mail})
        if not user_data:
            return None

        # Vérification du mot de passe hashé
        user = User.from_dict(user_data)
        if not check_password_hash(user.password, password):
            return None

        return user