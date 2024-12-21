import { Theme } from "../../themes/interface/theme";

export interface User {
    id: number;                    // Identifiant unique de l'utilisateur
    username: string;               // Nom d'utilisateur
    password: string;               // Mot de passe de l'utilisateur
    email: string;                  // Email de l'utilisateur
    subscribedThemes: Theme[];      // Liste des thèmes auxquels l'utilisateur est abonné
  }