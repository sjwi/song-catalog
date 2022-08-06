/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model.api.uac;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {
  @NotBlank String user;
  @NotBlank String password;
  @NotBlank String token;
}
