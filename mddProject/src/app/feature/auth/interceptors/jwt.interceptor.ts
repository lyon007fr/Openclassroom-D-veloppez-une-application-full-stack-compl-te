import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  
  /**
   * Intercepte les requêtes HTTP sortantes pour ajouter un token JWT dans l'en-tête d'autorisation.
   * Si le token est disponible dans le stockage local, il est ajouté à la requête.
   * 
   * @param request - La requête HTTP sortante à intercepter.
   * @param next - Le gestionnaire de la requête HTTP pour continuer la chaîne de traitement.
   * @returns Un observable de l'événement HTTP résultant de la requête, potentiellement modifiée.
   */
  intercept(
    request: HttpRequest<any>,
    next: HttpHandler,
  ): Observable<HttpEvent<any>> {
    // Récupère le token JWT depuis le stockage local du navigateur
    const token = localStorage.getItem('token'); 

    // Si le token existe, il est ajouté dans les en-têtes de la requête HTTP
    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`, // Ajoute le token dans l'en-tête Authorization
        },
      });
    }

    // Permet à la requête modifiée de continuer dans la chaîne de traitement HTTP
    return next.handle(request);
  }
}
