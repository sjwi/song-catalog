package com.sjwi.catalog.task;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import com.sjwi.catalog.config.AudioConfiguration;
import com.sjwi.catalog.model.Recording;

import org.apache.log4j.Logger;

public class AudioFileSync extends Thread {

	private final List<Recording> recordings;
	private final String path;
	private static final Logger log = Logger.getLogger(AudioFileSync.class.getName());

	public AudioFileSync(List<Recording> recordings, String path){
		this.recordings = recordings;
		this.path = path;
	}

	public void run() {
		new File(path + AudioConfiguration.AUDIO_DIRECTORY).mkdir();
		recordings.forEach(r -> {
			try (FileOutputStream output = new FileOutputStream(path + r.getPath())){
				byte[] buffer = new byte[1024];
				while (r.getFileInputStream().read(buffer) > 0) {
					output.write(buffer);
				}
				log.info(r.getPath() + " writtin to application context.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
