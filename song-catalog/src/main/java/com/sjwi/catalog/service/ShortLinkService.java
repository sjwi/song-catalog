/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.service;

import com.sjwi.catalog.dao.ShortLinkDao;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShortLinkService {

  @Autowired ShortLinkDao shortLinkDao;

  public String registerPath(String path) {
    String key = UUID.randomUUID().toString().substring(32);
    Runnable registerToDb =
        new Runnable() {
          public void run() {
            shortLinkDao.registerPath(key, path);
          }
        };
    new Thread(registerToDb).start();
    return key;
  }

  public String getPath(String key) {
    return shortLinkDao.getPath(key);
  }
}
