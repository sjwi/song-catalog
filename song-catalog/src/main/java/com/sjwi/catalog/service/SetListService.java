/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sjwi.catalog.config.ServletConstants;
import com.sjwi.catalog.controller.ControllerHelper;
import com.sjwi.catalog.dao.SetListDao;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.model.SetList;
import com.sjwi.catalog.model.SetListState;
import com.sjwi.catalog.model.song.SetListSong;
import com.sjwi.catalog.model.song.Song;

@Component
public class SetListService {

  @Autowired SetListDao setListDao;

  @Autowired CustomLogger logger;

  @Autowired ControllerHelper controllerHelper;

  @Autowired UserService userService;

  public SetList getSetListById(int id) {
    SetList sl = setListDao.getSetListById(id);
    return sl == null ? sl : sl.transpose(userService.getSetState(id));
  }

  public SetList getLyricsToSetListById(int id) {
    return setListDao.getSetListById(id).transposeToLyrics();
  }

  public List<SetList> getSetLists() {
    return setListDao.getSetLists();
  }

  public List<SetList> getSetLists(int quantity) {
    return transposeFromState(setListDao.getSetLists(quantity));
  }

  private List<SetList> transposeFromState(List<SetList> setlists) {
    Map<Integer, SetListState> state = userService.getAllSetlistStatesForUser();
    return setlists.stream()
        .map(
            s -> {
              if (state.containsKey(s.getId())) return s.transpose(state.get(s.getId()));
              else return s;
            })
        .collect(Collectors.toList());
  }

  private List<Song> transposeFromState(List<Song> songs, int setId) {
    SetListState state = userService.getSetState(setId);
    return songs.stream()
        .map(
            s -> {
              SetListSong sl = (SetListSong) s;
              if (state.getSongSettings().containsKey(sl.getSetListSongId()))
                return sl.transpose(state.getSongSettings().get(sl.getSetListSongId()).getKey());
              else return sl;
            })
        .collect(Collectors.toList());
  }

  public SetList getLatestSet() {
    return setListDao.getLatestSet();
  }

  public SetList getLatestSetByOrg(int id) {
    return setListDao.getLatestSetListByOrg(id);
  }

  public List<SetList> searchSetLists(String searchTerm) {
    return transposeFromState(
        setListDao.searchSetLists(
            searchTerm == null ? null : "%" + searchTerm.toLowerCase() + "%"));
  }

  public List<SetList> getSetListPage(int pageSize, int cursor) {
    return transposeFromState(setListDao.getSetListPage(pageSize, cursor));
  }

  public List<Song> getSetSongs(int id) {
    return transposeFromState(setListDao.getSetSongs(id),id);
  }

  public int createSet(String setListName, String user, int unit, int subUnit) {
    int setListId = setListDao.createSet(setListName, user, unit, subUnit);
    logger.logUserActionWithEmail(
        "New set list created: "
            + setListName
            + "\n"
            + ServletConstants.FULL_URL
            + "/setlist/"
            + setListId);
    return setListId;
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

  public Integer getSetListIdBySong(int songId) {
    return setListDao.getSetListIdForSetSong(songId);
  }
}
