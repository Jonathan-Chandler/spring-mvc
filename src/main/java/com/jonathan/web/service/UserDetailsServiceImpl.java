package com.jonathan.web.service;

import com.jonathan.web.entities.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import com.jonathan.web.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
  @Autowired
  UserRepository userRepository;

  private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

  public UserDetails loadUserByUsername(String username)
  {
    UserDetails returnDetails = userRepository.findOneByUsername(username).orElse(null);

    if (returnDetails != null)
    {
      logger.info("Returned UserDetails username: " + returnDetails.getUsername());
      //logger.info("returned UserDetails password: " + returnDetails.getPassword());
    }
    else
    {
      logger.info("Returned null");
    }

    return returnDetails;
  }
}

