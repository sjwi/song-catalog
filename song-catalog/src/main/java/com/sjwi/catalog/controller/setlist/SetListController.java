/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.controller.setlist;

import com.sjwi.catalog.controller.ControllerHelper;
import com.sjwi.catalog.model.SetList;
import com.sjwi.catalog.model.api.setlist.AddSongToSetRequest;
import com.sjwi.catalog.model.api.setlist.AddSongsToSetRequest;
import com.sjwi.catalog.model.api.setlist.NewSetList;
import com.sjwi.catalog.service.OrganizationService;
import com.sjwi.catalog.service.SetListService;
import com.sjwi.catalog.service.SongService;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

@RestController("/setlists")
public class SetListController {

  public static final int SET_LISTS_PER_PAGE = 25;

  @Autowired ControllerHelper controllerHelper;

  @Autowired SetListService setListService;

  @Autowired OrganizationService organizationService;

  @Autowired SongService songService;

  @GetMapping("/")
  public ResponseEntity<List<SetList>> getSets(
      @RequestParam(required = false) Integer cursor,
      @RequestParam(required = false) String searchTerm,
      @RequestParam(required = false) Integer orgId) {
    if (orgId != null) return ResponseEntity.ok(setListService.getSetListsByOrg(orgId));
    return ResponseEntity.ok(
        setListService.getSetListPage(SET_LISTS_PER_PAGE, cursor == null ? 0 : cursor));
  }

  @PostMapping("/")
  public ResponseEntity<SetList> createSet(
      @RequestBody NewSetList setList,
      Principal principal,
      Authentication auth,
      HttpServletRequest request,
      HttpServletResponse response) {
    String setListName = controllerHelper.buildSetlistName(setList);
    SetList newSetList = setListService.createSet(setListName, setList, principal.getName());
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/entries/{id}")
            .buildAndExpand(newSetList.getId())
            .toUri();
    return ResponseEntity.created(uri).body(newSetList);
  }

  @GetMapping("/{id}")
  @ResponseStatus(value = HttpStatus.OK)
  public ResponseEntity<SetList> setListDetails(
      @RequestParam(name = "keys[]", required = false) List<String> keys, @PathVariable int id) {
    SetList setList = setListService.getSetListById(id);
    if (keys != null) setList = setList.transpose(keys);
    return ResponseEntity.ok(setList);
  }

  @PutMapping(value = "/{id}", params = "name")
  @ResponseStatus(value = HttpStatus.OK)
  public ResponseEntity<Object> renameSet(@PathVariable int id, @RequestParam String name)
      throws UnsupportedEncodingException {
    setListService.renameSet(id, URLDecoder.decode(name, StandardCharsets.UTF_8.name()));
    return ResponseEntity.ok().build();
  }

  @PutMapping(value = "/{id}", params = "pin")
  public ResponseEntity<Object> pinLatestSetList(@PathVariable int id) {
    setListService.pinLatestSetList(id);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteSet(@PathVariable int id) {
    setListService.deleteSet(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping(value = "/{id}", params = "add")
  public ResponseEntity<Object> addSongToSet(
      @PathVariable Integer id, @RequestBody @Valid AddSongToSetRequest addSongToSetRequest) {
    setListService.addSongToSet(
        addSongToSetRequest.getSongId(),
        id,
        addSongToSetRequest.getKey(),
        addSongToSetRequest.getIndex());
    return ResponseEntity.ok().build();
  }

  @PatchMapping(value = "/{id}", params = "multi_add")
  public ResponseEntity<Object> addSongsToSet(
      @PathVariable Integer id, @RequestBody @Valid AddSongsToSetRequest addSongsToSetRequest) {
    setListService.addSongsToSet(addSongsToSetRequest.getSongIds(), id);
    return ResponseEntity.ok().build();
  }

  @PatchMapping(value = "/{id}", params = "sort")
  public ResponseEntity<Object> sortSongs(
      @PathVariable Integer id, @RequestBody List<Integer> songIds) {
    setListService.sortSetList(songIds);
    return ResponseEntity.ok().build();
  }

  @PatchMapping(value = "/{id}", params = "remove")
  public ResponseEntity<Object> removeSongsFromSet(
      @PathVariable Integer id, @RequestBody List<Integer> songIds) {
    songIds.stream().forEach(songId -> setListService.removeSongFromSet(id, songId));
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/latest")
  public RedirectView siteLatestSet() {
    return new RedirectView("/setlists/" + setListService.getLatestSet().getId());
  }

  @PutMapping(value = "/song/{id}", params = "key")
  public ResponseEntity<Object> changeSetSongKey(
      @PathVariable Integer id, @RequestParam String key) {
    setListService.setDefaultSetKey(key, id);
    return ResponseEntity.ok().build();
  }

  @PutMapping(value = "/song/{id}", params = "version")
  public ResponseEntity<Object> changeVersion(
      @PathVariable Integer id, @RequestParam Integer version) {
    setListService.changeVersion(id, version);
    return ResponseEntity.ok().build();
  }
}
