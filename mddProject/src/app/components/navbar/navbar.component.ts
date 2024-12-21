import { Component, OnInit } from '@angular/core';
import { SessionService } from '../../feature/auth/services/session.service'; 
import { Router } from '@angular/router';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  isLoggedIn: boolean = false;
  isMenuOpen = false; // État du menu

  
  // Fonction pour basculer l'état du menu
  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }

  constructor(private sessionService: SessionService, private router: Router) {}

  
  
  ngOnInit(): void {
    // On s'abonne à l'observable isLoggedIn$
    this.sessionService.isLoggedIn$.subscribe(isLoggedIn => {
      this.isLoggedIn = isLoggedIn;
    });

  
  }
    
}

  