import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import {User} from '../interface/user';


@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = `http://localhost:8080/api`; 

  constructor(private http: HttpClient) {}

  /**
   * Récupère les informations de l'utilisateur connecté.
   * Utilise le token d'authentification dans les en-têtes.
   * @returns L'utilisateur courant
   */
  public getUser(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/me`).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Met à jour les informations de l'utilisateur (seulement username et email)
   * @param user L'utilisateur avec les champs username et email
   * @returns Observable<User>
   */
  public updateUser(user: { username: string; email: string }): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/update`, user).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Abonne un utilisateur à un thème.
   * @param themeId L'ID du thème auquel s'abonner
   * @returns L'utilisateur mis à jour avec ses nouveaux abonnements
   */
  public subscribeToTheme(themeId: number): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/subscribe/${themeId}`, {}).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Désabonne un utilisateur d'un thème.
   * @param themeId L'ID du thème dont se désabonner
   * @returns L'utilisateur mis à jour avec ses abonnements
   */
  public unsubscribeFromTheme(themeId: number): Observable<User> {
    return this.http.delete<User>(`${this.apiUrl}/unsubscribe/${themeId}`).pipe(
      catchError(this.handleError)
    );
  }

  
  /**
   * Gère les erreurs qui peuvent survenir lors des requêtes HTTP.
   * @param error L'erreur capturée lors de l'appel API
   * @returns Un Observable d'erreur
   */
  private handleError(error: any): Observable<never> {
    console.error('An error occurred:', error);
    throw error; 
  }
}
