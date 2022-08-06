/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model.api.song;

import lombok.Data;

@Data
public class EditSongRequest {
  String songTitle;
  String songBody;
  String defaultKey;
  String updatedKey;
  Integer category;
}
