/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.controller;

import static com.sjwi.catalog.model.KeySet.NUMBER_SYSTEM_KEY_CODE;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.Part;
import javax.ws.rs.BadRequestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjwi.catalog.config.ServletConstants;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.mail.MailConstants;
import com.sjwi.catalog.model.TransposableString;
import com.sjwi.catalog.model.api.song.EditSongRequest;
import com.sjwi.catalog.model.api.song.NewSong;
import com.sjwi.catalog.model.song.MasterSong;
import com.sjwi.catalog.model.song.Song;
import com.sjwi.catalog.model.user.CfUser;
import com.sjwi.catalog.service.RecordingService;
import com.sjwi.catalog.service.SetListService;
import com.sjwi.catalog.service.SongService;
import com.sjwi.catalog.service.UserService;
import com.sjwi.catalog.service.VersionService;

@RestController()
@RequestMapping("/songs")
public class SongController {

  @Autowired CustomLogger logger;

  @Autowired ControllerHelper controllerHelper;

  @Autowired SongService songService;

  @Autowired VersionService versionService;

  @Autowired UserService userService;

  @Autowired RecordingService recordingService;

  @Autowired SetListService setListService;

  @GetMapping("/")
  public ResponseEntity<List<Song>> getSongs(@RequestParam(required = false) String searchTerm) {
    return ResponseEntity.ok(songService.searchSongs(searchTerm));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Song> getSong(
      @PathVariable int id,
      @RequestParam(required = false) Boolean return_parent,
      @RequestParam(required = false) String key) {
    Song song = songService.getSongById(id);
    if (return_parent && song.getRelated() != 0) song = songService.getSongById(song.getRelated());
    if (key != null) song = song.transpose(key);
    return ResponseEntity.ok(song);
  }

  @GetMapping(value = "/", params = "group_by")
  public ResponseEntity<Map<Song, Integer>> getSongsGroupedByFrequency(
      @RequestParam(required = false) Integer orgId,
      @RequestParam(required = false) List<Integer> serviceIds) {
    return ResponseEntity.ok(songService.getSongFrequencyCount(orgId, serviceIds));
  }

  @PostMapping("/songs")
  public ResponseEntity<Object> createSong(
      Authentication auth,
      Principal principal,
      @RequestParam(required = false) Integer parent_id,
      @RequestBody NewSong song)
      throws IOException {
    Song existingSong = songService.getSongByName(song.getSongTitle());
    if (parent_id == null && !Objects.isNull(existingSong))
      return new ResponseEntity<Object>(HttpStatus.CONFLICT);
    TransposableString body = new TransposableString(song.getSongBody(), song.getChordedIn());
    CfUser cfUser = userService.loadCfUserByUsername(principal.getName());
    Song newSong =
        parent_id == null
            ? songService.addSong(
                controllerHelper.titleCase(song.getSongTitle()),
                body,
                song.getChordedIn(),
                cfUser,
                song.getCategory())
            : versionService.createNewVersion(
                parent_id,
                cfUser.getUsername(),
                body.getTransposedString(NUMBER_SYSTEM_KEY_CODE),
                song.getChordedIn());
    logger.logMessageWithEmail(
        "New song created by "
            + auth.getName()
            + ": "
            + song.getSongTitle()
            + "\n "
            + ServletConstants.FULL_URL
            + "/song/"
            + newSong.getId());
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/entries/{id}")
            .buildAndExpand(newSong.getId())
            .toUri();
    return ResponseEntity.created(uri).body(newSong);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> editSong(
      @PathVariable int id,
      @RequestParam(value = "newVersion", required = false) String newVersion,
      @RequestParam(value = "setSongId", required = false) int setSongId,
      @RequestPart(value = "song", required = true) String song,
      Principal principal)
      throws IOException {
    EditSongRequest editedSong = new ObjectMapper().readValue(song, EditSongRequest.class);
    Song originalSong = songService.getSongById(id);
    Song revisedSong =
        MasterSong.from(
            editedSong, originalSong, userService.loadCfUserByUsername(principal.getName()));
    if (newVersion != null)
      versionService.createNewVersion(
          revisedSong.getRelated() == 0 ? id : revisedSong.getRelated(),
          principal.getName(),
          revisedSong.getBody(),
          revisedSong.getDefaultKey());
    else songService.updateSong(revisedSong, principal.getName());
    if (setSongId != 0 && newVersion != null) setListService.changeVersion(setSongId, id);
    logger.mailRevisionDeltas(originalSong, revisedSong, MailConstants.EDIT_ACTION);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteSong(@PathVariable int id) {
    songService.deleteSong(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping(value = "/{id}", params = "recording")
  public ResponseEntity<Object> removeRecording(
      @RequestParam String recording,
      @RequestPart(value = "file", required = false) Part songAudio,
      @PathVariable int id)
      throws IOException {
    if ("add".equalsIgnoreCase(recording)) recordingService.addOrUpdateRecording(id, songAudio);
    else recordingService.deleteRecording(id);
    return ResponseEntity.ok().build();
  }

  @PatchMapping(value = "/{id}", params = "master")
  public ResponseEntity<Object> swapMaster(
      @PathVariable int id, @RequestParam int master, Authentication auth) {
    Song newMaster = songService.getSongById(master);
    if (newMaster.getRelated() != 0) {
      if (newMaster.getRelated() != id) throw new BadRequestException();
      versionService.changeMaster(master, newMaster.getRelated());
    }
    return ResponseEntity.ok().build();
  }
}
