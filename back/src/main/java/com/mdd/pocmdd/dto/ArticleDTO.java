package com.mdd.pocmdd.dto;



import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
public class ArticleDTO {

    
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private Long themeId;
    private String authorName;
    private String themeTitle;
    List<CommentDTO> comments;  // Liste des commentaires associés à l'article

}

