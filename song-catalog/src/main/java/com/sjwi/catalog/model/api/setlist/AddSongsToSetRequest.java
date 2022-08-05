/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model.api.setlist;

import java.util.List;
import lombok.Data;

@Data
public class AddSongsToSetRequest {
  List<Integer> songIds;
}
