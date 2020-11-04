package com.sjwi.catalog.controller.setlist;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public String populateEmailModal(HttpServletRequest request, HttpServletResponse response,
			@PathVariable int id, Authentication auth) throws IOException {
		try {
			SetList setList = setListService.getSetListById(id);
			request.setAttribute("set", setList);	
			request.setAttribute("addressBook", addressBookService.getAddressBookEntries());
			request.setAttribute("addressBookGroups", addressBookService.getAddressBookGroups());
			request.setAttribute("from", ((CfUser)auth.getPrincipal()).getFirstName());
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
			response.sendRedirect(request.getContextPath() + "/error");
		}
		return "modal/dynamic/email-set";
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
			@PathVariable int id) throws IOException {
		try {
			Map<String, String> attachments = new HashMap<String, String>();
			if (attachPpt != null) {
				FileGenerator pptGenerator = new PptFileGenerator(true,0);
				SetList setList = controllerHelper.buildSetFile(id, pptGenerator, true);
				attachments.put(pptGenerator.getFileName(), setList.getNormalizedSetListName() + ".pptx");
			}
			if (attachPdf != null) {
				FileGenerator pdfGenerator = new PdfFileGenerator(0,controllerHelper.getFullUrL(request));
				SetList setList = controllerHelper.buildSetFile(id, pdfGenerator, false);
				attachments.put(pdfGenerator.getFileName(), setList.getNormalizedSetListName() + ".pdf");
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
			response.sendRedirect(request.getContextPath() + "/error");
		}
	}
}
