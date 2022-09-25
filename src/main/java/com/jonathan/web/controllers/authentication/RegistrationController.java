package com.jonathan.web.controllers.authentication;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.jonathan.web.service.UserService;

import com.jonathan.web.dao.UserRepository;
import com.jonathan.web.resources.UserRegistrationDto;


@RestController
public class RegistrationController
{
  //private final UserRepository userRepository;
  //private final PasswordEncoder passwordEncoder;
  @Autowired
  private UserService userService;

  //public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) 
  //{
  //  this.userRepository = userRepository;
  //  this.passwordEncoder = passwordEncoder;
  //}

  @PostMapping("/register")
  @ResponseStatus(code = HttpStatus.CREATED)
  public void register(@RequestBody UserRegistrationDto newUserRequest) 
  {
    userService.register(newUserRequest);
    //userService.register(userLogin.getUsername(), userLogin.getPassword());
    //Instant start = Instant.now();
    //UserData user = UserData.builder()
    //  .username(newUserRequest.getUsername())
    //  .email(newUserRequest.getEmail())
    //  .password(passwordEncoder.encode(newUserRequest.getPassword()))
    //  .enabled(true)
    //  .build();
    //Instant end = Instant.now();

    //System.out.println(String.format(
    //        "Hashing took %s ms",
    //        ChronoUnit.MILLIS.between(start, end)
    //));
    //userRepository.save(user);
  }
}
