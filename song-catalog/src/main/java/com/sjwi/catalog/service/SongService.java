package com.sjwi.catalog.service;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sjwi.catalog.dao.SongDao;
import com.sjwi.catalog.model.KeySet;
import com.sjwi.catalog.model.TransposableString;
import com.sjwi.catalog.model.song.MasterSong;
import com.sjwi.catalog.model.song.Song;
@Component
public class SongService {

	@Autowired 
	SongDao songDao;
	
	public Song getSongById(int id) {
		return songDao.getSongById(id);
	}
	public List<Song> getSongs(){
		return songDao.getSongs();
	}
	
	public synchronized List<Song> searchSongs(String searchValue){
		return songDao.searchSongs(searchValue == null? null: "%" + searchValue.toLowerCase() + "%");
	}
	public void setDefaultKey(String updatedVersionKey, int songId, String user) {
		songDao.setDefaultKey(updatedVersionKey, songId, user);
	}
	public int addSong(String songTitle, String songBody, String chordedIn, String name, int category) {
		return songDao.addSong(new MasterSong(null,
				0,
				songTitle,
				new TransposableString(songBody,chordedIn),
				0,chordedIn,null,null,name,name, new Date(),0,false,category,null));
	}
	public void deleteSong(int songId) {
		songDao.deleteSong(songId);
	}
	public void updateSong(Song song, String user) {
		songDao.updateSong(song, user);
	}
	public List<KeySet> getKeySets() {
		return songDao.getKeySets();
	}
	public Song buildSongFromResultSet(ResultSet rs) {
		return songDao.buildSongFromResultSet(rs);
	}
	public Map<Integer, String> getSongCategories(){
		return songDao.getSongCategories();
	}
	public Map<Song, Integer> getFrequencyCountByOrg(int orgId) {
		return songDao.getFrequencyCountByOrg(orgId);
	}
}
