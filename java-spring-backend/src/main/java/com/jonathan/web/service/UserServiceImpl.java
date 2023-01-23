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
import com.jonathan.web.entities.User;

import java.util.List;
import java.util.ArrayList;

import com.jonathan.web.service.JwtService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.security.core.Authentication;
import org.slf4j.Logger;

import org.springframework.security.core.userdetails.UserDetails;
import com.jonathan.web.service.UserDetailsServiceImpl;
import com.jonathan.web.resources.UserLoginDto;
import org.slf4j.LoggerFactory;

@Service
public class UserServiceImpl implements UserService
{
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserRepository userRepository;

	@Autowired
	AuthenticationProvider authenticationProvider;

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtService jwtService;

	public String login(UserLoginDto loginCredentials) throws JOSEException, RuntimeException
	{
		UserDetails daoUserDetails;
		Authentication upAuthToken;

		try
		{
			daoUserDetails = userDetailsService.loadUserByUsername(loginCredentials.getUsername());
			upAuthToken = authenticationProvider.authenticate(
					new UsernamePasswordAuthenticationToken(daoUserDetails, loginCredentials.getPassword()));
		}
		catch (UsernameNotFoundException e)
		{
			logger.info("Failed to find user " + loginCredentials.getUsername() + ": " + e.getMessage());
			throw new RuntimeException("Bad login credentials");
		}
		catch (AuthenticationException e)
		{
			logger.info("Failed to auth user " + loginCredentials.getUsername() + ": " + e.getMessage());
			throw new RuntimeException("Bad login credentials");
		}
		catch (Exception e)
		{
			logger.error("Unknown error while trying to auth user " + loginCredentials.getUsername() + ": " + e.getMessage());
			throw new RuntimeException("Bad login credentials");
		}

		if (upAuthToken == null)
		{
			logger.info("Returned null auth token when authenticating user " + loginCredentials.getUsername());
			throw new RuntimeException("Bad authentication token");
		}

		String generatedToken = jwtService.generateToken(loginCredentials.getUsername());
		if (generatedToken == null || generatedToken == "")
		{
			logger.info("Failed to generate token for user " + loginCredentials.getUsername());
			throw new RuntimeException("Failed to create JWT");
		}

		//logger.info("Created token: " + generatedToken + " for user " + loginCredentials.getUsername());
		return generatedToken;
	}

	@Override
	public String register(UserRegistrationDto newUserRequest) throws RuntimeException
	{
		User daoUserInfo = null;

		try 
		{
			daoUserInfo = userRepository.findOneByUsername(newUserRequest.getUsername()).orElse(null);
		}
		catch (UsernameNotFoundException e)
		{
			// exception is expected for new users
		}

		// user exists
		if (daoUserInfo != null)
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

		//Instant start = Instant.now();
		//Instant end = Instant.now();

		//logger.info(String.format(
		//        "Hashing took %s ms",
		//        ChronoUnit.MILLIS.between(start, end)
		//));
		return "Success";
	}

	public void deleteById(String id)
	{
		userRepository.deleteById(id);
	}
}

