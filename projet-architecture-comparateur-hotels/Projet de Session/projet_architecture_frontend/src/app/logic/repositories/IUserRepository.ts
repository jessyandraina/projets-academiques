// repositories/IAuthRepository.ts

import { CreateUserPayload, LoginPayload, User } from "../models/user";



export interface IAuthRepository {
    register(payload: CreateUserPayload): Promise<User>; // Inscription
    login(payload: LoginPayload): Promise<User | null>;   // Connexion
    getUserByEmail(email: string): Promise<User | null>; // Récupérer un utilisateur par email
}
