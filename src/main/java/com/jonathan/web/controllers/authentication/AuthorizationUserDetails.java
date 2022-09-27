//package com.jonathan.web.controllers.authentication;
//
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.jonathan.web.entities.User;
////import com.jonathan.web.entities.UserData;
//import com.jonathan.web.dao.UserRepository;
////import com.jonathan.web.controllers.authentication.UserDetailsMapper;
//import com.jonathan.web.resources.UserLoginDto;
//import java.util.Optional;
//import org.springframework.beans.factory.annotation.Autowired;
//
//@Service
//@Transactional
//public class AuthorizationUserDetails implements UserDetailsService 
//{
//  @Autowired
//  private UserRepository userRepository;
//
//  //@Autowired
//  //private UserDetailsMapper userDetailsMapper;
//
//  @Override
//  public UserDetails loadUserByUsername(String username)
//          throws UsernameNotFoundException 
//  {
//    //UserLoginCredentialsDto userCredentials = userRepository.findById(username).orElse(null);
//    try
//    {
//      User user = userRepository.findOneByUsername(username).orElse(null);
//      if (user == null)
//      {
//        throw new UsernameNotFoundException("User not found with username " + username);
//      }
//      //System.out.println("looked up user: " + returnName + " with password: " + returnPassword);
//
//      System.out.println("AuthorizationUserDetails found user details for user: " + username);
//
//      //return userDetailsMapper.toUserDetails(user);
//      return user.toUserDetails(user);
//    }
//    catch (Exception e)
//    {
//      System.out.println("Dao throws error");
//      throw new UsernameNotFoundException("Dao exception thrown with username " + username);
//    }
//  }
//}
//
////  @Override
////  public UserDetails updatePassword(UserDetails user, String newPassword) {
////    UserCredentials userCredentials = userRepository.findByUsername(user.getUsername());
////    userCredentials.setPassword(newPassword);
////    return userDetailsMapper.toUserDetails(userCredentials);
////  }
//
//    //Optional<UserLoginDto> loginData = userRepository.findOneByUsername(username);
//
//    //String returnName = loginData.isPresent() ? loginData.get().getUsername() : "";
//    //String returnPassword = loginData.isPresent() ? loginData.get().getPassword() : "";
//    //if (returnName == "")
//    //{
//    //  System.out.println("could not find user: " + username);
//    //  throw new UsernameNotFoundException("User not found with username " + username);
//    //}
//
