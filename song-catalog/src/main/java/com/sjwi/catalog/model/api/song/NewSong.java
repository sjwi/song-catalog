/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model.api.song;

import lombok.Data;

@Data
public class NewSong {
  String songTitle;
  String songBody;
  String chordedIn;
  Integer category;
}
