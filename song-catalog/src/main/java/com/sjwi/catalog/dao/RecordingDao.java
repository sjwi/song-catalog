/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.dao;

import com.sjwi.catalog.model.Recording;
import java.io.FileNotFoundException;
import java.util.List;
import org.springframework.dao.DataAccessException;

public interface RecordingDao {

  public void addOrUpdateRecording(Recording recording)
      throws DataAccessException, FileNotFoundException;

  public void deleteRecording(int id);

  public Recording getRecordingBySongId(int id);

  public List<Recording> getAllRecordingsWithFileStreams();
}
