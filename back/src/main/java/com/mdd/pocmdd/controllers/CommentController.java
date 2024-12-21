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
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //passer le cette méthode dans le controleur article
    @GetMapping("/article/{articleId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByArticleId(@PathVariable Long articleId) {
        List<CommentDTO> comments = commentService.getCommentsByArticleId(articleId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/comment")
    public ResponseEntity<CommentDTO> saveComment(@RequestBody CommentPayload commentPayload) {
        log.info("Request to save comment: {}", commentPayload);
        CommentDTO comment = commentService.saveComment(commentPayload);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }
}

