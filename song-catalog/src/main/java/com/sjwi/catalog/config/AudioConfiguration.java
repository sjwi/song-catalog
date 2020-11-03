package com.sjwi.catalog.config;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sjwi.catalog.service.RecordingService;
import com.sjwi.catalog.task.AudioFileSync;

@Component
public class AudioConfiguration {
	
	@Autowired
	RecordingService recordingService;
	
	@Autowired
	ServletContext context;

	@PostConstruct
	private void synchronizeAudioDirectoryFromDatabase(){
		new AudioFileSync(recordingService.getAllRecordings(),context.getRealPath("/")).start();
	}
}