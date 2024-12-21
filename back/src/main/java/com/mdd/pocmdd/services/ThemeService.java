package com.mdd.pocmdd.services;

import org.springframework.stereotype.Service;

import com.mdd.pocmdd.dto.ThemeDTO;
import com.mdd.pocmdd.mapper.ThemeMapper;
import com.mdd.pocmdd.models.Theme;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import com.mdd.pocmdd.repository.ThemeRepository;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ThemeMapper themeMapper;

    //constructor
    public ThemeService(ThemeRepository themeRepository, ThemeMapper themeMapper) {
        this.themeRepository = themeRepository;
        this.themeMapper = themeMapper;
    }

  

    //get all themes
    public List<ThemeDTO> getAllThemes() {
        log.info("Request to get all themes");
        List<Theme> themes = themeRepository.findAll();
        return themes.stream()
            .map(themeMapper::toDto)
            .collect(Collectors.toList());
    }

    public ThemeDTO getThemeById(Long id) {
        log.info("Request to get theme by id: {}", id);
        Theme theme = themeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Theme not found"));
        return themeMapper.toDto(theme);
    }

    //save theme
    public ThemeDTO saveTheme(ThemeDTO themeDTO) {
        log.info("Request to save theme: {}", themeDTO);
        Theme theme = themeMapper.toEntity(themeDTO);
        theme = themeRepository.save(theme);
        return themeMapper.toDto(theme);
    }

}
