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

  public String login(String username, String password) throws JOSEException
  {
    try 
    {
      UserDetails testDetails = userDetailsService.loadUserByUsername(username);

      List<String> roles = Arrays.asList(new String[] {""});
      Authentication upAuthToken = authenticationProvider.authenticate(
          new UsernamePasswordAuthenticationToken(testDetails, password));
      if (upAuthToken == null)
      {
        logger.info("Failed to authenticate user " + username);
        return "";
      }

      String generatedToken = jwtTokenService.generateJwtToken(username);
      if (generatedToken == "")
      {
        logger.info("Failed to generate token for user " + username);
      }

      //logger.info("Created token: " + generatedToken + " for user " + username);
      return generatedToken;
    }
    catch(AuthenticationException e)
    {
      logger.info("Failed in login service: " + e);
      throw e;
    }
  }

  //public String register(UserData user)
  @Override
  public String register(UserRegistrationDto newUserRequest)
  {
    //Instant start = Instant.now();
    User user = User.builder()
      .username(newUserRequest.getUsername())
      .email(newUserRequest.getEmail())
      .password(passwordEncoder.encode(newUserRequest.getPassword()))
      .enabled(true)
      .build();
    //Instant end = Instant.now();

    //logger.info(String.format(
    //        "Hashing took %s ms",
    //        ChronoUnit.MILLIS.between(start, end)
    //));

    userRepository.save(user);
    return "";
  }

  public void deleteById(String id)
  {
    userRepository.deleteById(id);
  }
}

