package com.sjwi.catalog.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.sjwi.catalog.dao.SongDao;
import com.sjwi.catalog.model.KeySet;
import com.sjwi.catalog.model.SearchTerm;
import com.sjwi.catalog.model.TransposableString;
import com.sjwi.catalog.model.song.MasterSong;
import com.sjwi.catalog.model.song.Song;
import com.sjwi.catalog.model.user.CfUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class SongService {

	private static final String SEARCH_TERM_DELIMITER = ":";

	@Autowired 
	SongDao songDao;

	public Song getSongById(int id) {
		return songDao.getSongById(id);
	}
	public List<Song> getSongs(){
		return songDao.getSongs();
	}
	
	public List<Song> searchSongs(String searchValue){
		if (searchValue == null || searchValue.trim().isEmpty())
			return songDao.getSongs();
		if (searchValue.contains(SEARCH_TERM_DELIMITER)) {
			String[] searchParts = searchValue.split(SEARCH_TERM_DELIMITER);
			String termKey = searchParts[0];
			String termValue = searchParts[1];
			SearchTerm term = SearchTerm.fromString(termKey);
			if (term != null)
				return songDao.searchSongsWithTerm(term, termValue);
		}
		return songDao.searchSongs("%" + searchValue.toLowerCase() + "%");
	}
	public void setDefaultKey(String updatedVersionKey, int songId, String user) {
		songDao.setDefaultKey(updatedVersionKey, songId, user);
	}
	public int addSong(String songTitle, String songBody, String chordedIn, CfUser user, int category) {
		return songDao.addSong(new MasterSong(null,0,songTitle,
				new TransposableString(songBody,chordedIn),
				chordedIn,null,null,user,user, new Date(),0,false,category,null));
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
	public Map<Song, Integer> getFrequencyCount() {
		return songDao.getFrequencyCount();
	}
	public Map<Song, Integer> getFrequencyCountByOrg(int orgId) {
		return songDao.getFrequencyCountByOrg(orgId);
	}
	public Map<Song, Integer> getServiceFrequencyCountByOrg(int id, List<Integer> services) {
		return services == null? new HashMap<Song, Integer>(): songDao.getServiceFrequencyCountByOrg(id,services);
	}
	public Map<Song, Integer> getServiceFrequencyCount(List<Integer> services) {
		return services == null? new HashMap<Song, Integer>(): songDao.getServiceFrequencyCount(services);
	}
	public static List<AbstractDelta<String>> generateSongRevisionDiff(Song originalSong, Song revisedSong) {
		List<String> originalSongAsList = new ArrayList<String>(Arrays.asList(originalSong.getBody().split("\n")));
		List<String> revisedSongAsList = new ArrayList<String>(Arrays.asList(revisedSong.getBody().split("\n")));
		originalSongAsList.add(0,originalSong.getName());
		revisedSongAsList.add(0,revisedSong.getName());

		return DiffUtils.diff(originalSongAsList, revisedSongAsList).getDeltas();
	}
  public Song getSongByName(String songTitle) {
    return songDao.getSongByName(songTitle);
  }
}
