package com.mdd.pocmdd.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.mdd.pocmdd.models.User;

/**
 * Service pour la génération de tokens JWT.
 * Ce service utilise un {@link JwtEncoder} pour créer des tokens sécurisés,
 * conformes au protocole JWT (JSON Web Token).
 */
@Service
public class JWTService {

        private JwtEncoder jwtEncoder;

        /**
         * Constructeur pour initialiser le service avec un encodeur JWT.
         *
         * @param jwtEncoder L'encodeur utilisé pour générer les tokens JWT.
         */
        public JWTService(JwtEncoder jwtEncoder) {
                this.jwtEncoder = jwtEncoder;
        }

        /**
         * Génère un token JWT pour un utilisateur donné.
         *
         * @param user L'utilisateur pour lequel le token est généré.
         * @return Une chaîne de caractères représentant le token JWT.
         */
        public String generateToken(User user) {
                Instant now = Instant.now();
                JwtClaimsSet claims = JwtClaimsSet.builder()
                                .issuer("self") // Déclare que le token est émis par le système lui-même.
                                .issuedAt(now) // Date et heure d'émission du token.
                                .expiresAt(now.plus(1, ChronoUnit.DAYS)) // Expiration dans 1 jour.
                                .subject(String.valueOf(user.getId())) // ID de l'utilisateur comme sujet du token.
                                .build();
                JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters
                                .from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
                return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
        }

}
