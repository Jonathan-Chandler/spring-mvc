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
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;


import com.jonathan.web.dao.UserRepository;
import com.jonathan.web.entities.User;
import com.jonathan.web.resources.UserCredentialsDto;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RestController
public class RegistrationController
{
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  //public RegistrationController(UserRepository userRepository) {
  public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  
  @PostMapping("/registration")
  @ResponseStatus(code = HttpStatus.CREATED)
  public void register(@RequestBody UserCredentialsDto userCredentialsDto) {
    //Instant start = Instant.now();  // start timer
    User user = User.builder()
      .id(0)
      .name(userCredentialsDto.getName())
      .email(userCredentialsDto.getEmail())
      .password(passwordEncoder.encode(userCredentialsDto.getPassword()))
      .build();
    //Instant end = Instant.now();    // end timer

    //System.out.println(String.format(
    //        "Hashing took %s ms",
    //        ChronoUnit.MILLIS.between(start, end)
    //));
    userRepository.save(user);
  }
}
