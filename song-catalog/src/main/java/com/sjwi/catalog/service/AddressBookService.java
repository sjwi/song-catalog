package com.sjwi.catalog.service;

import java.util.List;

import com.sjwi.catalog.dao.AddressBookDao;
import com.sjwi.catalog.model.addressbook.AddressBookEntry;
import com.sjwi.catalog.model.addressbook.AddressBookGroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressBookService {
	
	@Autowired
	AddressBookDao addressBookDao;

	public List<AddressBookEntry> getAddressBookEntries() {
		return addressBookDao.getAddressBookEntries();
	}

	public List<AddressBookGroup> getAddressBookGroups() {
		return addressBookDao.getAddressBookGroups();
	}

	public List<AddressBookGroup> getAddressBookGroupEntries(String searchTerm) {
		return addressBookDao.getAddressBookGroups(searchTerm == null? null: "%" + searchTerm.toLowerCase() + "%");
	}

	public List<AddressBookEntry> getAddressBookEntries(String searchTerm) {
		return addressBookDao.getAddressBookEntries(searchTerm == null? null: "%" + searchTerm.toLowerCase() + "%");
	}

	public AddressBookEntry getAddressBookEntryById(int id) {
		return addressBookDao.getAddressBookEntryById(id);
	}

	public AddressBookEntry getAddressBookEntryByEmail(String email) {
		return addressBookDao.getAddressBookEntryByEmail(email);
	}

	public AddressBookGroup getAddressBookGroupById(int id) {
		return addressBookDao.getAddressBookGroupById(id);
	}

	public void createEntry(AddressBookEntry addressBookEntry) {
		addressBookDao.createEntry(addressBookEntry);
	}

	public int createGroup(String groupName) {
		return addressBookDao.createGroup(groupName);
	}

	public void deleteGroup(int id) {
		addressBookDao.deleteGroup(id);
	}

	public void deleteEntry(int id) {
		addressBookDao.deleteEntry(id);
	}

	public void addMemberToGroup(int entryId, int groupId) {
		addressBookDao.addMemberToGroup(entryId, groupId);
	}

	public void removeMemberFromGroup(int entryId, int groupId) {
		addressBookDao.removeMemberFromGroup(entryId, groupId);
	}

	public void editEntryById(AddressBookEntry addressBookEntry) {
		addressBookDao.editEntryById(addressBookEntry);
	}

	public void editEntryByEmail(AddressBookEntry addressBookEntry) {
		addressBookDao.editEntryByEmail(addressBookEntry);
	}

  public List<AddressBookEntry> getAddressBookEntriesWithPopulatedEmails() {
		return addressBookDao.getAddressBookEntriesWithPopulatedEmails();
  }

  public List<AddressBookEntry> getAddressBookEntriesWithPopulatedPhoneNumbers() {
		return addressBookDao.getAddressBookEntriesWithPopulatedPhoneNumbers();
  }
}
