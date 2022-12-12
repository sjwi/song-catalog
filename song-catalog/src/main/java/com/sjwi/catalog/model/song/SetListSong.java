/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model.song;

import java.util.Date;

import com.sjwi.catalog.model.Recording;
import com.sjwi.catalog.model.TransposableString;
import com.sjwi.catalog.model.user.CfUser;

public class SetListSong extends Song {

  protected final int setListSongId;
  protected final int setListId;
  protected final int sortOrder;

  public SetListSong(
      int setListSongId,
      int setListId,
      int sortOrder,
      int id,
      String name,
      TransposableString transposableString,
      String defaultKey,
      String artist,
      String notes,
      CfUser createdBy,
      CfUser modifiedBy,
      Date changedOn,
      int related,
      boolean priv,
      int category,
      Recording recording) {
    super(
        id,
        name,
        transposableString,
        defaultKey,
        artist,
        notes,
        createdBy,
        modifiedBy,
        changedOn,
        related,
        priv,
        category,
        recording);
    this.setListSongId = setListSongId;
    this.setListId = setListId;
    this.sortOrder = sortOrder;
  }

  public int getSetListSongId() {
    return setListSongId;
  }

  public int getSetListId() {
    return setListId;
  }

  public int getSortOrder() {
    return sortOrder;
  }

  @Override
  public Song transpose(String targetTranspositionKey) {
    return new SetListSong(
        setListSongId,
        setListId,
        sortOrder,
        id,
        name,
        transposableString,
        targetTranspositionKey,
        artist,
        notes,
        createdBy,
        modifiedBy,
        changedOn,
        related,
        priv,
        category,
        recording);
  }
}
