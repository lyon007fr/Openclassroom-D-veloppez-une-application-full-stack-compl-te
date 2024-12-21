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


    public ArticleService(ArticleRepository articleRepository, ArticleMapper articleMapper, UserRespository userRepository, ThemeRepository themeRepository) {
        this.articleMapper = articleMapper;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.themeRepository = themeRepository;
      
    
    }


    //get all articles
    public List<ArticleDTO> findallArticleByTheme(List<Long> themeids) {
        
        //variable to store the articlesdto
        List<ArticleDTO> articleDTOs = new ArrayList<>();
        //loop through the articles
        for (Long themeId : themeids) {
            //get the articles
            List<Article> articles = articleRepository.findByThemeId(themeId);
            //loop through the articles
            for (Article article : articles) {
                //check if the author is null
                if (article.getUser() == null || article.getTheme() == null) {
                    throw new IllegalArgumentException("Author or theme not found");
                }
                //create a new articleDTO
                ArticleDTO articleDTO = articleMapper.toDto(article);
                articleDTO.setAuthorName(article.getUser().getUsername());
                articleDTO.setThemeTitle(article.getTheme().getTitle());
                
                //add the articleDTO to the list
                articleDTOs.add(articleDTO);
            }
        }


        return articleDTOs;
    }

    //get one article
    public ArticleDTO findArticleById(Long id) {
        //get the article
        Article article = articleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Article not found"));
        //check if the author is null
        if (article.getUser() == null || article.getTheme() == null) {
            throw new IllegalArgumentException("Author or theme not found");
        }
        //create a new articleDTO
        ArticleDTO articleDTO = articleMapper.toDto(article);
        articleDTO.setAuthorName(article.getUser().getUsername());
        articleDTO.setThemeTitle(article.getTheme().getTitle());

         // Mapper les commentaires de l'article
    List<CommentDTO> commentDTOs = article.getComments().stream()
        .map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setId(comment.getId());
            commentDTO.setContent(comment.getContent());
            commentDTO.setUserId(comment.getUser() != null ? comment.getUser().getId() : null); // Assurez-vous que l'utilisateur est présent
            commentDTO.setAuthorName(comment.getUser() != null ? comment.getUser().getUsername() : null); // Assurez-vous que l'utilisateur est présent
            commentDTO.setArticleId(comment.getArticle() != null ? comment.getArticle().getId() : null);
            return commentDTO;
        })
        .collect(Collectors.toList());

        // Affecter les commentaires à l'article DTO
        articleDTO.setComments(commentDTOs);

        return articleDTO;
    }

    //create an article
    public ArticleDTO createArticle(ArticleDTO articleDTO, Long userId) {
        //check if the articleDTO is null
        if (articleDTO == null) {
            throw new IllegalArgumentException("Article empty");
        }
        // get the user and theme
        User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        Theme theme = themeRepository.findById(articleDTO.getThemeId())
                    .orElseThrow(() -> new RuntimeException("Theme not found"));
        
        //create a new article
        Article article = articleMapper.toEntity(articleDTO);
        
        
        article.setUser(user);
        article.setTheme(theme); 
        article.setCreatedAt(LocalDateTime.now());       
        //save the article
        articleRepository.save(article);   

        ArticleDTO newArticleDTO = articleMapper.toDto(article);
        
        newArticleDTO.setAuthorName(user.getUsername());
        newArticleDTO.setThemeTitle(theme.getTitle());
        newArticleDTO.setCreatedAt(article.getCreatedAt());

        return newArticleDTO;
        }

        
    
    }

