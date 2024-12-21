package com.mdd.pocmdd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long id;
    private String content;
    private Long userId;
    private String authorName;
    private Long articleId;
    


}
