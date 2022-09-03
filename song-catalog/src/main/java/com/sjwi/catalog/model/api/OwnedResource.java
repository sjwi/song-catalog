/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model.api;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface OwnedResource {
  @JsonIgnore
  public String getOwner();
}
