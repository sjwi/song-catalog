package com.sjwi.catalog.model.addressbook;

import java.util.List;

import com.google.gson.Gson;

public class AddressBookGroup {

	private int id;
	private String name;
	private List<AddressBookEntry> entries;

	public AddressBookGroup(int addressBookId, String addressBookName, List<AddressBookEntry> addressBookEntires) {
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
