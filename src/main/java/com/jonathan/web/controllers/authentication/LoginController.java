//package com.jonathan.web.controller.authentication;
//
//import java.time.Instant;
//import java.util.stream.Collectors;
//import java.util.Map;
//import java.util.HashMap;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
////import org.springframework.security.oauth2.jwt.JwtClaimsSet;
////import org.springframework.security.oauth2.jwt.JwtEncoder;
////import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.security.SecureRandom;
//import com.nimbusds.jose.*;
//import com.nimbusds.jose.crypto.*;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//
//import com.jonathan.web.service.UserService;
//
//
//import org.springframework.security.core.AuthenticationException;
//import com.jonathan.web.resources.UserLoginDto;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//////import com.auth0.jwt.algorithms.Algorithm;
//////import com.auth0.jwt.JWT;
//////import com.auth0.jwt.exceptions.JWTCreationException;
//////import com.auth0.jwt.interfaces.Claim;
//////import com.auth0.jwt.interfaces.DecodedJWT;
//////import com.auth0.jwt.interfaces.JWTVerifier;
//
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.beans.factory.annotation.Autowired;
//
//@RestController
//public class LoginController 
//{
//  @Autowired
//  private UserService userService;
//
//	@PostMapping("/login")
//  //public String signin(String username, String password) {
//  // UserLoginDto
//  @RequestMapping(
//    value = {"/login"},
//    method = RequestMethod.POST,
//    produces = "application/json",
//    consumes = "application/json"
//  )
//  //public String signin(@ResponseBody UserLoginDto userLogin) {
//  @ResponseBody
//  public Map<String, String> signin(@RequestBody UserLoginDto userLogin) 
//  {
//    HashMap<String, String> responseJson = new HashMap<>();
//    String loginJwt;
//
//    try 
//    {
//      loginJwt = userService.login(userLogin.getUsername(), userLogin.getPassword());
//    } catch (Exception e) 
//    {
//      logger.info("Failed jwt");
//      responseJson.put("jwt_response", "");
//      return responseJson;
//    }
//    System.out.println("Returned jwt: " + loginJwt);
//
//    responseJson.put("jwt_response", loginJwt);
//    return responseJson;
//    //return "{'jwt_response':'" + loginJwt + "'}";
//  }
//}
