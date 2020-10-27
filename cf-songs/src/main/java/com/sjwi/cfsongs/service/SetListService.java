package com.sjwi.cfsongs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sjwi.cfsongs.dao.SetListDao;
import com.sjwi.cfsongs.log.CustomLogger;
import com.sjwi.cfsongs.model.SetList;
import com.sjwi.cfsongs.model.song.Song;

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

	public List<SetList> getSetLists(int quantity){
		return setListDao.getSetLists(quantity);
	}

	public SetList getLatestSet(){
		return setListDao.getSetListById(setListDao.getLatestSet());
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
	
	public int createSet(String setListName, String user, int unit) {
		log.logMessageWithEmail("New setlist created: " + setListName + " | created by: " + user);
		return setListDao.createSet(setListName,user, unit);
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
		setListDao.setDefaultSetKey(newKey, songId);
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
}
