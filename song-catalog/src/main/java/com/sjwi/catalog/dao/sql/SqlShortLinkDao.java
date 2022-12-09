/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.dao.sql;

import com.sjwi.catalog.dao.ShortLinkDao;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SqlShortLinkDao implements ShortLinkDao {
  @Autowired private Map<String, String> queryStore;
  @Autowired JdbcTemplate jdbcTemplate;

  public void registerPath(String key, String path) {
    jdbcTemplate.update(queryStore.get("deleteRegisteredKey"), new Object[] {key});
    jdbcTemplate.update(queryStore.get("registerPath"), new Object[] {key, path});
  }

  public String getPath(String key) {
    return jdbcTemplate.query(
        queryStore.get("getPath"),
        new Object[] {key},
        rs -> {
          if (rs.next()) {
            return rs.getString("PATH");
          } else {
            return null;
          }
        });
  }
}
