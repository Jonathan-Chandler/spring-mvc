package com.jonathan.web.controllers.authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jonathan.web.dao.UserRepository;
import com.jonathan.web.entities.UserData;

import com.jonathan.web.controllers.authentication.UserDetailsMapper;

@Transactional
@Service
public class AuthorizationPasswordService implements UserDetailsPasswordService 
{
  private final UserRepository userRepository;

  private final UserDetailsMapper userDetailsMapper;

  public AuthorizationPasswordService(
      UserRepository userRepository, UserDetailsMapper userDetailsMapper) 
  {
    this.userRepository = userRepository;
    this.userDetailsMapper = userDetailsMapper;
  }

  @Override
  public UserDetails updatePassword(UserDetails user, String newPassword) 
  {
    //UserData userData = userRepository.findById(user.getUsername());
    UserData userData = userRepository.findById(user.getUsername()).orElse(null);
    userData.setPassword(newPassword);

    return userDetailsMapper.toUserDetails(userData);
  }
}

