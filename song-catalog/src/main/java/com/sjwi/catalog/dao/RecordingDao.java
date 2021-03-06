package com.sjwi.catalog.dao;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.sjwi.catalog.model.Recording;

public interface RecordingDao {

	public void addOrUpdateRecording(Recording recording) throws DataAccessException, FileNotFoundException;

	public void deleteRecording(int id);

	public Recording getRecordingBySongId(int id);

	public Recording getRecordingWithFileBySongId(int id);

	public List<Recording> getAllRecordingsWithFileStreams();
}
