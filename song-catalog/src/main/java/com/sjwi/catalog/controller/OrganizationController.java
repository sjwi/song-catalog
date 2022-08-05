/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.controller;

import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.model.Organization;
import com.sjwi.catalog.model.SetList;
import com.sjwi.catalog.service.OrganizationService;
import com.sjwi.catalog.service.SetListService;
import com.sjwi.catalog.service.SongService;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriUtils;

@RestController
public class OrganizationController {

  @Autowired OrganizationService organizationService;

  @Autowired SongService songService;

  @Autowired SetListService setListService;

  @Autowired CustomLogger log;

  @GetMapping("/organizations")
  public ResponseEntity<List<Organization>> getOrganizations() {
    return ResponseEntity.ok(organizationService.getOrganizations());
  }

  @GetMapping("/services")
  public ResponseEntity<Map<Integer, String>> getServices() {
    return ResponseEntity.ok(organizationService.getMeetingServices());
  }

  @GetMapping("/groups")
  public ResponseEntity<Map<Integer, String>> getGroups() {
    return ResponseEntity.ok(organizationService.getGroups());
  }

  @GetMapping("/categories")
  public ResponseEntity<Map<Integer, String>> getCategories() {
    return ResponseEntity.ok(songService.getSongCategories());
  }

  @GetMapping("/organizations/{id}")
  public ResponseEntity<Organization> getOrganization(@PathVariable Integer id) {
    return ResponseEntity.ok(organizationService.getOrganizationById(id));
  }

  @GetMapping("/lyrics")
  public RedirectView siteLatestLyrics() {
    SetList setList = setListService.getLatestSet();
    String today = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
    return new RedirectView(
        "forward:/"
            + "setlist/pdf/"
            + setList.getId()
            + "/CF Worship "
            + UriUtils.encode(today, "UTF-8")
            + "?lyricsOnly=true&fontSize=18");
  }

  @RequestMapping(
      value = {"/org/{id}/{endpoint}"},
      method = RequestMethod.GET)
  public ModelAndView latestOrgResource(
      @PathVariable int id, @PathVariable String endpoint, HttpServletResponse response)
      throws IOException {
    SetList setList = setListService.getLatestSetByOrg(id);
    Organization organization = organizationService.getOrganizationById(id);
    String today = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
    switch (endpoint) {
      case "lyrics-handout":
      case "latest-handout":
        log.logUserActionWithEmail(organization.getName() + " lyric handout shortlink visited");
        String dynamicName = organization.getId() == 0 ? "CF" : organization.getName();
        return new ModelAndView("redirect:/org/" + id + "/" + dynamicName + "%20Worship%20Handout");
      case "deck":
      case "slides":
      case "slideshow":
      case "ppt":
      case "presentation":
        return new ModelAndView(
            "forward:/"
                + "setlist/ppt/"
                + setList.getId()
                + "/"
                + UriUtils.encode(organization.getName() + " PowerPoint Deck " + today, "UTF-8"));
      case "set":
      case "setlist":
        return new ModelAndView("forward:/setlist/" + setListService.getLatestSetByOrg(id).getId());
      default:
        return new ModelAndView(
            "forward:/"
                + "setlist/pdf/"
                + setList.getId()
                + "/"
                + UriUtils.encode(organization.getName() + " Worship " + today, "UTF-8")
                + "?lyricsOnly=true&fontSize=18&qrCode=on");
    }
  }
}
