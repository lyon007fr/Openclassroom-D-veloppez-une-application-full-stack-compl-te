import { User } from "../../me/interface/user";

export interface Theme {
    id: number;                    // Identifiant du thème
    title: string;                 // Titre du thème
    description: string;           // Description du thème
    subscribedUsers: User[];       // Liste des utilisateurs abonnés à ce thème
  }