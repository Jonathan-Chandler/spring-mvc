package com.jonathan.web.service;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.jonathan.web.service.UserService;
import com.jonathan.web.dao.UserRepository;
//import com.jonathan.web.entities.UserData;
import com.jonathan.web.entities.User;

import java.util.List;

//import com.jonathan.web.entities.UserData;
import com.jonathan.web.service.JwtTokenService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.jonathan.web.configuration.SecurityConfiguration;
import org.springframework.context.annotation.Bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collection;

import com.jonathan.web.resources.UserRegistrationDto;
//import org.springframework.context.annotation.Lazy;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.security.core.Authentication;
import org.slf4j.Logger;

import org.springframework.security.core.userdetails.UserDetails;
import com.jonathan.web.service.UserDetailsServiceImpl;
import com.jonathan.web.resources.UserLoginDto;

@Service
public class UserServiceImpl implements UserService
{
	@Autowired
	Logger logger;

	@Autowired
	UserRepository userRepository;

	@Autowired
	AuthenticationProvider authenticationProvider;

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenService jwtTokenService;

	public String login(UserLoginDto userLogin) throws JOSEException, RuntimeException
	{
		UserDetails testDetails = userDetailsService.loadUserByUsername(userLogin.getUsername());

		//List<String> roles = Arrays.asList(new String[] {""});
		Authentication upAuthToken = authenticationProvider.authenticate(
				new UsernamePasswordAuthenticationToken(testDetails, userLogin.getPassword()));
		if (upAuthToken == null)
		{
			logger.info("Failed to authenticate user " + userLogin.getUsername());
			throw new RuntimeException("Bad login credentials");
		}

		String generatedToken = jwtTokenService.generateJwtToken(userLogin.getUsername());
		if (generatedToken == "")
		{
			logger.info("Failed to generate token for user " + userLogin.getUsername());
			throw new RuntimeException("Failed to create JWT");
		}

		//logger.info("Created token: " + generatedToken + " for user " + userLogin.getUsername());
		return generatedToken;
	}

	@Override
	public String register(UserRegistrationDto newUserRequest) throws RuntimeException
	{
		System.out.println("newUserRequest: " + newUserRequest.getUsername());

		// username exists in db
		if (userRepository.findOneByUsername(newUserRequest.getUsername()).orElse(null) != null)
		{
			System.out.println("Attempted registration for " + newUserRequest.getUsername() + " failed - name taken");
      throw new RuntimeException("User exists");
		}

		// add user to db
		User user = User.builder()
			.username(newUserRequest.getUsername())
			.email(newUserRequest.getEmail())
			.password(passwordEncoder.encode(newUserRequest.getPassword()))
			.enabled(true)
			.build();
		userRepository.save(user);

		return "Success";

		//Instant end = Instant.now();

		//logger.info(String.format(
		//        "Hashing took %s ms",
		//        ChronoUnit.MILLIS.between(start, end)
		//));
	}

	public void deleteById(String id)
	{
		userRepository.deleteById(id);
	}
}

