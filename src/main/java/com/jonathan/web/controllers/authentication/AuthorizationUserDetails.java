package com.jonathan.web.controllers.authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jonathan.web.dao.UserRepository;
import com.jonathan.web.controllers.authentication.UserDetailsMapper;
import com.jonathan.web.resources.UserLoginDto;

@Service
@Transactional
public class AuthorizationUserDetails implements UserDetailsService 
{
  private final UserRepository userRepository;
  private final UserDetailsMapper userDetailsMapper;

  public AuthorizationUserDetails(UserRepository userRepository, UserDetailsMapper userDetailsMapper) 
  {
    this.userRepository = userRepository;
    this.userDetailsMapper = userDetailsMapper;
  }

  @Override
  public UserDetails loadUserByUsername(String username)
          throws UsernameNotFoundException 
  {
    //UserLoginCredentialsDto userCredentials = userRepository.findById(username).orElse(null);
    UserLoginDto loginData = userRepository.findByUsername(username);
    if (loginData == null)
    {
      System.out.println("could not find user: " + username);
      throw new UsernameNotFoundException("User not found with username " + username);
    }

    System.out.println("looked up user: " + loginData.getUsername());
    return userDetailsMapper.toUserDetails(loginData);
  }
}

//  @Override
//  public UserDetails updatePassword(UserDetails user, String newPassword) {
//    UserCredentials userCredentials = userRepository.findByUsername(user.getUsername());
//    userCredentials.setPassword(newPassword);
//    return userDetailsMapper.toUserDetails(userCredentials);
//  }

