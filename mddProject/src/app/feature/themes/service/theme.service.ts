import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { Theme } from '../interface/theme';  

@Injectable({
  providedIn: 'root'
})
export class ThemeService {

  private apiUrl = `http://localhost:8080/api`;  // L'URL de l'API

  constructor(private http: HttpClient) { }

  // Récupérer tous les thèmes
  getThemes(): Observable<Theme[]> {
    return this.http.get<Theme[]>(`${this.apiUrl}/themes`).pipe(
      catchError(this.handleError)
    );
  }

  // Récupérer un thème par son ID
  getThemeById(id: number): Observable<Theme> {
    return this.http.get<Theme>(`${this.apiUrl}/theme/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  // Créer un nouveau thème
  createTheme(theme: Theme): Observable<Theme> {
    return this.http.post<Theme>(`${this.apiUrl}/theme`, theme).pipe(
      catchError(this.handleError)
    );
  }

  unsubscribeFromTheme( themeId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/unsubscribe/${themeId}`).pipe(
      catchError(this.handleError)
    );
  }

  // Gérer les erreurs
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

  // S'abonner à un thème
subscribeToTheme(themeId: number): Observable<void> {
  return this.http.post<void>(`${this.apiUrl}/subscribe/${themeId}`, {}).pipe(
    catchError(this.handleError)
  );
}
}
