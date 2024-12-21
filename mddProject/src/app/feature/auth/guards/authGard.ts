import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { SessionService } from '../services/session.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard {
  constructor(
    private router: Router,
    private sessionService: SessionService
  ) {}

  /**
   * Vérifie si l'utilisateur est autorisé à accéder à la route protégée.
   * Initialise l'utilisateur si nécessaire et effectue une redirection si l'utilisateur n'est pas connecté.
   */
  async canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Promise<boolean | UrlTree> {
    // Assure que l'utilisateur est bien initialisé
    await this.sessionService.initializeUser();

    // Récupère l'état de la connexion de manière synchrone
    const isAuthenticated = await firstValueFrom(this.sessionService.isLoggedIn$);

    // Si l'utilisateur est connecté, permet l'accès à la route
    if (isAuthenticated) {
      return true;
    }

    // Si l'utilisateur n'est pas connecté, redirige vers la page de connexion
    this.router.navigate(['/home'], {
      queryParams: { returnUrl: state.url } // Redirige vers l'URL d'origine après la connexion
    });
    return false;
  }
}
