package com.jonathan.web.controllers.authentication;
 
import java.io.IOException;
 
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
 
import com.jonathan.web.service.JwtTokenService;
import com.jonathan.web.entities.User;
import com.jonathan.web.dao.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
@Component
public class JwtTokenFilter extends OncePerRequestFilter 
{
  @Autowired
  private JwtTokenService jwtTokenService;

  @Autowired
  private UserRepository userRepository;

  private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException 
  {
    if (!hasAuthorizationBearer(request)) 
    {
      filterChain.doFilter(request, response);
      return;
    }

    String token = getAccessToken(request);
    if (!jwtTokenService.validateJwtToken(token)) 
    {
      filterChain.doFilter(request, response);
      return;
    }

    setAuthenticationContext(token, request);
    logger.info("FilterChain dofilter for request/response");
    filterChain.doFilter(request, response);
  }

  private boolean hasAuthorizationBearer(HttpServletRequest request) 
  {
    String header = request.getHeader("Authorization");
    if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) 
    {
      logger.info("Failed to get Authorization Bearer header: " + header);
      return false;
    }

    logger.info("Successfully get Authorization Bearer header: " + header);
    return true;
  }

  private String getAccessToken(HttpServletRequest request) {
    String header = request.getHeader("Authorization");
    String token = header.split(" ")[1].trim();
    logger.info("Return token: " + token);
    return token;
  }

  private void setAuthenticationContext(String token, HttpServletRequest request) 
  {
    logger.info("setAuthenticationContext token: " + token);
    UserDetails userDetails = getUserDetails(token);

    UsernamePasswordAuthenticationToken
      authentication = new UsernamePasswordAuthenticationToken(userDetails, null, null);

    logger.info("UsernamePasswordAuthenticationToken: " + authentication);

    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private UserDetails getUserDetails(String token) 
  {
    String username = jwtTokenService.getUsernameFromToken(token);
    logger.info("Return username from token: " + username);

    User userDetails = userRepository.findOneByUsername(username).orElse(null);
    if (userDetails == null)
    {
      logger.info("userDetails returns null");
    }

    return userDetails;
  }
}
