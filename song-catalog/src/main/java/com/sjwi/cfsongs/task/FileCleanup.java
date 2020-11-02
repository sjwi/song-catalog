package com.sjwi.cfsongs.task;

import static com.sjwi.cfsongs.file.pdf.PdfFileGenerator.PDF_SUB_DIRECTORY;
import static com.sjwi.cfsongs.file.ppt.PptFileGenerator.PPT_SUB_DIRECTORY;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sjwi.cfsongs.exception.MailException;
import com.sjwi.cfsongs.log.CustomLogger;
import com.sjwi.cfsongs.service.RecordingService;

@Component
public class FileCleanup {
	
	@Autowired
	ServletContext servletContext;
	
	@Autowired
	CustomLogger logger;
	
	@Autowired
	RecordingService recordingService;

	@Scheduled(cron = "0 0 0 * * *")
	public void fileCleanup() throws MailException, IOException {
		String pptDirectory = servletContext.getRealPath("/") + PPT_SUB_DIRECTORY;
		String pdfDirectory = servletContext.getRealPath("/") + PDF_SUB_DIRECTORY;
		logger.info("Removing ppt files");
		FileUtils.cleanDirectory(new File(pptDirectory));
		logger.info("Removing pdf files");
		FileUtils.cleanDirectory(new File(pdfDirectory));
		logger.logMessageWithEmail("Directories cleaned\n" + pptDirectory + "\n" + pdfDirectory);
	}
}
