/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.dao.sql;

import com.sjwi.catalog.dao.AddressBookDao;
import com.sjwi.catalog.model.api.addressbook.AddressBookEntry;
import com.sjwi.catalog.model.api.addressbook.AddressBookGroup;
import com.sjwi.catalog.model.api.addressbook.NewAddressBookEntry;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class SqlAddressBookDao implements AddressBookDao {

  @Autowired private Map<String, String> queryStore;

  @Autowired JdbcTemplate jdbcTemplate;

  @Override
  public List<AddressBookGroup> getAddressBookGroups() {
    return jdbcTemplate.query(
        queryStore.get("getAddressBookGroupEntries"),
        r1 -> {
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
      return jdbcTemplate.query(
          queryStore.get("getAddressBookGroupEntriesByName"),
          r1 -> {
            List<AddressBookGroup> groups = new ArrayList<AddressBookGroup>();
            while (r1.next()) {
              List<AddressBookEntry> groupEntries = new ArrayList<AddressBookEntry>();
              List<Integer> entryIds =
                  jdbcTemplate.query(
                      queryStore.get("getEntryIdFromABRelated"),
                      r2 -> {
                        List<Integer> rids = new ArrayList<Integer>();
                        while (r2.next()) {
                          rids.add(r2.getInt("ENTRY_ID"));
                        }
                        return rids;
                      },
                      r1.getInt("ID"));
              for (Integer entryId : entryIds) {
                groupEntries.add(getAddressBookEntryById(entryId));
              }
              groups.add(new AddressBookGroup(r1.getInt("ID"), r1.getString("NAME"), groupEntries));
            }
            return groups;
          },
          searchTerm);
    }
  }

  @Override
  public List<AddressBookEntry> getAddressBookEntries() {
    return jdbcTemplate.query(
        queryStore.get("getAddressBookEntries"),
        r -> {
          List<AddressBookEntry> addressBookEntries = new ArrayList<AddressBookEntry>();
          while (r.next()) {
            addressBookEntries.add(
                new AddressBookEntry(
                    r.getInt("ID"),
                    r.getString("FirstName"),
                    r.getString("LastName"),
                    r.getString("username"),
                    r.getString("Email"),
                    r.getString("Phone")));
          }
          return addressBookEntries;
        });
  }

  @Override
  public List<AddressBookEntry> getAddressBookEntries(String searchTerm) {
    if (searchTerm == null) {
      return getAddressBookEntries();
    } else {
      return jdbcTemplate.query(
          queryStore.get("searchAddressBookEntries"),
          r -> {
            List<AddressBookEntry> addressBookEntries = new ArrayList<AddressBookEntry>();
            while (r.next()) {
              addressBookEntries.add(
                  new AddressBookEntry(
                      r.getInt("ID"),
                      r.getString("FirstName"),
                      r.getString("LastName"),
                      r.getString("username"),
                      r.getString("Email"),
                      r.getString("Phone")));
            }
            return addressBookEntries;
          },
          searchTerm,
          searchTerm,
          searchTerm,
          searchTerm,
          searchTerm);
    }
  }

  @Override
  public AddressBookEntry getAddressBookEntryById(int id) {
    return jdbcTemplate.query(
        queryStore.get("getAddressBookEntryById"),
        r -> {
          if (r.next()) {
            return new AddressBookEntry(
                r.getInt("ID"),
                r.getString("FirstName"),
                r.getString("LastName"),
                r.getString("username"),
                r.getString("Email"),
                r.getString("Phone"));
          } else {
            return null;
          }
        },
        id);
  }

  @Override
  public AddressBookEntry getAddressBookEntryByEmail(String email) {
    return jdbcTemplate.query(
        queryStore.get("getAddressBookEntryByEmail"),
        r -> {
          if (r.next()) {
            return new AddressBookEntry(
                r.getInt("ID"),
                r.getString("FirstName"),
                r.getString("LastName"),
                r.getString("username"),
                r.getString("Email"),
                r.getString("Phone"));
          } else return null;
        },
        email);
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
    jdbcTemplate.update(
        queryStore.get("updateAddressBookEntryById"),
        new Object[] {
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
    jdbcTemplate.update(queryStore.get("addMemberToGroup"), new Object[] {groupId, entryId});
  }

  @Override
  public AddressBookGroup getAddressBookGroupById(int id) {
    List<AddressBookEntry> groupEntries = new ArrayList<AddressBookEntry>();
    List<Integer> entryIds =
        jdbcTemplate.query(
            queryStore.get("getEntryIdFromABRelated"),
            r -> {
              List<Integer> rids = new ArrayList<Integer>();
              while (r.next()) {
                rids.add(r.getInt("ENTRY_ID"));
              }
              return rids;
            },
            id);
    for (Integer entryId : entryIds) {
      groupEntries.add(getAddressBookEntryById(entryId));
    }
    return jdbcTemplate.query(
        queryStore.get("getAddressBookGroupById"),
        r -> {
          if (r.next()) {
            return new AddressBookGroup(id, r.getString("NAME"), groupEntries);
          } else {
            return null;
          }
        },
        id);
  }

  @Override
  public AddressBookEntry createEntry(NewAddressBookEntry addressBookEntry) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    PreparedStatementCreatorFactory preparedStatementCreatorFactory =
        new PreparedStatementCreatorFactory(
            queryStore.get("createAddressBookEntry"),
            Types.VARCHAR,
            Types.VARCHAR,
            Types.VARCHAR,
            Types.VARCHAR,
            Types.VARCHAR);
    preparedStatementCreatorFactory.setReturnGeneratedKeys(true);
    jdbcTemplate.update(
        preparedStatementCreatorFactory.newPreparedStatementCreator(
            Arrays.asList(
                addressBookEntry.getUsername(),
                addressBookEntry.getFirstName(),
                addressBookEntry.getLastName(),
                addressBookEntry.getEmail(),
                formatPhone(addressBookEntry.getPhone()))),
        keyHolder);
    return getAddressBookEntryById(keyHolder.getKey().intValue());
  }

  @Override
  public void editEntryByEmail(AddressBookEntry addressBookEntry) {
    jdbcTemplate.update(
        queryStore.get("updateAddressBookEntryByEmail"),
        new Object[] {
          addressBookEntry.getUsername(), addressBookEntry.getLastName(),
          addressBookEntry.getFirstName(), addressBookEntry.getEmail()
        });
  }

  @Override
  public AddressBookGroup createGroup(String groupName) {
    jdbcTemplate.update(queryStore.get("createAddressBookGroup"), new Object[] {groupName});
    return jdbcTemplate.query(
        queryStore.get("getLatestAbGroup"),
        r -> {
          r.next();
          return getAddressBookGroupById(r.getInt("ID"));
        });
  }

  @Override
  public void removeMemberFromGroup(int entryId, int groupId) {
    jdbcTemplate.update(queryStore.get("removeMemberFromGroup"), new Object[] {entryId, groupId});
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
      return "("
          + rawInput.substring(0, 3)
          + ") "
          + rawInput.substring(3, 6)
          + "-"
          + rawInput.substring(6);
    } else {
      return rawInput;
    }
  }

  @Override
  public List<AddressBookEntry> getAddressBookEntriesWithPopulatedEmails() {
    return jdbcTemplate.query(
        queryStore.get("getAddressBookEntriesWithPopulatedEmails"),
        r -> {
          List<AddressBookEntry> entries = new ArrayList<>();
          while (r.next())
            entries.add(
                new AddressBookEntry(
                    r.getInt("ID"),
                    r.getString("FirstName"),
                    r.getString("LastName"),
                    r.getString("username"),
                    r.getString("Email"),
                    r.getString("Phone")));
          return entries;
        });
  }

  @Override
  public List<AddressBookEntry> getAddressBookEntriesWithPopulatedPhoneNumbers() {
    return jdbcTemplate.query(
        queryStore.get("getAddressBookEntriesWithPopulatedPhoneNumbers"),
        r -> {
          List<AddressBookEntry> entries = new ArrayList<>();
          while (r.next())
            entries.add(
                new AddressBookEntry(
                    r.getInt("ID"),
                    r.getString("FirstName"),
                    r.getString("LastName"),
                    r.getString("username"),
                    r.getString("Email"),
                    r.getString("Phone")));
          return entries;
        });
  }

  @Override
  public List<AddressBookGroup> getAddressBookGroupsWithPopulatedEmails() {
    return getAddressBookGroups();
  }

  @Override
  public List<AddressBookGroup> getAddressBookGroupsWithPopulatedPhoneNumbers() {
    return jdbcTemplate.query(
        queryStore.get("getAddressBookGroupEntries"),
        r1 -> {
          List<AddressBookGroup> groups = new ArrayList<AddressBookGroup>();
          while (r1.next()) {
            groups.add(getAddressBookGroupWithPhonesById(r1.getInt("ID")));
          }
          return groups;
        });
  }

  private AddressBookGroup getAddressBookGroupWithPhonesById(int id) {
    List<AddressBookEntry> groupEntries = new ArrayList<AddressBookEntry>();
    List<Integer> entryIds =
        jdbcTemplate.query(
            queryStore.get("getEntryIdFromABRelated"),
            new Object[] {id},
            r -> {
              List<Integer> rids = new ArrayList<Integer>();
              while (r.next()) {
                rids.add(r.getInt("ENTRY_ID"));
              }
              return rids;
            });
    for (Integer entryId : entryIds) {
      AddressBookEntry entry = getAddressBookEntryById(entryId);
      if (entry.getEmail() != null && !entry.getEmail().trim().isEmpty()) groupEntries.add(entry);
    }
    return jdbcTemplate.query(
        queryStore.get("getAddressBookGroupById"),
        new Object[] {id},
        r -> {
          if (r.next()) {
            return new AddressBookGroup(id, r.getString("NAME"), groupEntries);
          } else {
            return null;
          }
        });
  }
}
