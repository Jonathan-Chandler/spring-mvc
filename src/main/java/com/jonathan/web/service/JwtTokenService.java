package com.jonathan.web.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;

import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenService
{
  //@Value("${security.jwt.token.secret-key:secret-key}")
  @Value("${security.jwt.token.secret-key:secret-key}")
  private String secretKey;

  @Value("${security.jwt.token.expire-length:3600000}")
  private long validityInMilliseconds = 3600000; // 1h
  private JWSSigner signer;

  JwtTokenService() throws KeyLengthException
  {
    SecureRandom random = new SecureRandom();
    byte[] sharedSecret = new byte[32];
    random.nextBytes(sharedSecret);

    // Create HMAC signer
    signer = new MACSigner(sharedSecret);
  }

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
  public String createToken(String username, List<String> roles) throws JOSEException
  {
    // Apply the HMAC
    JWSObject jwsTest = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload("test"));

    jwsTest.sign(signer);
    System.out.println("signed jwsTest: " + jwsTest.serialize());

    return jwsTest.serialize();
  }
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
}
