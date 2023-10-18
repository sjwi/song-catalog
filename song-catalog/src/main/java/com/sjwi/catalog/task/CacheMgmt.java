/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.task;

import com.sjwi.catalog.service.SetListService;
import com.sjwi.catalog.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheMgmt {

  @Autowired SongService songService;
  @Autowired SetListService setListService;

  @Scheduled(cron = "0 */10 * * * *")
  public void cleanDb() {
    songService.refreshSongCache();
    setListService.refreshSetListCache();
  }
}
