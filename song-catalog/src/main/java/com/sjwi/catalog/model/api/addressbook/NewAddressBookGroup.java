/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model.api.addressbook;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewAddressBookGroup {
  @NotBlank public String name;
}
