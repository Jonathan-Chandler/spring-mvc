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
public class UserLoginDto 
{
  // data transfer object for user login
  @NotEmpty
  @Size(min=8, max=50)
  private String username;

  @NotEmpty
  private String password;
}

