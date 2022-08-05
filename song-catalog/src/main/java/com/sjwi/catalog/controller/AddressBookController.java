/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.controller;

import com.sjwi.catalog.mail.Mailer;
import com.sjwi.catalog.model.api.addressbook.AddressBookEntry;
import com.sjwi.catalog.model.api.addressbook.AddressBookGroup;
import com.sjwi.catalog.model.api.addressbook.NewAddressBookEntry;
import com.sjwi.catalog.model.api.addressbook.NewAddressBookGroup;
import com.sjwi.catalog.service.AddressBookService;
import com.sjwi.catalog.service.OrganizationService;
import com.sjwi.catalog.service.UserService;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/address-book")
public class AddressBookController {

  @Autowired ControllerHelper controllerHelper;

  @Autowired UserService userService;

  @Autowired AddressBookService addressBookService;

  @Autowired OrganizationService organizationService;

  @Autowired Mailer mailer;

  @GetMapping("/entries")
  public ResponseEntity<List<AddressBookEntry>> listAddressBookEntries(
      @RequestParam(name = "searchTerm", required = false) String searchTerm) {
    return ResponseEntity.ok(addressBookService.getAddressBookEntries(searchTerm));
  }

  @GetMapping("/groups")
  public ResponseEntity<List<AddressBookGroup>> listAddressBookGroups(
      @RequestParam(name = "searchTerm", required = false) String searchTerm) {
    return ResponseEntity.ok(addressBookService.getAddressBookGroupEntries(searchTerm));
  }

  @PostMapping("/entries")
  public ResponseEntity<AddressBookEntry> createEntry(
      @RequestBody @Valid NewAddressBookEntry newEntry) {
    AddressBookEntry entry = addressBookService.createEntry(newEntry);
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/entries/{id}")
            .buildAndExpand(entry.getId())
            .toUri();
    return ResponseEntity.created(uri).body(entry);
  }

  @PostMapping("/groups")
  public ResponseEntity<AddressBookGroup> createGroup(
      @RequestBody @Valid NewAddressBookGroup newGroup) {
    AddressBookGroup group = addressBookService.createGroup(newGroup.getName());
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/groups/{id}")
            .buildAndExpand(group.getId())
            .toUri();
    return ResponseEntity.created(uri).body(group);
  }

  @DeleteMapping("/entries/{id}")
  public ResponseEntity<Object> deleteEntry(@PathVariable int id) {
    addressBookService.deleteEntry(id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/groups/{id}")
  public ResponseEntity<Object> deleteGroup(@PathVariable int id) {
    addressBookService.deleteGroup(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/groups/{id}")
  public ResponseEntity<AddressBookGroup> getGroup(@PathVariable int id) {
    return ResponseEntity.ok(addressBookService.getAddressBookGroupById(id));
  }

  @PutMapping("/entries/{id}")
  public ResponseEntity<Object> updateEntry(
      @PathVariable int id, @RequestBody NewAddressBookEntry entry) {
    addressBookService.editEntryById(
        new AddressBookEntry(
            id,
            entry.getFirstName(),
            entry.getLastName(),
            entry.getUsername(),
            entry.getEmail(),
            entry.getPhone()));
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/groups/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<Object> addMemberToGroup(
      Authentication auth,
      @PathVariable Integer id,
      @RequestParam(required = false) boolean removeEntries,
      @RequestBody Map.Entry<String, List<Integer>> body) {
    if (removeEntries) {
      body.getValue().forEach(e -> addressBookService.removeMemberFromGroup(e, id));
    } else {
      List<Integer> existingMembers =
          addressBookService.getAddressBookGroupById(id).getAddressBookEntries().stream()
              .map(e -> e.getId())
              .collect(Collectors.toList());
      body.getValue().stream()
          .filter(e -> !existingMembers.contains(e))
          .forEach(e -> addressBookService.addMemberToGroup(e, id));
    }
    return ResponseEntity.ok().build();
  }
}
