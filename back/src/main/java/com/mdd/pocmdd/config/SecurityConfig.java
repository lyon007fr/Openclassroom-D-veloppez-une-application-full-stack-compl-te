package com.mdd.pocmdd.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	//Clé secrète utilisée pour signer les tokens JWT
	private String jwtKey = "MJKiHixapXXXwLS93K3RDTriSQJ2K8vC";
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	 	@Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http.csrf(csrf -> csrf.disable())	        
	            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	            .authorizeHttpRequests(auth -> auth 
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/auth/register").permitAll()
	            .requestMatchers("/api/register").permitAll()// Permettre l'accès sans authentification à /api/auth/register
	            .anyRequest().authenticated())
	            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
	            .httpBasic(Customizer.withDefaults());

	        // Register the DaoAuthenticationProvider
	        http.authenticationProvider(authenticationProvider(customUserDetailsService));

	        return http.build();
	    }
	
	//Bean pour encoder les JWT en utilisant la clé secrète définie 	
	@Bean
	public JwtEncoder jwtEncoder() {
		return new NimbusJwtEncoder(new ImmutableSecret<>(this.jwtKey.getBytes()));
	}
	
	//décoder les JWT en utilisant la clé secrète définie
	@Bean
	public JwtDecoder jwtDecoder() {
		SecretKeySpec secretKey = new SecretKeySpec(this.jwtKey.getBytes(), "HmacSHA256");
	return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
}

	//Fournisseur d'authentification basé sur DAO qui utilise le CustomUserDetailsService pour charger les détails de l'utilisateur et un encodeur de mot de passe (BCryptPasswordEncoder)
	@Bean
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

	//gére l'authentification en utilisant CustomUserDetailsService et BCryptPasswordEncoder
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
		return authenticationManagerBuilder.build();
	}
    

	@Bean
	public BCryptPasswordEncoder  passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}