/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.dao;

import com.sjwi.catalog.model.addressbook.AddressBookEntry;
import com.sjwi.catalog.model.addressbook.AddressBookGroup;
import java.util.List;

public interface AddressBookDao {

  public List<AddressBookGroup> getAddressBookGroups();

  public List<AddressBookGroup> getAddressBookGroups(String searchTerm);

  public List<AddressBookEntry> getAddressBookEntries();

  public List<AddressBookEntry> getAddressBookEntries(String searchTerm);

  public AddressBookEntry getAddressBookEntryById(int id);

  public void deleteGroup(int id);

  public void deleteEntry(int id);

  public void editEntryById(AddressBookEntry addressBookEntry);

  public void addMemberToGroup(int entryId, int groupId);

  public AddressBookGroup getAddressBookGroupById(int id);

  public void createEntry(AddressBookEntry addressBookEntry);

  public void editEntryByEmail(AddressBookEntry addressBookEntry);

  public int createGroup(String groupName);

  public void removeMemberFromGroup(int entryId, int groupId);

  public AddressBookEntry getAddressBookEntryByEmail(String email);

  public List<AddressBookEntry> getAddressBookEntriesWithPopulatedEmails();

  public List<AddressBookEntry> getAddressBookEntriesWithPopulatedPhoneNumbers();

  public List<AddressBookGroup> getAddressBookGroupsWithPopulatedEmails();

  public List<AddressBookGroup> getAddressBookGroupsWithPopulatedPhoneNumbers();
}
