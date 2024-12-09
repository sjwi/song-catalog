/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sjwi.catalog.dao.RecordingDao;
import com.sjwi.catalog.model.Recording;
  

@Component
public class RecordingService {

  private static ConcurrentHashMap<Integer, Recording> recordingCache = new ConcurrentHashMap<>();

  @Autowired RecordingDao recordingDao;

  @Autowired ServletContext context;

  @Value("${com.sjwi.settings.audioDirectory}")
  String root;

  public void addOrUpdateRecording(int id, Part songAudioPart) throws IOException {
    if (songAudioPart.getInputStream().available() != 0)
      recordingDao.addOrUpdateRecording(writeAudioFileToSystem(id, songAudioPart));
    refreshRecordingCache();
  }

  public void deleteRecording(int id) {
    recordingDao.deleteRecording(id);
    refreshRecordingCache();
  }

  public Recording getRecordingBySongId(int id) {
    if (recordingCache.isEmpty())
      recordingCache.putAll(recordingDao.getAllRecordingsWithFileStreams()
      .stream().collect(Collectors.toMap(Recording::getSongId, recording -> recording)));
    return recordingCache.get(id);
  }

  public Map<Integer, Recording> getRecordingsBySongIds() {
    return getAllRecordings().stream()
        .collect(Collectors.toMap(Recording::getSongId, recording -> recording));
  }

  public List<Recording> getAllRecordings() {
    if (recordingCache.isEmpty())
      recordingCache.putAll(recordingDao.getAllRecordingsWithFileStreams()
      .stream().collect(Collectors.toMap(Recording::getSongId, recording -> recording)));
    return new ArrayList<>(recordingCache.values());
  }

  private Recording writeAudioFileToSystem(int id, Part filePart) throws IOException {
    String extension = getPartExtension(filePart);
    String fileName = "song_" + id + "_" + System.currentTimeMillis() + "." + extension;
    Files.copy(filePart.getInputStream(), Paths.get(root).resolve(fileName));
    return new Recording(0, fileName, extension, id);
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

  public void refreshRecordingCache() {
    recordingCache.clear();
    Thread clearCache =
        new Thread(
            new Runnable() {
              @Override
              public void run() {
                recordingCache.putAll(getRecordingsBySongIds());
              }
            });
    clearCache.start();
  }
}
