package com.mdd.pocmdd.services;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import com.mdd.pocmdd.repository.ArticleRepository;
import com.mdd.pocmdd.repository.UserRespository;
import com.mdd.pocmdd.dto.ArticleDTO;
import com.mdd.pocmdd.dto.CommentDTO;
import com.mdd.pocmdd.models.Article;
import com.mdd.pocmdd.mapper.ArticleMapper;
import com.mdd.pocmdd.repository.ThemeRepository;
import com.mdd.pocmdd.models.User;
import com.mdd.pocmdd.models.Theme;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ArticleService {

    private ArticleRepository articleRepository;
    private ArticleMapper articleMapper;
    private UserRespository userRepository;
    private ThemeRepository themeRepository;

    /**
     * Constructeur permettant d'injecter les dépendances nécessaires au service.
     * 
     * @param articleRepository Le repository pour les articles.
     * @param articleMapper     Le mapper pour convertir les entités Article en
     *                          ArticleDTO.
     * @param userRepository    Le repository pour les utilisateurs.
     * @param themeRepository   Le repository pour les thèmes.
     */
    public ArticleService(ArticleRepository articleRepository, ArticleMapper articleMapper,
            UserRespository userRepository, ThemeRepository themeRepository) {
        this.articleMapper = articleMapper;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.themeRepository = themeRepository;
    }

    /**
     * Méthode pour récupérer tous les articles associés à un ou plusieurs thèmes.
     * 
     * @param themeids Liste des IDs des thèmes.
     * @return Une liste d'objets ArticleDTO représentant les articles associés aux
     *         thèmes.
     */
    public List<ArticleDTO> findallArticleByTheme(List<Long> themeids) {

        // Liste pour stocker les ArticleDTO
        List<ArticleDTO> articleDTOs = new ArrayList<>();

        // Parcours de chaque thème pour récupérer les articles associés
        for (Long themeId : themeids) {
            // Récupération des articles associés au thème
            List<Article> articles = articleRepository.findByThemeId(themeId);

            // Parcours de chaque article
            for (Article article : articles) {
                // Vérification que l'article possède bien un auteur et un thème
                if (article.getUser() == null || article.getTheme() == null) {
                    throw new IllegalArgumentException("Author or theme not found");
                }
                // Création d'un nouvel ArticleDTO
                ArticleDTO articleDTO = articleMapper.toDto(article);
                articleDTO.setAuthorName(article.getUser().getUsername()); // Définir le nom de l'auteur
                articleDTO.setThemeTitle(article.getTheme().getTitle()); // Définir le titre du thème

                // Ajouter l'ArticleDTO à la liste
                articleDTOs.add(articleDTO);
            }
        }

        return articleDTOs;
    }

    /**
     * Méthode pour récupérer un article spécifique par son ID.
     * 
     * @param id L'ID de l'article.
     * @return Un ArticleDTO représentant l'article trouvé.
     */
    public ArticleDTO findArticleById(Long id) {
        // Récupération de l'article depuis la base de données
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));

        // Vérification que l'article possède bien un auteur et un thème
        if (article.getUser() == null || article.getTheme() == null) {
            throw new IllegalArgumentException("Author or theme not found");
        }

        // Création de l'ArticleDTO à partir de l'entité Article
        ArticleDTO articleDTO = articleMapper.toDto(article);
        articleDTO.setAuthorName(article.getUser().getUsername()); // Définir le nom de l'auteur
        articleDTO.setThemeTitle(article.getTheme().getTitle()); // Définir le titre du thème

        // Mapper les commentaires de l'article
        List<CommentDTO> commentDTOs = article.getComments().stream()
                .map(comment -> {
                    // Créer un CommentDTO pour chaque commentaire
                    CommentDTO commentDTO = new CommentDTO();
                    commentDTO.setId(comment.getId()); // ID du commentaire
                    commentDTO.setContent(comment.getContent()); // Contenu du commentaire
                    // Si l'utilisateur est présent, définir son ID et son nom
                    commentDTO.setUserId(comment.getUser() != null ? comment.getUser().getId() : null);
                    commentDTO.setAuthorName(comment.getUser() != null ? comment.getUser().getUsername() : null);
                    commentDTO.setArticleId(comment.getArticle() != null ? comment.getArticle().getId() : null);
                    return commentDTO;
                })
                .collect(Collectors.toList());

        // Affecter les commentaires à l'ArticleDTO
        articleDTO.setComments(commentDTOs);

        return articleDTO;
    }

    /**
     * Méthode pour créer un nouvel article.
     * 
     * @param articleDTO L'ArticleDTO contenant les informations de l'article.
     * @param userId     L'ID de l'utilisateur qui crée l'article.
     * @return Un ArticleDTO représentant l'article créé.
     */
    public ArticleDTO createArticle(ArticleDTO articleDTO, Long userId) {
        // Vérification que l'articleDTO n'est pas null
        if (articleDTO == null) {
            throw new IllegalArgumentException("Article empty");
        }

        // Récupération de l'utilisateur et du thème à partir des IDs fournis
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Theme theme = themeRepository.findById(articleDTO.getThemeId())
                .orElseThrow(() -> new RuntimeException("Theme not found"));

        // Création d'une nouvelle entité Article à partir du DTO
        Article article = articleMapper.toEntity(articleDTO);

        article.setUser(user); // Associer l'utilisateur à l'article
        article.setTheme(theme); // Associer le thème à l'article
        article.setCreatedAt(LocalDateTime.now()); // Définir la date de création de l'article

        // Sauvegarde de l'article dans la base de données
        articleRepository.save(article);

        // Création du ArticleDTO à partir de l'article sauvegardé
        ArticleDTO newArticleDTO = articleMapper.toDto(article);

        newArticleDTO.setAuthorName(user.getUsername()); // Définir le nom de l'auteur
        newArticleDTO.setThemeTitle(theme.getTitle()); // Définir le titre du thème
        newArticleDTO.setCreatedAt(article.getCreatedAt()); // Définir la date de création

        return newArticleDTO;
    }

}
