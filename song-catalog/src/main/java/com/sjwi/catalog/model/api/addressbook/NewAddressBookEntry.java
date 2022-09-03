/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model.api.addressbook;

import com.sjwi.catalog.model.api.OwnedResource;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewAddressBookEntry implements OwnedResource {

  @NotBlank() public String firstName;
  @NotBlank() public String lastName;
  @NotBlank public String email;
  public String phone;
  public String username;

  public NewAddressBookEntry(
      String firstName, String lastName, String username, String email, String phone) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.email = email;
    this.phone = phone;
  }

  @Override
  public String getOwner() {
    return username;
  }
}
