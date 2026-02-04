"use client";

import { selectUser } from "@/app/logic/redux/store/userSlice";
import { useSearchParams } from "next/navigation";
import { useSelector } from "react-redux";
import { useEffect, useState } from "react";

const Billing = () => {
  const searchParams = useSearchParams();
  const [total, setTotal] = useState<string | null>(null);
  const [name, setName] = useState<string | null>(null);
  const [city, setCity] = useState<string | null>(null);
  const [guests, setGuests] = useState<string | null>(null);
  const [checkIn, setCheckIn] = useState<string | null>(null);
  const [checkOut, setCheckOut] = useState<string | null>(null);

  const user = useSelector(selectUser);

  useEffect(() => {
    // Mettre à jour les états uniquement si les paramètres changent
    const newTotal = searchParams.get("total");
    const newName = searchParams.get("name");
    const newCity = searchParams.get("city");
    const newGuests = searchParams.get("guests");
    const newCheckIn = searchParams.get("checkIn");
    const newCheckOut = searchParams.get("checkOut");

    setTotal(newTotal);
    setName(newName);
    setCity(newCity);
    setGuests(newGuests);
    setCheckIn(newCheckIn);
    setCheckOut(newCheckOut);
  }, [searchParams]); // Ne se déclenche que si `searchParams` change

  const handlePayment = async () => {
    if (!user?.id || !name || !city || !checkIn || !checkOut || !guests || !total) {
      alert("Veuillez remplir tous les champs avant de payer.");
      return;
    }

    const hotel = {
        name: name,
      city: city,
      price: total,
    };

    // {
    //     "idUser": "674e55621754628a19749775",
    //     "productType": "hotel",
    //     "product": {
    //       "name": "Hotel Eiffel Tower View",
    //       "city": "Paris",
    //       "price": 1200,
    //       "description": "A luxury hotel with a stunning view of the Eiffel Tower.",
    //       "image_url": "https://example.com/hotel.jpg"
    //     },
    //     "guests": 2,
    //     "checkIn": "2024-12-04",
    //     "checkOut": "2024-12-06",
    //     "total": 1200,
    //     "name": "Hotel Eiffel Tower View"
    //   }
      

    const booking = {
      idUser: user.id,
      productType: "hotel",
      product: hotel,
      guests: guests,
      checkIn: checkIn,
      checkOut: checkOut,
      total: total,
      name: name,
    };

    try {
      const response = await fetch("http://127.0.0.1:5000/bookings", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(booking),
      });

      if (response.ok) {
        alert("Réservation confirmée !");
      } else {
        alert("Une erreur est survenue. Veuillez réessayer.");
      }
    } catch (error) {
      console.error("Erreur lors de la réservation :", error);
      alert("Erreur de connexion. Veuillez réessayer.");
    }
  };

  return (
    <div className="p-10 flex flex-col items-center">
      <h1 className="text-2xl font-bold mb-4">Résumé de votre réservation</h1>
      <div className="w-full max-w-md p-6 border rounded-lg shadow-md bg-gray-100">
        <p className="text-lg">
          <strong>Destination :</strong> {name}
        </p>
        <p className="text-lg">
          <strong>Dates :</strong> {checkIn} - {checkOut}
        </p>
        <p className="text-lg">
          <strong>Nombre d'invités :</strong> {guests}
        </p>
        <p className="text-lg">
          <strong>Prix total :</strong> {total} €
        </p>
        <button
          onClick={handlePayment}
          className="mt-6 w-full bg-green-500 text-white py-2 px-4 rounded"
        >
          Payer
        </button>
      </div>
    </div>
  );
};

export default Billing;
