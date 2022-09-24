package com.jonathan.web.service;

import java.util.List;

import com.jonathan.web.entities.UserData;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;

public interface UserService
{
  public String login(String username, String password) throws Exception;

  public String register(UserData user);

  public void deleteById(String id);
}

