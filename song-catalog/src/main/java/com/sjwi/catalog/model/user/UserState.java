/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserState {
  private Integer lastOrg;
  private Integer lastService;
  private Integer lastGroup;

  public UserState() {
    this(1, 1, 1);
  }

  public UserState(Integer lastOrg, Integer lastService, Integer lastGroup) {
    this.lastOrg = lastOrg;
    this.lastService = lastService;
    this.lastGroup = lastGroup;
  }

  public Integer getLastOrg() {
    return lastOrg;
  }

  public Integer getLastService() {
    return lastService;
  }

  public Integer getLastGroup() {
    return lastGroup;
  }

  public void setLastOrg(Integer lastOrg) {
    this.lastOrg = lastOrg;
  }

  public void setLastService(Integer lastService) {
    this.lastService = lastService;
  }

  public void setLastGroup(Integer lastGroup) {
    this.lastGroup = lastGroup;
  }

  @JsonIgnore
  public boolean isDefault() {
    return lastOrg == 1 && lastService == 1 && lastGroup == 1;
  }
}
