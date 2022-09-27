//package com.jonathan.web.controllers.authentication;
//
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import com.jonathan.web.resources.UserLoginDto;
//import com.jonathan.web.entities.UserData;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import java.util.Collection;
//import java.util.List;
//import java.util.ArrayList;
//import java.util.Optional;
//
//// convert UserLoginCredentialsDto to User type
//@Component
//class UserDetailsMapper 
//{
//  // convert login credential DTO to UserDetails
//  UserDetails toUserDetails(UserLoginDto userLogin) 
//  {
//    System.out.println("non-optional UserLoginDto to toUserDetails mapper");
//    GrantedAuthority auth = new SimpleGrantedAuthority("User");
//    List<GrantedAuthority> authList = new ArrayList<>();
//    authList.add(auth);
//
//    return User.withUsername(userLogin.getUsername())
//        .password(userLogin.getPassword())
//        .roles("User")
//        .authorities(authList)
//        .build();
//        //.roles(userLogin.getRoles().toArray(String[]::new))
//  }
//
//  // convert local UserData to Spring UserDetails
//  UserDetails toUserDetails(UserData userData) 
//  {
//    System.out.println("userData to toUserDetails mapper");
//    GrantedAuthority auth = new SimpleGrantedAuthority("User");
//    List<GrantedAuthority> authList = new ArrayList<>();
//    authList.add(auth);
//
//    return User.withUsername(userData.getUsername())
//        .password(userData.getPassword())
//        .roles("User")
//        .authorities(authList)
//        .build();
//        //.roles(userData.getRoles().toArray(String[]::new))
//  }
//
//  // convert local UserData to Spring UserDetails
//  UserLoginDto toUserLoginDto(UserData userData) 
//  {
//    System.out.println("UserData to UserLogin DTO");
//    UserLoginDto loginDto = new UserLoginDto();
//    loginDto.setUsername(userData.getUsername());
//    loginDto.setPassword(userData.getPassword());
//    System.out.println("username: " + loginDto.getUsername() + " password: " + loginDto.getPassword());
//    return loginDto;
//  }
//
//  // convert local UserData to Spring UserDetails
//  UserDetails toUserDetails(Optional<UserLoginDto> loginData) 
//  {
//    System.out.println("optional userlogin DTO to toUserDetails mapper");
//    String returnName = loginData.isPresent() ? loginData.get().getUsername() : "";
//    String returnPassword = loginData.isPresent() ? loginData.get().getPassword() : "";
//    GrantedAuthority auth = new SimpleGrantedAuthority("User");
//    List<GrantedAuthority> authList = new ArrayList<>();
//    authList.add(auth);
//
//    return User.withUsername(returnName)
//      .password(returnPassword)
//      .roles("User")
//      .authorities(authList)
//      .build();
//  }
//}
//
