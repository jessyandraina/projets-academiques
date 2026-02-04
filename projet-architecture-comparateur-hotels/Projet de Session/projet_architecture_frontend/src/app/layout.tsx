"use client";
import { Provider, useSelector } from 'react-redux';
import './globals.css'; // Ajoute tes styles globaux ici
import Link from 'next/link';
import { store } from './logic/redux/store/store';

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <Provider store={store}>
      <html lang="fr">
        <body className="flex flex-col min-h-screen">
          <Header />
          <main className="flex-1">{children}</main>
          <Footer />
        </body>
      </html>
    </Provider>
  );
}

function Header() {
  const { user } = useUser(); // Utilise un hook personnalisé pour obtenir l'utilisateur

  return (
    <header className="bg-blue-500 text-white py-4 px-8 flex justify-between items-center">
      <h1 className="text-lg font-bold">Comparateur d&apos;Hôtels</h1>
      {user ? (
        <h1 className="text-lg font-bold">Bonjour {user.username}</h1>
      ) : (
        <Link href="/pages/Login" className="text-sm font-semibold underline">
          Se connecter
        </Link>
      )}
    </header>
  );
}

function Footer() {
  return (
    <footer className="bg-gray-800 text-white text-center py-4">
      <p>&copy; {new Date().getFullYear()} Comparateur d&apos;Hôtels. Tous droits réservés.</p>
    </footer>
  );
}

function useUser() {
  // Hook pour sélectionner l'utilisateur depuis Redux
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  return useSelector((state: any) => state.user);
}
