/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model.addressbook;

import java.util.List;
import lombok.Data;

@Data
public class AddressBookGroup extends NewAddressBookGroup {
  private Integer id;
  private List<AddressBookEntry> addressBookEntries;

  public AddressBookGroup(
      Integer id, String addressBookName, List<AddressBookEntry> addressBookEntires) {
    super(addressBookName);
    this.addressBookEntries = addressBookEntires;
    this.id = id;
  }
}
