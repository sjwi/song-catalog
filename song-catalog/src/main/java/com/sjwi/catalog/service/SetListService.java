package com.sjwi.catalog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sjwi.catalog.dao.SetListDao;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.model.SetList;
import com.sjwi.catalog.model.song.Song;

@Component
public class SetListService {
	
	@Autowired
	SetListDao setListDao;
	
	@Autowired
	CustomLogger log;
	
	public SetList getSetListById(int id) {
		return setListDao.getSetListById(id);
	}
	
	public SetList getLyricsToSetListById(int id) {
		return setListDao.getSetListById(id).transposeToLyrics();
	}

	public List<SetList> getSetLists() {
		return setListDao.getSetLists();
	}

	public List<SetList> getSetLists(int quantity){
		return setListDao.getSetLists(quantity);
	}

	public SetList getLatestSet(){
		return setListDao.getLatestSet();
	}
	
	public SetList getLatestSetByOrg(int id){
		return setListDao.getLatestSetListByOrg(id);
	}	
	
	public synchronized List<SetList> searchSetLists(String searchTerm){
		return setListDao.searchSetLists(searchTerm == null? null :"%" + searchTerm.toLowerCase() + "%");
	}
	public List<SetList> getSetListPage(int pageSize){
		return setListDao.getSetListPage(pageSize);
	}
	
	public List<Song> getSetSongs(int id){
		return setListDao.getSetSongs(id);
	}
	
	public int createSet(String setListName, String user, int unit, int subUnit) {
		log.logMessageWithEmail("New setlist created: " + setListName + " | created by: " + user);
		return setListDao.createSet(setListName,user, unit, subUnit);
	}
	
	public void deleteSet(int id) {
		setListDao.deleteSet(id);
	}

	public void addSongToSet(int songId, int setListId, String key, int sort) {
		setListDao.addSongToSet(songId, setListId, key, sort);
	}

	public void addSongsToSet(List<Integer> songIds, int setListId) {
		setListDao.addSongsToSet(songIds, setListId);
	}

	public void removeSongFromSet(int setListId, int songId) {
		setListDao.removeSongFromSet(setListId, songId);
	}

	public void changeVersion(int setId, int oldVersion, int newVersion) {
		setListDao.changeVersion(setId,oldVersion,newVersion);
	}

	public void setDefaultSetKey(String newKey, int songId) {
		setListDao.setDefaultSetKey(newKey, songId, setListDao.getSetListIdForSetSong(songId));
	}

	public void changeVersion(int setSongId, int versionId) {
		setListDao.changeVersion(setSongId, versionId);
	}

	public void sortSetList(List<Integer> songIds) {
		setListDao.sortSetList(songIds);
	}

	public void renameSet(int id, String setListName) {
		setListDao.renameSet(id, setListName);
	}

	public List<SetList> getSetListsByOrg(int id) {
		return setListDao.getSetListsByOrg(id);
	}
}
