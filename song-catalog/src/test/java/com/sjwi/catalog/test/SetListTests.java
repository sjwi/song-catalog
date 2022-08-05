/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.sjwi.catalog.model.SetList;
import com.sjwi.catalog.model.api.setlist.NewSetList;
import com.sjwi.catalog.model.song.SetListSong;
import com.sjwi.catalog.model.song.Song;
import com.sjwi.catalog.service.SetListService;
import com.sjwi.catalog.service.SongService;
import com.sjwi.catalog.test.config.SpringTestConfiguration;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@EnableAutoConfiguration
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringTestConfiguration.class})
public class SetListTests {

  @Autowired SetListService setListService;

  @Autowired SongService songService;

  @Test
  public void whenXNumberOfSetsRequested_SizeOfSetsListIsX() {
    final int requestSize = 4;
    assertEquals(requestSize, setListService.getSetLists(requestSize).size());
  }

  @Test
  public void whenNoKeyDefined_DefaultKeyIsSelected() {
    Song song = songService.getSongById(13);
    NewSetList newSet = new NewSetList();
    newSet.setUnit(1);
    newSet.setSubUnit(1);
    SetList setList = setListService.createSet("TEST_0", newSet, "demo_user");
    setListService.addSongToSet(song.getId(), setList.getId(), null, 0);
    SetList setList2 = setListService.getSetListById(setList.getId());
    assertEquals(song.getDefaultKey(), setList2.getSongs().get(0).getDefaultKey());
  }

  @Test
  public void whenKeyDefined_DefinedKeyIsSelected() {
    Song song = songService.getSongById(13);
    NewSetList newSet = new NewSetList();
    newSet.setUnit(1);
    newSet.setSubUnit(1);
    SetList setList = setListService.createSet("TEST_1", newSet, "demo_user");
    setListService.addSongToSet(song.getId(), setList.getId(), "F", 0);
    SetList setList2 = setListService.getSetListById(setList.getId());
    assertEquals("F", setList2.getSongs().get(0).getDefaultKey());
  }

  @Test
  public void whenLyricsRequested_AllSongsAreTransposedToLyrics() {
    SetList setList = setListService.getLyricsToSetListById(1);
    setList
        .getSongs()
        .forEach(
            s -> {
              assertEquals("L", s.getDefaultKey());
            });
  }

  @Test
  public void whenCreatedByInserted_CreatedByReturned() {
    NewSetList newSet = new NewSetList();
    newSet.setUnit(1);
    newSet.setSubUnit(1);
    SetList setList = setListService.createSet("TEST_2", newSet, "test_user");
    assertEquals("test_user", setListService.getSetListById(setList.getId()).getCreatedBy());
  }

  @Test
  public void whenOrgInserted_OrgReturned() {
    NewSetList newSet = new NewSetList();
    newSet.setUnit(2);
    newSet.setSubUnit(1);
    SetList setList = setListService.createSet("TEST_3", newSet, "test_user");
    assertEquals(2, setListService.getSetListById(setList.getId()).getOrganization());
  }

  @Test
  public void whenTermSearchedFor_CorrectSetListIsReturned() {
    assertEquals(3, setListService.searchSetLists("church3").get(0).getId());
  }

  @Test
  public void whenNewSetListCreated_LatestSetServiceReturnsNewSet() {
    NewSetList newSet = new NewSetList();
    newSet.setUnit(1);
    newSet.setSubUnit(1);
    assertEquals(
        setListService.createSet("TEST_4", newSet, "demo_user").getId(),
        setListService.getLatestSet().getId());
  }

  @Test
  public void whenNewSetListCreated_LatestSetByOrgServiceReturnsNewSet() {
    NewSetList newSet = new NewSetList();
    newSet.setUnit(4);
    newSet.setSubUnit(1);
    SetList setList = setListService.createSet("TEST_5", newSet, "demo_user");
    NewSetList newSet2 = new NewSetList();
    newSet2.setUnit(2);
    newSet2.setSubUnit(1);
    setListService.createSet("TEST_6", newSet2, "demo_user");
    assertEquals(setList.getId(), setListService.getLatestSetByOrg(4).getId());
  }

  @Test
  public void whenSongAddedToSet_SizeIncreasedByOne() {
    SetList setListPreAdd = setListService.getSetListById(1);
    setListService.addSongToSet(1, 1, null, 0);
    SetList setListPostAdd = setListService.getSetListById(1);
    assertEquals(setListPreAdd.getSongs().size() + 1, setListPostAdd.getSongs().size());
  }

  @Test
  public void whenNSongsAddedToSet_SizeIncreasedByN() {
    SetList setListPreAdd = setListService.getSetListById(1);
    setListService.addSongsToSet(new ArrayList<Integer>(Arrays.asList(1, 2, 3)), 1);
    SetList setListPostAdd = setListService.getSetListById(1);
    assertEquals(setListPreAdd.getSongs().size() + 3, setListPostAdd.getSongs().size());
  }

  @Test
  public void whenSongRemovedFromSet_SizeDecreasedByOne() {
    SetList setListPreRemove = setListService.getSetListById(3);
    setListService.removeSongFromSet(3, 10);
    SetList setListPostRemove = setListService.getSetListById(3);
    assertEquals(setListPreRemove.getSongs().size() - 1, setListPostRemove.getSongs().size());
  }

  @Test
  public void whenKeyChanged_KeyIsUpdated() {
    setListService.setDefaultSetKey("L", 2);
    SetList setList = setListService.getSetListById(1);
    assertEquals(
        setList.getSongs().stream()
            .filter(s -> ((SetListSong) s).getSetListSongId() == 2)
            .findFirst()
            .get()
            .getDefaultKey(),
        "L");
  }

  @Test
  public void whenSetDeleted_SetReturnsNull() {
    NewSetList newSet = new NewSetList();
    newSet.setUnit(4);
    newSet.setSubUnit(1);
    SetList setList = setListService.createSet("TEST_99", newSet, "test_user");
    setListService.deleteSet(setList.getId());
    assertNull(setListService.getSetListById(setList.getId()));
  }
}
