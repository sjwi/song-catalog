/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model.api.setlist;

import java.util.Date;
import lombok.Data;

@Data
public class NewSetList {
  Integer unit;
  String otherUnit;
  Integer subUnit;
  String otherSubUnit;
  Integer groupId;
  String otherGroupName;
  Date date;
}
