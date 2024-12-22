import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Comment } from '../interfaces/article';

@Injectable({
  providedIn: 'root', // Fournit ce service à l'ensemble de l'application
})
export class CommentService {
  private apiUrl = `http://localhost:8080/api/comments`; // URL de base pour les commentaires

  constructor(private http: HttpClient) {}

  /**
   * Enregistre un nouveau commentaire dans l'API.
   * @param comment - Objet contenant les données du commentaire (contenu, ID de l'article, ID de l'utilisateur).
   * @returns Observable contenant le commentaire créé.
   */
  saveComment(
    comment: Pick<Comment, 'content' | 'articleId' | 'userId'>,
  ): Observable<Comment> {
    return this.http.post<Comment>(`${this.apiUrl}/comment`, comment);
  }

  /**
   * Récupère tous les commentaires associés à un article spécifique.
   * @param articleId - L'identifiant unique de l'article.
   * @returns Observable contenant un tableau de commentaires.
   */
  getCommentsByArticleId(articleId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.apiUrl}/article/${articleId}`);
  }
}
