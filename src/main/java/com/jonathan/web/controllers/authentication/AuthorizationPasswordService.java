//package com.jonathan.web.controllers.authentication;
//
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsPasswordService;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import com.jonathan.web.dao.UserRepository;
////import com.jonathan.web.entities.UserData;
//import com.jonathan.web.entities.User;
//import org.springframework.beans.factory.annotation.Autowired;
//
////import com.jonathan.web.controllers.authentication.UserDetailsMapper;
//
//@Transactional
//@Service
//public class AuthorizationPasswordService implements UserDetailsPasswordService 
//{
//  @Autowired
//  private UserRepository userRepository;
//
//  //private final UserDetailsMapper userDetailsMapper;
//
//  @Override
//  public User updatePassword(User user, String newPassword) 
//  {
//    ////UserData userData = userRepository.findById(user.getUsername());
//    ////UserData userData = userRepository.findById(user.getUsername()).orElse(null);
//    //User user = userRepository.findById(user.getUsername()).orElse(null);
//    if (user == null)
//    {
//      System.out.println("Failed to update password");
//      return null;
//    }
//    user.setPassword(newPassword);
//
//    return user;
//  }
//}
//
