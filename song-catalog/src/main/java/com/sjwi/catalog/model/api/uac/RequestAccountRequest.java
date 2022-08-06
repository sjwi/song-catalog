/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model.api.uac;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestAccountRequest {
  @NotBlank String email;
  @NotBlank String justification;
}
