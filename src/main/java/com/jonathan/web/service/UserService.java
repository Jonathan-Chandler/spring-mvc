package com.jonathan.web.service;

import java.util.List;

import com.jonathan.web.entities.UserData;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.jonathan.web.resources.UserRegistrationDto;

public interface UserService
{
  public String login(String username, String password) throws Exception;

  public String register(UserRegistrationDto newUserRequest);

  public void deleteById(String id);
}

