import { Component, OnInit, Input } from '@angular/core';
import { ThemeService } from '../../themes/service/theme.service';
import { Theme } from '../../themes/interface/theme';
import { UserService } from '../../me/service/userservice.service';
import { User } from '../../me/interface/user';

@Component({
  selector: 'app-theme',
  templateUrl: './theme.component.html',
  styleUrls: ['./theme.component.css'],
})
export class ThemeComponent implements OnInit {
  themes: Theme[] = []; // Liste des thèmes disponibles
  subscribedThemes: number[] = []; // IDs des thèmes auxquels l'utilisateur est abonné
  user: User | null = null; // Utilisateur connecté

  constructor(
    private userService: UserService,
    private themeService: ThemeService,
  ) {}

  /**
   * Méthode d'initialisation du composant.
   * Charge l'utilisateur connecté et la liste des thèmes disponibles.
   */
  ngOnInit(): void {
    this.loadUser();
    this.loadThemes();
  }

  /**
   * Charge l'utilisateur connecté et initialise ses abonnements.
   * Récupère les IDs des thèmes abonnés et les stocke dans `subscribedThemes`.
   */
  private loadUser(): void {
    this.userService.getUser().subscribe(
      (user: User) => {
        this.user = user;
        this.subscribedThemes = user.subscribedThemes.map((theme) => theme.id); // Récupère les IDs des thèmes abonnés
      },
      (error) => {
        console.error('Error loading user:', error);
      },
    );
  }

  /**
   * Charge la liste des thèmes disponibles depuis l'API.
   * Met à jour la liste `themes` avec les thèmes récupérés.
   */
  private loadThemes(): void {
    this.themeService.getThemes().subscribe(
      (themes: Theme[]) => {
        this.themes = themes;
      },
      (error) => {
        console.error('Error loading themes:', error);
      },
    );
  }

  /**
   * Bascule l'abonnement à un thème.
   * Si l'utilisateur est déjà abonné, il se désabonne, sinon il s'abonne.
   *
   * @param themeId L'ID du thème auquel l'utilisateur souhaite s'abonner ou se désabonner.
   */
  toggleSubscription(themeId: number): void {
    if (this.isSubscribed(themeId)) {
      this.unsubscribeFromTheme(themeId);
    } else {
      this.subscribeToTheme(themeId);
    }
  }

  /**
   * S'abonne à un thème.
   * Ajoute l'ID du thème à la liste `subscribedThemes` de l'utilisateur.
   *
   * @param themeId L'ID du thème auquel l'utilisateur souhaite s'abonner.
   */
  private subscribeToTheme(themeId: number): void {
    if (!this.user) {
      console.error('User is not loaded.');
      return;
    }

    this.userService.subscribeToTheme(themeId).subscribe(
      (updatedUser: User) => {
        this.subscribedThemes.push(themeId); // Ajoute le thème à la liste des abonnements
        console.log(`Successfully subscribed to theme ID ${themeId}`);
      },
      (error) => {
        console.error(`Error subscribing to theme ID ${themeId}:`, error);
      },
    );
  }

  /**
   * Se désabonne d'un thème.
   * Supprime l'ID du thème de la liste `subscribedThemes` de l'utilisateur.
   *
   * @param themeId L'ID du thème auquel l'utilisateur souhaite se désabonner.
   */
  private unsubscribeFromTheme(themeId: number): void {
    if (!this.user) {
      console.error('User is not loaded.');
      return;
    }

    this.userService.unsubscribeFromTheme(themeId).subscribe(
      (updatedUser: User) => {
        this.subscribedThemes = this.subscribedThemes.filter(
          (id) => id !== themeId,
        ); // Supprime le thème des abonnements
        console.log(`Successfully unsubscribed from theme ID ${themeId}`);
      },
      (error) => {
        console.error(`Error unsubscribing from theme ID ${themeId}:`, error);
      },
    );
  }

  /**
   * Vérifie si l'utilisateur est abonné à un thème spécifique.
   *
   * @param themeId L'ID du thème à vérifier.
   * @returns {boolean} `true` si l'utilisateur est abonné au thème, sinon `false`.
   */
  isSubscribed(themeId: number): boolean {
    return this.subscribedThemes.includes(themeId);
  }
}
