package com.jonathan.web.resources;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto 
{
  // data transfer object for registering new user
  @NotEmpty
  @Size(min=8, max=50)
  private String username;

  @NotEmpty
  @Size(min=10, max=100)
  private String email;

  @NotEmpty
  private String password;
}


