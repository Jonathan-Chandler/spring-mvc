package com.jonathan.web.controllers.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.jonathan.web.dao.UserRepository;
import com.jonathan.web.entities.UserData;
import org.springframework.security.crypto.password.*;
import com.jonathan.web.configuration.SecurityConfiguration;
import com.jonathan.web.resources.UserLoginDto;
import org.springframework.stereotype.Service;
//import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import java.util.Optional;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider 
{
  //@Resource
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;
  //private final AuthenticationProvider authenticationProvider;

//@Lazy UserRepository userRepository, 
//PasswordEncoder passwordEncoder)
      //AuthenticationProvider authenticationProvider)
  public CustomAuthenticationProvider()
  {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    //this.authenticationProvider = authenticationProvider;
    //this.AuthenticationManager = AuthenticationManager;
  }

  @Override
  public Authentication authenticate(Authentication authentication)
    throws AuthenticationException 
  {
    System.out.println("Enter CustomAuthenticationProvider authenticate");
    String name = authentication.getName();
    System.out.println("name: " + name);
    String password = authentication.getCredentials().toString();
    System.out.println("pass: " + password);

    //Optional<UserLoginDto> optDbLoginDto = userRepository.findOneByUsername(name);
    //String returnName = optDbLoginDto.isPresent() ? optDbLoginDto.get().getUsername() : "";
    //String returnPassword = optDbLoginDto.isPresent() ? optDbLoginDto.get().getPassword() : "";

    System.out.println("did FindByUsername");

    //// make sure username exists in database
    //if (dbLoginDto == null)
    //{
    //  dbLoginDto.getValue(

    //  System.out.println("Fail to find username: " + name + "password: " + password);
    //  throw new BadCredentialsException("Fail to find username");
    //}
    //String matchedName = Optional.<UserLoginDto>ofNullable(null).orElse(getUsername());
    //if (matchedName == null || matchedPassword == null)
    //{
    //  System.out.println("Fail to find username: " + name + "password: " + password);
    //  throw new BadCredentialsException("Fail to find username");
    //}

    //dbLoginDto = optDbLoginDto.get();
    //if (returnName != "" && returnPassword != "")
    //{
    //  System.out.println("Found username: " + returnName + "password: " + returnPassword);
    //}
    //else
    //{
    //  throw new BadCredentialsException("Wrong password");
    //}

    //// check if encoded password from dao matches credentials
    //if (passwordEncoder.matches(password, returnPassword))
    //{
    //  Authentication auth = new UsernamePasswordAuthenticationToken(name, password);

    //  return auth;
    //}
    //else
    //{
    //  throw new BadCredentialsException("Wrong password");
    //}
    return null;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }

  //@Override
  //public boolean supports(Class<?> authentication) {
  //  return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
  //}
}
