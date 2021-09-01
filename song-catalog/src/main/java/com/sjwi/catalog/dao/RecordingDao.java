package com.sjwi.catalog.dao;

import java.io.FileNotFoundException;
import java.util.List;

import com.sjwi.catalog.model.Recording;

import org.springframework.dao.DataAccessException;

public interface RecordingDao {

	public void addOrUpdateRecording(Recording recording) throws DataAccessException, FileNotFoundException;

	public void deleteRecording(int id);

	public Recording getRecordingBySongId(int id);

	public List<Recording> getAllRecordingsWithFileStreams();
}
