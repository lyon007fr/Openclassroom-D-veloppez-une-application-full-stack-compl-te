import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth'; // URL de l'API
  private tokenKey = 'authToken'; // Clé pour le stockage du token dans le localStorage

  constructor(private http: HttpClient) {}

  /**
   * Appelle l'API pour effectuer une connexion.
   * @param usernameOrEmail - Email ou nom d'utilisateur de l'utilisateur.
   * @param password - Mot de passe de l'utilisateur.
   * @returns Un observable contenant la réponse de l'API.
   */
  login(usernameOrEmail: string, password: string): Observable<any> {
    const body = { usernameOrEmail, password };
    return this.http.post(`${this.apiUrl}/login`, body);
  }

  /**
   * Appelle l'API pour enregistrer un nouvel utilisateur.
   * @param user - Objet contenant les informations de l'utilisateur à enregistrer.
   * @returns Un observable contenant la réponse de l'API.
   */
  register(user: {
    username: string;
    email: string;
    password: string;
  }): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, user);
  }
}
