import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { SessionService } from '../services/session.service';

@Injectable({
  providedIn: 'root'
})
export class UnGuard {
  constructor(
    private router: Router,
    private sessionService: SessionService
  ) {}

  /**
   * Vérifie si l'utilisateur n'est pas connecté avant d'accéder à la route.
   * Redirige l'utilisateur vers la page d'accueil si déjà connecté.
   */
  async canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Promise<boolean | UrlTree> {
    // Récupère l'état de la connexion de manière synchrone
    const isAuthenticated = await firstValueFrom(this.sessionService.isLoggedIn$);

    // Si l'utilisateur est déjà connecté, redirige vers la page d'accueil
    if (isAuthenticated) {
      this.router.navigate(['/home']); 
      return false;
    }

    // Si l'utilisateur n'est pas connecté, permet l'accès à la route
    return true;
  }
}
