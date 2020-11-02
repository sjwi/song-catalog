package com.sjwi.cfsongs.file;

import com.sjwi.cfsongs.exception.FileUtilityException;
import com.sjwi.cfsongs.model.SetList;
import com.sjwi.cfsongs.model.song.Song;

public interface FileGenerator {

	public static final String LICENSE_TEXT = "CCLI Copyright License #890312";

	public String buildFile(Song song) throws FileUtilityException;
	public String buildFile(SetList setList) throws FileUtilityException;
	public void removeFile() throws FileUtilityException;
	public String getFileName();
	public String getContextFilePath();
}
