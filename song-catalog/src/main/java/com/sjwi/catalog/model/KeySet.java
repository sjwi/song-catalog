/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class KeySet {
  public static final String NUMBER_SYSTEM_KEY_CODE = "NumSys";
  public static final String LYRICS_ONLY_KEY_CODE = "L";

  private String keyId;
  private String I;
  private String iSharp;
  private String ii;
  private String iii;
  private String IV;
  private String ivSharp;
  private String V;
  private String vi;
  private String viSharp;
  private String vii;

  public KeySet(String keyId) {
    this.keyId = keyId;
  }

  public KeySet(String keyId, ResultSet rs) throws SQLException {
    this.keyId = keyId;
    if (rs != null) {
      I = rs.getString("I");
      iSharp = rs.getString("isharp");
      ii = rs.getString("ii");
      iii = rs.getString("iii");
      IV = rs.getString("IV");
      ivSharp = rs.getString("ivsharp");
      V = rs.getString("V");
      vi = rs.getString("vi");
      viSharp = rs.getString("visharp");
      vii = rs.getString("vii");
    }
  }

  public String getKeyId() {
    return keyId;
  }

  public String getI() {
    return I;
  }

  public String getiSharp() {
    return iSharp;
  }

  public String getIi() {
    return ii;
  }

  public String getIii() {
    return iii;
  }

  public String getIV() {
    return IV;
  }

  public String getIvSharp() {
    return ivSharp;
  }

  public String getV() {
    return V;
  }

  public String getVi() {
    return vi;
  }

  public String getViSharp() {
    return viSharp;
  }

  public String getVii() {
    return vii;
  }
}
