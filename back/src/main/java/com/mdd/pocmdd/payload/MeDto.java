package com.mdd.pocmdd.payload;

import java.util.ArrayList;
import java.util.List;

import com.mdd.pocmdd.dto.ThemeDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeDto {

        private Long id;
        private String username;
        private String email;
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

