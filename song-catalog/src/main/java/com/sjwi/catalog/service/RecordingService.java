/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.service;

import com.sjwi.catalog.dao.RecordingDao;
import com.sjwi.catalog.model.Recording;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RecordingService {

  @Autowired RecordingDao recordingDao;

  @Autowired ServletContext context;

  @Value("${com.sjwi.settings.audioDirectory}")
  String root;

  public void addOrUpdateRecording(int id, Part songAudioPart) throws IOException {
    if (songAudioPart.getInputStream().available() != 0)
      recordingDao.addOrUpdateRecording(writeAudioFileToSystem(id, songAudioPart));
  }

  public void deleteRecording(int id) {
    recordingDao.deleteRecording(id);
  }

  public Recording getRecordingBySongId(int id) {
    return recordingDao.getRecordingBySongId(id);
  }

  public List<Recording> getAllRecordings() {
    return recordingDao.getAllRecordingsWithFileStreams();
  }

  private Recording writeAudioFileToSystem(int id, Part filePart) throws IOException {
    String extension = getPartExtension(filePart);
    String fileName = "song_" + id + "_" + System.currentTimeMillis() + "." + extension;
    Files.copy(filePart.getInputStream(), Paths.get(root).resolve(fileName));
    return new Recording(id, fileName, extension);
  }

  private String getPartExtension(Part part) {
    String extension = null;
    String fileName = null;
    final String partHeader = part.getHeader("content-disposition");
    for (String content : partHeader.split(";")) {
      if (content.trim().startsWith("filename")) {
        fileName = content.substring(content.lastIndexOf("=") + 1).replace("\"", "");
      }
    }
    int i = fileName.lastIndexOf('.');
    if (i > 0) {
      extension = fileName.substring(i + 1);
    }
    return extension;
  }
}
