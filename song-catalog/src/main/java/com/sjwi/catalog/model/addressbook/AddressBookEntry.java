/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model.addressbook;

import lombok.Data;

@Data
public class AddressBookEntry extends NewAddressBookEntry {
  public Integer id;

  public AddressBookEntry(
      Integer id, String firstName, String lastName, String username, String email, String phone) {
    super(firstName, lastName, username, email, phone);
    this.id = id;
  }
}
