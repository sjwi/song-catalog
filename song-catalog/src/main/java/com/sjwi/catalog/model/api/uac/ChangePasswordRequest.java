/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model.api.uac;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ChangePasswordRequest {
  String password;
  String newPassword;
  String confirmPassword;

  @JsonIgnore
  public boolean isValid() {
    return !password.equals(newPassword) && newPassword.equals(confirmPassword);
  }
}
