/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sjwi.catalog.service.SetListService;
import com.sjwi.catalog.service.SongService;

@Component
public class CacheMgmt {

  @Autowired SongService songService;
  @Autowired SetListService setListService;

  @Scheduled(cron = "0 */10 * * * *")
  public void cleanDb() {
    System.out.println("REFRESHING");
    songService.refreshSongCache();
    setListService.refreshSetListCache();
  }
}
