package com.jonathan.web.controller.authentication;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.oauth2.jwt.JwtClaimsSet;
//import org.springframework.security.oauth2.jwt.JwtEncoder;
//import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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

@RestController
public class LoginController 
{
	//@PostMapping("/login")
	//public String token() throws KeyLengthException, JOSEException
  //{
  //  // Generate random 256-bit (32-byte) shared secret
  //  SecureRandom random = new SecureRandom();
  //  byte[] sharedSecret = new byte[32];
  //  random.nextBytes(sharedSecret);

  //  // Create HMAC signer
  //  JWSSigner signer = new MACSigner(sharedSecret);

  //  // Apply the HMAC
  //  JWSObject jwsTest = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload("test"));

  //  jwsTest.sign(signer);

  //  return jwsTest.serialize();
  //}
  private final UserService userService;

  LoginController(UserService userService)
  {
    this.userService = userService;
  }

	@PostMapping("/login")
  //public String signin(String username, String password) {
  // UserLoginDto
  @RequestMapping(
    value = {"/login"},
    method = RequestMethod.POST,
    produces = "application/json",
    consumes = "application/json"
  )
  @ResponseBody
  //public String signin(@ResponseBody UserLoginDto userLogin) {
  public String signin(@RequestBody UserLoginDto userLogin) {
    String loginJwt;
    System.out.println("requestbody username: " + userLogin.getUsername());
    System.out.println("requestbody pass: " + userLogin.getPassword());
    try 
    {
      loginJwt = userService.login(userLogin.getUsername(), userLogin.getPassword());
    } catch (Exception e) 
    {
      System.out.println("failed jwt");
      return "Failed";
    }
    //try {
    //  authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    //  return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getAppUserRoles());
    //} catch (AuthenticationException e) {
    //  throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
    //}
    System.out.println("jwt: " + loginJwt);
    return loginJwt;
  }

//	@Autowired
//	JwtEncoder encoder;
//
//	@PostMapping("/login")
//	public String token(Authentication authentication) {
//		Instant now = Instant.now();
//		long expiry = 36000L;
//		// @formatter:off
//		String scope = authentication.getAuthorities().stream()
//				.map(GrantedAuthority::getAuthority)
//				.collect(Collectors.joining(" "));
//		JwtClaimsSet claims = JwtClaimsSet.builder()
//				.issuer("self")
//				.issuedAt(now)
//				.expiresAt(now.plusSeconds(expiry))
//				.subject(authentication.getName())
//				.claim("scope", scope)
//				.build();
//		// @formatter:on
//		return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
//	}
//
////	@PostMapping("/login")
////	public String token() 
////  {
////    String token;
////    Algorithm algorithm = Algorithm.HMAC256("secret");
////
////    try {
////      token = JWT.create()
////          .withIssuer("auth0")
////          .sign(algorithm);
////    } catch (JWTCreationException exception){
////        //Invalid Signing configuration / Couldn't convert Claims.
////      token = "invalid";
////    }
////    //Reusable verifier instance
////    JWTVerifier verifier = JWT.require(algorithm)
////        .withIssuer("auth0")
////        .build();
////    DecodedJWT jwt = verifier.verify(token);
////    Claim claim = jwt.getHeaderClaim("owner");
////        /**
////     * The "iss" (issuer) claim identifies the principal that issued the JWT.
////     * Refer RFC 7529 <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.1">Section 4.1.1</a>
////     */
////    //public static final String ISSUER = "iss";
////
////    /**
////     * The "sub" (subject) claim identifies the principal that is the subject of the JWT.
////     * Refer RFC 7529 <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.2">Section 4.1.2</a>
////     */
////    //public static final String SUBJECT = "sub";
////
////    /**
////     * The "aud" (audience) claim identifies the recipients that the JWT is intended for.
////     * Refer RFC 7529 <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.3">Section 4.1.3</a>
////     */
////    //public static final String AUDIENCE = "aud";
////
////    /**
////     * The "exp" (expiration time) claim identifies the expiration time on or after which the JWT MUST NOT be
////     * accepted for processing.
////     * Refer RFC 7529 <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.4">Section 4.1.4</a>
////     */
////    //public static final String EXPIRES_AT = "exp";
////
////    /**
////     * The "nbf" (not before) claim identifies the time before which the JWT MUST NOT be accepted for processing.
////     * Refer RFC 7529 <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.5">Section 4.1.5</a>
////     */
////    //public static final String NOT_BEFORE = "nbf";
////
////    /**
////     * The "iat" (issued at) claim identifies the time at which the JWT was issued.
////     * Refer RFC 7529 <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.6">Section 4.1.6</a>
////     */
////    //public static final String ISSUED_AT = "iat";
////
////    /**
////     * The "jti" (JWT ID) claim provides a unique identifier for the JWT.
////     */
////    //public static final String JWT_ID = "jti";
////    return token;
////  }
}

//package com.jonathan.web.controllers.authentication;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//
//import com.jonathan.web.dao.UserRepository;
//import com.jonathan.web.entities.UserData;
//import com.jonathan.web.resources.UserLoginDto;
//
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//
//@RestController
//public class LoginController
//{
//  private final AuthenticationManager authenticationManager;
//  private final JwtUtils jwtUtils;
//  private final UserRepository userRepository;
//  private final PasswordEncoder passwordEncoder;
//
//  public LoginController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserRepository userRepository, PasswordEncoder passwordEncoder) {
//    this.authenticationManager = authenticationManager;
//    this.jwtUtils = jwtUtils;
//    this.userRepository = userRepository;
//    this.passwordEncoder = passwordEncoder;
//  }
//  
//  //@PostMapping("/login")
//  //  public ResponseEntity<?> login(@valid @RequestBody UserLoginDto userLogin) 
//  //{
//  //  // UsernamePasswordAuthenticationToken token = 
//  //  //   new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword());
//  //  Authentication authentication = authenticationManager.authenticate(
//  //      new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword()));
//
//  //  SecurityContextHolder.getContext().setAuthentication(authentication);
//  //  String jwt = jwtUtils.generateJwtToken(authentication);
//
//  //  List<String> roles = "";
//
//  //  AuthorizationUserDetails userDetails = (AuthorizationUserDetails) authentication.getPrincipal();
//
//  //  // JwtResponse(token, type, id, username, email, roles)
//  //  return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.get
//
//  //  //UserDetails userDetails = toUserDetails(userLogin);
//  //  ////java.util.Optional<UserData> user = userRepository.findById(userLoginCredentialsDto.getUsername());
//  //  ////UserData user = userRepository.findById(userLogin.getUsername()).orElse(null);
//  //  ////userRepository.findById(email).orElse(null);
//  //  //try {
//  //  //  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//  //  //        userLogin.getUsername(), userLogin.getPassword())
//  //  //}
//  //  //catch (BadCredentialsException e)
//  //  //{
//  //  //  System.out.println("Failed to find user with name: " + userLogin.getUsername());
//  //  //  return new ResponseEntity<>("Login failed for username: " + userLogin.getUsername(), HttpStatus.BAD_REQUEST);
//  //  //}
//
//  //  //
//  //  //return new ResponseEntity<>("Login successful for username: " + userLogin.getUsername(), HttpStatus.OK);
//  //}
//}
//
