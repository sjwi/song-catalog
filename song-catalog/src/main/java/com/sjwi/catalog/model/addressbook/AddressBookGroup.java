/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model.addressbook;

import com.google.gson.Gson;
import java.util.List;

public class AddressBookGroup {

  private int id;
  private String name;
  private List<AddressBookEntry> entries;

  public AddressBookGroup(
      int addressBookId, String addressBookName, List<AddressBookEntry> addressBookEntires) {
    super();
    this.id = addressBookId;
    this.name = addressBookName;
    this.entries = addressBookEntires;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public List<AddressBookEntry> getEntries() {
    return entries;
  }

  public String getEntriesAsJson() {
    return new Gson().toJson(entries);
  }
}
