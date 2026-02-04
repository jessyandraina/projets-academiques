"use client"; 
import { AuthController } from '@/app/logic/controllers/userController';
import { LoginPayload } from '@/app/logic/models/user';
import { selectUser, setUser } from '@/app/logic/redux/store/userSlice';
import { UserRepository } from '@/app/logic/repositories/UserRepository';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import React, { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { useDispatch, useSelector } from 'react-redux';

const Login = () => {
  const { register, handleSubmit, formState: { errors } } = useForm<LoginPayload>();
  const dispatch = useDispatch();
  const user = useSelector(selectUser);
  const router = useRouter();
  const userRepository = new UserRepository();
  const userController = new AuthController(userRepository);

  useEffect(() => {
    if (user) {
      router.push("/");
    }
 
  }, [router, user])

  // Ajout d'un état pour gérer l'erreur de connexion
  const [error, setError] = useState<string | null>(null);

  const onSubmit = async (data: LoginPayload) => {
    try {
      const user = await userController.login(data);  // Appel au backend pour la connexion
      dispatch(setUser(user)); // Mettre à jour l'état Redux
      router.push("/");
      console.log('Utilisateur connecté', user);
    } catch (error) {
      console.error('Erreur de connexion', error);
      setError('Identifiants incorrects'); // Mise à jour de l'erreur dans l'état
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
        <h2 className="text-2xl font-bold text-center mb-6">Connexion</h2>
        {error && <p className="text-red-500 text-center mb-4">{error}</p>} {/* Affichage de l'erreur */}
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
          <div className="mb-4">
            <label htmlFor="email" className="block text-gray-700">Email</label>
            <input 
              {...register('mail', { required: 'Email requis' })} 
              className="w-full p-3 border border-gray-300 rounded-md mt-1"
              placeholder="Entrez votre email" 
            />
            {errors.mail && <span className="text-red-500 text-sm">{errors.mail.message}</span>}
          </div>
          <div className="mb-6">
            <label htmlFor="password" className="block text-gray-700">Mot de passe</label>
            <input 
              type="password" 
              {...register('password', { required: 'Mot de passe requis' })} 
              className="w-full p-3 border border-gray-300 rounded-md mt-1"
              placeholder="Entrez votre mot de passe" 
            />
            {errors.password && <span className="text-red-500 text-sm">{errors.password.message}</span>}
          </div>
          <button type="submit" className="w-full py-3 bg-blue-500 text-white rounded-md hover:bg-blue-600 focus:outline-none">Se connecter</button>
             {/* Lien vers la page d'inscription */}
        <p className="mt-4 text-center">
          Pas encore de compte ? 
          <Link href="/pages/Register" className="text-blue-500 hover:text-blue-700">
            S&apos;inscrire
          </Link>
        </p>
        </form>
      </div>
    </div>
  );
};

export default Login;
