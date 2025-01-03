/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.dao.sql;

import com.sjwi.catalog.dao.RecordingDao;
import com.sjwi.catalog.model.Recording;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SqlRecordingDao implements RecordingDao {

  @Autowired private Map<String, String> queryStore;

  @Autowired JdbcTemplate jdbcTemplate;

  @Override
  public void addOrUpdateRecording(Recording recording)
      throws DataAccessException, FileNotFoundException {
    deleteRecording(recording.getId());
    jdbcTemplate.update(
        queryStore.get("addRecording"),
        new Object[] {recording.getSongId(), recording.getPath(), recording.getExtension()});
  }

  @Override
  public void deleteRecording(int id) {
    jdbcTemplate.update(queryStore.get("deleteRecordingById"), new Object[] {id});
  }

  @Override
  public Recording getRecordingBySongId(int id) {
    return jdbcTemplate.query(
        queryStore.get("getRecordingBySongId"),
        new Object[] {id},
        r -> {
          if (r.next()) {
            return new Recording(
                r.getInt("ID"), r.getString("PATH"), r.getString("EXT"), r.getInt("SONG_ID"));
          } else {
            return null;
          }
        });
  }

  @Override
  public List<Recording> getAllRecordingsWithFileStreams() {
    return jdbcTemplate.query(
        queryStore.get("getAllRecordings"),
        r -> {
          List<Recording> recordings = new ArrayList<>();
          while (r.next()) {
            recordings.add(
                new Recording(
                    r.getInt("ID"), r.getString("PATH"), r.getString("EXT"), r.getInt("SONG_ID")));
          }
          return recordings;
        });
  }
}
