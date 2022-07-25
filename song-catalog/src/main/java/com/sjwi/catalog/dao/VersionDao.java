/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.dao;

import com.sjwi.catalog.model.song.SetListVersionSong;
import com.sjwi.catalog.model.song.Song;
import com.sjwi.catalog.model.song.VersionSong;
import java.util.List;

public interface VersionDao {

  public int createNewVersion(int id, String createdBy, String body, String defaultKey);

  public void changeMaster(int songId, int relatedId);

  public List<VersionSong> getVersionsByRelatedId(int id);

  public VersionSong getVersionById(int id);

  public SetListVersionSong getSetListVersionSongById(int id);

  public List<Song> getAllRelatedSongs(int id);
}
