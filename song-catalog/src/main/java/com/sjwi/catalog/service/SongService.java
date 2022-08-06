/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.service;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.sjwi.catalog.dao.SongDao;
import com.sjwi.catalog.model.KeySet;
import com.sjwi.catalog.model.SearchTerm;
import com.sjwi.catalog.model.TransposableString;
import com.sjwi.catalog.model.song.MasterSong;
import com.sjwi.catalog.model.song.Song;
import com.sjwi.catalog.model.user.CfUser;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SongService {

  private static final String SEARCH_TERM_DELIMITER = ":";

  @Autowired SongDao songDao;

  public Song getSongById(int id) {
    return songDao.getSongById(id);
  }

  public List<Song> getSongs() {
    return songDao.getSongs();
  }

  public List<Song> searchSongs(String searchValue) {
    if (searchValue == null || searchValue.trim().isEmpty()) return songDao.getSongs();
    if (searchValue.contains(SEARCH_TERM_DELIMITER)) {
      String[] searchParts = searchValue.split(SEARCH_TERM_DELIMITER);
      String termKey = searchParts[0];
      String termValue = searchParts[1];
      SearchTerm term = SearchTerm.fromString(termKey);
      if (term != null) return songDao.searchSongsWithTerm(term, termValue);
    }
    return songDao.searchSongs("%" + searchValue.toLowerCase() + "%");
  }

  public void setDefaultKey(String updatedVersionKey, int songId, String user) {
    songDao.setDefaultKey(updatedVersionKey, songId, user);
  }

  public Song addSong(
      String songTitle, TransposableString body, String chordedIn, CfUser user, int category) {
    return songDao.addSong(
        new MasterSong(
            null,
            0,
            songTitle,
            body,
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

  public Map<Integer, String> getSongCategories() {
    return songDao.getSongCategories();
  }

  public Map<Song, Integer> getSongFrequencyCount(Integer orgId, List<Integer> serviceIds) {
    if (orgId == null)
      if (serviceIds == null) return songDao.getFrequencyCount();
      else return songDao.getServiceFrequencyCount(serviceIds);
    else if (serviceIds == null) return songDao.getFrequencyCountByOrg(orgId);
    return songDao.getServiceFrequencyCountByOrg(orgId, serviceIds);
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
}
