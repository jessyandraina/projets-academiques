// controllers/AuthController.ts

import { CreateUserPayload, LoginPayload, User } from "../models/user";
import { IAuthRepository } from "../repositories/IUserRepository";



export class AuthController {
    private authRepo: IAuthRepository;

    constructor(authRepo: IAuthRepository) {
        this.authRepo = authRepo;
    }

    async register(payload: CreateUserPayload): Promise<User> {
        try {
            return await this.authRepo.register(payload);
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        } catch (error) {
            throw new Error('Erreur lors de l\'inscription');
        }
    }

    async login(payload: LoginPayload): Promise<User | null> {
        try {
            return await this.authRepo.login(payload);
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        } catch (error) {
            throw new Error('Erreur lors de la connexion');
        }
    }

    async getUserByEmail(email: string): Promise<User | null> {
        return await this.authRepo.getUserByEmail(email);
    }
}
