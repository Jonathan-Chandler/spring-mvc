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
 
@Component
public class JwtTokenFilter extends OncePerRequestFilter 
{
  @Autowired
  private JwtTokenService jwtTokenService;

  @Autowired
  private UserRepository userRepository;

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
    System.out.println("FilterChain dofilter for request/response");
    filterChain.doFilter(request, response);
  }

  private boolean hasAuthorizationBearer(HttpServletRequest request) 
  {
    String header = request.getHeader("Authorization");
    if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) 
    {
      System.out.println("Failed to get Authorization Bearer header: " + header);
      return false;
    }

    System.out.println("Successfully get Authorization Bearer header: " + header);
    return true;
  }

  private String getAccessToken(HttpServletRequest request) {
    String header = request.getHeader("Authorization");
    String token = header.split(" ")[1].trim();
    System.out.println("Return token: " + token);
    return token;
  }

  private void setAuthenticationContext(String token, HttpServletRequest request) 
  {
    System.out.println("setAuthenticationContext token: " + token);
    UserDetails userDetails = getUserDetails(token);

    UsernamePasswordAuthenticationToken
      authentication = new UsernamePasswordAuthenticationToken(userDetails, null, null);

    System.out.println("UsernamePasswordAuthenticationToken: " + authentication);

    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private UserDetails getUserDetails(String token) 
  {
    String username = jwtTokenService.getUsernameFromToken(token);
    System.out.println("Return username from token: " + username);

    User userDetails = userRepository.findOneByUsername(username).orElse(null);
    if (userDetails == null)
    {
      System.out.println("userDetails returns null");
    }

    return userDetails;
  }
}
