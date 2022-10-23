package com.jonathan.web.service;

import java.util.List;

//import com.jonathan.web.entities.UserData;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.jonathan.web.resources.UserRegistrationDto;
import com.jonathan.web.resources.UserLoginDto;
import com.jonathan.web.resources.OnlineUserDto;

public interface UserService
{
	public String login(UserLoginDto userLogin) throws JOSEException, RuntimeException;

  public String register(UserRegistrationDto newUserRequest);

  public void deleteById(String id);

	public List<OnlineUserDto> getOnlineUsers();
}

