/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sjwi.catalog.model.api.OwnedResource;
import com.sjwi.catalog.model.song.SetListSong;

public class SetListResponseBody implements OwnedResource {
  private final int id;
  private final String setListName;
  private final Date createdOn;

  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "dd-MM-yyyy HH:mm:ss",
      timezone = "US/New York")
  private final Date lastModifiedOn;

  private final Date flaggedAsMostRecentOn;
  private final String createdBy;
  private final int organization;
  private String namePrefix;
  private String nameSuffix;
  private final List<Map<String, Object>> songs;

  public SetListResponseBody(SetList setList) {
    super();
    this.id = setList.getId();
    this.setListName = setList.getSetListName();
    this.createdOn = setList.getCreatedOn();
    this.lastModifiedOn = setList.getLastModifiedOn();
    this.flaggedAsMostRecentOn = setList.getFlaggedAsMostRecentOn();
    this.createdBy = setList.getCreatedBy();
    this.organization = setList.getOrganization();
    this.songs =
        setList.getSongs().stream()
            .map(s -> {
              Map<String, Object> ret = new HashMap<>();
              ret.put("id", s.getId());
              ret.put("key", s.getDefaultKey());
              ret.put("setSongId", ((SetListSong) s).getSetListSongId());
              return ret;
            })
            .collect(Collectors.toList());
  }

  public int getId() {
    return id;
  }

  public String getSetListName() {
    return setListName;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public Date getLastModifiedOn() {
    return lastModifiedOn;
  }

  public Date getFlaggedAsMostRecentOn() {
    return flaggedAsMostRecentOn;
  }

  public List<Map<String, Object>> getSongs() {
    return songs;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public String getNormalizedSetListName() {
    return setListName.replace("/", "_").replace("\\", "_").replace(";", ".");
  }

  public int getOrganization() {
    return organization;
  }

  public String getNamePrefix() {
    String[] nameArray = setListName.split(" ");
    return String.join(" ", Arrays.copyOfRange(nameArray, 0, nameArray.length - 1));
  }

  public String getNameSuffix() {
    String[] nameArray = setListName.split(" ");
    return nameArray[nameArray.length - 1].replace("-", "/");
  }

  @Override
  public String getOwner() {
    return createdBy;
  }
}
