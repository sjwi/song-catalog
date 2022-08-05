/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model.mail;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class EmailRequestBody {
  private List<String> emailTo;
  private List<String> emailToCc;
  @NotBlank private String subject;
  @NotBlank private String finalMessage;
}
