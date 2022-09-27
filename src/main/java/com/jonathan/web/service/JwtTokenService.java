package com.jonathan.web.service;
import org.springframework.beans.factory.annotation.Value;
import com.nimbusds.jwt.*;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;

import java.security.SecureRandom;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Component
public class JwtTokenService
{
  //@Value("${security.jwt.token.secret-key:secret-key}")
  //private String secretKey;

  // Default expire time = 5 minutes
  //@Value("${security.jwt.token.expire-length:300000}")
  //private long expireMillisec = 300000;
  private int secondsToExpire = 300;

  private String issuer;
  private byte[] secretKey;
  private JWSSigner signer;
  private JWSVerifier verifier;

  JwtTokenService() throws KeyLengthException, JOSEException
  {
    // generate JWS key on initialization
    SecureRandom random = new SecureRandom();
    byte[] generatedSecret = new byte[64];
    random.nextBytes(generatedSecret);

    // Create HMAC signer/verifier
    this.signer = new MACSigner(generatedSecret);
    this.verifier = new MACVerifier(generatedSecret);
    this.secretKey = generatedSecret;
    this.issuer = "com.jonathan.web";
  }

  //public String createToken(String username, List<String> roles) throws JOSEException
  //{
  //  // Apply the HMAC
  //  JWSObject jwsTest = new JWSObject(new JWSHeader(JWSAlgorithm.HS512), new Payload("test"));

  //  jwsTest.sign(signer);
  //  System.out.println("signed jwsTest: " + jwsTest.serialize());

  //  return jwsTest.serialize();
  //}

  public String generateJwtToken(String username) 
  {
    try 
    {
      Map<String, Object> claims = new HashMap<>(); 

      JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
      .subject(username)
      .issuer(this.issuer)
      .expirationTime(new Date(System.currentTimeMillis() + secondsToExpire * 1000))
      .build();

      SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS512), claimsSet);
      signedJWT.sign(signer);
      String token = signedJWT.serialize();
      //System.out.println("Generated token: " + token);

      // check that valid token is returned
      if (!validateJwtToken(token) || !validateJwtTokenUsername(token, username))
      {
        System.out.println("Failed to validate generated token");
        return "";
      }

      return token;
    }
    catch(Exception e)
    {
      System.out.println("Failed to generate JWT token for user " + username);
      return "";
    }
  }

  public Boolean validateJwtToken(String token) 
  {
    try {
      SignedJWT signedJWT = SignedJWT.parse(token);

      if (!signedJWT.verify(verifier))
      {
        System.out.println("Failed to validate token");
        return false;
      }

      if (!(signedJWT.getJWTClaimsSet().getIssuer().equals(this.issuer))
          || !(new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime())))
      {
        System.out.println("Token fields did not match expected");
        System.out.println("subject: " + signedJWT.getJWTClaimsSet().getSubject());
        System.out.println("issuer: " + signedJWT.getJWTClaimsSet().getIssuer());
        System.out.println("expires: " + signedJWT.getJWTClaimsSet().getExpirationTime());
        System.out.println("Date(): " + new Date());
        return false;
      }
    }
    catch (Exception e)
    {
      System.out.println("Failed to validate token");
      return false;
    }

    return true;
  }

  public Boolean validateJwtTokenUsername(String token, String username) 
  {
    String tokenUsername = getUsernameFromToken(token);
    if (!username.equals(tokenUsername))
    {
      return false;
    }

    return true;
  }

  public String getUsernameFromToken(String token) 
  {
    try {
    SignedJWT signedJWT = SignedJWT.parse(token);

    // empty if token not valid
    if (!signedJWT.verify(verifier))
    {
      return "";
    }

    String username = signedJWT.getJWTClaimsSet().getSubject();
    //System.out.println("return username: " + username);

    return username;
    }
    catch (Exception e)
    {
      System.out.println("Failed to read subject from token");
      return "";
    }
  }
}

  //public Boolean validateJwtToken(String token, UserDetails userDetails) 
  //public Boolean validateJwtToken(String token, String username) 
  //{
  //  try {
  //    SignedJWT signedJWT = SignedJWT.parse(token);

  //    if (!signedJWT.verify(verifier))
  //    {
  //      System.out.println("Failed to validate token");
  //      return false;
  //    }

  //    if (!(signedJWT.getJWTClaimsSet().getSubject().equals(username))
  //        || !(signedJWT.getJWTClaimsSet().getIssuer().equals(this.issuer))
  //        || !(new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime())))
  //    {
  //      System.out.println("Token fields did not match expected");
  //      System.out.println("subject: " + signedJWT.getJWTClaimsSet().getSubject());
  //      System.out.println("issuer: " + signedJWT.getJWTClaimsSet().getIssuer());
  //      System.out.println("expires: " + signedJWT.getJWTClaimsSet().getExpirationTime());
  //      System.out.println("Date(): " + new Date());
  //      return false;
  //    }
  //  }
  //  catch (Exception e)
  //  {
  //    System.out.println("Failed to validate token");
  //    return false;
  //  }

  //  return true;
  //}


//    Claims claims = Jwts.claims().setSubject(username);
//    claims.put("auth", appUserRoles.stream().map(s -> new SimpleGrantedAuthority(s.getAuthority())).filter(Objects::nonNull).collect(Collectors.toList()));
//
//    Date now = new Date();
//    Date validity = new Date(now.getTime() + validityInMilliseconds);
//
//    return Jwts.builder()//
//        .setClaims(claims)//
//        .setIssuedAt(now)//
//        .setExpiration(validity)//
//        .signWith(SignatureAlgorithm.HS256, secretKey)//
//        .compact();
//  }
//  OAuth2AccessToken(
//  OAuth2AccessToken.TokenType tokenType, // Bearer
//  java.lang.String tokenValue, 
//  java.time.Instant issuedAt, 
//  java.time.Instant expiresAt,
//  (optional) Set<String> scopes
//  )
//
//  OAuth2LoginAuthenticationProvider(
//      OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient, 
//      OAuth2UserService<OAuth2UserRequest,OAuth2User> userService
//    )
//
//    .setAuthoritiesMapper(GrantedAuthoritiesMapper authoritiesMapper) 	
//      Sets the GrantedAuthoritiesMapper used for mapping OAuth2AuthenticatedPrincipal.getAuthorities() 
//      to a new set of authorities which will be associated to the OAuth2LoginAuthenticationToken.
//
// OAuth2AccessTokenResponseClient<T extends AbstractOAuth2AuthorizationGrantRequest>
//    .getTokenResponse(T authorizationGrantRequest)
