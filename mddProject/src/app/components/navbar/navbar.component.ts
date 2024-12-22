import { Component, OnInit } from '@angular/core';
import { SessionService } from '../../feature/auth/services/session.service';
import { Router } from '@angular/router';

/**
 * Composant représentant la barre de navigation de l'application.
 * Gère l'état de connexion de l'utilisateur et les interactions avec le menu.
 */
@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit {
  /**
   * Indique si l'utilisateur est connecté.
   */
  isLoggedIn: boolean = false;

  /**
   * Indique l'état du menu (ouvert ou fermé).
   */
  isMenuOpen = false;

  /**
   * Permet de basculer l'état du menu entre ouvert et fermé.
   */
  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }

  /**
   * Constructeur injectant les services nécessaires.
   * @param sessionService Service pour gérer la session et l'état de l'utilisateur.
   * @param router Service de routage pour la navigation.
   */
  constructor(
    private sessionService: SessionService,
    private router: Router,
  ) {}

  /**
   * Méthode appelée après l'initialisation du composant.
   * S'abonne à l'observable `isLoggedIn$` pour suivre l'état de connexion de l'utilisateur.
   */
  ngOnInit(): void {
    this.sessionService.isLoggedIn$.subscribe((isLoggedIn) => {
      this.isLoggedIn = isLoggedIn;
    });
  }
}
