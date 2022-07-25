/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.dao;

import com.sjwi.catalog.model.SetList;
import com.sjwi.catalog.model.song.Song;
import java.util.List;

public interface SetListDao {

  static final int SETLIST_PAGE_SIZE = 10;

  public List<SetList> getSetLists();

  public List<SetList> getSetLists(int qty);

  public List<SetList> getSetListPage(int page, int cursor);

  public void addSongToSet(int songId, int setListId, String setDefaultKey, int sort);

  public void addSongsToSet(List<Integer> songIds, int setListId);

  public int createSet(String setListName, String user, int unit, int subUnit);

  public void deleteSet(int id);

  public SetList getSetListById(int id);

  public void removeSongFromSet(int setId, int songId);

  public void setDefaultSetKey(String toKey, int songToTrans, int setListId);

  public int getSetListIdForSetSong(int setSongId);

  public void sortSetList(List<Integer> sortedSongs);

  public List<SetList> searchSetLists(String searchTerm);

  public SetList getLatestSet();

  public List<Song> getSetSongs(int setListId);

  public void changeVersion(int setId, int oldVersion, int newVersion);

  public void changeVersion(int setSongId, int versionId);

  public void renameSet(int id, String setListName);

  public SetList getLatestSetListByOrg(int id);

  public List<SetList> getSetListsByOrg(int id);

  public void flagSetListAsMostRecent(int id);
}
