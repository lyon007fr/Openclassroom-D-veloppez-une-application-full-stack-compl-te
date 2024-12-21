import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ArticleService } from '../../services/article.service';
import { CommentService } from '../../services/comment.service';
import { Article, Comment } from '../../interfaces/article';
import { SessionService } from 'src/app/feature/auth/services/session.service';
import { User } from 'src/app/feature/me/interface/user';

@Component({
  selector: 'app-article-detail',
  templateUrl: './article-detail.component.html',
  styleUrls: ['./article-detail.component.css']
})
export class ArticleDetailComponent implements OnInit {
  article: Article | null = null;
  isLoading: boolean = true;
  newCommentContent: string = ''; // Contenu du nouveau commentaire
  articleId: number = 0; // ID de l'article
  currentUser: User | null = null; // Utilisateur actuellement connecté

  constructor(
    private articleService: ArticleService,
    private commentService: CommentService,
    private route: ActivatedRoute,
    private sessionService: SessionService // Injecte ton SessionService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.articleId = id;
      this.fetchArticle(id);
      this.fetchComments(id);
    }

    

    // Souscrire aux changements de l'utilisateur connecté
    this.sessionService.user$.subscribe(user => {
      this.currentUser = user; // Mettre à jour l'utilisateur actuel
    });
  }

  fetchArticle(id: number): void {
    this.articleService.getArticleById(id).subscribe({
      next: (data) => {
        this.article = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erreur lors de la récupération de l\'article:', err);
        this.isLoading = false;
      }
    });
  }

  fetchComments(articleId: number): void {
    this.commentService.getCommentsByArticleId(articleId).subscribe({
      next: (comments) => {
        if (this.article) {
          this.article.comments = comments; // Met à jour les commentaires de l'article
        }
      },
      error: (err) => {
        console.error('Erreur lors de la récupération des commentaires:', err);
      }
    });
  }

  submitComment(): void {
    if (this.newCommentContent && this.article && this.currentUser) {
      const newComment: Pick<Comment, 'content' | 'articleId' | 'userId'> = {
        content: this.newCommentContent,
        articleId: this.articleId, // ID de l'article auquel le commentaire appartient
        userId: this.currentUser.id // Utilise l'ID de l'utilisateur connecté
      };
  
      this.commentService.saveComment(newComment).subscribe({
        next: (comment) => {
          if (this.article) {
            this.article.comments.push(comment); // Ajouter le commentaire à la liste
            this.newCommentContent = ''; // Réinitialiser le champ de commentaire
          }
        },
        error: (err) => {
          console.error('Erreur lors de l\'ajout du commentaire:', err);
        }
      });
    }
  }
}
