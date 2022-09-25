package com.jonathan.web.controllers.authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jonathan.web.dao.UserRepository;
import com.jonathan.web.controllers.authentication.UserDetailsMapper;
import com.jonathan.web.resources.UserLoginDto;
import java.util.Optional;

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
    UserLoginDto loginData = userRepository.findOneByUsername(username).orElse(null);
    if (loginData == null)
    {
      throw new UsernameNotFoundException("User not found with username " + username);
    }
    //System.out.println("looked up user: " + returnName + " with password: " + returnPassword);

    return userDetailsMapper.toUserDetails(loginData);
  }
}

//  @Override
//  public UserDetails updatePassword(UserDetails user, String newPassword) {
//    UserCredentials userCredentials = userRepository.findByUsername(user.getUsername());
//    userCredentials.setPassword(newPassword);
//    return userDetailsMapper.toUserDetails(userCredentials);
//  }

    //Optional<UserLoginDto> loginData = userRepository.findOneByUsername(username);

    //String returnName = loginData.isPresent() ? loginData.get().getUsername() : "";
    //String returnPassword = loginData.isPresent() ? loginData.get().getPassword() : "";
    //if (returnName == "")
    //{
    //  System.out.println("could not find user: " + username);
    //  throw new UsernameNotFoundException("User not found with username " + username);
    //}

