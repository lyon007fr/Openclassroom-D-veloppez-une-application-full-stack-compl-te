package com.mdd.pocmdd.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import com.mdd.pocmdd.repository.UserRespository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.mdd.pocmdd.services.UserService;
import com.mdd.pocmdd.dto.LoginRequestDTO;
import com.mdd.pocmdd.dto.RegisterDTO;
import com.mdd.pocmdd.dto.UserDTO;
import com.mdd.pocmdd.models.User;
import com.mdd.pocmdd.payload.MessageResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.mdd.pocmdd.services.JWTService;
import com.mdd.pocmdd.payload.JwtResponse;
import com.mdd.pocmdd.payload.MeDto;

import org.springframework.security.core.AuthenticationException;

import lombok.extern.log4j.Log4j2;

/**
 * Controller class for handling user-related operations such as registration,
 * login, subscription, and profile management.
 */
@CrossOrigin(origins = "http://localhost:4200")
@Log4j2
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final UserRespository userRespository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    /**
     * Constructor for initializing dependencies.
     * 
     * @param userService           The service to handle user operations
     * @param userRespository       The repository to interact with the user data
     * @param bCryptPasswordEncoder The password encoder for secure password
     *                              handling
     * @param authenticationManager The manager to handle authentication requests
     * @param jwtService            The service to generate JWT tokens
     */
    public UserController(UserService userService, UserRespository userRespository,
            BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager,
            JWTService jwtService) {
        this.userService = userService;
        this.userRespository = userRespository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /**
     * Endpoint to retrieve the current authenticated user's profile information.
     *
     * @param authentication The current authentication object
     * @return The user's profile details
     */
    @GetMapping("/me")
    public ResponseEntity<MeDto> me(Authentication authentication) {
        log.info("Request to get user: {}", authentication.getName());
        if (authentication.getName() == null) {
            return ResponseEntity.badRequest().build();
        }
        UserDTO userDto = userService.findById(Long.valueOf(authentication.getName()));
        if (userDto == null) {
            return ResponseEntity.notFound().build();
        }
        MeDto meDto = new MeDto(userDto.getId(), userDto.getUsername(), userDto.getEmail(),
                userDto.getSubscribedThemes());
        return ResponseEntity.ok().body(meDto);
    }

    /**
     * Endpoint to update the current user's profile information.
     *
     * @param meDto          The updated user profile data
     * @param authentication The current authentication object
     * @return The updated user profile details
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody MeDto meDto, Authentication authentication) {
        log.info("Request to update user: {}", meDto);
        if (meDto == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Request body is empty!"));
        }
        if (authentication.getName() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found!"));
        }
        try {
            MeDto updatedUser = userService.updateUser(Long.valueOf(authentication.getName()), meDto);
            return ResponseEntity.ok().body(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Endpoint to subscribe the current user to a specific theme.
     *
     * @param themeId        The theme ID to subscribe to
     * @param authentication The current authentication object
     * @return The updated user data after subscribing to the theme
     */
    @PostMapping("/subscribe/{themeId}")
    public ResponseEntity<?> subscribe(@PathVariable Long themeId, Authentication authentication) {
        log.info("Request to subscribe user: {} and themeId {}", authentication.getName(), themeId);
        if (authentication.getName() == null || themeId == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Request body is empty!"));
        }
        try {
            UserDTO userDTO = userService.subscribeUserToTheme(Long.valueOf(authentication.getName()), themeId);
            return ResponseEntity.ok().body(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Endpoint to unsubscribe the current user from a specific theme.
     *
     * @param themeId        The theme ID to unsubscribe from
     * @param authentication The current authentication object
     * @return The updated user data after unsubscribing from the theme
     */
    @DeleteMapping("/unsubscribe/{themeId}")
    public ResponseEntity<?> unsubscribe(@PathVariable Long themeId, Authentication authentication) {
        log.info("Request to unsubscribe user: {} and themeId {}", authentication.getName(), themeId);
        if (themeId == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Request body is empty!"));
        }
        try {
            UserDTO userDTO = userService.unsubscribeUserFromTheme(Long.valueOf(authentication.getName()), themeId);
            return ResponseEntity.ok().body(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Endpoint to authenticate and log in a user.
     *
     * @param authRequest The login request containing the username or email and
     *                    password
     * @return The JWT token upon successful authentication
     */
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO authRequest) {
        log.info("Login attempt for user with email or username: {}", authRequest.getUsernameOrEmail());
        try {
            User user = userService.findByEmail(authRequest.getUsernameOrEmail());
            if (user == null) {
                user = userService.findByUsername(authRequest.getUsernameOrEmail());
                if (user == null) {
                    log.warn("Login failed for user with emailorUsername: {} - User not found",
                            authRequest.getUsernameOrEmail());
                    return ResponseEntity.status(401).body(new MessageResponse("User not found"));
                }
            }

            if (user == null || !bCryptPasswordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
                log.warn("Login failed for user with email: {} - Invalid email or password",
                        authRequest.getUsernameOrEmail());
                return ResponseEntity.status(401).body("Invalid email or password");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsernameOrEmail(),
                            authRequest.getPassword()));

            String token = jwtService.generateToken(user);
            JwtResponse jwtResponse = new JwtResponse(token);
            log.info("Login successful for user with email: {}", authRequest.getUsernameOrEmail());

            return ResponseEntity.ok(jwtResponse);
        } catch (AuthenticationException e) {
            log.error("Login failed for user with email: {}", authRequest.getUsernameOrEmail(), e);
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }

    /**
     * Endpoint to register a new user.
     *
     * @param registerDTO   The registration data including username, email, and
     *                      password
     * @param bindingResult The result of validating the registration form
     * @return A message response indicating the success or failure of the
     *         registration
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/auth/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errors);
        }
        log.info("Registration attempt for user: {}", registerDTO.getUsername());
        try {
            if (userService.findByUsername(registerDTO.getUsername()) != null) {
                log.warn("Registration failed: User with name {} already exists", registerDTO.getUsername());
                return ResponseEntity.badRequest().body(new MessageResponse("User with the same name already exists"));
            }
            if (userService.findByEmail(registerDTO.getEmail()) != null) {
                log.warn("Registration failed: User with email {} already exists", registerDTO.getEmail());
                return ResponseEntity.badRequest().body(new MessageResponse("User with the same email already exists"));
            }

            userService.registerUser(registerDTO);

            log.info("Registration successful for user: {}", registerDTO.getUsername());
            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (IllegalArgumentException e) {
            log.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
