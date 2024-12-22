import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Router,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { SessionService } from '../services/session.service';

@Injectable({
  providedIn: 'root', // Fournit ce guard à l'ensemble de l'application
})
export class AuthGuard {
  constructor(
    private router: Router,
    private sessionService: SessionService,
  ) {}

  /**
   * Vérifie si l'utilisateur est autorisé à accéder à une route protégée.
   * @param route - Snapshot de la route activée contenant les informations de la route.
   * @param state - Snapshot de l'état du routeur contenant l'URL actuelle.
   * @returns Une promesse résolvant un booléen ou une UrlTree :
   * - `true` si l'accès est autorisé.
   * - Une `UrlTree` si une redirection est nécessaire (ex. : vers la page de connexion).
   */
  async canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot,
  ): Promise<boolean | UrlTree> {
    // Assure que l'utilisateur est initialisé avant d'effectuer la vérification
    await this.sessionService.initializeUser();

    // Récupère l'état d'authentification de l'utilisateur
    const isAuthenticated = await firstValueFrom(
      this.sessionService.isLoggedIn$,
    );

    // Autorise l'accès si l'utilisateur est connecté
    if (isAuthenticated) {
      return true;
    }

    // Redirige vers la page de connexion si l'utilisateur n'est pas connecté
    this.router.navigate(['/home'], {
      queryParams: { returnUrl: state.url }, // Permet de revenir à l'URL demandée après authentification
    });
    return false;
  }
}
