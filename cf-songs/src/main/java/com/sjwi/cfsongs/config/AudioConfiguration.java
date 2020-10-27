package com.sjwi.cfsongs.config;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sjwi.cfsongs.service.RecordingService;
import com.sjwi.cfsongs.task.AudioFileSync;

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