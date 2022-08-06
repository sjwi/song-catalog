/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model.api.uac;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EnrollmentRequest {
  @NotBlank String username;
  @NotBlank String password;
  @NotBlank String email;
  @NotBlank String firstName;
  @NotBlank String lastName;
  @NotBlank String user;
  @NotBlank String token;
}
