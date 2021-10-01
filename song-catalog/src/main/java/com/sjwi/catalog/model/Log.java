package com.sjwi.catalog.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Log {
  private int id;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date date;
  @JsonFormat(pattern = "HH:mm:ss")
  private Date time;
  private String level;
  private String username;
  private String device;
  private String method;
  private String requestUrl;
  private List<String> params;
  public Log(int id, Date timestamp, String level, String username, String device, String method, String requestUrl,
      String[] params) {
    this.id = id;
    this.date = timestamp;
    this.time = timestamp;
    this.level = level;
    this.username = username;
    this.device = device;
    this.method = method;
    this.requestUrl = requestUrl;
    this.params = Arrays.asList(params);
  }
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public Date getDate() {
    return date;
  }
  public Date getTime() {
    return time;
  }
  public void setTimestamp(Date timestamp) {
    this.date = timestamp;
  }
  public String getLevel() {
    return level;
  }
  public void setLevel(String level) {
    this.level = level;
  }
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  public String getDevice() {
    return device;
  }
  public void setDevice(String device) {
    this.device = device;
  }
  public String getMethod() {
    return method;
  }
  public void setMethod(String method) {
    this.method = method;
  }
  public String getRequestUrl() {
    return requestUrl;
  }
  public void setRequestUrl(String requestUrl) {
    this.requestUrl = requestUrl;
  }
  public List<String> getParams() {
    return params;
  }
  public void setParams(List<String> params) {
    this.params = params;
  }
}

  