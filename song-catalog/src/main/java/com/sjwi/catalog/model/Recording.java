/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model;

public class Recording {
  private final int id;
  private final int songId;
  private final String path;
  private final String extension;

  public Recording(int id, String path, String ext, int songId) {
    this.path = path;
    this.extension = ext;
    this.id = id;
    this.songId = songId;
  }

  public int getId() {
    return id;
  }

  public int getSongId() {
    return songId;
  }

  public String getPath() {
    return path;
  }

  public String getExtension() {
    return extension;
  }
}
