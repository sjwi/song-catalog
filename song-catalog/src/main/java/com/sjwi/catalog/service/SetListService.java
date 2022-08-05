/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.service;

import com.sjwi.catalog.config.ServletConstants;
import com.sjwi.catalog.controller.ControllerHelper;
import com.sjwi.catalog.dao.SetListDao;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.model.SetList;
import com.sjwi.catalog.model.api.setlist.NewSetList;
import com.sjwi.catalog.model.song.Song;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetListService {

  @Autowired SetListDao setListDao;

  @Autowired CustomLogger logger;

  @Autowired ControllerHelper controllerHelper;

  public SetList getSetListById(int id) {
    return setListDao.getSetListById(id);
  }

  public SetList getLyricsToSetListById(int id) {
    return setListDao.getSetListById(id).transposeToLyrics();
  }

  public List<SetList> getSetLists() {
    return setListDao.getSetLists();
  }

  public List<SetList> getSetLists(int quantity) {
    return setListDao.getSetLists(quantity);
  }

  public SetList getLatestSet() {
    return setListDao.getLatestSet();
  }

  public SetList getLatestSetByOrg(int id) {
    return setListDao.getLatestSetListByOrg(id);
  }

  public List<SetList> searchSetLists(String searchTerm) {
    return setListDao.searchSetLists(
        searchTerm == null ? null : "%" + searchTerm.toLowerCase() + "%");
  }

  public List<SetList> getSetListPage(int pageSize, int cursor) {
    return setListDao.getSetListPage(pageSize, cursor);
  }

  public List<Song> getSetSongs(int id) {
    return setListDao.getSetSongs(id);
  }

  public SetList createSet(String setListName, NewSetList setList, String user) {
    int setListId =
        setListDao.createSet(setListName, user, setList.getUnit(), setList.getSubUnit());
    logger.logUserActionWithEmail(
        "New set list created: "
            + setListName
            + "\n"
            + ServletConstants.FULL_URL
            + "/setlist/"
            + setListId);
    return setListDao.getSetListById(setListId);
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
    setListDao.changeVersion(setId, oldVersion, newVersion);
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

  public void pinLatestSetList(int id) {
    setListDao.flagSetListAsMostRecent(id);
  }
}
