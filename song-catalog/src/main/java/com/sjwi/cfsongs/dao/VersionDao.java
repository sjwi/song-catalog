package com.sjwi.cfsongs.dao;

import java.util.List;

import com.sjwi.cfsongs.model.song.SetListVersionSong;
import com.sjwi.cfsongs.model.song.Song;
import com.sjwi.cfsongs.model.song.VersionSong;

public interface VersionDao {

	public int createNewVersion(int id, String createdBy, String body, String defaultKey);

	public void changeMaster(int songId, int relatedId);

	public List<VersionSong> getVersionsByRelatedId(int id);
	
	public VersionSong getVersionById(int id);
	
	public SetListVersionSong getSetListVersionSongById(int id);

	public List<Song> getAllRelatedSongs(int id);
}
