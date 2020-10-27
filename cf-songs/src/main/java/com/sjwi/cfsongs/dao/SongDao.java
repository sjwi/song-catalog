package com.sjwi.cfsongs.dao;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.sjwi.cfsongs.model.KeySet;
import com.sjwi.cfsongs.model.song.Song;

public interface SongDao {

	public List<Song> getMySongs(String user);
	
	public List<Song> getSongs();
	
	public List<Song> getSongs(int qty);
	
	public int addSong(Song song);

	public Song getSongById(int id);

	public KeySet getKey(String keyId);
	
	public List<KeySet> getKeySets();
	
	public void updateSong(Song theSong, String user);
	
	public void updateSongTitleAndBody(int songId, String name, String body, String user);

	public void deleteSong(int id);
	
	public List<Song> searchSongs(String searchTerm);
	
	public void setDefaultKey(String defaultKey, int id, String user);
	
	public void addRecording(String name, String ext, int id);
	
	public void makeSongPublic(int id);

	public void makeSongPrivate(int id);

	public Song buildSongFromResultSet(ResultSet rs);

	public Map<Integer, String> getSongCategories();
}
