import { Component, OnInit } from '@angular/core';
import { UserService } from '../../service/userservice.service';
import { SessionService } from '../../../auth/services/session.service';
import { Router } from '@angular/router';
import { Theme } from '../../../themes/interface/theme';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ThemeService } from '../../../themes/service/theme.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.css'],
})
export class MeComponent implements OnInit {
  profileForm: FormGroup; // Formulaire pour gérer le profil de l'utilisateur
  subscribedThemes: Theme[] = []; // Liste des thèmes abonnés par l'utilisateur

  constructor(
    private fb: FormBuilder,
    private userService: UserService, // Service pour la gestion de l'utilisateur
    private sessionService: SessionService, // Service pour gérer la session de l'utilisateur
    private themeService: ThemeService, // Service pour récupérer les thèmes
    private router: Router, // Service pour la navigation
    private snackbar: MatSnackBar, // Service pour afficher des notifications
  ) {
    // Initialisation du formulaire avec validation pour username et email
    this.profileForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
    });
  }

  /**
   * Méthode de cycle de vie Angular, appelée lors de l'initialisation du composant.
   * Récupère les informations de l'utilisateur et les thèmes abonnés via les services.
   */
  ngOnInit(): void {
    // Récupération des informations de l'utilisateur
    this.userService.getUser().subscribe(
      (user) => {
        console.log('Utilisateur récupéré', user);
        // Remplissage du formulaire avec les informations de l'utilisateur
        this.profileForm.patchValue({
          username: user.username,
          email: user.email,
        });

        // Récupération des thèmes abonnés
        this.subscribedThemes = user.subscribedThemes || [];
      },
      (error) => {
        console.error("Erreur lors de la récupération de l'utilisateur", error);
      },
    );
  }

  /**
   * Sauvegarde les informations du profil de l'utilisateur après validation du formulaire.
   * Effectue un appel API pour mettre à jour les informations de l'utilisateur.
   */
  saveProfile(): void {
    if (this.profileForm.valid) {
      const updatedUser = {
        username: this.profileForm.value.username,
        email: this.profileForm.value.email,
      };

      // Appel API pour mettre à jour l'utilisateur
      this.userService.updateUser(updatedUser).subscribe(
        (updatedUser) => {
          console.log('Profil mis à jour', updatedUser);
          this.snackbar.open('Mise à jour réussie', 'Fermer', {
            duration: 3000,
          });
        },

        (error) => {
          console.error('Erreur lors de la mise à jour du profil', error);
          this.snackbar.open('Erreur lors de la mise à jour', 'Fermer', {
            duration: 3000,
          });
        },
      );
    } else {
      console.log('Le formulaire est invalide');
    }
  }

  /**
   * Permet à l'utilisateur de se désabonner d'un thème spécifique.
   * Effectue un appel API pour se désabonner et met à jour la liste des thèmes abonnés.
   *
   * @param themeId L'ID du thème auquel l'utilisateur souhaite se désabonner.
   */
  unsubscribeFromTheme(themeId: number): void {
    this.themeService.unsubscribeFromTheme(themeId).subscribe(
      () => {
        // Mise à jour de la liste des thèmes abonnés après la désinscription
        this.subscribedThemes = this.subscribedThemes.filter(
          (theme) => theme.id !== themeId,
        );
        console.log('Désabonné du thème avec ID: ', themeId);
      },
      (error) => {
        console.error('Erreur lors de la désinscription', error);
      },
    );
  }

  /**
   * Permet à l'utilisateur de se déconnecter de la session active.
   * Effectue un appel au service de déconnexion et redirige l'utilisateur vers la page de connexion.
   */
  logout(): void {
    // Appel du service de déconnexion
    this.sessionService.logOut();
    console.log('Déconnecté');
    this.router.navigate(['/home']); // Rediriger vers la page de connexion
  }
}
