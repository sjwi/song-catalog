/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.service;

import com.sjwi.catalog.dao.VersionDao;
import com.sjwi.catalog.model.song.Song;
import com.sjwi.catalog.model.song.VersionSong;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VersionService {

  @Autowired VersionDao versionDao;

  private static final String VERSION_CACHE_KEY_ROOT = "versions";
  private static ConcurrentHashMap<String, Map<Integer, List<VersionSong>>> versionCache =
      new ConcurrentHashMap<>();

  public int createNewVersion(int songId, String user, String transposedSongBody, String key) {
    int vId = versionDao.createNewVersion(songId, user, transposedSongBody, key);
    refreshVersionCache();
    return vId;
  }

  public List<Song> getAllRelatedSongs(int id) {
    return versionDao.getAllRelatedSongs(id);
  }

  public VersionSong getVersionById(int id) {
    return (VersionSong) versionDao.getVersionById(id);
  }

  public void changeMaster(int newId, int id) {
    versionDao.changeMaster(newId, id);
    refreshVersionCache();
  }

  public Song getSetListVersionSongById(int id) {
    return versionDao.getSetListVersionSongById(id);
  }

  public List<VersionSong> getVersionsByRelatedId(int id) {
    return versionDao.getVersionsByRelatedId(id);
  }

  public Map<Integer, List<VersionSong>> getVersionsByRelatedIds() {
    if (!versionCache.containsKey(VERSION_CACHE_KEY_ROOT))
      versionCache.put(VERSION_CACHE_KEY_ROOT, versionDao.getVersionsByRelatedIds());
    return versionCache.get(VERSION_CACHE_KEY_ROOT);
  }

  public void refreshVersionCache() {
    versionCache.clear();
    Thread clearCache =
        new Thread(
            new Runnable() {
              @Override
              public void run() {
                versionCache.put(VERSION_CACHE_KEY_ROOT, versionDao.getVersionsByRelatedIds());
              }
            });
    clearCache.start();
  }
}
