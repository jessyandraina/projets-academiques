import { CreateUserPayload, User, LoginPayload } from "../models/user";
import { IAuthRepository } from "./IUserRepository";


export class UserRepository implements IAuthRepository {
    private apiUrl = 'http://localhost:5000'; // L'URL de ton backend Flask
    async register(payload: CreateUserPayload): Promise<User> {
        try {
            const response = await fetch(`${this.apiUrl}/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(payload),
            });

            if (!response.ok) {
                throw new Error('Erreur lors de la création de l\'utilisateur');
            }

            const user = await response.json();
            return user; // L'utilisateur créé sera retourné
        } catch (error) {
            console.error('Erreur de connexion avec le backend:', error);
            throw error; // Propager l'erreur
        }
    }
    async login(payload: LoginPayload): Promise<User | null> {
        try {
            const response = await fetch(`${this.apiUrl}/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(payload),
            });

            if (!response.ok) {
                throw new Error('Erreur lors de la connexion');
            }

            const user = await response.json();
            return user || null;
        } catch (error) {
            console.error('Erreur de connexion avec le backend:', error);
            return null; // Retourner null en cas d'erreur
        }
    }
    async getUserByEmail(email: string): Promise<User | null> {
        try {
            const response = await fetch(`${this.apiUrl}/user?email=${email}`);
            if (!response.ok) {
                throw new Error('Erreur de récupération de l\'utilisateur');
            }

            const user = await response.json();
            return user || null; // Retourner l'utilisateur ou null si non trouvé
        } catch (error) {
            console.error('Erreur de connexion avec le backend:', error);
            return null; // Retourner null en cas d'erreur
        }
    }

}
