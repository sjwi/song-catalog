/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model.song;

import com.sjwi.catalog.model.Recording;
import com.sjwi.catalog.model.TransposableString;
import com.sjwi.catalog.model.user.CfUser;
import java.util.Date;
import java.util.List;

public class MinSong extends Song {

  public MinSong(
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
  }

  @Override
  public MinSong transpose(String targetTranspositionKey) {
    return new MinSong(
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

  public MasterSong toMaster(List<VersionSong> versions, Recording recording) {
    return new MasterSong(
        versions,
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
  }
}
