package com.mdd.pocmdd.services;

import com.mdd.pocmdd.repository.UserRespository;
import lombok.extern.log4j.Log4j2;
import com.mdd.pocmdd.dto.RegisterDTO;
import com.mdd.pocmdd.dto.ThemeDTO;
import com.mdd.pocmdd.dto.UserDTO;
import com.mdd.pocmdd.mapper.UserMapper;
import com.mdd.pocmdd.models.User;
import com.mdd.pocmdd.payload.MeDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.mdd.pocmdd.repository.ThemeRepository;
import com.mdd.pocmdd.models.Theme;

/**
 * Service pour la gestion des utilisateurs.
 */
@Log4j2
@Service
public class UserService {

    /**
     * Dépôt pour accéder aux données des utilisateurs.
     */
    private UserRespository userRepository;

    /**
     * Dépôt pour accéder aux données des thèmes.
     */
    private ThemeRepository themeRepository;

    /**
     * Mapper pour convertir les entités utilisateur en DTO et vice versa.
     */
    private UserMapper userMapper;

    /**
     * Encodeur pour sécuriser les mots de passe.
     */
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Constructeur pour injecter les dépendances du service.
     *
     * @param userRepository        Le dépôt des utilisateurs.
     * @param themeRepository       Le dépôt des thèmes.
     * @param userMapper            Le mapper des utilisateurs.
     * @param bCryptPasswordEncoder L'encodeur de mots de passe.
     */
    public UserService(UserRespository userRepository, ThemeRepository themeRepository, UserMapper userMapper,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.themeRepository = themeRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     *
     * @param username Le nom d'utilisateur à rechercher.
     * @return L'utilisateur correspondant ou null s'il n'existe pas.
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Recherche un utilisateur par son adresse email.
     *
     * @param email L'email à rechercher.
     * @return L'utilisateur correspondant ou null s'il n'existe pas.
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Enregistre un nouvel utilisateur.
     *
     * @param registerDTO Les données d'inscription de l'utilisateur.
     * @throws IllegalArgumentException Si le nom d'utilisateur ou l'email est déjà
     *                                  utilisé ou si des champs obligatoires sont
     *                                  vides.
     */
    public void registerUser(RegisterDTO registerDTO) {
        if (userRepository.findByUsername(registerDTO.getUsername()) != null) {
            throw new IllegalArgumentException("Nom d'utilisateur déjà utilisé");
        }
        if (userRepository.findByEmail(registerDTO.getEmail()) != null) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }
        if (registerDTO.getUsername() == null || registerDTO.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Le nom d'utilisateur ne doit pas être vide");
        }
        if (registerDTO.getPassword() == null || registerDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne doit pas être vide");
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setRole("User");
        user.setPassword(bCryptPasswordEncoder.encode(registerDTO.getPassword()));

        userRepository.save(user);
    }

    /**
     * Récupère la liste de tous les utilisateurs.
     *
     * @return Une liste d'utilisateurs.
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Recherche un utilisateur par son identifiant et retourne ses données sous
     * forme de DTO.
     *
     * @param id L'identifiant de l'utilisateur.
     * @return Le DTO de l'utilisateur ou null s'il n'existe pas.
     */
    public UserDTO findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        UserDTO userDTO = null;
        if (user.isPresent()) {
            userDTO = new UserDTO();
            userDTO.setId(user.get().getId());
            userDTO.setUsername(user.get().getUsername());
            userDTO.setEmail(user.get().getEmail());
            List<ThemeDTO> themes = user.get().getSubscribedThemes().stream()
                    .map(theme -> new ThemeDTO(theme.getId(), theme.getTitle(), theme.getDescription()))
                    .toList();
            userDTO.setSubscribedThemes(themes);
            log.info("Utilisateur trouvé : {}", userDTO);
        }
        return userDTO;
    }

    /**
     * Abonne un utilisateur à un thème.
     *
     * @param id      L'identifiant de l'utilisateur.
     * @param themeId L'identifiant du thème.
     * @return Le DTO de l'utilisateur mis à jour.
     * @throws IllegalArgumentException Si l'utilisateur ou le thème n'existe pas,
     *                                  ou si l'utilisateur est déjà abonné au
     *                                  thème.
     */
    public UserDTO subscribeUserToTheme(Long id, Long themeId) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Utilisateur non trouvé");
        }
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("Thème non trouvé"));
        if (user.get().getSubscribedThemes().contains(theme)) {
            throw new IllegalArgumentException("Utilisateur déjà abonné à ce thème");
        }
        user.get().getSubscribedThemes().add(theme);
        userRepository.save(user.get());
        return userMapper.toDto(user.get());
    }

    /**
     * Désabonne un utilisateur d'un thème.
     *
     * @param userId  L'identifiant de l'utilisateur.
     * @param themeId L'identifiant du thème.
     * @return Le DTO de l'utilisateur mis à jour.
     * @throws IllegalArgumentException Si l'utilisateur ou le thème n'existe pas,
     *                                  ou si l'utilisateur n'est pas abonné au
     *                                  thème.
     */
    public UserDTO unsubscribeUserFromTheme(long userId, Long themeId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Utilisateur non trouvé");
        }
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("Thème non trouvé"));
        if (!user.get().getSubscribedThemes().contains(theme)) {
            throw new IllegalArgumentException("Utilisateur non abonné à ce thème");
        }
        user.get().getSubscribedThemes().remove(theme);
        userRepository.save(user.get());
        return userMapper.toDto(user.get());
    }

    /**
     * Met à jour les informations d'un utilisateur.
     *
     * @param userId L'identifiant de l'utilisateur.
     * @param meDTO  Les nouvelles données de l'utilisateur.
     * @return Les données mises à jour sous forme de DTO.
     * @throws IllegalArgumentException Si le nom d'utilisateur ou l'email est déjà
     *                                  utilisé.
     */
    public MeDto updateUser(Long userId, MeDto meDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        boolean isUpdated = false;

        if (!user.getEmail().equals(meDTO.getEmail())) {
            if (userRepository.existsByEmail(meDTO.getEmail())) {
                throw new IllegalArgumentException("Erreur : l'email est déjà utilisé !");
            }
            user.setEmail(meDTO.getEmail());
            isUpdated = true;
        }

        if (!user.getUsername().equals(meDTO.getUsername())) {
            User existingUser = userRepository.findByUsername(meDTO.getUsername());
            if (existingUser != null && !user.getUsername().equals(meDTO.getUsername())) {
                throw new IllegalArgumentException("Erreur : le nom d'utilisateur est déjà utilisé !");
            }
            user.setUsername(meDTO.getUsername());
            isUpdated = true;
        }

        if (isUpdated) {
            userRepository.save(user);
        }

        List<ThemeDTO> themeDTOs = new ArrayList<>();
        for (Theme theme : user.getSubscribedThemes()) {
            themeDTOs.add(new ThemeDTO(theme.getId(), theme.getTitle(), theme.getDescription()));
        }
        return new MeDto(user.getId(), user.getUsername(), user.getEmail(), themeDTOs);
    }
}
