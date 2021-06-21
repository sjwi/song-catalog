package com.sjwi.catalog.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.Part;

import com.sjwi.catalog.config.AudioConfiguration;
import com.sjwi.catalog.dao.RecordingDao;
import com.sjwi.catalog.model.Recording;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RecordingService {
	
	@Autowired
	RecordingDao recordingDao;
	
	@Autowired
	ServletContext context;

	public void addOrUpdateRecording(int id, Part songAudioPart) throws IOException {
		if(songAudioPart.getInputStream().available() != 0)
			recordingDao.addOrUpdateRecording(writeAudioFileToSystem(id, songAudioPart));
	}

	public void deleteRecording(int id) {
		recordingDao.deleteRecording(id);
	}

	public Recording getRecordingBySongId(int id) {
		return recordingDao.getRecordingBySongId(id);
	}
	public Recording getRecordingWithFileBySongId(int id) {
		return recordingDao.getRecordingWithFileBySongId(id);
	}
	public List<Recording> getAllRecordings(){
		return recordingDao.getAllRecordingsWithFileStreams();
	}

	private Recording writeAudioFileToSystem(int id, Part filePart) throws IOException {
		
		String root = context.getRealPath("/") + AudioConfiguration.AUDIO_DIRECTORY + "/";
		String extension = getPartExtension(filePart);
	    String fileName = "song_" + id + "_" + System.currentTimeMillis() + "." + extension;
	    
	    OutputStream out = null;
	    InputStream filecontent = null;
		try {
	        new File(root).mkdir();
	        out = new FileOutputStream(new File(root + fileName));
	        filecontent = filePart.getInputStream();
	        if(filecontent.available()==0) {
	        	throw new IOException();
	        }
	
	        int read = 0;
	        final byte[] bytes = new byte[1024];
	
	        while ((read = filecontent.read(bytes)) != -1) {
	            out.write(bytes, 0, read);
	        }
			return new Recording(id, "/audio/" + fileName, extension, new FileInputStream(new File(root + fileName)));
	    } finally {
	        if (out != null) {
	            out.close();
	        }
	        if (filecontent != null) {
	            filecontent.close();
	        }
	    }
	}

	private String getPartExtension(Part part) {
		String extension = null;
		String fileName = null;
	    final String partHeader = part.getHeader("content-disposition");
	    for (String content : partHeader.split(";")) {
	        if (content.trim().startsWith("filename")) {
	        	fileName = content.substring(content.lastIndexOf("=")+1).replace("\"","");
	        }
	    }
		int i = fileName.lastIndexOf('.');
		if (i > 0) {
		    extension = fileName.substring(i+1);
		}
		return extension;
	}
}
