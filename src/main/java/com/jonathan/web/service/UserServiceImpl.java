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

import com.jonathan.web.controllers.authentication.CustomAuthenticationProvider;
import com.jonathan.web.resources.UserRegistrationDto;
//import org.springframework.context.annotation.Lazy;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.security.core.Authentication;

@Service
public class UserServiceImpl implements UserService
{
  @Autowired
  UserRepository userRepository;

  @Autowired
  AuthenticationProvider authenticationProvider;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private final JwtTokenService jwtTokenService;

  public UserServiceImpl(
      JwtTokenService jwtTokenService)
  {
    this.jwtTokenService = jwtTokenService;
  }
  //private final JwtTokenService jwtTokenService;
  //private final CustomAuthenticationProvider customAuthenticationProvider;

  //public UserServiceImpl(
  //    //PasswordEncoder passwordEncoder, 
  //    JwtTokenService jwtTokenService, 
  //    CustomAuthenticationProvider customAuthenticationProvider)
  //{
  //  //this.passwordEncoder = passwordEncoder;
  //  this.jwtTokenService = jwtTokenService;
  //  this.customAuthenticationProvider = customAuthenticationProvider;
  //  //this.AuthenticationManager = AuthenticationManager;
  //}

  public String login(String username, String password) throws JOSEException
  {
    try 
    {
      //String encodedPassword = passwordEncoder.encode(password);
      System.out.println("username: " + username);
      //System.out.println("encoded pass: " + encodedPassword);
      ////GrantedAuthority testAuth = new SimpleGrantedAuthority("User");
      ////List<GrantedAuthority> testAuthList = new ArrayList<>();
      ////testAuthList.add(testAuth);

      List<String> roles = Arrays.asList(new String[] {""});
      Authentication upAuthToken = authenticationProvider.authenticate(
          new UsernamePasswordAuthenticationToken(username, password));
      if (upAuthToken == null)
      {
        System.out.println("Failed to authenticate");
        return "";
      }
      System.out.println("UsernamePasswordAuthenticationToken success - return: " + upAuthToken);

      String generatedToken = jwtTokenService.generateJwtToken(username);
      if (generatedToken == "")
      {
        System.out.println("Failed to generate token");
      }
      //return jwtTokenService.createToken(username, roles);
      return jwtTokenService.generateJwtToken(username);
    }
    catch(AuthenticationException e)
    {
      System.out.println("Failed in login service");
      throw e;
    }
  }

  //public String register(UserData user)
  @Override
  public String register(UserRegistrationDto newUserRequest)
  {
    Instant start = Instant.now();
    User user = User.builder()
      .username(newUserRequest.getUsername())
      .email(newUserRequest.getEmail())
      .password(passwordEncoder.encode(newUserRequest.getPassword()))
      .enabled(true)
      .build();
    Instant end = Instant.now();

    System.out.println(String.format(
            "Hashing took %s ms",
            ChronoUnit.MILLIS.between(start, end)
    ));

    userRepository.save(user);
    return "";

    //  return jwtTokenProvider.createToken(appUser.getUsername(), appUser.getAppUserRoles());

    //userRepository.save(user);
    //if (!userRepository.existsByUsername(appUser.getUsername())) 
    //{
    //  appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
    //  userRepository.save(appUser);
    //  return jwtTokenProvider.createToken(appUser.getUsername(), appUser.getAppUserRoles());
    //} else {
    //  throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
    //}
    //return "";
  }

  public void deleteById(String id)
  {
    userRepository.deleteById(id);
  }
}
//
////@Service
////public class UserServiceImpl implements UserService
////{
////  private UserRepository userRepository;
////
////  public UserServiceImpl(UserRepository userRepository)
////  {
////    this.userRepository = userRepository;
////  }
////
////  @Override
////  public List<UserData> findAll()
////  {
////    return userRepository.findAll();
////  }
////
////  @Override
////  public UserData findById(String id)
////  {
////    Optional<UserData> result = userRepository.findById(id);
////    UserData user = null;
////
////    if (result.isPresent())
////    {
////      user = result.get();
////    }
////    else
////    {
////      throw new RuntimeException("Fail to find username: " + id);
////    }
////
////    return user;
////  }
////
////  @Override
////  public void save(UserData user)
////  {
////    userRepository.save(user);
////  }
////
////  @Override
////  public void deleteById(String id)
////  {
////    userRepository.deleteById(id);
////  }
////}
////
////
