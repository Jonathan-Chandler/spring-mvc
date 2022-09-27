package com.jonathan.web.controllers.authentication;
//
import java.io.IOException;
//
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.jonathan.web.service.UserService;
import com.jonathan.web.service.JwtTokenService;

import com.jonathan.web.dao.UserRepository;
import com.jonathan.web.entities.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.core.context.SecurityContextHolder;

@Component
public class JwtTokenFilter extends OncePerRequestFilter 
{
  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtTokenService jwtTokenService;
  //private final UserRepo userRepo;

  //private boolean isEmpty(String value)
  //{
  //  if (
  //}

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain)
    throws ServletException, IOException 
  {
    // Get authorization header and validate
    final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header == null || !header.startsWith("Bearer ")) 
    {
      chain.doFilter(request, response);
      return;
    }

    // Get jwt token and validate
    final String token = header.split(" ")[1].trim();
    if (!jwtTokenService.validateJwtToken(token)) 
    {
      chain.doFilter(request, response);
      return;
    }

    // Get user identity and set it on the spring security context
    User user = userRepository
      .findOneByUsername(jwtTokenService.getUsernameFromToken(token))
      .orElse(null);

    if (user == null)
    {
      
    }

    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());

    authentication.setDetails(
        new WebAuthenticationDetailsSource().buildDetails(request)
        );

    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(request, response);
  }
}

//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.GenericFilterBean;
//
//import org.springframework.web.filter.OncePerRequestFilter;
//
//// We should use OncePerRequestFilter since we are doing a database call, there is no point in doing this more than once
//public class JwtTokenFilter extends OncePerRequestFilter 
//{
//  private JwtTokenProvider jwtTokenProvider;
//
//  public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) 
//  {
//    this.jwtTokenProvider = jwtTokenProvider;
//  }
//
//  @Override
//  protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException
//  {
//    String token = jwtTokenProvider.resolveToken(httpServletRequest);
//    try 
//    {
//      if (token != null && jwtTokenProvider.validateToken(token)) 
//      {
//        Authentication auth = jwtTokenProvider.getAuthentication(token);
//        SecurityContextHolder.getContext().setAuthentication(auth);
//      }
//    } catch (Exception ex) 
//    {
//      //this is very important, since it guarantees the user is not authenticated at all
//      SecurityContextHolder.clearContext();
//      httpServletResponse.sendError(ex.getHttpStatus().value(), ex.getMessage());
//      return;
//    }
//
//    filterChain.doFilter(httpServletRequest, httpServletResponse);
//  }
//}
//
//package com.javainuse.config;
//
//import java.io.IOException;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import com.javainuse.service.JwtUserDetailsService;
//
//import io.jsonwebtoken.ExpiredJwtException;
//
//@Component
//public class JwtRequestFilter extends OncePerRequestFilter {
//
//  @Autowired
//  private JwtUserDetailsService jwtUserDetailsService;
//
//  @Autowired
//  private JwtTokenUtil jwtTokenUtil;
//
//  @Override
//  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//      throws ServletException, IOException {
//
//    final String requestTokenHeader = request.getHeader("Authorization");
//
//    String username = null;
//    String jwtToken = null;
//    // JWT Token is in the form "Bearer token". Remove Bearer word and get
//    // only the Token
//    if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
//      jwtToken = requestTokenHeader.substring(7);
//      try {
//        username = jwtTokenUtil.getUsernameFromToken(jwtToken);
//      } catch (IllegalArgumentException e) {
//        System.out.println("Unable to get JWT Token");
//      } catch (ExpiredJwtException e) {
//        System.out.println("JWT Token has expired");
//      }
//    } else {
//      logger.warn("JWT Token does not begin with Bearer String");
//    }
//
//    // Once we get the token validate it.
//    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//      UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
//
//      // if token is valid configure Spring Security to manually set
//      // authentication
//      if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
//
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//            userDetails, null, userDetails.getAuthorities());
//        usernamePasswordAuthenticationToken
//            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        // After setting the Authentication in the context, we specify
//        // that the current user is authenticated. So it passes the
//        // Spring Security Configurations successfully.
//        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//      }
//    }
//    chain.doFilter(request, response);
//  }
//
//}
//
