package com.sjwi.cfsongs.dao;

import java.util.List;

import com.sjwi.cfsongs.model.SetList;
import com.sjwi.cfsongs.model.song.Song;

public interface SetListDao {

	static final int SETLIST_PAGE_SIZE = 10;
	
	public List<SetList> getSetLists();
	
	public List<SetList> getSetLists(int qty);

	public List<SetList> getSetListPage(int page);

	public void addSongToSet(int songId, int setListId, String setDefaultKey, int sort);

	public void addSongsToSet(List<Integer> songIds, int setListId);
	
	public int createSet(String setListName, String user, int unit);

	public void deleteSet(int id);

	public SetList getSetListById(int id);
	
	public void removeSongFromSet(int setId, int songId);

	public void setDefaultSetKey(String toKey, int songToTrans);

	public void sortSetList(List<Integer> sortedSongs);

	public List<SetList> searchSetLists(String searchTerm);
	
	public SetList getLatestSet();

	public void updateSetTitle(int id, String title);
	
	public List<Song> getSetSongs(int setListId);

	public void changeVersion(int setId, int oldVersion, int newVersion);

	public void changeVersion(int setSongId, int versionId);

	public void renameSet(int id, String setListName);
	
	public SetList getLatestSetListByOrg(int id);
}
