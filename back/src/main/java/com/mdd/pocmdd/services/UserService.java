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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.mdd.pocmdd.repository.ThemeRepository;
import com.mdd.pocmdd.models.Theme;



@Log4j2
@Service
public class UserService {
    
    private UserRespository userRepository;

    private ThemeRepository themeRepository;

    private UserMapper userMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    //constructor
    public UserService(UserRespository userRepository, ThemeRepository themeRepository, UserMapper userMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.themeRepository = themeRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    //@Autowired
    //private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

    public void registerUser(RegisterDTO registerDTO) {
        //check si l'utilisateur existe déjà
        if(userRepository.findByUsername(registerDTO.getUsername()) != null){
            throw new IllegalArgumentException("Username already in use");
        }
        if (userRepository.findByEmail(registerDTO.getEmail()) != null) {
            throw new IllegalArgumentException("Email already in use");
        }
        // Vérification des champs
        if (registerDTO.getUsername() == null || registerDTO.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username must not be empty");
        }
        if (registerDTO.getPassword() == null || registerDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password must not be empty");
        }


        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setRole("User");

        //penser à encoder le mot de passe
        user.setPassword(bCryptPasswordEncoder.encode(registerDTO.getPassword()));

        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public UserDTO findById(Long id){
        //cherche l'utilisateur par son id
        Optional<User> user = userRepository.findById(id);
        //Vérifie si l'utilisateur existe
        UserDTO userDTO = null;
        if(user.isPresent()){
            //récupère l'utilisateur
            userDTO = new UserDTO();
            userDTO.setId(user.get().getId());
            userDTO.setUsername(user.get().getUsername());
            userDTO.setEmail(user.get().getEmail());
            //convertir la liste des abonnements en ThemeDTO
            List<ThemeDTO> themes = user.get().getSubscribedThemes().stream()
                .map(theme -> new ThemeDTO(theme.getId(), theme.getTitle(), theme.getDescription()))
                .toList();
            userDTO.setSubscribedThemes(themes);
            log.info("User found: {}", userDTO);
        }
        return userDTO;
    }

    public UserDTO subscribeUserToTheme(Long id, Long themeId) {
        Optional<User> user = userRepository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        Theme theme = themeRepository.findById(themeId)
                          .orElseThrow(() -> new IllegalArgumentException("Theme not found"));
        boolean isSubscribed = user.get().getSubscribedThemes().contains(theme);
        if (isSubscribed) {
            throw new IllegalArgumentException("User already subscribed to this theme");
        }
            
        user.get().getSubscribedThemes().add(theme);
        userRepository.save(user.get());
        return userMapper.toDto(user.get());
    }

    public UserDTO unsubscribeUserFromTheme(long userId, Long themeId) {
        Optional<User> user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
                        
        Theme theme = themeRepository.findById(themeId)
                          .orElseThrow(() -> new IllegalArgumentException ("Theme not found"));
        boolean isSubscribed = user.get().getSubscribedThemes().contains(theme);
        if (!isSubscribed) {
            throw new IllegalArgumentException("User not subscribed to this theme");
        }
        user.get().getSubscribedThemes().remove(theme);
        userRepository.save(user.get());
        return userMapper.toDto(user.get());
    }

    public MeDto updateUser(Long userId, MeDto meDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    
        boolean isUpdated = false;
    
        // check if the email is different and update if necessary
        if (!user.getEmail().equals(meDTO.getEmail())) {
            // check if the email is already in use
            if (userRepository.existsByEmail(meDTO.getEmail())) {
                throw new IllegalArgumentException("Error: Email is already in use!");
            }
            user.setEmail(meDTO.getEmail());
            isUpdated = true;
        }
    
        // check if the username is different and update if necessary
        if (!user.getUsername().equals(meDTO.getUsername())) {
            // check if the username is already in use
            User existingUser = userRepository.findByUsername(meDTO.getUsername());

            if (existingUser != null && !user.getUsername().equals(meDTO.getUsername())) {
                throw new IllegalArgumentException("Error: Username is already in use!");
            }
            user.setUsername(meDTO.getUsername());
            isUpdated = true;
        }
    
        // Save the user if it has been updated
        if (isUpdated) {
            userRepository.save(user);
        }
        List<ThemeDTO> themeDTOs = new ArrayList<>();
        for (Theme theme : user.getSubscribedThemes()) {
            themeDTOs.add(new ThemeDTO(theme.getId(), theme.getTitle(), theme.getDescription()));  // Adapté en fonction des attributs de ThemeDTO
        }
        MeDto updatedMeDto = new MeDto(user.getId(),user.getUsername(),user.getEmail(), themeDTOs);
        return updatedMeDto;
    }
}
