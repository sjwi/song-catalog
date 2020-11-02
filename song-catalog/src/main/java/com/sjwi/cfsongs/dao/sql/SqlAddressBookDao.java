package com.sjwi.cfsongs.dao.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sjwi.cfsongs.dao.AddressBookDao;
import com.sjwi.cfsongs.model.addressbook.AddressBookEntry;
import com.sjwi.cfsongs.model.addressbook.AddressBookGroup;

@Repository
public class SqlAddressBookDao implements AddressBookDao {

	@Autowired
	private Map<String,String> queryStore;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public List<AddressBookGroup> getAddressBookGroups() {
		return jdbcTemplate.query(queryStore.get("getAddressBookGroupEntries"), r1 -> {
			List<AddressBookGroup> groups = new ArrayList<AddressBookGroup>();
			while (r1.next()) {
				groups.add(getAddressBookGroupById(r1.getInt("ID")));
			}
			return groups;
		});
	}

	@Override
	public List<AddressBookGroup> getAddressBookGroups(String searchTerm) {
		if (searchTerm == null) {
			return getAddressBookGroups(); 
		} else {
			return jdbcTemplate.query(queryStore.get("getAddressBookGroupEntriesByName"), new Object[] {searchTerm}, r1 -> {
				List<AddressBookGroup> groups = new ArrayList<AddressBookGroup>();
				while (r1.next()) {
					List<AddressBookEntry> groupEntries = new ArrayList<AddressBookEntry>();
					List<Integer> entryIds = jdbcTemplate.query(queryStore.get("getEntryIdFromABRelated"), new Object[] {r1.getInt("ID")}, r2 -> {
						List<Integer> rids = new ArrayList<Integer>();
						while (r2.next()) {
							rids.add(r2.getInt("ENTRY_ID"));
						}
						return rids;
					});
					for (Integer entryId: entryIds) {
						groupEntries.add(getAddressBookEntryById(entryId));
					}
					groups.add(new AddressBookGroup(r1.getInt("ID"),r1.getString("NAME"),groupEntries));
				}
				return groups;
			});
		}
	}
	
	@Override
	public List<AddressBookEntry> getAddressBookEntries() {
		return jdbcTemplate.query(queryStore.get("getAddressBookEntries"), r -> {
			List<AddressBookEntry> addressBookEntries = new ArrayList<AddressBookEntry>();
			while (r.next()) {
				addressBookEntries.add(new AddressBookEntry(r.getInt("ID"),r.getString("FirstName") , r.getString("LastName"),r.getString("username"),r.getString("Email"),r.getString("Phone")));
			}
			return addressBookEntries;
		});
	}
	
	@Override
	public List<AddressBookEntry> getAddressBookEntries(String searchTerm) {
		if (searchTerm == null) {
			return getAddressBookEntries();
		} else {
			return jdbcTemplate.query(queryStore.get("searchAddressBookEntries"), new Object[] {
					searchTerm,searchTerm,searchTerm,searchTerm,searchTerm},
					r -> {
				List<AddressBookEntry> addressBookEntries = new ArrayList<AddressBookEntry>();
				while (r.next()) {
					addressBookEntries.add(new AddressBookEntry(r.getInt("ID"),r.getString("FirstName") , r.getString("LastName"),r.getString("username"),r.getString("Email"),r.getString("Phone")));
				}
				return addressBookEntries;
			});
		}
	}

	@Override
	public AddressBookEntry getAddressBookEntryById(int id) {
		return jdbcTemplate.query(queryStore.get("getAddressBookEntryById"), new Object[] {id}, r -> {
			if(r.next()) {
				return new AddressBookEntry(r.getInt("ID"),r.getString("FirstName") , r.getString("LastName"),r.getString("username"),r.getString("Email"),r.getString("Phone"));
			} else {
				return null;
			}
		});
	}

	@Override
	public AddressBookEntry getAddressBookEntryByEmail(String email) {
		return jdbcTemplate.query(queryStore.get("getAddressBookEntryByEmail"),
				new Object[] {email}, r -> {
					if (r.next()) {
						return new AddressBookEntry(r.getInt("ID"),r.getString("FirstName") , r.getString("LastName"),r.getString("username"),r.getString("Email"),r.getString("Phone"));
					} else return null;
				});
	}

