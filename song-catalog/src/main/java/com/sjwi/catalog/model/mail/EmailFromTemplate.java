package com.sjwi.catalog.model.mail;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.IOUtils;

public class EmailFromTemplate extends Email {
	
	private String msgTemplate;
	private List<String> imagePath;
	private String[] msgArgs;

	@Override
	protected void setMessageContent() throws MessagingException {
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(replaceTemplateArguments(getMsgBodyFromTemplate(msgTemplate)), CONTENT_TYPE);
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		imagePath.stream().forEach(i -> {
			try {
				multipart.addBodyPart(getEmbeddedImageBodyPart(i));
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		});
		message.setContent(multipart);
	}
	
	private String replaceTemplateArguments(String messageTemplate){
		if (msgArgs != null) {
			for (int i = 0; i < msgArgs.length; i++) {
				messageTemplate = messageTemplate.replace("{{ARG_" + i + "}}", msgArgs[i]);
			}
		}
		return messageTemplate;
	}

	private static String getMsgBodyFromTemplate(String templateFileName) throws MessagingException {
		try {
			return IOUtils.toString(Thread.currentThread()
					.getContextClassLoader()
					.getResourceAsStream(templateFileName), StandardCharsets.UTF_8.name());
		} catch (IOException e) {
			throw new MessagingException("Unable to read from email template file",e);
		}
	}

	private BodyPart getEmbeddedImageBodyPart(String resourceName) throws MessagingException {
		try {
			MimeBodyPart imagePart = new MimeBodyPart();
			File file = getFileFromResource(resourceName);
			imagePart.attachFile(file.getAbsolutePath());
			imagePart.setContentID("<" + file.getName() + ">");
			imagePart.setDisposition(MimeBodyPart.INLINE);
			return imagePart;
		} catch (IOException e) {
			throw new MessagingException("Unable to set embedded image in email ", e);
		}
	}
	
	private File getFileFromResource(String resourceName) {
		return new File (Thread.currentThread().getContextClassLoader().getResource(resourceName).getFile());
	}

	public EmailFromTemplate setMsgTemplate(String msgTemplate) {
		this.msgTemplate = msgTemplate;
		return this;
	}

	public EmailFromTemplate setImagePath(List<String> imagePath) {
		this.imagePath = imagePath;
		return this;
	}

	public EmailFromTemplate setMsgArgs(String[] msgArgs) {
		this.msgArgs = msgArgs;
		return this;
	}
}
