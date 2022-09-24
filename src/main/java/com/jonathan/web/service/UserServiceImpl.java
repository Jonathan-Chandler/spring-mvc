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
import com.jonathan.web.entities.UserData;

import java.util.List;

import com.jonathan.web.entities.UserData;
import com.jonathan.web.service.JwtTokenService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;

@Service
public class UserServiceImpl implements UserService
{
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenService jwtTokenService;
  private final AuthenticationProvider authenticationProvider;

  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenService jwtTokenService, AuthenticationProvider authenticationProvider)
  {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtTokenService = jwtTokenService;
    this.authenticationProvider = authenticationProvider;
    this.AuthenticationManager = AuthenticationManager;
  }

  public String login(String username, String password) throws JOSEException
  {
    try {
      //String encodedPassword = passwordEncoder.encode(password);
      System.out.println("username: " + username);
      //System.out.println("encoded pass: " + encodedPassword);
      List<String> roles = Arrays.asList(new String[] {""});
      //authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(username, password));
      authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(username, password));
      System.out.println("successfully authenticated - getting token");
      return jwtTokenService.createToken(username, roles);
      //return jwtTokenService.createToken(username, new List<String> Arrays.asList(""));
      //return jwtTokenService.createToken(username, userRepository.findByUsername(username).getAppUserRoles());
    }
    catch(AuthenticationException e)
    {
      System.out.println("Failed in login service");
      throw e;
    }
  }

  public String register(UserData user)
  {
    //if (!userRepository.existsByUsername(appUser.getUsername())) 
    //{
    //  appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
    //  userRepository.save(appUser);
    //  return jwtTokenProvider.createToken(appUser.getUsername(), appUser.getAppUserRoles());
    //} else {
    //  throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
    //}
    return "";
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
