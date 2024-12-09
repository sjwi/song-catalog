/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.sjwi.catalog.dao.SongDao;
import com.sjwi.catalog.model.KeySet;
import com.sjwi.catalog.model.SearchTerm;
import com.sjwi.catalog.model.TransposableString;
import com.sjwi.catalog.model.song.MasterSong;
import com.sjwi.catalog.model.song.Song;
import com.sjwi.catalog.model.user.CfUser;

@Component
public class SongService {

  private static final String SEARCH_TERM_DELIMITER = ":";

  private static final String SONG_CACHE_KEY_ROOT = "songs";
  private static ConcurrentHashMap<String, List<Song>> songCache = new ConcurrentHashMap<>();

  @Autowired SongDao songDao;
  @Autowired VersionService versionService;
  @Autowired RecordingService recordingService;

  public Song getSongById(int id) {
    return songDao.getSongById(id);
  }

  public List<Song> getSongs() {
    if (!songCache.containsKey(SONG_CACHE_KEY_ROOT))
      songCache.put(SONG_CACHE_KEY_ROOT, songDao.getSongs());
    return songCache.get(SONG_CACHE_KEY_ROOT);
  }

  public List<Song> searchSongs(String searchValue) {
    if (searchValue == null || searchValue.trim().isEmpty()) return getSongs();

    String cacheKey = SONG_CACHE_KEY_ROOT + searchValue;
    if (songCache.containsKey(cacheKey)) return songCache.get(cacheKey);

    if (searchValue.contains(SEARCH_TERM_DELIMITER)) {
      String[] searchParts = searchValue.split(SEARCH_TERM_DELIMITER);
      String termKey = searchParts[0];
      String termValue = searchParts[1];
      SearchTerm term = SearchTerm.fromString(termKey);
      if (term != null) return songDao.searchSongsWithTerm(term, termValue);
    }
    songCache.put(cacheKey, songDao.searchSongs("%" + searchValue.toLowerCase() + "%"));
    return songCache.get(cacheKey);
  }

  public void setDefaultKey(String updatedVersionKey, int songId, String user) {
    songDao.setDefaultKey(updatedVersionKey, songId, user);
  }

  public int addSong(
      String songTitle, String songBody, String chordedIn, CfUser user, int category) {
    int song =
        songDao.addSong(
            new MasterSong(
                null,
                0,
                songTitle,
                new TransposableString(songBody, chordedIn),
                chordedIn,
                null,
                null,
                user,
                user,
                new Date(),
                0,
                false,
                category,
                null));
    refreshSongCache();
    return song;
  }

  public void deleteSong(int songId) {
    songDao.deleteSong(songId);
    refreshSongCache();
  }

  public void updateSong(Song song, String user) {
    songDao.updateSong(song, user);
    refreshSongCache();
  }

  public List<KeySet> getKeySets() {
    return songDao.getKeySets();
  }

  public Song buildSongFromResultSet(ResultSet rs) {
    return songDao.buildSongFromResultSet(rs);
  }

  public Map<Integer, String> getSongCategories() {
    return songDao.getSongCategories();
  }

  public Map<Song, Integer> getFrequencyCount() {
    return songDao.getFrequencyCount();
  }

  public Map<Song, Integer> getFrequencyCountByOrg(int orgId) {
    return songDao.getFrequencyCountByOrg(orgId);
  }

  public Map<Song, Integer> getServiceFrequencyCountByOrg(int id, List<Integer> services) {
    return services == null
        ? new HashMap<Song, Integer>()
        : songDao.getServiceFrequencyCountByOrg(id, services);
  }

  public Map<Song, Integer> getServiceFrequencyCount(List<Integer> services) {
    return services == null
        ? new HashMap<Song, Integer>()
        : songDao.getServiceFrequencyCount(services);
  }

  public static List<AbstractDelta<String>> generateSongRevisionDiff(
      Song originalSong, Song revisedSong) {
    List<String> originalSongAsList =
        new ArrayList<String>(Arrays.asList(originalSong.getBody().split("\n")));
    List<String> revisedSongAsList =
        new ArrayList<String>(Arrays.asList(revisedSong.getBody().split("\n")));
    originalSongAsList.add(0, originalSong.getName());
    revisedSongAsList.add(0, revisedSong.getName());

    return DiffUtils.diff(originalSongAsList, revisedSongAsList).getDeltas();
  }

  public Song getSongByName(String songTitle) {
    return songDao.getSongByName(songTitle);
  }

  public void refreshSongCache() {
    songCache.clear();
    Thread clearCache =
        new Thread(
            new Runnable() {
              @Override
              public void run() {
                songCache.put(SONG_CACHE_KEY_ROOT, songDao.getSongs());
                versionService.refreshVersionCache();
                recordingService.refreshRecordingCache();
              }
            });
    clearCache.start();
  }
}
