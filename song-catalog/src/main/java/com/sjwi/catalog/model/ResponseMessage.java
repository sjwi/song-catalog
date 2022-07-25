/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model;

public class ResponseMessage {

  private String status;
  private Object action;

  public ResponseMessage(String status) {
    super();
    this.status = status;
  }

  public ResponseMessage(String status, Object action) {
    super();
    this.status = status;
    this.action = action;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Object getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }
}
