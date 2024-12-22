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
export class UnGuard {
  constructor(
    private router: Router,
    private sessionService: SessionService,
  ) {}

  /**
   * Vérifie si l'utilisateur n'est pas authentifié pour accéder à la route.
   * @param route - Snapshot de la route activée contenant les informations de la route.
   * @param state - Snapshot de l'état du routeur contenant l'URL actuelle.
   * @returns Une promesse résolvant un booléen ou une UrlTree :
   * - `true` si l'utilisateur n'est pas connecté, permettant l'accès.
   * - `false` si l'utilisateur est connecté, entraînant une redirection vers `/home`.
   */
  async canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot,
  ): Promise<boolean | UrlTree> {
    // Vérifie l'état d'authentification de manière synchrone
    const isAuthenticated = await firstValueFrom(
      this.sessionService.isLoggedIn$,
    );

    // Redirige vers la page d'accueil si l'utilisateur est déjà connecté
    if (isAuthenticated) {
      this.router.navigate(['/home']); // Redirection vers la page d'accueil
      return false; // Accès refusé
    }

    // Permet l'accès à la route si l'utilisateur n'est pas connecté
    return true;
  }
}
