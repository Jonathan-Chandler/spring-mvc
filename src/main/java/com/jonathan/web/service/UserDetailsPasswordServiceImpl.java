////package com.jonathan.web.service;
////
////import com.jonathan.web.entities.User;
////import org.springframework.security.core.userdetails.UsernameNotFoundException;
////import org.springframework.security.core.userdetails.UserDetailsService;
////import org.springframework.security.core.userdetails.UserDetails;
////import com.jonathan.web.dao.UserRepository;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Service;
////import java.util.Optional;
////import org.slf4j.Logger;
////
//////@Service
//////public class UserDetailsPasswordServiceImpl implements UserDetailsPasswordService
//////{
//////  @Autowired
//////  UserRepository userRepository;
//////
//////  @Autowired
//////  Logger logger;
//////
//////  public UserDetails loadUserByUsername(String username)
//////  {
//////    UserDetails returnDetails = userRepository.findOneByUsername(username).orElse(null);
//////
//////    if (returnDetails != null)
//////    {
//////      logger.info("returned UserDetails username: " + returnDetails.getUsername());
//////      logger.info("returned UserDetails password: " + returnDetails.getPassword());
//////    }
//////    else
//////    {
//////      logger.info("returned null");
//////    }
//////
//////    return returnDetails;
//////  }
//////}
////
////import org.springframework.security.core.userdetails.UserDetails;
////import org.springframework.security.core.userdetails.UserDetailsPasswordService;
////import org.springframework.stereotype.Service;
////import org.springframework.transaction.annotation.Transactional;
////import com.jonathan.web.dao.UserRepository;
//////import com.jonathan.web.entities.UserData;
////import com.jonathan.web.entities.User;
////import org.springframework.beans.factory.annotation.Autowired;
////
//////import com.jonathan.web.controllers.authentication.UserDetailsMapper;
////
////@Transactional
////@Service
////public class UserDetailsPasswordServiceImpl implements UserDetailsPasswordService 
////{
////  @Autowired
////  private UserRepository userRepository;
////
////  @Override
////  public UserDetails updatePassword(UserDetails userDetails, String newPassword) 
////  {
////    ////UserData userData = userRepository.findById(user.getUsername());
////    ////UserData userData = userRepository.findById(user.getUsername()).orElse(null);
////    User user = userRepository.findOneByUsername(userDetails.getUsername()).orElse(null);
////    if (user == null)
////    {
////      System.out.println("Failed to update password");
////      return null;
////    }
////    user.setPassword(newPassword);
////
////    return user;
////  }
////}
////
////
