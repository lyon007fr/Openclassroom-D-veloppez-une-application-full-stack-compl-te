import { Component } from '@angular/core';
import { Router } from '@angular/router';
import {AuthService} from '../services/auth/auth.service' // Import du service AuthService
import {SessionService} from '../services/session.service' // Import du service SessionService
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  loginForm : FormGroup // Déclaration du formulaire
  errorMessage: string = ''; // Message d'erreur à afficher en cas de problème

  constructor(private authService: AuthService,
              private sessionService: SessionService,
              private router: Router, private fb: FormBuilder ) {
                //Initialisation du formulaire
                this.loginForm = this.fb.group({
                  usernameOrEmail: ['',[Validators.required, Validators.minLength(3)]],
                  password: ['', [Validators.required, Validators.minLength(8)]]
                })
              }

  

  /**
   * Gère la soumission du formulaire de connexion.
   */
  onLogin(): void {
    if (this.loginForm.invalid) {
      this.errorMessage = 'Veuillez remplir tous les champs.';
      return;
    }

    
    // Appelle le service d'authentification pour se connecter
    const formLogin = this.loginForm.value
    this.authService.login(formLogin.usernameOrEmail,formLogin.password).subscribe({
      next: (response) => {
        console.log('Connexion réussie:');
        if (response.token) {
          this.sessionService.logIn(response.token); // Sauvegarde du token
          this.router.navigate(['/articles']); // Redirige vers la page principale
        } else {
          this.errorMessage = 'Token non fourni. Veuillez contacter l\'administrateur.';
        }
      },
      error: (error) => {
        console.error('Erreur de connexion:', error);
        this.errorMessage = 'Identifiants incorrects. Veuillez réessayer.';
      }
    });
  }

  /**
   * Gère le clic sur la flèche pour revenir en arrière.
   */
  goBack(): void {
    console.log('Redirection en arrière vers /home');
    this.router.navigate(['/home']); // Redirige vers la page d'accueil
  }
}
