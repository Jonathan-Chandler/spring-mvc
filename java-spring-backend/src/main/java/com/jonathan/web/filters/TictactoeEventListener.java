//package com.jonathan.web.filters;
// 
//import java.io.IOException;
// 
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
// 
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.util.ObjectUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
// 
//import com.jonathan.web.service.JwtTokenService;
//import com.jonathan.web.entities.User;
//import com.jonathan.web.dao.UserRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
// 
//@Component
//public class TictactoeEventListener
//{
//  @Autowired
//  private JwtTokenService jwtTokenService;
//
//  @Autowired
//  private UserRepository userRepository;
//
//  // don't autowire because of cyclic dependency
//  //private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);
//  private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//  @Override
//  protected void doFilterInternal(HttpServletRequest request,
//      HttpServletResponse response, FilterChain filterChain)
//    throws ServletException, IOException 
//  {
//    try
//    {
//      // check if header has bearer token
//      if (!hasAuthorizationBearer(request)) 
//      {
//        filterChain.doFilter(request, response);
//        return;
//      }
//
//      // read JWT from header
//      String token = getRequestHeaderJwt(request);
//      if (!jwtTokenService.validateJwtToken(token)) 
//      {
//        filterChain.doFilter(request, response);
//        return;
//      }
//
//      // get JWT from 
//      String username = jwtTokenService.getUsernameFromToken(token);
//      if (username == null)
//      {
//        logger.info("Failed to get username from token");
//        filterChain.doFilter(request, response);
//        return;
//      }
//
//      // generate refresh JWT from username in current JWT
//      String refreshToken = jwtTokenService.generateJwtToken(username);
//      if (refreshToken == null)
//      {
//        logger.error("Failed to refresh token");
//      }
//      else
//      {
//        response.addHeader("Authorization", refreshToken);
//      }
//
//      // authenticate the request for filter
//      setAuthenticationContext(token, request);
//
//      // continue to authenticated page or username/password authentication
//      logger.info("Success filterChain dofilter for request/response");
//      filterChain.doFilter(request, response);
//    }
//    catch (Exception e)
//    {
//      logger.info("JWT authorization failed");
//      filterChain.doFilter(request, response);
//      return;
//    }
//  }
//
//  private boolean hasAuthorizationBearer(HttpServletRequest request) 
//  {
//    String header = request.getHeader("Authorization");
//    if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) 
//    {
//      logger.info("Failed to get Authorization Bearer header: " + header);
//      return false;
//    }
//
//    logger.info("Successfully get Authorization Bearer header: " + header);
//    return true;
//  }
//
//  private String getRequestHeaderJwt(HttpServletRequest request) {
//    String header = request.getHeader("Authorization");
//    String token = header.split(" ")[1].trim();
//    //UserDetails userDetails = getUserDetails(token);
//    //if (userDetails == null)
//    //{
//    //  logger.error("Failed to get user details from header");
//    //  return "";
//    //}
//    //logger.info("User " + userDetails.getUsername() + " accessing with token: " + token);
//    return token;
//  }
//
//  private void setAuthenticationContext(String token, HttpServletRequest request) 
//  {
//    UserDetails userDetails = getUserDetails(token);
//
//    UsernamePasswordAuthenticationToken
//      authentication = new UsernamePasswordAuthenticationToken(userDetails, null, null);
//
//    logger.info("UsernamePasswordAuthenticationToken: " + authentication);
//
//    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//    SecurityContextHolder.getContext().setAuthentication(authentication);
//  }
//
//  private UserDetails getUserDetails(String token) 
//  {
//    String username = jwtTokenService.getUsernameFromToken(token);
//    logger.info("Return username from token: " + username);
//
//    User userDetails = userRepository.findOneByUsername(username).orElse(null);
//    if (userDetails == null)
//    {
//      logger.info("userDetails returns null");
//    }
//
//    return userDetails;
//  }
//}
