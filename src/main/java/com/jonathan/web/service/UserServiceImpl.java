package com.jonathan.web.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.jonathan.web.service.UserService;
import com.jonathan.web.dao.UserRepository;
import com.jonathan.web.entities.User;

@Service
public class UserServiceImpl implements UserService
{
  private UserRepository userRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository)
  {
    this.userRepository = userRepository;
  }

  @Override
  public List<User> findAll()
  {
    return userRepository.findAll();
  }

  @Override
  public User findById(int id)
  {
    Optional<User> result = userRepository.findById(id);
    User user = null;

    if (result.isPresent())
    {
      user = result.get();
    }
    else
    {
      throw new RuntimeException("Fail to find user with id " + id);
    }

    return user;
  }

  @Override
  public void save(User user)
  {
    userRepository.save(user);
  }

  @Override
  public void deleteById(int id)
  {
    userRepository.deleteById(id);
  }
}


