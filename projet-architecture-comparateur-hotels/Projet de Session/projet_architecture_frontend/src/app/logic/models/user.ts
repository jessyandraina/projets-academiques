// Modèle utilisateur
export interface User {
    id: string;
    username: string;
    mail: string;
    password: string;
  }
  
  // Modèle pour la création d'un utilisateur (inscription)
  export interface CreateUserPayload {
    username: string;
    mail: string;
    password: string;
  }
  
  // Modèle pour la connexion d'un utilisateur
  export interface LoginPayload {
    mail: string;
    password: string;
  }
  