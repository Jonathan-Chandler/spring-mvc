//package com.jonathan.web.controllers.authentication;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import com.jonathan.web.service.UserService;
//
//import com.jonathan.web.dao.UserRepository;
//import com.jonathan.web.resources.UserRegistrationDto;
//
//
//@RestController
//public class RegistrationController
//{
//  @Autowired
//  private UserService userService;
//
//  @PostMapping("/register")
//  @ResponseStatus(code = HttpStatus.CREATED)
//  public void register(@RequestBody UserRegistrationDto newUserRequest) 
//  {
//    userService.register(newUserRequest);
//  }
//}
