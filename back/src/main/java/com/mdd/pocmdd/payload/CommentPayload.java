package com.mdd.pocmdd.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentPayload {

    private String content;
    private Long userId;
    private Long articleId;
    
}
