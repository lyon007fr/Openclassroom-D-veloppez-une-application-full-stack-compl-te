import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { Theme } from '../interface/theme';

@Injectable({
  providedIn: 'root',
})
export class ThemeService {
  private apiUrl = `http://localhost:8080/api`; // L'URL de l'API

  constructor(private http: HttpClient) {}

  /**
   * Récupère tous les thèmes disponibles depuis l'API.
   *
   * @returns {Observable<Theme[]>} Un observable contenant la liste des thèmes.
   */
  getThemes(): Observable<Theme[]> {
    return this.http
      .get<Theme[]>(`${this.apiUrl}/themes`)
      .pipe(catchError(this.handleError));
  }

  /**
   * Récupère un thème spécifique en fonction de son ID.
   *
   * @param id L'ID du thème à récupérer.
   * @returns {Observable<Theme>} Un observable contenant les informations du thème.
   */
  getThemeById(id: number): Observable<Theme> {
    return this.http
      .get<Theme>(`${this.apiUrl}/theme/${id}`)
      .pipe(catchError(this.handleError));
  }

  /**
   * Crée un nouveau thème en envoyant les données au serveur.
   *
   * @param theme L'objet thème à créer.
   * @returns {Observable<Theme>} Un observable contenant les informations du thème créé.
   */
  createTheme(theme: Theme): Observable<Theme> {
    return this.http
      .post<Theme>(`${this.apiUrl}/theme`, theme)
      .pipe(catchError(this.handleError));
  }

  /**
   * Permet à un utilisateur de se désabonner d'un thème en fonction de son ID.
   *
   * @param themeId L'ID du thème duquel l'utilisateur souhaite se désabonner.
   * @returns {Observable<void>} Un observable pour indiquer que l'action a été effectuée.
   */
  unsubscribeFromTheme(themeId: number): Observable<void> {
    return this.http
      .delete<void>(`${this.apiUrl}/unsubscribe/${themeId}`)
      .pipe(catchError(this.handleError));
  }

  /**
   * Gère les erreurs de réponse API et renvoie un message d'erreur compréhensible.
   *
   * @param error L'objet d'erreur à traiter.
   * @returns {Observable<never>} Un observable contenant le message d'erreur.
   */
  private handleError(error: any): Observable<never> {
    let errorMessage = 'An unknown error occurred!';
    if (error.error instanceof ErrorEvent) {
      // Erreur côté client
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Erreur côté serveur
      errorMessage = `Server returned code: ${error.status}, error message is: ${error.message}`;
    }
    return throwError(errorMessage);
  }

  /**
   * Permet à un utilisateur de s'abonner à un thème en fonction de son ID.
   *
   * @param themeId L'ID du thème auquel l'utilisateur souhaite s'abonner.
   * @returns {Observable<void>} Un observable pour indiquer que l'action a été effectuée.
   */
  subscribeToTheme(themeId: number): Observable<void> {
    return this.http
      .post<void>(`${this.apiUrl}/subscribe/${themeId}`, {})
      .pipe(catchError(this.handleError));
  }
}
