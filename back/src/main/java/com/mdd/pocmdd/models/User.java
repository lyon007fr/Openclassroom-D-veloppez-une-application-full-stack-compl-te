package com.mdd.pocmdd.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Pattern;

@Data
// @Builder
@EntityListeners(AuditingEntityListener.class) // Enables JPA Auditing
@NoArgsConstructor // Generates a default constructor with no parameters. If a class doesn't have
                   // any constructor, compiler automatically creates a no-arg constructor for the
                   // class.
@AllArgsConstructor // Generates a constructor with 1 parameter for each field in your class.
@Entity
@Table(name = "USERS", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"), // email unique
        @UniqueConstraint(columnNames = "username") // nom est unique
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false)
    private String username;

    @NonNull
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}", message = "Password must contain at least one digit, one uppercase letter, and be at least 8 characters long")
    @Column(nullable = false)
    private String password;

    @NonNull
    @Column(nullable = false)
    @Email // Type email
    private String email;

    @Column(nullable = false)
    private String role;

    @ManyToMany
    @JoinTable(name = "subscription", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "theme_id"))
    private List<Theme> subscribedThemes = new ArrayList<>();

    public List<Long> getSubscribedThemeIds() {
        // Retourne une liste des IDs des thèmes abonnés
        List<Long> themeIds = new ArrayList<>();
        for (Theme theme : subscribedThemes) {
            themeIds.add(theme.getId()); // Ajoute l'ID du thème à la liste
        }
        return themeIds;
    }
}
