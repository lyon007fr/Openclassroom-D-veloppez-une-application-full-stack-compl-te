package com.mdd.pocmdd.models;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Relation avec l'utilisateur qui a écrit le commentaire

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article; // Relation avec l'article sur lequel le commentaire est posté

    private String authorName;

}
