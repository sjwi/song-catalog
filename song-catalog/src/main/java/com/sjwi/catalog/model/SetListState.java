package com.sjwi.catalog.model;

import java.util.HashMap;
import java.util.Map;

public class SetListState {

  public void updateSetting(int songId, SetSongSetting setting) {
    songSettings.put(songId, setting);
  }

  public SetListState() {
    this.songSettings = new HashMap<>();
  }

  Map<Integer, SetSongSetting> songSettings;
  
  public Map<Integer, SetSongSetting> getSongSettings() {
    return songSettings;
  }

  public static class SetSongSetting {
    Integer capo;
    String key;

    public SetSongSetting(int capo, String key) {
      this.capo = capo;
      this.key = key;
    }
    public int getCapo() {
      return capo;
    }
    public void setCapo(Integer capo) {
      this.capo = capo;
    }
    public String getKey() {
      return key;
    }
    public void setKey(String key) {
      this.key = key;
    }
  }
}
