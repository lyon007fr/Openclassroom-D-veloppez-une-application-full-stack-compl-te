package com.mdd.pocmdd.services;

import lombok.extern.log4j.Log4j2;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.mdd.pocmdd.models.Article;
import com.mdd.pocmdd.models.Comment;
import com.mdd.pocmdd.models.User;
import com.mdd.pocmdd.payload.CommentPayload;
import com.mdd.pocmdd.dto.CommentDTO;
import com.mdd.pocmdd.repository.CommentRepository;
import com.mdd.pocmdd.repository.UserRespository;
import com.mdd.pocmdd.repository.ArticleRepository;

@Log4j2
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRespository userRepository;
    private final ArticleRepository articleRepository;

    /**
     * Constructeur du service pour injecter les dépendances nécessaires.
     * 
     * @param commentRepository Repository pour gérer les commentaires.
     * @param userRepository    Repository pour gérer les utilisateurs.
     * @param articleRepository Repository pour gérer les articles.
     */
    public CommentService(CommentRepository commentRepository, UserRespository userRepository,
            ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    /**
     * Récupère tous les commentaires associés à un article en fonction de son ID.
     * 
     * @param articleId ID de l'article pour lequel récupérer les commentaires.
     * @return Une liste de CommentDTO représentant les commentaires.
     */
    public List<CommentDTO> getCommentsByArticleId(Long articleId) {
        // Récupérer les commentaires liés à l'article.
        List<Comment> comments = commentRepository.findByArticleId(articleId);

        // Mapper les entités Comment vers des DTO.
        return comments.stream()
                .map(comment -> new CommentDTO(
                        comment.getId(), // ID du commentaire.
                        comment.getContent(), // Contenu du commentaire.
                        comment.getUser().getId(), // ID de l'utilisateur ayant posté le commentaire.
                        comment.getUser().getUsername(), // Nom de l'utilisateur.
                        comment.getArticle().getId())) // ID de l'article.
                .collect(Collectors.toList());
    }

    /**
     * Sauvegarde un commentaire en base de données.
     * 
     * @param commentPayload Données du commentaire à sauvegarder.
     * @return Un CommentDTO représentant le commentaire sauvegardé.
     */
    public CommentDTO saveComment(CommentPayload commentPayload) {
        // Récupérer l'article correspondant à l'ID fourni.
        Optional<Article> article = articleRepository.findById(commentPayload.getArticleId());

        // Récupérer l'utilisateur correspondant à l'ID fourni.
        Optional<User> user = userRepository.findById(commentPayload.getUserId());

        // Créer un nouvel objet Comment.
        Comment comment = new Comment();
        comment.setContent(commentPayload.getContent()); // Définir le contenu du commentaire.
        comment.setArticle(article.get()); // Associer l'article au commentaire.
        comment.setUser(user.get()); // Associer l'utilisateur au commentaire.
        comment.setAuthorName(user.get().getUsername()); // Définir le nom de l'auteur.

        // Sauvegarder le commentaire en base de données.
        comment = commentRepository.save(comment);

        // Retourner le commentaire sous forme de DTO.
        return new CommentDTO(
                comment.getId(), // ID du commentaire.
                comment.getContent(), // Contenu du commentaire.
                comment.getUser().getId(), // ID de l'utilisateur ayant posté le commentaire.
                comment.getUser().getUsername(), // Nom de l'utilisateur.
                comment.getArticle().getId()); // ID de l'article.
    }
}
