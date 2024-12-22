package com.mdd.pocmdd.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor // Generates a default constructor with no parameters. If a class doesn't have
                   // any constructor, compiler automatically creates a no-arg constructor for the
                   // class.
@AllArgsConstructor // Generates a constructor with 1 parameter for each field in your class.
public class UserDTO {
    private Long id;

    @NonNull
    private String username;

    @JsonIgnore
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}", message = "Password must contain at least one digit, one uppercase letter, and be at least 8 characters long")
    @NonNull
    private String password;

    @NonNull
    @Size(max = 50)
    @Email
    private String email;

    private String role;

    private List<ThemeDTO> subscribedThemes;

    public List<Long> getSubscribedThemeIds() {
        // Retourne une liste des IDs des thèmes abonnés
        List<Long> themeIds = new ArrayList<>();
        for (ThemeDTO theme : subscribedThemes) {
            themeIds.add(theme.getId()); // Ajoute l'ID du thème à la liste
        }
        return themeIds;
    }

}