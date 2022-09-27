package com.jonathan.web.controllers.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.jonathan.web.dao.UserRepository;
import com.jonathan.web.entities.User;
//import com.jonathan.web.entities.UserData;
import org.springframework.security.crypto.password.*;
import com.jonathan.web.configuration.SecurityConfiguration;
import com.jonathan.web.resources.UserLoginDto;
import org.springframework.stereotype.Service;
//import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import java.util.Optional;
import java.util.ArrayList;
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

  @Override
  public Authentication authenticate(Authentication authentication)
    throws AuthenticationException 
  {
    String username = authentication.getName();
    String password = authentication.getCredentials().toString();

    try 
    {
      User user = userRepository.findOneByUsername(username).orElse(null);
      if (user == null)
      {
        System.out.println("Fail to find username: " + username);
        throw new BadCredentialsException("Fail to find username");
      }
      else
      {
        // compare encoded password to login credentials
        if (!passwordEncoder.matches(password, user.getPassword()))
        {
          System.out.println("Password did not match: '" + password + "' Encoded password: " + user.getPassword());
          return null;
        }
        else
        {
          System.out.println("Input password matched: " + password);
          // return approved authentication token with authorities and no password
          return new UsernamePasswordAuthenticationToken(username, "", user.getAuthorities());
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("User repo throws exception");
      throw new BadCredentialsException("Fail to find username");
    }
  }

  @Override
  public boolean supports(Class<?> authentication) 
  {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}

    //private final AuthenticationProvider authenticationProvider;

    //  this.passwordEncoder = passwordEncoder;
    //@Lazy UserRepository userRepository, 
    //PasswordEncoder passwordEncoder)
    //AuthenticationProvider authenticationProvider)
    //public CustomAuthenticationProvider()
    //{
    //  this.userRepository = userRepository;
    //  this.passwordEncoder = passwordEncoder;
    //  //this.authenticationProvider = authenticationProvider;
    //  //this.AuthenticationManager = AuthenticationManager;
    //}
    //Optional<UserLoginDto> optDbLoginDto = userRepository.findOneByUsername(name);
    //String returnName = optDbLoginDto.isPresent() ? optDbLoginDto.get().getUsername() : "";
    //String returnPassword = optDbLoginDto.isPresent() ? optDbLoginDto.get().getPassword() : "";


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
    //return null;
//  }

  //@Override
  //public boolean supports(Class<?> authentication) {
  //  return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
  //}
