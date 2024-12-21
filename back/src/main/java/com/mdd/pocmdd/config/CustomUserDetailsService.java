package com.mdd.pocmdd.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mdd.pocmdd.repository.UserRespository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    	@Autowired
	private UserRespository userRepository;

	
	@Override
	public UserDetails loadUserByUsername(String emailorUsername) throws UsernameNotFoundException {
		 // Rechercher l'utilisateur par email ou nom d'utilisateur
         com.mdd.pocmdd.models.User user = userRepository.findByEmail(emailorUsername);
         if (user == null) {
             user = userRepository.findByUsername(emailorUsername);
         }
         if (user == null) {
             throw new UsernameNotFoundException("User not found with email or username: " + emailorUsername);
         }
         return new org.springframework.security.core.userdetails.User(
                 user.getUsername(), user.getPassword(), getGrantedAuthorities(user.getRole()));
     }

	private List<GrantedAuthority> getGrantedAuthorities(String role) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
		return authorities;
	}


}
