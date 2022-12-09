/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.dao;

public interface ShortLinkDao {
  public void registerPath(String key, String Path);

  public String getPath(String key);
}
