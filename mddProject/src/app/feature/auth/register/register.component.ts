// src/app/register/register.component.ts

import { Component } from '@angular/core';
import {AuthService} from '../services/auth/auth.service' // Import du service AuthService
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
  
  registerForm: FormGroup // Déclaration du formulaire
  errorMessage = '';

 

  private registerSubscription?: Subscription;  // Variable pour gérer l'abonnement

  constructor(private location: Location, private authService: AuthService, private router: Router,
    private snackbar: MatSnackBar,private fb: FormBuilder
   ) {
    // Initialisation du formulaire
    this.registerForm = this.fb.group({
      username:['',[Validators.required,Validators.minLength(3)]],
      email:['',[Validators.required, Validators.email]],
      password:['',[Validators.required, Validators.minLength(8)]]
    })
   }

  goBack(): void {
    this.router.navigate(['/home']); // Redirige vers la page d'accueil
  }

  /**
   * Gère la soumission du formulaire d'inscription.
   */
  onRegister(): void {
    if (this.registerForm.invalid) {
      this.errorMessage = 'Erreur dans le formulaire.';
      console.log('Form Value:', this.registerForm.value);
      return;
    }

    const newUser = this.registerForm.value;
    this.authService.register(newUser).subscribe({
      next: (response) => {
        //console.log('Inscription réussie:', response);
        console.log('Form Value:', this.registerForm.value);
        this.snackbar.open("Inscription réussie", "Fermer",{duration:3000});
        
        
        this.router.navigate(['/home']); // Redirige vers la page de connexion
      },
      error: (error) => {
        console.error('Erreur d\'inscription:', error);
        
        this.snackbar.open('Erreur d\'inscription', "Fermer",{duration:3000})
        
      }
    });
  }

  
}
