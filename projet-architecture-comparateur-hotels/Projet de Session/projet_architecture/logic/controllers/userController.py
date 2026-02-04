from flask import Flask, request, jsonify
from logic.models.user import User
from logic.repositories.userRepository import IUserRepository

class UserController:
    def __init__(self, repository: IUserRepository):
        self.repository = repository

    def register_routes(self, app: Flask):
        @app.route("/register", methods=["POST"])
        def register_user():
            data = request.json
            if not data or not data.get("username") or not data.get("mail") or not data.get("password"):
                return jsonify({"error": "Missing required fields"}), 400

            # Création d'un utilisateur
            user = User(
                username=data["username"],
                mail=data["mail"],
                password=data["password"],  # Le mot de passe sera hashé dans le repo
            )

            created_user = self.repository.register(user)
            return jsonify({
                "id": created_user.id,
                "username": created_user.username,
                "mail": created_user.mail,
            }), 201

        @app.route("/login", methods=["POST"])
        def login_user():
            data = request.json
            if not data or not data.get("mail") or not data.get("password"):
                return jsonify({"error": "Missing required fields"}), 400

            # Login de l'utilisateur
            user = self.repository.login(data["mail"], data["password"])
            if not user:
                return jsonify({"error": "Invalid credentials"}), 401

            return jsonify({
                "id": user.id,
                "username": user.username,
                "mail": user.mail,
            }), 200

