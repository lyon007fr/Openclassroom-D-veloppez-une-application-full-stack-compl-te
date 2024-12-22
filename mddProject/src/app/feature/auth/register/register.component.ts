// src/app/register/register.component.ts

import { Component } from '@angular/core';
import { AuthService } from '../services/auth/auth.service'; // Import du service AuthService
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Location } from '@angular/common'; // Import de Location
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
  registerForm: FormGroup; // Déclaration du formulaire
  errorMessage = ''; // Message d'erreur à afficher en cas de problème

  constructor(
    private location: Location,
    private authService: AuthService,
    private router: Router,
    private snackbar: MatSnackBar,
    private fb: FormBuilder,
  ) {
    // Initialisation du formulaire avec des validations pour chaque champ
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]], // Validation du nom d'utilisateur
      email: ['', [Validators.required, Validators.email]], // Validation de l'email
      password: ['', [Validators.required, Validators.minLength(8)]], // Validation du mot de passe
    });
  }

  /**
   * Permet à l'utilisateur de revenir à la page d'accueil.
   * Utilise le routeur pour naviguer vers la route '/home'.
   */
  goBack(): void {
    this.router.navigate(['/home']); // Redirige vers la page d'accueil
  }

  /**
   * Gère la soumission du formulaire d'inscription.
   * Si le formulaire est valide, il appelle le service d'inscription pour enregistrer l'utilisateur.
   * En cas de succès, un message de confirmation est affiché et l'utilisateur est redirigé vers la page d'accueil.
   * En cas d'échec, un message d'erreur est affiché.
   */
  onRegister(): void {
    // Vérifie si le formulaire est invalide et affiche un message d'erreur
    if (this.registerForm.invalid) {
      this.errorMessage = 'Erreur dans le formulaire.';
      console.log('Form Value:', this.registerForm.value); // Log des valeurs du formulaire pour débogage
      return;
    }

    // Récupère les valeurs du formulaire pour l'enregistrement
    const newUser = this.registerForm.value;

    // Appelle le service d'authentification pour enregistrer l'utilisateur
    this.authService.register(newUser).subscribe({
      next: (response) => {
        // En cas de succès, affiche un message de confirmation
        console.log('Form Value:', this.registerForm.value);
        this.snackbar.open('Inscription réussie', 'Fermer', { duration: 3000 });

        // Redirige l'utilisateur vers la page d'accueil après une inscription réussie
        this.router.navigate(['/home']);
      },
      error: (error) => {
        // En cas d'erreur, affiche un message d'erreur et log l'erreur pour débogage
        console.error("Erreur d'inscription:", error);
        this.snackbar.open("Erreur d'inscription", 'Fermer', {
          duration: 3000,
        });
      },
    });
  }
}
