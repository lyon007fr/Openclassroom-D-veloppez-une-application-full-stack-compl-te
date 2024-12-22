package com.mdd.pocmdd.controllers;

import com.mdd.pocmdd.dto.CommentDTO;
import com.mdd.pocmdd.payload.CommentPayload;
import com.mdd.pocmdd.services.CommentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;

@Log4j2
@CrossOrigin(origins = "http://localhost:4200") // Permet l'accès depuis l'application front-end (Angular) à partir de
                                                // ce domaine.
@RestController // Indique que cette classe est un contrôleur REST.
@RequestMapping("/api/comments") // Définit le chemin de base pour les requêtes liées aux commentaires.
public class CommentController {

    private final CommentService commentService; // Injection du service CommentService qui contient la logique métier
                                                 // liée aux commentaires.

    // Constructeur pour l'injection du service CommentService.
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Récupère la liste des commentaires associés à un article.
     * 
     * @param articleId L'ID de l'article pour lequel récupérer les commentaires.
     * @return Une réponse contenant la liste des commentaires de l'article.
     */
    @GetMapping("/article/{articleId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByArticleId(@PathVariable Long articleId) {
        // Récupère les commentaires de l'article en utilisant le service.
        List<CommentDTO> comments = commentService.getCommentsByArticleId(articleId);

        // Retourne les commentaires dans la réponse avec un statut HTTP 200 (OK).
        return ResponseEntity.ok(comments);
    }

    /**
     * Sauvegarde un commentaire pour un article spécifique.
     * 
     * @param commentPayload L'objet contenant les informations du commentaire à
     *                       sauvegarder.
     * @return Une réponse contenant le commentaire sauvegardé.
     */
    @PostMapping("/comment")
    public ResponseEntity<CommentDTO> saveComment(@RequestBody CommentPayload commentPayload) {
        log.info("Request to save comment: {}", commentPayload); // Logge l'action de sauvegarde du commentaire.

        // Sauvegarde le commentaire en utilisant le service et récupère l'objet
        // CommentDTO créé.
        CommentDTO comment = commentService.saveComment(commentPayload);

        // Retourne le commentaire sauvegardé avec un statut HTTP 201 (Créé).
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }
}
