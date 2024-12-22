import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ArticleService } from '../../services/article.service';
import { CommentService } from '../../services/comment.service';
import { Article, Comment } from '../../interfaces/article';
import { SessionService } from 'src/app/feature/auth/services/session.service';
import { User } from 'src/app/feature/me/interface/user';

/**
 * Composant pour afficher les détails d'un article ainsi que ses commentaires.
 */
@Component({
  selector: 'app-article-detail',
  templateUrl: './article-detail.component.html',
  styleUrls: ['./article-detail.component.css'],
})
export class ArticleDetailComponent implements OnInit {
  /**
   * Article actuellement affiché.
   */
  article: Article | null = null;

  /**
   * Indique si les données sont en cours de chargement.
   */
  isLoading: boolean = true;

  /**
   * Contenu du nouveau commentaire en cours de rédaction.
   */
  newCommentContent: string = '';

  /**
   * Identifiant de l'article.
   */
  articleId: number = 0;

  /**
   * Utilisateur actuellement connecté.
   */
  currentUser: User | null = null;

  /**
   * Constructeur injectant les services nécessaires.
   * @param articleService Service pour gérer les articles.
   * @param commentService Service pour gérer les commentaires.
   * @param route Service pour accéder aux paramètres de la route.
   * @param sessionService Service pour gérer la session de l'utilisateur.
   */
  constructor(
    private articleService: ArticleService,
    private commentService: CommentService,
    private route: ActivatedRoute,
    private sessionService: SessionService,
  ) {}

  /**
   * Méthode du cycle de vie appelée à l'initialisation du composant.
   * Récupère les détails de l'article et les commentaires associés,
   * et souscrit aux changements de l'utilisateur connecté.
   */
  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.articleId = id;
      this.fetchArticle(id);
      this.fetchComments(id);
    }

    // Souscription pour suivre l'utilisateur actuellement connecté
    this.sessionService.user$.subscribe((user) => {
      this.currentUser = user;
    });
  }

  /**
   * Récupère les détails d'un article à partir de son identifiant.
   * @param id Identifiant de l'article.
   */
  fetchArticle(id: number): void {
    this.articleService.getArticleById(id).subscribe({
      next: (data) => {
        this.article = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error("Erreur lors de la récupération de l'article:", err);
        this.isLoading = false;
      },
    });
  }

  /**
   * Récupère les commentaires associés à un article.
   * @param articleId Identifiant de l'article.
   */
  fetchComments(articleId: number): void {
    this.commentService.getCommentsByArticleId(articleId).subscribe({
      next: (comments) => {
        if (this.article) {
          this.article.comments = comments;
        }
      },
      error: (err) => {
        console.error('Erreur lors de la récupération des commentaires:', err);
      },
    });
  }

  /**
   * Soumet un nouveau commentaire pour l'article affiché.
   * Vérifie que l'utilisateur est connecté et que le contenu du commentaire est renseigné.
   */
  submitComment(): void {
    if (this.newCommentContent && this.article && this.currentUser) {
      const newComment: Pick<Comment, 'content' | 'articleId' | 'userId'> = {
        content: this.newCommentContent,
        articleId: this.articleId,
        userId: this.currentUser.id,
      };

      this.commentService.saveComment(newComment).subscribe({
        next: (comment) => {
          if (this.article) {
            this.article.comments.push(comment);
            this.newCommentContent = '';
          }
        },
        error: (err) => {
          console.error("Erreur lors de l'ajout du commentaire:", err);
        },
      });
    }
  }
}
