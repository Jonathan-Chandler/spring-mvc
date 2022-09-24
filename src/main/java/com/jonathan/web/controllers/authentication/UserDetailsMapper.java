package com.jonathan.web.controllers.authentication;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.jonathan.web.resources.UserLoginDto;
import com.jonathan.web.entities.UserData;

// convert UserLoginCredentialsDto to User type
@Component
class UserDetailsMapper 
{
  // convert login credential DTO to UserDetails
  UserDetails toUserDetails(UserLoginDto userLogin) 
  {
    return User.withUsername(userLogin.getUsername())
        .password(userLogin.getPassword())
        .roles("")
        .build();
        //.roles(userLogin.getRoles().toArray(String[]::new))
  }

  // convert local UserData to Spring UserDetails
  UserDetails toUserDetails(UserData userData) 
  {
    return User.withUsername(userData.getUsername())
        .password(userData.getPassword())
        .roles("")
        .build();
        //.roles(userData.getRoles().toArray(String[]::new))
  }
}

