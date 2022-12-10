/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.task;

import com.sjwi.catalog.dao.UserDao;
import com.sjwi.catalog.log.CustomLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DbCleanup {

  @Autowired UserDao userDao;
  @Autowired CustomLogger logger;

  @Scheduled(cron = "0 0 * * * *")
  @ConditionalOnProperty(
      value = "spring.datasource.driver-class-name",
      havingValue = "com.mysql.jdbc.Driver",
      matchIfMissing = false)
  public void cleanDb() {
    logger.logMessageWithEmail("Cleaning up bots from db");
    userDao.cleanBots();
  }
}
