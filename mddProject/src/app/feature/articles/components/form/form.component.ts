import { Component, OnInit } from '@angular/core';
import { ThemeService } from '../../../themes/service/theme.service'; // Service pour gérer les thèmes
import { ArticleService } from '../../services/article.service'; // Service pour gérer les articles
import { Theme } from '../../../themes/interface/theme'; // Interface pour représenter un thème
import { Article } from '../../interfaces/article'; // Interface pour représenter un article
import { FormBuilder, FormGroup, Validators } from '@angular/forms'; // Outils pour construire et valider les formulaires
import { MatSnackBar } from '@angular/material/snack-bar'; // Composant Material pour afficher des notifications
import { Router } from '@angular/router'; // Service pour gérer la navigation entre les pages

@Component({
  selector: 'app-create-article',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.css'],
})
export class CreateArticleComponent implements OnInit {
  articleForm: FormGroup; // Formulaire pour créer un article
  themes: Theme[] = []; // Liste des thèmes disponibles
  isLoading = false; // Indicateur de chargement pour désactiver les actions pendant une requête
  errorMessage = ''; // Message d'erreur à afficher en cas de problème

  constructor(
    private fb: FormBuilder, // Builder pour initialiser le formulaire
    private articleService: ArticleService, // Service pour créer un article
    private themeService: ThemeService, // Service pour récupérer les thèmes
    private snackbar: MatSnackBar, // Notifications Material
    private router: Router, // Service de navigation
  ) {
    // Initialisation du formulaire avec des champs requis et des règles de validation
    this.articleForm = this.fb.group({
      themeId: [null, Validators.required], // ID du thème, requis
      title: ['', [Validators.required, Validators.minLength(3)]], // Titre, requis avec une longueur minimale
      content: ['', [Validators.required, Validators.minLength(10)]], // Contenu, requis avec une longueur minimale
    });
  }

  ngOnInit(): void {
    // Récupération des thèmes depuis le service et mise à jour de la liste
    this.themeService.getThemes().subscribe((themes) => (this.themes = themes));
  }

  /**
   * Méthode appelée lors de la soumission du formulaire.
   * Vérifie la validité du formulaire et crée un article si toutes les conditions sont remplies.
   */
  onSubmit(): void {
    if (this.articleForm.invalid) {
      // Affiche un message d'erreur si le formulaire est invalide
      this.errorMessage = 'Veuillez remplir tous les champs correctement.';
      return;
    }

    const formValues = this.articleForm.value; // Valeurs du formulaire
    const newArticle: Pick<Article, 'title' | 'content' | 'themeId'> = {
      themeId: formValues.themeId, // ID du thème sélectionné
      title: formValues.title, // Titre de l'article
      content: formValues.content, // Contenu de l'article
    };

    this.isLoading = true; // Active l'indicateur de chargement
    this.articleService.createArticle(newArticle).subscribe({
      next: (response) => {
        // Succès de la création
        console.log('Article créé:', response);
        this.isLoading = false;
        this.errorMessage = ''; // Réinitialise le message d'erreur
        this.snackbar.open('Article créé', 'Fermer', { duration: 3000 }); // Affiche une notification de succès
        this.router.navigate(['/articles']); // Redirige vers la liste des articles
      },
      error: (error) => {
        // Erreur lors de la création
        console.error("Erreur lors de la création de l'article:", error);
        this.isLoading = false;
        this.snackbar.open("Une erreur s'est produite", 'Fermer', {
          duration: 3000, // Affiche une notification d'erreur
        });
      },
    });
  }
}
