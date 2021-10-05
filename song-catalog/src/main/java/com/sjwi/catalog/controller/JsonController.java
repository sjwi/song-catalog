package com.sjwi.catalog.controller;

import java.util.ArrayList;
import java.util.List;

import com.sjwi.catalog.aspect.IgnoreAspect;
import com.sjwi.catalog.model.addressbook.AddressBookEntry;
import com.sjwi.catalog.model.addressbook.AddressBookGroup;
import com.sjwi.catalog.service.AddressBookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@IgnoreAspect
public class JsonController {
  @Autowired
  AddressBookService addressBookService;
  @Autowired
  ControllerHelper controllerHelper;

	@RequestMapping(value= {"/addressbook/emails.json"}, method = RequestMethod.GET)
	@ResponseBody
	public List<AddressBookEntry> emailsJson() {
		try {
			return addressBookService.getAddressBookEntriesWithPopulatedEmails();
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
			return new ArrayList<AddressBookEntry>();
		}
	}
	@RequestMapping(value= {"/addressbook/group-emails.json"}, method = RequestMethod.GET)
	@ResponseBody
	public List<AddressBookGroup> groupEmailsJson() {
		try {
			return addressBookService.getAddressBookGroupsWithPopulatedEmails();
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
			return new ArrayList<AddressBookGroup>();
		}
	}
	@RequestMapping(value= {"/addressbook/phones.json"}, method = RequestMethod.GET)
	@ResponseBody
	public List<AddressBookEntry> phonesJson() {
		try {
			return addressBookService.getAddressBookEntriesWithPopulatedPhoneNumbers();
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
			return new ArrayList<AddressBookEntry>();
		}
	}
	@RequestMapping(value= {"/addressbook/group-phones.json"}, method = RequestMethod.GET)
	@ResponseBody
	public List<AddressBookGroup> groupPhonesJson() {
		try {
			return addressBookService.getAddressBookGroupsWithPopulatedPhoneNumbers();
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
			return new ArrayList<AddressBookGroup>();
		}
	}
}
