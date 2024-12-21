package com.mdd.pocmdd.controllers;

import java.util.List;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdd.pocmdd.dto.ThemeDTO;
import com.mdd.pocmdd.payload.MessageResponse;
import com.mdd.pocmdd.services.ThemeService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class ThemeController {

    private final ThemeService themeService;
    
    //constructor
    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }


    @GetMapping("/themes")
    public ResponseEntity<?> getThemes() {
        log.info("Request to get all themes");
        try{
            List<ThemeDTO> themes = themeService.getAllThemes();
            return ResponseEntity.ok().body(themes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Themes not found"));
        }
    }

    @GetMapping("/theme/{id}")
    public ResponseEntity<?> getThemeById(@PathVariable Long id) {
        log.info("Request to get theme by id: {}", id);
        try {
            ThemeDTO theme = themeService.getThemeById(id);
            return ResponseEntity.ok().body(theme);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Theme not found"));
        }
    }

    @PostMapping("/theme")
    public ResponseEntity<?> createTheme(@RequestBody ThemeDTO themeDTO) {
        log.info("Request to create theme: {}", themeDTO);
        try {
            ThemeDTO savedThemeDTO = themeService.saveTheme(themeDTO);
            return ResponseEntity.ok().body(savedThemeDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error creating theme"));
        }
    }

}
