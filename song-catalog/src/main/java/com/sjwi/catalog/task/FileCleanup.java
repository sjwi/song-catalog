package com.sjwi.catalog.task;

import static com.sjwi.catalog.file.pdf.PdfFileGenerator.PDF_SUB_DIRECTORY;
import static com.sjwi.catalog.file.ppt.PptFileGenerator.PPT_SUB_DIRECTORY;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import com.sjwi.catalog.exception.MailException;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.service.RecordingService;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
		logger.info("Directories cleaned\n" + pptDirectory + "\n" + pdfDirectory);
	}
}
