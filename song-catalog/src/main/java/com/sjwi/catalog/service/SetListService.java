/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.service;

import com.sjwi.catalog.config.ServletConstants;
import com.sjwi.catalog.controller.ControllerHelper;
import com.sjwi.catalog.dao.SetListDao;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.model.SetList;
import com.sjwi.catalog.model.SetListState;
import com.sjwi.catalog.model.song.SetListSong;
import com.sjwi.catalog.model.song.Song;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetListService {

  private static final String SETLIST_CACHE_KEY_ROOT = "setlist";
  private static ConcurrentHashMap<String, List<SetList>> setListCache = new ConcurrentHashMap<>();

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
    if (!setListCache.containsKey(SETLIST_CACHE_KEY_ROOT))
      setListCache.put(SETLIST_CACHE_KEY_ROOT, setListDao.getSetLists());
    return setListCache.get(SETLIST_CACHE_KEY_ROOT);
  }

  public List<SetList> getSetLists(int quantity) {
    String cacheKey = SETLIST_CACHE_KEY_ROOT + String.valueOf(quantity);
    if (!setListCache.containsKey(cacheKey))
      setListCache.put(cacheKey, transposeFromState(setListDao.getSetLists(quantity)));
    return setListCache.get(cacheKey);
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
    return transposeFromState(setListDao.getSetSongs(id), id);
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
    refreshSetListCache();
    return setListId;
  }

  public void deleteSet(int id) {
    setListDao.deleteSet(id);
    refreshSetListCache();
  }

  public void addSongToSet(int songId, int setListId, String key, int sort) {
    setListDao.addSongToSet(songId, setListId, key, sort);
    refreshSetListCache();
  }

  public void addSongsToSet(List<Integer> songIds, int setListId) {
    setListDao.addSongsToSet(songIds, setListId);
    refreshSetListCache();
  }

  public void removeSongFromSet(int setListId, int songId) {
    setListDao.removeSongFromSet(setListId, songId);
    refreshSetListCache();
  }

  public void changeVersion(int setId, int oldVersion, int newVersion) {
    setListDao.changeVersion(setId, oldVersion, newVersion);
    refreshSetListCache();
  }

  public void setDefaultSetKey(String newKey, int songId) {
    setListDao.setDefaultSetKey(newKey, songId, setListDao.getSetListIdForSetSong(songId));
    refreshSetListCache();
  }

  public void changeVersion(int setSongId, int versionId) {
    setListDao.changeVersion(setSongId, versionId);
    refreshSetListCache();
  }

  public void sortSetList(List<Integer> songIds) {
    setListDao.sortSetList(songIds);
    refreshSetListCache();
  }

  public void renameSet(int id, String setListName) {
    setListDao.renameSet(id, setListName);
    refreshSetListCache();
  }

  public List<SetList> getSetListsByOrg(int id) {
    return setListDao.getSetListsByOrg(id);
  }

  public void pinLatestSetList(int id) {
    setListDao.flagSetListAsMostRecent(id);
    refreshSetListCache();
  }

  public Integer getSetListIdBySong(int songId) {
    return setListDao.getSetListIdForSetSong(songId);
  }

  public SetList getSetListById(Integer id, boolean transposeFromState) {
    if (transposeFromState) return getSetListById(id);
    else return setListDao.getSetListById(id);
  }

  public void refreshSetListCache() {
    setListCache.clear();
    Thread clearCache =
        new Thread(
            new Runnable() {
              @Override
              public void run() {
                List<String> counts = new ArrayList<>(Arrays.asList("25", "10"));
                counts.forEach(
                    c ->
                        setListCache.put(
                            SETLIST_CACHE_KEY_ROOT + c,
                            setListDao.getSetLists(Integer.valueOf(c))));
              }
            });
    clearCache.start();
  }
}
