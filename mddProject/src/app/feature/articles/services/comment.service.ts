import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Comment } from '../interfaces/article';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private apiUrl = `http://localhost:8080/api/comments`;  

  constructor(private http: HttpClient) {}

  // Enregistrer un commentaire
  saveComment(comment: Pick<Comment, 'content' | 'articleId'| 'userId'>): Observable<Comment> { 
    return this.http.post<Comment>(`${this.apiUrl}/comment`, comment);
  }

  // Récupérer les commentaires d'un article
  getCommentsByArticleId(articleId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.apiUrl}/article/${articleId}`);
  }
}
