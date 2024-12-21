package com.mdd.pocmdd.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdd.pocmdd.models.Article;
import com.mdd.pocmdd.dto.ArticleDTO;

@Mapper(componentModel = "spring")
public interface ArticleMapper extends EntityMapper<ArticleDTO, Article> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "authorName") // Map le nom de l'utilisateur
    @Mapping(source = "theme.id", target = "themeId") // Map l'identifiant du thème
    @Mapping(source = "theme.title", target = "themeTitle") // Map le titre du thème
    
    ArticleDTO toDto(Article article);
}
