package com.mdd.pocmdd.services;

import lombok.extern.log4j.Log4j2;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.mdd.pocmdd.models.Article;
import com.mdd.pocmdd.models.Comment;
import com.mdd.pocmdd.models.User;
import com.mdd.pocmdd.payload.CommentPayload;
import com.mdd.pocmdd.dto.CommentDTO;
import com.mdd.pocmdd.repository.CommentRepository;
import com.mdd.pocmdd.repository.UserRespository;
import com.mdd.pocmdd.repository.ArticleRepository;

@Log4j2
@Service
public class CommentService {

     private final CommentRepository commentRepository;
     private final UserRespository userRepository;
     private final ArticleRepository articleRepository;

    public CommentService(CommentRepository commentRepository, UserRespository userRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    public List<CommentDTO> getCommentsByArticleId(Long articleId) {
        List<Comment> comments = commentRepository.findByArticleId(articleId);
        return comments.stream()
            .map(comment -> new CommentDTO(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getArticle().getId()
            ))
            .collect(Collectors.toList());
    }

    public CommentDTO saveComment(CommentPayload commentPayload) {
        //get article by id
        Optional<Article> article = articleRepository.findById(commentPayload.getArticleId());
        //get user by id
        Optional<User> user = userRepository.findById(commentPayload.getUserId());
        //save comment


        Comment comment = new Comment();
        comment.setContent(commentPayload.getContent());
        comment.setArticle(article.get());
        comment.setUser(user.get());
        comment.setAuthorName(user.get().getUsername());
        comment = commentRepository.save(comment);
        return new CommentDTO(
            comment.getId(),
            comment.getContent(),
            comment.getUser().getId(),
            comment.getUser().getUsername(),
            comment.getArticle().getId()
        );
    }

}
