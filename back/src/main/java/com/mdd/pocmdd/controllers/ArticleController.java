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
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;


    @GetMapping("/articles")
    public ResponseEntity<?> getArticles(Authentication authentication) {
        log.info("Request to get all articles");
        UserDTO userDto = userService.findById(Long.valueOf(authentication.getName()));
        List<Long> themeids = userDto.getSubscribedThemeIds();
        try {
            List<ArticleDTO> articles = articleService.findallArticleByTheme(themeids);
            return ResponseEntity.ok().body(articles);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Articles not found"));
        }
        }

    @GetMapping("/article/{id}")
    public ResponseEntity<?> getArticleById(@PathVariable Long id) {
        log.info("Request to get article by id: {}", id);
        try {
            ArticleDTO article = articleService.findArticleById(Long.valueOf(id));
            return ResponseEntity.ok().body(article);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Article not found"));
        }
        
        
    }

    @PostMapping("/article")
    public ResponseEntity<?> createArticle(@RequestBody ArticleDTO articleDTO, Authentication authentication) {
        log.info("Request to create article: {}", articleDTO);
        if ( articleDTO.getTitle() == null || articleDTO.getContent() == null || articleDTO.getThemeId() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Request body is empty!"));
        }
        try
        {
            ArticleDTO newArticleDTO = articleService.createArticle(articleDTO, Long.valueOf(authentication.getName()));
            return ResponseEntity.ok().body(newArticleDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        
    }
}