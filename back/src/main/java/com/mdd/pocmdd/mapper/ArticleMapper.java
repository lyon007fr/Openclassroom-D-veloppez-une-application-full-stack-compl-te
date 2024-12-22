package com.mdd.pocmdd.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdd.pocmdd.models.Article;
import com.mdd.pocmdd.dto.ArticleDTO;

/**
 * Mapper pour convertir un objet Article en ArticleDTO et vice versa.
 * Utilise MapStruct pour automatiser la conversion entre les entités et les DTOs.
 * 
 * @Mapper(componentModel = "spring") permet à Spring de détecter et d'injecter ce mapper comme un bean.
 */
@Mapper(componentModel = "spring")
public interface ArticleMapper extends EntityMapper<ArticleDTO, Article> {

    /**
     * Convertit un Article en ArticleDTO.
     * 
     * @param article L'entité Article à convertir.
     * @return L'ArticleDTO correspondant.
     */
    @Mapping(source = "user.id", target = "userId") // Mappe l'ID de l'utilisateur à "userId" dans le DTO
    @Mapping(source = "user.username", target = "authorName") // Mappe le nom d'utilisateur à "authorName" dans le DTO
    @Mapping(source = "theme.id", target = "themeId") // Mappe l'ID du thème à "themeId" dans le DTO
    @Mapping(source = "theme.title", target = "themeTitle") // Mappe le titre du thème à "themeTitle" dans le DTO
    ArticleDTO toDto(Article article); // Méthode qui effectue la conversion d'un Article en ArticleDTO
}
