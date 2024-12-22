import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth/auth.service'; // Import du service AuthService
import { SessionService } from '../services/session.service'; // Import du service SessionService
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  loginForm: FormGroup; // Déclaration du formulaire
  errorMessage: string = ''; // Message d'erreur à afficher en cas de problème

  constructor(
    private authService: AuthService,
    private sessionService: SessionService,
    private router: Router,
    private fb: FormBuilder,
  ) {
    // Initialisation du formulaire avec des validations
    this.loginForm = this.fb.group({
      usernameOrEmail: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(8)]],
    });
  }

  /**
   * Gère la soumission du formulaire de connexion.
   * Si le formulaire est valide, appelle le service d'authentification pour tenter de connecter l'utilisateur.
   * En cas de succès, le token est sauvegardé et l'utilisateur est redirigé vers la page principale.
   * En cas d'échec, un message d'erreur est affiché.
   */
  onLogin(): void {
    // Vérifie si le formulaire est invalide et affiche un message d'erreur
    if (this.loginForm.invalid) {
      this.errorMessage = 'Veuillez remplir tous les champs.';
      return;
    }

    // Récupère les valeurs du formulaire
    const formLogin = this.loginForm.value;

    // Appelle le service d'authentification pour tenter la connexion
    this.authService
      .login(formLogin.usernameOrEmail, formLogin.password)
      .subscribe({
        next: (response) => {
          console.log('Connexion réussie:');
          // Si un token est retourné, l'utilisateur est connecté
          if (response.token) {
            this.sessionService.logIn(response.token); // Sauvegarde du token dans le service de session
            this.router.navigate(['/articles']); // Redirige vers la page des articles
          } else {
            this.errorMessage =
              "Token non fourni. Veuillez contacter l'administrateur."; // Si le token est absent
          }
        },
        error: (error) => {
          console.error('Erreur de connexion:', error);
          this.errorMessage = 'Identifiants incorrects. Veuillez réessayer.'; // Message d'erreur en cas d'échec de la connexion
        },
      });
  }

  /**
   * Gère le clic sur la flèche pour revenir à la page d'accueil.
   * Utilise le routeur pour naviguer vers la route '/home'.
   */
  goBack(): void {
    console.log('Redirection en arrière vers /home');
    this.router.navigate(['/home']); // Redirige vers la page d'accueil
  }
}
