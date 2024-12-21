import { Component, OnDestroy, OnInit } from '@angular/core';
import { ThemeService } from '../../../themes/service/theme.service';
import { ArticleService } from '../../services/article.service';
import { Theme } from '../../../themes/interface/theme';
import { Article } from '../../interfaces/article';
import { FormBuilder, FormGroup, Validators  } from '@angular/forms';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-article',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.css']
})
export class CreateArticleComponent implements OnInit {
  articleForm: FormGroup;
  themes: Theme[] = [];
  isLoading = false;
  errorMessage = '';
  

  constructor(
    private fb: FormBuilder,
    private articleService: ArticleService,
    private themeService: ThemeService,
    private snackbar: MatSnackBar,
    private router : Router
  ) {
    this.articleForm = this.fb.group({
      themeId: [null, Validators.required],
      title: ['', [Validators.required, Validators.minLength(3)]],
      content: ['', [Validators.required, Validators.minLength(10)]],
    });
  }


  ngOnInit(): void {
    this.themeService.getThemes().subscribe((themes) => (this.themes = themes));
  }

  onSubmit(): void {
    if (this.articleForm.invalid) {
      this.errorMessage = 'Veuillez remplir tous les champs correctement.';
      return;
    }

    const formValues = this.articleForm.value;
    const newArticle:|Pick<Article,'title' | 'content' | 'themeId'>  = {
      themeId: formValues.themeId,
      title: formValues.title,
      content: formValues.content,
  
    };

    this.isLoading = true;
    this.articleService.createArticle(newArticle).subscribe({
      next: (response) => {
        console.log('Article créé:', response);
        this.isLoading = false;
        this.errorMessage = '';
        this.snackbar.open("Article créé", "Fermer",{duration:3000});
        this.router.navigate(['/articles'])
      },
      error: (error) => {
        console.error('Erreur lors de la création de l\'article:', error);
        this.isLoading = false;
        
        this.snackbar.open("Une erreur s'est produite", "Fermer",{duration:3000});
      },
    });
  }
}

