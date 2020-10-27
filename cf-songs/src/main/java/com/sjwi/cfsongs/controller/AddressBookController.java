package com.sjwi.cfsongs.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.sjwi.cfsongs.mail.Mailer;
import com.sjwi.cfsongs.model.addressbook.AddressBookEntry;
import com.sjwi.cfsongs.model.addressbook.AddressBookGroup;
import com.sjwi.cfsongs.model.mail.Email;
import com.sjwi.cfsongs.model.user.CfUser;
import com.sjwi.cfsongs.service.AddressBookService;
import com.sjwi.cfsongs.service.UserService;

@Controller
public class AddressBookController {

	@Autowired
	ControllerHelper controllerHelper;

	@Autowired
	UserService userService;
	
	@Autowired
	AddressBookService addressBookService;
	
	@Autowired
	Mailer mailer;
	
	@RequestMapping(value = {"/addressbook"}, method = RequestMethod.GET)
	public ModelAndView addressBook(Authentication auth, HttpServletRequest request,
			@RequestParam(name="searchValue",required=false) String searchTerm, 
			@RequestParam(name="searchType",required=false) String searchType, 
			@RequestParam(name="view",required=false) String view) throws IOException {
		ModelAndView mv = new ModelAndView(view==null?"address-book":view);
		List<AddressBookEntry> entries = null;
		List<AddressBookGroup> groups = null;
		if (searchType != null && searchTerm != null) {
			if ("group".equals(searchType)) {
				groups = addressBookService.getAddressBookGroupEntries(searchTerm);
				entries = addressBookService.getAddressBookEntries();
			} else {
				entries = addressBookService.getAddressBookEntries(searchTerm);
				groups = addressBookService.getAddressBookGroups();
			}
		} else {
			entries = addressBookService.getAddressBookEntries();
			groups = addressBookService.getAddressBookGroups();
		}
		mv.addObject("addressbookentries",entries);
		mv.addObject("addressbookgroups",groups);
		return mv;
	}

	@RequestMapping(value= {"/addressbook/create/entry"}, method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void createAddressBookEntry(Authentication auth, 
			@RequestParam(name="firstName", required=true) String firstName,
			@RequestParam(name="lastName", required=true) String lastName,
			@RequestParam(name="phone", required=true) String phone,
			@RequestParam(name="email", required=true) String email,
			HttpServletRequest request) {
		addressBookService.createEntry(new AddressBookEntry(0,firstName,lastName,null,email,phone));
	}
	
	@RequestMapping(value= {"/addressbook/create/group"}, method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void createAddressBookGroup(Authentication auth, 
			@RequestParam(name="groupName", required=true) String groupName,
			HttpServletRequest request) {
		addressBookService.createGroup(groupName);
	}

	@RequestMapping(value= {"/addressbook/{type}/delete/{id}"}, method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteAddressBookItem(Authentication auth, @PathVariable String type, @PathVariable int id) {
		if ("group".equals(type)) {
			addressBookService.deleteGroup(id);
		} else {
			addressBookService.deleteEntry(id);
		}
	}

	@RequestMapping(value= {"/addressbook/group/{id}"}, method = RequestMethod.GET)
	public ModelAndView getAddressBookGroupDetails(Authentication auth, 
			@RequestParam(name="view",required = true) String view,
			@PathVariable int id, HttpServletRequest request) {
		ModelAndView mv =  new ModelAndView(view);
		mv.addObject("group",addressBookService.getAddressBookGroupById(id));
		return mv;
	}

	@RequestMapping(value= {"/addressbook/email/{id}"}, method = RequestMethod.GET)
	public ModelAndView emailAddressBookGroup(Authentication auth, 
			@PathVariable int id, HttpServletRequest request) {
		ModelAndView mv =  new ModelAndView("modal/dynamic/email-group");
		mv.addObject("group",addressBookService.getAddressBookGroupById(id));
		mv.addObject("addressBook",addressBookService.getAddressBookEntries());
		mv.addObject("addressBookGroups",addressBookService.getAddressBookGroups());
		return mv;
	}
	
	@RequestMapping(value = {"addressbook/email"}, method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void sendAddressBookGroupEmail(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="emailTo", required=true) List<String> emailTo,
			@RequestParam(value="emailToCC", required=false) List<String> emailToCc,
			@RequestParam(value="subject", required=true) String subject,
			@RequestParam(value="finalMessageBody", required=true) String finalMessageBody,
			Authentication auth) throws IOException {
		try {
			String userEmail = ((CfUser) auth.getPrincipal()).getEmail();
			if (!emailTo.contains(userEmail) && !emailToCc.contains(userEmail)) {
				emailTo.add(userEmail);
			}
			mailer.sendMail(new Email()
					.setTo(controllerHelper.recipientsToString(emailTo))
					.setCc(controllerHelper.recipientsToString(emailToCc))
					.setBody(finalMessageBody)
					.setSubject(subject));
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
			response.sendRedirect(request.getContextPath() + "/error");
		}
	}

	@RequestMapping(value= {"/addressbook/edit/{id}"}, method = RequestMethod.GET)
	public ModelAndView getAddressBookEntryDetails(Authentication auth, @PathVariable int id, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("modal/dynamic/edit-address-book-entry");
		mv.addObject("entry",addressBookService.getAddressBookEntryById(id));
		return mv;
	}

	@RequestMapping(value= {"/addressbook/edit/{id}"}, method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void getAddressBookEntryDetails(Authentication auth, @PathVariable int id,
			@RequestParam(name="firstName", required=true) String firstName,
			@RequestParam(name="lastName", required=true) String lastName,
			@RequestParam(name="phone", required=true) String phone,
			@RequestParam(name="email", required=true) String email,
			@RequestParam(name="username", required=false) String username,
			HttpServletRequest request) {
		addressBookService.editEntryById(new AddressBookEntry(id,firstName,lastName,username,email,phone));
	}

	@RequestMapping(value= {"/addressbook/add-member-to-group"}, method = RequestMethod.GET)
	public ModelAndView addMemberToGroupModal(Authentication auth,
			@RequestParam(name="entryId",required=true) int entryId
			) {
		ModelAndView mv = new ModelAndView("modal/dynamic/add-ab-entry-to-group");
		mv.addObject("entry", addressBookService.getAddressBookEntryById(entryId));
		mv.addObject("groups", addressBookService.getAddressBookGroups());
		return mv;
	}
	@RequestMapping(value= {"/addressbook/add-member-to-group"}, method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void addMemberToGroup(Authentication auth,
			@RequestParam(name="entryId",required=true) int entryId,
			@RequestParam(name="groupId",required=true) int groupId,
			@RequestParam(name="newGroup",required=false) String newGroup
			) {
		if (groupId == 0 && newGroup != null) {
			groupId = addressBookService.createGroup(newGroup);
		}
		addressBookService.addMemberToGroup(entryId,groupId);
	}
	@RequestMapping(value= {"/addressbook/add-multiple-members"}, method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void addMultipleMemberToGroup(Authentication auth,
			@RequestParam(name="abEntriesToAdd",required=true) int[] entryIds,
			@RequestParam(name="groupId",required=true) int groupId
			) {
		Arrays.stream(entryIds).forEach(e -> {
			addressBookService.addMemberToGroup(e,groupId);
		});
	}
	@RequestMapping(value= {"/addressbook/remove-member-from-group"}, method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void removeMemberFromGroup(Authentication auth,
			@RequestParam(name="entryId",required=true) int entryId,
			@RequestParam(name="groupId",required=true) int groupId
			) {
		addressBookService.removeMemberFromGroup(entryId,groupId);
	}
}