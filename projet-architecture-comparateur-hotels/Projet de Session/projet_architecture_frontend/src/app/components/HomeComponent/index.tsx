"use client";

import React, { useState } from "react";
import { useRouter } from "next/navigation";
import { useSelector } from "react-redux";
import { selectUser } from "@/app/logic/redux/store/userSlice";

const HomeView = () => {
  const [cityName, setCityName] = useState<string>("");
  const [results, setResults] = useState<any[]>([]);
  const [cityResults, setCityResults] = useState<any[]>([]);
  const [showModal, setShowModal] = useState<boolean>(false);
  const [showGuestsModal, setShowGuestsModal] = useState<boolean>(false);
  const [guestCount, setGuestCount] = useState<number>(2);
  const [checkInDate, setCheckInDate] = useState<string>("");
  const [checkOutDate, setCheckOutDate] = useState<string>("");
  const [debounceTimeout, setDebounceTimeout] = useState<NodeJS.Timeout | null>(null);
  const user = useSelector(selectUser);

  const router = useRouter();

  const handleCitySearch = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setCityName(value);

    if (value === "") {
      setShowModal(false);
      setCityResults([]);
      return;
    }

    if (debounceTimeout) clearTimeout(debounceTimeout);

    const timeout = setTimeout(async () => {
      try {
        const response = await fetch(`http://127.0.0.1:5000/cities?search=${value}`);
        const data = await response.json();
        if (Array.isArray(data)) {
          setCityResults(data);
          setShowModal(true);
        }
      } catch (error) {
        console.log("Erreur lors de la récupération des villes:", error);
        setShowModal(false);
      }
    }, 500);

    setDebounceTimeout(timeout);
  };

  const handleSearchClick = async () => {
    try {
      const response = await fetch(`http://127.0.0.1:5000/hotels?search=${cityName}`);
      const data = await response.json();
      if (Array.isArray(data)) {
        setResults(data);
        setShowModal(false);
      }
    } catch (error) {
      console.log("Erreur lors de la récupération des hôtels:", error);
    }
  };

  const handleCitySelect = (city: string) => {
    setCityName(city);
    setShowModal(false);
  };

  const handleGuestsClick = () => {
    setShowGuestsModal(true);
  };

  const closeGuestsModal = () => {
    setShowGuestsModal(false);
  };

  const handleGuestChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setGuestCount(Number(e.target.value));
  };

  const calculateTotalPrice = (price: number) => {
    const nights =
      checkInDate && checkOutDate
        ? Math.max(
            (new Date(checkOutDate).getTime() - new Date(checkInDate).getTime()) / (1000 * 3600 * 24),
            1
          )
        : 0;
    return price * nights * guestCount;
  };

  const handleViewPrices = (price: number, name: string) => {
    if (!user) {
      router.push("/pages/Login");
    } else if (checkInDate && checkOutDate && cityName) {
      const totalPrice = calculateTotalPrice(price);
      router.push(
       `pages/Billing?name=${name}&total=${totalPrice}&city=${cityName}&guests=${guestCount}&checkIn=${checkInDate}&checkOut=${checkOutDate}`

      );
    } else {
      alert("Veuillez remplir toutes les informations (dates, ville, et invités).");
    }
  };

  return (
    <div className="mt-10">
      <div className="mb-5 ml-16">
        <h1 className="font-extrabold text-xl mb-3">
          {"Obtenez 40% de réductions sur votre prochaine réservation d'hôtel"}
        </h1>
        <h2>{"Nous comparons des prix d'hôtels venant de plus de 100 sites"}</h2>
      </div>
      <div className="mx-14 flex justify-around border rounded-md">
        <div className="flex flex-col">
          <label htmlFor="cityInput" className="mb-2 text-sm font-medium">Où voulez-vous aller ?</label>
          <input
            id="cityInput"
            type="text"
            className="flex-1 p-2 mt-4 h-14 rounded-md focus:border-blue-500"
            placeholder="Où voulez-vous aller ?"
            value={cityName}
            onChange={handleCitySearch}
          />
        </div>
        <div className="flex flex-col">
          <label htmlFor="checkInDate" className="mb-2 text-sm font-medium">Check-in</label>
          <input
            id="checkInDate"
            type="date"
            value={checkInDate}
            onChange={(e) => setCheckInDate(e.target.value)}
            className="flex-1 p-2 mt-4 h-14 rounded-md"
          />
        </div>
        <div className="flex flex-col">
          <label htmlFor="checkOutDate" className="mb-2 text-sm font-medium">Check-out</label>
          <input
            id="checkOutDate"
            type="date"
            value={checkOutDate}
            onChange={(e) => setCheckOutDate(e.target.value)}
            className="flex-1 p-2 mt-4 h-14 rounded-md"
          />
        </div>
        <div className="mx-5 cursor-pointer" onClick={handleGuestsClick}>
          <h1 className="font-extrabold">{"Invités"}</h1>
          <h2>{guestCount} invités</h2>
        </div>
        <button
          className="bg-blue-500 text-white font-bold py-2 px-4 rounded my-2 mx-2"
          onClick={handleSearchClick}
        >
          Rechercher
        </button>
      </div>

      {/* Modal pour afficher les résultats des villes */}
      {showModal && (
        <div className=" top-full mt-2 left-14 right-14 bg-white rounded-md p-4 shadow-lg w-full max-w-md z-10">
          <h2 className="font-bold text-lg mb-2">Résultats pour "{cityName}"</h2>
          <ul className="space-y-2">
            {cityResults.map((result: any) => (
              <li
                key={result.city}
                className="p-2 border-b flex flex-col cursor-pointer"
                onClick={() => handleCitySelect(result.city)}
              >
                <strong>{result.city}</strong>
                <span>{result.country}</span>
              </li>
            ))}
          </ul>
        </div>
      )}

      {/* Modal pour choisir le nombre d'invités */}
      {showGuestsModal && (
        <div className="absolute top-1/4 left-1/2 transform -translate-x-1/2 bg-white rounded-md p-4 shadow-lg w-full max-w-md z-20">
          <h2 className="font-bold text-lg mb-2">Sélectionner le nombre d'invités</h2>
          <input
            type="number"
            min="1"
            max="10"
            value={guestCount}
            onChange={handleGuestChange}
            className="w-full p-2 border rounded-md"
          />
          <div className="mt-4 flex justify-between">
            <button
              onClick={closeGuestsModal}
              className="bg-red-500 text-white font-bold py-2 px-4 rounded"
            >
              Annuler
            </button>
            <button
              onClick={closeGuestsModal}
              className="bg-blue-500 text-white font-bold py-2 px-4 rounded"
            >
              Confirmer
            </button>
          </div>
        </div>
      )}

      {/* Liste des résultats */}
      <div className="mt-10 mx-14">
        <h2 className="text-xl font-bold mb-4">Résultats de la recherche</h2>
        <div className="flex flex-col space-y-4">
          {results.map((result, index) => (
            <div key={index} className="border rounded-md p-4 shadow-md">
              <img
                src={result.image_url}
                alt={result.name}
                className="w-full h-40 object-cover rounded-md mb-4"
              />
              <h3 className="font-bold text-lg">{result.name}</h3>
              <p className="text-gray-600">{result.description}</p>
              <p className="text-sm text-gray-500">{result.city}, {result.country}</p>
              <p className="text-blue-600 font-bold mt-2">{result.price} € / nuit</p>
              <button
                onClick={() => handleViewPrices(result.price, result.name)}
                className="mt-4 bg-blue-500 text-white font-bold py-2 px-4 rounded"
              >
                Voir les prix
              </button>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default HomeView;
