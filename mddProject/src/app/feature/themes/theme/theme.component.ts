import { Component, OnInit, Input } from '@angular/core';
import { ThemeService } from '../../themes/service/theme.service';
import { Theme } from '../../themes/interface/theme';
import { UserService } from '../../me/service/userservice.service';
import { User } from '../../me/interface/user';


@Component({
  selector: 'app-theme',
  templateUrl: './theme.component.html',
  styleUrls: ['./theme.component.css']
})
export class ThemeComponent implements OnInit {
  themes: Theme[] = []; // Liste des thèmes disponibles
  subscribedThemes: number[] = []; // IDs des thèmes auxquels l'utilisateur est abonné
  user: User | null = null; // Utilisateur connecté

  constructor(
    private userService: UserService,
    private themeService: ThemeService
  ) {}

  ngOnInit(): void {
    this.loadUser();
    this.loadThemes();
  }

  // Charge l'utilisateur connecté et initialise ses abonnements
  private loadUser(): void {
    this.userService.getUser().subscribe(
      (user: User) => {
        this.user = user;
        this.subscribedThemes = user.subscribedThemes.map(theme => theme.id); // Récupère les IDs des thèmes abonnés
      },
      (error) => {
        console.error('Error loading user:', error);
      }
    );
  }

  // Charge la liste des thèmes disponibles
  private loadThemes(): void {
    this.themeService.getThemes().subscribe(
      (themes: Theme[]) => {
        this.themes = themes;
      },
      (error) => {
        console.error('Error loading themes:', error);
      }
    );
  }

  // Basculer entre abonnement et désabonnement
  toggleSubscription(themeId: number): void {
    if (this.isSubscribed(themeId)) {
      this.unsubscribeFromTheme(themeId);
    } else {
      this.subscribeToTheme(themeId);
    }
  }

  // S'abonner à un thème
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
      }
    );
  }

  // Se désabonner d'un thème
  private unsubscribeFromTheme(themeId: number): void {
    if (!this.user) {
      console.error('User is not loaded.');
      return;
    }

    this.userService.unsubscribeFromTheme(themeId).subscribe(
      (updatedUser: User) => {
        this.subscribedThemes = this.subscribedThemes.filter(id => id !== themeId); // Supprime le thème des abonnements
        console.log(`Successfully unsubscribed from theme ID ${themeId}`);
      },
      (error) => {
        console.error(`Error unsubscribing from theme ID ${themeId}:`, error);
      }
    );
  }

  // Vérifie si l'utilisateur est abonné à un thème
  isSubscribed(themeId: number): boolean {
    return this.subscribedThemes.includes(themeId);
  }
}