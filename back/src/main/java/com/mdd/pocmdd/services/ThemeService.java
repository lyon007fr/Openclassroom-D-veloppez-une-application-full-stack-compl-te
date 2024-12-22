package com.mdd.pocmdd.services;

import org.springframework.stereotype.Service;
import com.mdd.pocmdd.dto.ThemeDTO;
import com.mdd.pocmdd.mapper.ThemeMapper;
import com.mdd.pocmdd.models.Theme;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import com.mdd.pocmdd.repository.ThemeRepository;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des thèmes.
 * Fournit des méthodes pour récupérer, créer et manipuler les thèmes.
 */
@Log4j2
@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ThemeMapper themeMapper;

    /**
     * Constructeur pour injecter les dépendances nécessaires.
     *
     * @param themeRepository le référentiel pour les opérations sur les thèmes.
     * @param themeMapper     le mapper pour convertir entre les entités et les DTO.
     */
    public ThemeService(ThemeRepository themeRepository, ThemeMapper themeMapper) {
        this.themeRepository = themeRepository;
        this.themeMapper = themeMapper;
    }

    /**
     * Récupère tous les thèmes disponibles.
     *
     * @return une liste de {@link ThemeDTO} représentant tous les thèmes.
     */
    public List<ThemeDTO> getAllThemes() {
        log.info("Requête pour récupérer tous les thèmes");
        List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(themeMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupère un thème par son ID.
     *
     * @param id l'identifiant du thème.
     * @return un {@link ThemeDTO} représentant le thème correspondant.
     * @throws IllegalArgumentException si le thème avec l'ID donné n'existe pas.
     */
    public ThemeDTO getThemeById(Long id) {
        log.info("Requête pour récupérer un thème par ID : {}", id);
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Thème non trouvé"));
        return themeMapper.toDto(theme);
    }

    /**
     * Sauvegarde un nouveau thème ou met à jour un thème existant.
     *
     * @param themeDTO le DTO contenant les informations du thème à sauvegarder.
     * @return un {@link ThemeDTO} représentant le thème sauvegardé.
     */
    public ThemeDTO saveTheme(ThemeDTO themeDTO) {
        log.info("Requête pour sauvegarder un thème : {}", themeDTO);
        Theme theme = themeMapper.toEntity(themeDTO);
        theme = themeRepository.save(theme);
        return themeMapper.toDto(theme);
    }
}
