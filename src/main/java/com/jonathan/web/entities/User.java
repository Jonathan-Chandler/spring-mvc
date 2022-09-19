package com.jonathan.web.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="id")
  private int id;

  @Column(name="name")
  private String name;

  @Column(name="email")
  private String email;

  @Column(name="password")
  private String password;

  //@Id 
  //private String username;

  //private String password;

  //boolean enabled;

  //@ElementCollection
  //@JoinTable(
  //    name = "authorities",
  //    joinColumns = {@JoinColumn(name = "username")})
  //@Column(name = "authority")
  //private Set<String> roles;
}

//@Entity
//@Table(name="user")
//public class User
//{
//  @Id
//  @GeneratedValue(strategy=GenerationType.IDENTITY)
//  @Column(name="id")
//  private int id;
//
//  @Column(name="name")
//  private String name;
//
//  @Column(name="email")
//  private String email;
//
//  @Column(name="password")
//  private String password;
//
//  public User()
//  {
//  }
//  
//  public User(String name, String email, String password)
//  {
//    this.name = name;
//    this.email = email;
//    this.password = password;
//  }
//
//  // get/set
//  public int getId()
//  {
//    return this.id;
//  }
//  public void setId(int id)
//  {
//    this.id = id;
//  }
//  public String getName()
//  {
//    return this.name;
//  }
//  public void setName(String name)
//  {
//    this.name = name;
//  }
//  public String getEmail()
//  {
//    return this.email;
//  }
//  public void setEmail(String email)
//  {
//    this.email = email;
//  }
//  public String getPassword()
//  {
//    return this.password;
//  }
//  public void setPassword(String password)
//  {
//    this.password = password;
//  }
//
//  // convert to string
//  @Override
//  public String toString()
//  {
//    return "Employee [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + "]";
//  }
//}


//package com.jonathan.web.entities;
//import java.util.HashSet;
//import java.util.Set;
//import javax.persistence.*;
//import javax.validation.constraints.Email;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Size;
//
//@Entity
//@Table(name = "users",
//  uniqueConstraints = {
//    @UniqueConstraint(columnNames = "name"),
//    @UniqueConstraint(columnNames = "email")
//}
//)
//  
//public class User {
//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  private Long id;
//
//  @NotBlank
//  @Size(max=30)
//  private String username;
//
//  @NotBlank
//  @Size(max=50)
//  private String email;
//
//  @NotBlank
//  @Size(max=50)
//  private String password;
//
//  //@ManyToMany(fetch = FetchType.Lazy)
//  //@JoinTable(name = "user_roles", 
//  //    joinColumns = @JoinColumn(name = "user_id"), 
//  //    inverseJoinColumns = @JoinColumn(name = "role_id"))
//  //private Set<Role> roles = new HashSet<>();
//
//  public User()
//  {
//  }
//
//  public User(String username, String email, String password)
//  {
//    this.username = username;
//    this.email = email;
//    this.password = password;
//  }
//
//  public Long getId()
//  {
//    return id;
//  }
//
//  public void setId(Long id)
//  {
//    this.id = id;
//  }
//
//  public String getUsername()
//  {
//    return this.username;
//  }
//
//  public void setUsername(String username)
//  {
//    this.username = username;
//  }
//
//  public String getEmail()
//  {
//    return this.email;
//  }
//
//  public void setEmail(String email)
//  {
//    this.email = email;
//  }
//
//  public String getPassword()
//  {
//    return this.password;
//  }
//
//  public void setPassword(String password)
//  {
//    this.password = password;
//  }
//
//  //public Set<Role> getRoles()
//  //{
//  //  return roles;
//  //}
//
//  //public Set<Role> setRoles(Set<Role> roles)
//  //{
//  //  this.roles = roles;
//  //}
//}
