/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model.addressbook;

public class AddressBookEntry {

  private int id;
  private String name;
  private String firstName;
  private String lastName;
  private String username;
  private String email;
  private String phone;

  public AddressBookEntry(
      int id, String firstName, String lastName, String username, String email, String phone) {
    super();
    this.id = id;
    this.name = firstName + " " + lastName;
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.email = email;
    this.phone = phone;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email.toLowerCase();
  }

  public String getPhone() {
    return phone;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  @Override
  public String toString() {
    return "AddressBookEntry [id="
        + id
        + ", name="
        + name
        + ", firstName="
        + firstName
        + ", lastName="
        + lastName
        + ", username="
        + username
        + ", email="
        + email
        + ", phone="
        + phone
        + "]";
  }
}
