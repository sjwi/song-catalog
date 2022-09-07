package com.sjwi.catalog.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sjwi.catalog.dao.UserDao;

@Component
public class DbCleanup {

  @Autowired
  UserDao userDao;

  @Scheduled(cron = "0 0 * * * *")
  public void cleanDb() {
    userDao.cleanBots();
  }
  
}
