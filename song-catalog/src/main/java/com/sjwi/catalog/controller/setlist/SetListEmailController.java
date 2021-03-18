package com.sjwi.catalog.controller.setlist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sjwi.catalog.controller.ControllerHelper;
import com.sjwi.catalog.dao.sql.SqlVersionDao;
import com.sjwi.catalog.file.FileGenerator;
import com.sjwi.catalog.file.pdf.PdfFileGenerator;
import com.sjwi.catalog.file.ppt.PptFileGenerator;
import com.sjwi.catalog.mail.Mailer;
import com.sjwi.catalog.model.SetList;
import com.sjwi.catalog.model.mail.EmailWithAttachment;
import com.sjwi.catalog.model.user.CfUser;
import com.sjwi.catalog.service.AddressBookService;
import com.sjwi.catalog.service.SetListService;

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

@Controller
public class SetListEmailController {
	@Autowired
	ControllerHelper controllerHelper;
	
	@Autowired 
	SetListService setListService;
	
	@Autowired
	SqlVersionDao versionDao;
	
	@Autowired
	AddressBookService addressBookService;
	
	@Autowired
	Mailer mailer;
	
	@RequestMapping(value = {"setlist/email/{id}"}, method = RequestMethod.GET)
	public ModelAndView populateEmailModal(HttpServletRequest request, HttpServletResponse response,
			@PathVariable int id, Authentication auth) {
		try {
			ModelAndView mv = new ModelAndView("modal/dynamic/email-set");
			SetList setList = setListService.getSetListById(id);
			mv.addObject("set", setList);	
			mv.addObject("addressBook", addressBookService.getAddressBookEntries());
			mv.addObject("addressBookGroups", addressBookService.getAddressBookGroups());
			mv.addObject("from", ((CfUser)auth.getPrincipal()).getFirstName());
			return mv;
		} catch (Exception e) {
			return controllerHelper.errorHandler(e);
		}
	}
	@RequestMapping(value = {"setlist/email/{id}"}, method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void sendSetEmail(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="emailTo", required=true) List<String> emailTo,
			@RequestParam(value="emailToCC", required=false) List<String> emailToCc,
			@RequestParam(value="subject", required=true) String subject,
			@RequestParam(value="finalMessageBody", required=true) String finalMessageBody,
			@RequestParam(value="includePpt", required=false) String attachPpt,
			@RequestParam(value="includePdf", required=false) String attachPdf,
			Authentication auth,
			@PathVariable int id) {
		try {
			Map<String, String> attachments = new HashMap<String, String>();
			if (attachPpt != null) {
				FileGenerator pptGenerator = new PptFileGenerator(true,0,false,false);
				SetList setList = controllerHelper.buildSetFile(id, pptGenerator, true);
				attachments.put(pptGenerator.getFilePath(), setList.getNormalizedSetListName() + ".pptx");
			}
			if (attachPdf != null) {
				FileGenerator pdfGenerator = new PdfFileGenerator(0,controllerHelper.getFullUrl());
				SetList setList = controllerHelper.buildSetFile(id, pdfGenerator, false);
				attachments.put(pdfGenerator.getFilePath(), setList.getNormalizedSetListName() + ".pdf");
			}
			String userEmail = ((CfUser) auth.getPrincipal()).getEmail();
			String emailToStr = controllerHelper.recipientsToString(emailTo);
			String emailToCcStr = controllerHelper.recipientsToString(emailToCc);
			if (!emailToStr.contains(userEmail) && !emailToCcStr.contains(userEmail)) {
				emailToStr+= "," + userEmail;
			}
			mailer.sendMail(new EmailWithAttachment()
					.setAttachments(attachments)
					.setTo(emailToStr)
					.setCc(emailToCcStr)
					.setBody(finalMessageBody)
					.setSubject(subject));
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
		}
	}
}
