/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.file;

import com.sjwi.catalog.exception.FileUtilityException;
import com.sjwi.catalog.model.SetList;
import com.sjwi.catalog.model.song.Song;

public interface FileGenerator {

  public static final String LICENSE_TEXT = "CCLI Copyright License #890312";

  public String buildFile(Song song) throws FileUtilityException;

  public String buildFile(SetList setList) throws FileUtilityException;

  public void removeFile() throws FileUtilityException;

  public String getFilePath();
}
