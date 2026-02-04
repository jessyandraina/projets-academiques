from flask import Flask, json, jsonify, request

class HotelService:
    def __init__(self):
        pass

    def load_hotels(self):
        with open('./logic/services/hotels.json', 'r') as file:
            return json.load(file)
        
    def load_cities(self):      
        with open("./logic/services/cities.json", 'r') as file:
            return json.load(file)
        

    def register_routes(self, app: Flask):
        @app.route('/cities', methods=['GET'])
        def get_cities():
            cities = self.load_cities()

            search = request.args.get('search', '').lower()  # Par défaut, une chaîne vide

            filtered_cities = [
                city for city in cities if city["city"].lower().startswith(search)
            ]
            return jsonify(filtered_cities)
        
        @app.route('/hotels', methods=['GET'])
        def get_hotels():

            hotel_service = HotelService()  # Créer une instance de HotelService
            hotels = hotel_service.load_hotels() 
            
            # Récupération du paramètre 'search' dans l'URL
            search = request.args.get('search', '').lower()  # Par défaut, une chaîne vide

            # Filtrer les hôtels dont le pays commence par 'search'
            filtered_hotels = [
                hotel for hotel in hotels if hotel["city"].lower() == search
            ]

            # Retourner les résultats sous forme de JSON
            return jsonify(filtered_hotels)