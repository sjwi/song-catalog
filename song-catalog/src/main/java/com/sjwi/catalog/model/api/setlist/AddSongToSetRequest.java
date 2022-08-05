/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model.api.setlist;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddSongToSetRequest {
  @NotBlank Integer songId;
  String key;
  Integer index;
}
