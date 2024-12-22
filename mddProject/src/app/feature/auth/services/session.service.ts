import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, firstValueFrom } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from './auth/auth.service';
import { ThemeService } from '../../themes/service/theme.service';
import { UserService } from '../../me/service/userservice.service';
import { User } from '../../me/interface/user';
import { Theme } from '../../themes/interface/theme';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  // Subject privé pour stocker l'utilisateur actuellement connecté
  private _user: BehaviorSubject<User | null> =
    new BehaviorSubject<User | null>(null);
  // Observable exposant l'utilisateur à d'autres parties de l'application
  public user$: Observable<User | null> = this._user.asObservable();

  // Subject privé pour gérer les thèmes abonnés
  private _subscribedThemes: BehaviorSubject<Theme[]> = new BehaviorSubject<
    Theme[]
  >([]);
  // Observable exposant les thèmes abonnés à d'autres parties de l'application
  public subscribedThemes$: Observable<Theme[]> =
    this._subscribedThemes.asObservable();

  // Subject pour gérer l'état de la connexion de l'utilisateur
  private _isLoggedSubject$: BehaviorSubject<boolean> =
    new BehaviorSubject<boolean>(false);
  // Observable pour savoir si l'utilisateur est connecté
  public isLoggedIn$: Observable<boolean> =
    this._isLoggedSubject$.asObservable();

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private themeService: ThemeService,
  ) {
    // Lorsque le service est initialisé, on tente de récupérer l'utilisateur courant
    this.initializeUser();
  }

  /**
   * Connexion de l'utilisateur avec un token
   * @param token Le token d'authentification de l'utilisateur
   */
  public logIn(token: string): void {
    console.log('Logging in with token:', token); // Log de connexion
    localStorage.setItem('token', token);
    this._isLoggedSubject$.next(true);
    this.initializeUser();
  }

  /**
   * Déconnexion de l'utilisateur
   */
  public logOut(): void {
    console.log('Logging out user'); // Log de déconnexion
    localStorage.removeItem('token');
    this._user.next(null);
    this._isLoggedSubject$.next(false);
    this._subscribedThemes.next([]); // On efface les thèmes abonnés
  }

  /**
   * Mise à jour des informations de l'utilisateur
   * @param updatedUser L'utilisateur mis à jour
   */
  public updateUser(updatedUser: User): void {
    console.log('Updating user:', updatedUser); // Log de mise à jour utilisateur
    this._user.next(updatedUser);
    this.loadUserThemes(updatedUser);
  }

  /**
   * Récupération des informations de l'utilisateur et de ses thèmes abonnés
   */
  public async initializeUser(): Promise<void> {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        console.log('Initializing user with token'); // Log lors de l'initialisation
        const user = await firstValueFrom(
          this.userService.getUser().pipe(
            catchError((error) => {
              if (error.status === 401) {
                localStorage.removeItem('token');
                this._isLoggedSubject$.next(false);
              }
              throw error;
            }),
          ),
        );

        if (user) {
          console.log('User initialized:', user); // Log de l'utilisateur récupéré
          this._user.next(user);
          this.loadUserThemes(user);
          this._isLoggedSubject$.next(true);
        }
      } catch (error) {
        console.error(
          "Erreur lors de l'initialisation de l'utilisateur",
          error,
        );
      }
    }
  }

  /**
   * Charge les thèmes auxquels l'utilisateur est abonné.
   * @param user L'utilisateur dont on souhaite récupérer les thèmes abonnés
   */
  private async loadUserThemes(user: User): Promise<void> {
    console.log('Loading themes for user:', user); // Log de début du chargement des thèmes
    try {
      const themes: Theme[] = await firstValueFrom(
        this.themeService.getThemes(),
      );
      console.log('All themes retrieved:', themes); // Log des thèmes récupérés

      const subscribedThemes: Theme[] = themes.filter((theme) =>
        (user.subscribedThemes ?? []).some(
          (subscribedTheme) => subscribedTheme.id === theme.id,
        ),
      );

      console.log('Subscribed themes:', subscribedThemes); // Log des thèmes abonnés
      this._subscribedThemes.next(subscribedThemes);
    } catch (error) {
      console.error('Erreur lors du chargement des thèmes abonnés', error);
    }
  }
}
