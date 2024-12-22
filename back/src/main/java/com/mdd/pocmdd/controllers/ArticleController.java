package com.mdd.pocmdd.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import com.mdd.pocmdd.services.ArticleService;
import com.mdd.pocmdd.services.UserService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.mdd.pocmdd.dto.ArticleDTO;
import com.mdd.pocmdd.dto.UserDTO;
import com.mdd.pocmdd.payload.MessageResponse;
import lombok.extern.log4j.Log4j2;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Log4j2
@CrossOrigin(origins = "http://localhost:4200") // Permet l'accès depuis l'application frontend (Angular) à partir de ce
                                                // domaine.
@RestController // Indique que cette classe est un contrôleur REST qui gère les requêtes HTTP.
@RequestMapping("/api") // Définit le chemin de base de l'API.
public class ArticleController {

    @Autowired
    private ArticleService articleService; // Injection du service ArticleService qui contient la logique métier liée
                                           // aux articles.

    @Autowired
    private UserService userService; // Injection du service UserService qui permet de gérer les utilisateurs.

    /**
     * Récupère tous les articles en fonction des thèmes auxquels l'utilisateur est
     * abonné.
     * 
     * @param authentication L'authentification de l'utilisateur courant.
     * @return Une réponse contenant la liste des articles ou un message d'erreur.
     */
    @GetMapping("/articles")
    public ResponseEntity<?> getArticles(Authentication authentication) {
        log.info("Request to get all articles");

        // Récupère les thèmes auxquels l'utilisateur est abonné.
        UserDTO userDto = userService.findById(Long.valueOf(authentication.getName()));
        List<Long> themeids = userDto.getSubscribedThemeIds();

        try {
            // Récupère les articles liés aux thèmes de l'utilisateur.
            List<ArticleDTO> articles = articleService.findallArticleByTheme(themeids);
            return ResponseEntity.ok().body(articles); // Retourne la liste des articles.
        } catch (Exception e) {
            // En cas d'erreur, retourne un message d'erreur.
            return ResponseEntity.badRequest().body(new MessageResponse("Articles not found"));
        }
    }

    /**
     * Récupère un article spécifique par son ID.
     * 
     * @param id L'ID de l'article à récupérer.
     * @return Une réponse contenant l'article ou un message d'erreur.
     */
    @GetMapping("/article/{id}")
    public ResponseEntity<?> getArticleById(@PathVariable Long id) {
        log.info("Request to get article by id: {}", id);

        try {
            // Récupère l'article par son ID.
            ArticleDTO article = articleService.findArticleById(Long.valueOf(id));
            return ResponseEntity.ok().body(article); // Retourne l'article.
        } catch (Exception e) {
            // En cas d'erreur, retourne un message d'erreur.
            return ResponseEntity.badRequest().body(new MessageResponse("Article not found"));
        }
    }

    /**
     * Crée un nouvel article.
     * 
     * @param articleDTO     L'objet ArticleDTO contenant les informations de
     *                       l'article à créer.
     * @param authentication L'authentification de l'utilisateur courant.
     * @return Une réponse contenant l'article créé ou un message d'erreur.
     */
    @PostMapping("/article")
    public ResponseEntity<?> createArticle(@RequestBody ArticleDTO articleDTO, Authentication authentication) {
        log.info("Request to create article: {}", articleDTO);

        // Vérifie si les informations nécessaires à la création de l'article sont
        // présentes.
        if (articleDTO.getTitle() == null || articleDTO.getContent() == null || articleDTO.getThemeId() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Request body is empty!"));
        }

        try {
            // Crée un nouvel article avec les informations fournies.
            ArticleDTO newArticleDTO = articleService.createArticle(articleDTO, Long.valueOf(authentication.getName()));
            return ResponseEntity.ok().body(newArticleDTO); // Retourne l'article créé.
        } catch (Exception e) {
            // En cas d'erreur, retourne un message d'erreur.
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
