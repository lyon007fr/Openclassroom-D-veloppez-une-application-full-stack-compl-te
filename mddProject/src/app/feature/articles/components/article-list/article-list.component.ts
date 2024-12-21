import { Component, OnInit } from '@angular/core';
import { ArticleService } from '../../services/article.service';  // Import du service
import { Article } from "../../interfaces/article"; // Modèle pour représenter un article

@Component({
  selector: 'app-article-list',
  templateUrl: './article-list.component.html',
  styleUrls: ['./article-list.component.css']
})
export class ArticleListComponent implements OnInit {

  articles: Article[] = [];  // Liste des articles
  sortedArticles: Article[] = []; // Liste triée à afficher
  selectedSort: string = 'title'; // Critère de tri sélectionné ('title' ou 'date')

  constructor(private articleService: ArticleService) { }

  
  // Récupérer les articles au chargement du composant
  ngOnInit(): void {
      this.articleService.getAllArticles().subscribe(
        (articles) => {
          this.articles = articles;
          this.sortArticles(); // Trier les articles dès qu'ils sont récupérés
        },
        (error) => {
          console.error('Erreur lors de la récupération des articles:', error);
        }
      );
  }

  // Trie les articles en fonction du critère sélectionné
  sortArticles(): void {
    this.sortedArticles = [...this.articles].sort((a, b) => {
      if (this.selectedSort === 'title') {
        return a.title.localeCompare(b.title); // Trie alphabétique
      } else if (this.selectedSort === 'date') {
        return new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime(); // Trie chronologique
      }
      return 0;
    });
  }

}
