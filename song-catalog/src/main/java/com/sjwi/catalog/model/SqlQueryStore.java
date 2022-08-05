/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

public class SqlQueryStore {
  private static final String MYSQL_YML_FILENAME = "mysql.yaml";
  private static final String SQL_DIR_CLASSPATH = "classpath:sql/";

  private Map<String, String> sqlQueries;

  @SuppressWarnings("unchecked")
  public SqlQueryStore() throws FileNotFoundException {
    sqlQueries =
        (Map<String, String>)
            new Yaml()
                .load(
                    new FileReader(ResourceUtils.getFile(SQL_DIR_CLASSPATH + MYSQL_YML_FILENAME)));
  }

  public Map<String, String> getQueries() {
    return sqlQueries;
  }
}
