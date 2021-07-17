package com.sjwi.catalog.config;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import com.sjwi.catalog.service.RecordingService;
import com.sjwi.catalog.task.AudioFileSync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AudioConfiguration {

	public static final String AUDIO_DIRECTORY = "/audio";
	
	@Autowired
	RecordingService recordingService;
	
	@Autowired
	ServletContext context;

	@Scheduled(cron = "*/5 * * * *")
	@PostConstruct
	private void synchronizeAudioDirectoryFromDatabase(){
		new AudioFileSync(recordingService.getAllRecordings(),context.getRealPath("/")).start();
	}
}