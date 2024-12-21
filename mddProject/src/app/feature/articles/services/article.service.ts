import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Article } from '../interfaces/article';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ArticleService {
  private baseUrl = `http://localhost:8080/api/`;  

  constructor(private http: HttpClient) {}

  // Récupérer tous les articles
  getAllArticles(): Observable<Article[]> {
    return this.http.get<Article[]>(`${this.baseUrl}articles`);  
  }

  // Récupérer un article par ID
  getArticleById(id: number): Observable<Article> {
    return this.http.get<Article>(`${this.baseUrl}article/${id}`);  
  }

  // Créer un article
  createArticle(article: Pick<Article, 'title' | 'content' | 'themeId'>): Observable<Article> {
    return this.http.post<Article>(`${this.baseUrl}article`, article, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
    });
  }
}
