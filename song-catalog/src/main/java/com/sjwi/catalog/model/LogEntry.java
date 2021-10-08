package com.sjwi.catalog.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class LogEntry {
  private int id;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date date;
  @JsonFormat(pattern = "HH:mm:ss")
  private Date time;
  private String level;
  private String username;
  private String device;
  private String ip;
  private String method;
  private String requestUrl;
  private boolean standAlone;
  private String protocol;
  private List<String> params;

  public LogEntry(int id, Date timestamp, String level, String username, String device, String ip, String method, String requestUrl,
    boolean standAlone, String protocol, String[] params) {
    this.id = id;
    this.date = timestamp;
    this.time = timestamp;
    this.level = level;
    this.username = username;
    this.device = device;
    this.ip = ip;
    this.method = method;
    this.requestUrl = requestUrl;
    this.standAlone = standAlone;
    this.protocol = protocol;
    this.params = Arrays.asList(params);
  }
  public boolean isStandAlone() {
    return standAlone;
  }
  public String getProtocol() {
    return protocol;
  }
  public String getIp() {
    return ip;
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

  