package com.sjwi.catalog.task;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import com.sjwi.catalog.model.Recording;

public class AudioFileSync extends Thread {

	private final List<Recording> recordings;
	private final String path;

	public AudioFileSync(List<Recording> recordings, String path){
		this.recordings = recordings;
		this.path = path;
	}

	public void run() {
		File baseDirectory = new File(path + "/audio");
		if (!baseDirectory.exists())
			baseDirectory.mkdir();
		recordings.forEach(r -> {
			try (FileOutputStream output = new FileOutputStream(path + r.getPath())){
				byte[] buffer = new byte[1024];
				while (r.getFileInputStream().read(buffer) > 0) {
					output.write(buffer);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
