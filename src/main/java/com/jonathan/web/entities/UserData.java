package com.jonathan.web.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotEmpty;

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
public class UserData 
{
  @Id
  @NotEmpty
  @Column(unique=true, name="username", length=50)
  @Size(min=8, max=50)
  private String username;

  @NotEmpty
  @Column(unique=true, name="email", length=100)
  @Size(min=10, max=100)
  private String email;

  @NotEmpty
  @Column(name="password")
  private String password;

  @Column(name="active")
  boolean enabled;

  //@Column(name = "authority")
  //private Set<String> roles;

  //@Id 
  //private String username;

  //private String password;


  //@ElementCollection
  //@JoinTable(
  //    name = "authorities",
  //    joinColumns = {@JoinColumn(name = "username")})
  //@Column(name = "authority")
  //private Set<String> roles;
}
