import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Article } from '../interfaces/article';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root', // Fournit ce service à l'ensemble de l'application
})
export class ArticleService {
  private baseUrl = `http://localhost:8080/api/`; // URL de base pour les appels API

  constructor(private http: HttpClient) {}

  /**
   * Récupère la liste de tous les articles depuis l'API.
   * @returns Observable contenant un tableau d'articles.
   */
  getAllArticles(): Observable<Article[]> {
    return this.http.get<Article[]>(`${this.baseUrl}articles`);
  }

  /**
   * Récupère les détails d'un article spécifique via son ID.
   * @param id - L'identifiant unique de l'article.
   * @returns Observable contenant les détails de l'article.
   */
  getArticleById(id: number): Observable<Article> {
    return this.http.get<Article>(`${this.baseUrl}article/${id}`);
  }

  /**
   * Envoie une requête pour créer un nouvel article dans l'API.
   * @param article - Objet contenant les données nécessaires à la création de l'article (titre, contenu, ID du thème).
   * @returns Observable contenant l'article créé.
   */
  createArticle(
    article: Pick<Article, 'title' | 'content' | 'themeId'>,
  ): Observable<Article> {
    return this.http.post<Article>(`${this.baseUrl}article`, article, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json', // Définit le format des données envoyées dans la requête
      }),
    });
  }
}
