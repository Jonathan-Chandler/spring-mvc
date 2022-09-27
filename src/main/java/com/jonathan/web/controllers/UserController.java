package com.jonathan.web.controller.authentication;

import java.time.Instant;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.oauth2.jwt.JwtClaimsSet;
//import org.springframework.security.oauth2.jwt.JwtEncoder;
//import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.security.SecureRandom;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.jonathan.web.service.UserService;


import org.springframework.security.core.AuthenticationException;
import com.jonathan.web.resources.UserLoginDto;
import org.springframework.web.bind.annotation.RequestMapping;

////import com.auth0.jwt.algorithms.Algorithm;
////import com.auth0.jwt.JWT;
////import com.auth0.jwt.exceptions.JWTCreationException;
////import com.auth0.jwt.interfaces.Claim;
////import com.auth0.jwt.interfaces.DecodedJWT;
////import com.auth0.jwt.interfaces.JWTVerifier;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.jonathan.web.service.UserService;

import com.jonathan.web.dao.UserRepository;
import com.jonathan.web.resources.UserRegistrationDto;
import org.slf4j.Logger;

@RestController
public class UserController 
{
  @Autowired
  private UserService userService;

  @Autowired
  Logger logger;

  //@PostMapping("/login")
  //public String signin(String username, String password) {
  //public String signin(@ResponseBody UserLoginDto userLogin) {
  // UserLoginDto
  @RequestMapping(
    value = {"/login"},
    method = RequestMethod.POST,
    produces = "application/json",
    consumes = "application/json"
  )
  @ResponseBody
  public Map<String, String> signin(@RequestBody UserLoginDto userLogin) 
  {
    HashMap<String, String> responseJson = new HashMap<>();
    String loginJwt;

    try 
    {
      loginJwt = userService.login(userLogin.getUsername(), userLogin.getPassword());
    } catch (Exception e) 
    {
      logger.info("Failed jwt");
      responseJson.put("jwt_response", "");
      return responseJson;
    }
    logger.info("Returned jwt: '" + loginJwt + "' for user " + userLogin.getUsername());

    responseJson.put("jwt_response", loginJwt);
    return responseJson;
  }

  @PostMapping("/register")
  @ResponseStatus(code = HttpStatus.CREATED)
  public void register(@RequestBody UserRegistrationDto newUserRequest) 
  {
    userService.register(newUserRequest);
  }
}
