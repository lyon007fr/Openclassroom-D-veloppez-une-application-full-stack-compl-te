package com.mdd.pocmdd.controllers;

import java.util.List;
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
@CrossOrigin(origins = "http://localhost:4200") // Permet les requêtes depuis l'application front-end (Angular) située à
                                                // http://localhost:4200.
@RestController // Indique que cette classe est un contrôleur REST.
@RequestMapping("/api") // Définit le préfixe pour toutes les routes liées aux thèmes.
public class ThemeController {

    private final ThemeService themeService; // Injection du service ThemeService, qui contient la logique métier des
                                             // thèmes.

    // Constructeur permettant d'injecter le service ThemeService.
    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    /**
     * Récupère la liste de tous les thèmes.
     * 
     * @return Une réponse contenant la liste des thèmes.
     */
    @GetMapping("/themes")
    public ResponseEntity<?> getThemes() {
        log.info("Request to get all themes"); // Log l'action de récupération des thèmes.
        try {
            // Récupère la liste de tous les thèmes à partir du service.
            List<ThemeDTO> themes = themeService.getAllThemes();
            return ResponseEntity.ok().body(themes); // Retourne la liste des thèmes avec un statut HTTP 200 OK.
        } catch (Exception e) {
            // En cas d'erreur, retourne un message d'erreur avec un statut HTTP 400 Bad
            // Request.
            return ResponseEntity.badRequest().body(new MessageResponse("Themes not found"));
        }
    }

    /**
     * Récupère un thème spécifique par son ID.
     * 
     * @param id L'ID du thème à récupérer.
     * @return Une réponse contenant le thème avec l'ID donné.
     */
    @GetMapping("/theme/{id}")
    public ResponseEntity<?> getThemeById(@PathVariable Long id) {
        log.info("Request to get theme by id: {}", id); // Log l'action de récupération d'un thème par ID.
        try {
            // Récupère le thème spécifié par son ID à partir du service.
            ThemeDTO theme = themeService.getThemeById(id);
            return ResponseEntity.ok().body(theme); // Retourne le thème trouvé avec un statut HTTP 200 OK.
        } catch (Exception e) {
            // En cas d'erreur (ex. si le thème n'est pas trouvé), retourne un message
            // d'erreur.
            return ResponseEntity.badRequest().body(new MessageResponse("Theme not found"));
        }
    }

    /**
     * Crée un nouveau thème.
     * 
     * @param themeDTO L'objet contenant les données du thème à créer.
     * @return Une réponse contenant le thème créé.
     */
    @PostMapping("/theme")
    public ResponseEntity<?> createTheme(@RequestBody ThemeDTO themeDTO) {
        log.info("Request to create theme: {}", themeDTO); // Log l'action de création du thème.
        try {
            // Sauvegarde le thème en utilisant le service et récupère l'objet ThemeDTO
            // créé.
            ThemeDTO savedThemeDTO = themeService.saveTheme(themeDTO);
            return ResponseEntity.ok().body(savedThemeDTO); // Retourne le thème créé avec un statut HTTP 200 OK.
        } catch (Exception e) {
            // En cas d'erreur lors de la création, retourne un message d'erreur.
            return ResponseEntity.badRequest().body(new MessageResponse("Error creating theme"));
        }
    }
}
