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
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtService
{
  //private int secondsToExpire = 300;

  // jwt will expire in 8 hours
  private int secondsToExpire = 8*60*60;
  private String issuer;
  private byte[] secretKey;
  private JWSSigner signer;
  private JWSVerifier verifier;

  //private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  JwtService() throws KeyLengthException, JOSEException
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

  public String generateToken(String username) 
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

      // check that valid token is returned
      if (!validateToken(token))
      {
        logger.info("Failed to validate generated token for user " + username);
        return "";
      }

      return "Bearer " + token;
    }
    catch(Exception e)
    {
      logger.info("Failed to generate JWT token for user " + username + " return: " + e.getMessage());
      return "";
    }
  }

  public Boolean validateToken(String token) 
  {
    try {
      // token is not empty
      if (token == null || token.isEmpty())
      {
        return false;
      }

      // expiration time is in the future
      SignedJWT signedJWT = SignedJWT.parse(token);
      if (!signedJWT.verify(verifier))
      {
        logger.error("Failed to validate token signature");
        return false;
      }

      // token was issued by this service
      if (!(signedJWT.getJWTClaimsSet().getIssuer().equals(this.issuer))
          || !(new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime())))
      {
        logger.info("Token fields did not match expected for user " + signedJWT.getJWTClaimsSet().getSubject());
        return false;
      }
    }
    catch (Exception e)
    {
      logger.info("Failed to validate token: " + e);
      return false;
    }

    return true;
  }

  public Boolean validateTokenUsername(String token, String username) 
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
    try 
    {
      SignedJWT signedJWT = SignedJWT.parse(token);

      // empty if token not valid
      if (!signedJWT.verify(verifier))
      {
        return "";
      }

      String username = signedJWT.getJWTClaimsSet().getSubject();

      return username;
    }
    catch (Exception e)
    {
      logger.info("Failed to read subject from token");
      return "";
    }
  }
}