	@Override
	public void deleteGroup(int id) {
		jdbcTemplate.update(queryStore.get("deleteAddressBookGroupRelated"), new Object[] {id});
		jdbcTemplate.update(queryStore.get("deleteAddressBookGroup"), new Object[] {id});
	}
	
	@Override
	public void deleteEntry(int id) {
		jdbcTemplate.update(queryStore.get("deleteAddressBookEntryRelated"), new Object[] {id});
		jdbcTemplate.update(queryStore.get("deleteAddressBookEntry"), new Object[] {id});
	}

	@Override
	public void editEntryById(AddressBookEntry addressBookEntry) {
		jdbcTemplate.update(queryStore.get("updateAddressBookEntryById"), new Object[] {
				addressBookEntry.getUsername(),
				addressBookEntry.getLastName(),
				addressBookEntry.getFirstName(),
				addressBookEntry.getEmail(),
				formatPhone(addressBookEntry.getPhone()),
				addressBookEntry.getId()
		});
	}

	@Override
	public void addMemberToGroup(int entryId, int groupId) {
		jdbcTemplate.update(queryStore.get("addMemberToGroup"),new Object[] {
				groupId,entryId
		});
	}

	@Override
	public AddressBookGroup getAddressBookGroupById(int id) {
		List<AddressBookEntry> groupEntries = new ArrayList<AddressBookEntry>();
		List<Integer> entryIds = jdbcTemplate.query(queryStore.get("getEntryIdFromABRelated"), new Object[] {id}, r -> {
			List<Integer> rids = new ArrayList<Integer>();
			while (r.next()) {
				rids.add(r.getInt("ENTRY_ID"));
			}
			return rids;
		});
		for (Integer entryId: entryIds) {
			groupEntries.add(getAddressBookEntryById(entryId));
		}
		return jdbcTemplate.query(queryStore.get("getAddressBookGroupById"), new Object[] {id}, r ->{
			if (r.next()) {
				return new AddressBookGroup(id,r.getString("NAME"),groupEntries);
			} else {
				return null;
			}
		});
	}

	@Override
	public void createEntry(AddressBookEntry addressBookEntry) {
		jdbcTemplate.update(queryStore.get("createAddressBookEntry"),new Object[] {
				addressBookEntry.getUsername(),
				addressBookEntry.getLastName(),
				addressBookEntry.getFirstName(),
				addressBookEntry.getEmail(),
				formatPhone(addressBookEntry.getPhone()),
		});
	}
	
	@Override
	public void editEntryByEmail(AddressBookEntry addressBookEntry) {
		jdbcTemplate.update(queryStore.get("updateAddressBookEntryByEmail"), new Object[] {
				addressBookEntry.getUsername(),addressBookEntry.getLastName(),
				addressBookEntry.getFirstName(),addressBookEntry.getEmail()
		});
	}
	
	@Override
	public int createGroup(String groupName) {
		jdbcTemplate.update(queryStore.get("createAddressBookGroup"),new Object[] {groupName});
		return jdbcTemplate.query(queryStore.get("getLatestAbGroup"), r -> {
			r.next();
			return r.getInt("ID");
		});
	}

	@Override
	public void removeMemberFromGroup(int entryId, int groupId) {
		jdbcTemplate.update(queryStore.get("removeMemberFromGroup"),new Object[] {
				entryId,groupId
		});
	}

	private String formatPhone(String rawInput) {
		if (rawInput == null) return null;
		rawInput = rawInput.replace(" ", "");
		if (rawInput.length() == 11 && rawInput.charAt(0) == '1') {
			rawInput = rawInput.substring(1);
		} else if (rawInput.length() == 7) {
			rawInput = "859" + rawInput;
		}
		if (rawInput.length() == 10) {
			return "(" + rawInput.substring(0,3) + ") " + 
					rawInput.substring(3,6) + "-" + rawInput.substring(6);
		} else {
			return rawInput;
		}
	}
}
