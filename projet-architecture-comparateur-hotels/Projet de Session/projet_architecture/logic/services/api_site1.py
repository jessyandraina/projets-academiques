# import requests

# class ApiSite1:
#     BASE_URL = 'booking-com15.p.rapidapi.com'

#     def get_hotel_prices(self, destination, check_in, check_out, guests):
#         params = {
#             'destination': destination,
#             'check_in': check_in,
#             'check_out': check_out,
#             'guests': guests
#         }
#         response = requests.get(self.BASE_URL, params=params)
#         if response.status_code == 200:
#             return self._format_response(response.json())
#         else:
#             raise Exception(f"API Site 1 error: {response.status_code}")

#     def _format_response(self, data):
#         return [
#             {
#                 'hotel_name': item['name'],
#                 'price': item['price'],
#                 'currency': item['currency'],
#                 'url': item['booking_url']
#             }
#             for item in data.get('hotels', [])
#         ]
