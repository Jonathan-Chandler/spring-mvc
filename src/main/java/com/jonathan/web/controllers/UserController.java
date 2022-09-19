package com.jonathan.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.jonathan.web.entities.User;
import com.jonathan.web.service.UserService;

@RestController
@CrossOrigin(origins="http://localhost:3000")
public class UserController
{
  private UserService userService;

  @Autowired
  UserController(UserService userService)
  {
    this.userService = userService;
  }

  @GetMapping("/users")
  public List<User> findAll()
  {
    return userService.findAll();
  }

  @GetMapping("/users/{userId}")
  public User getUser(@PathVariable int userId)
  {
    User user = userService.findById(userId);
    if (user == null)
      throw new RuntimeException("User id not found " + userId);

    return user;
  }

  @PostMapping("/users")
  public User addUser(@RequestBody User user)
  {
    user.setId(0);

    userService.save(user);

    return user;
  }

  @PutMapping("/users")
  public User updateUser(@RequestBody User user)
  {
    userService.save(user);

    return user;
  }

  @DeleteMapping("/users/{userId}")
  public String deleteUser(@PathVariable int id)
  {
    User user = userService.findById(id);

    if (user == null)
      throw new RuntimeException("Deleted user id not found " + id);

    userService.deleteById(id);

    return "Deleted user id " + id;
  }
}
