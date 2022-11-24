/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.task;

import com.sjwi.catalog.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DbCleanup {

  @Autowired UserDao userDao;

  @Scheduled(cron = "0 0 * * * *")
  public void cleanDb() {
    userDao.cleanBots();
  }
}
