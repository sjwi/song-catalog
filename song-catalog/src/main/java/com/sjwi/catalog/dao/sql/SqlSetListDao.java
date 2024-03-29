/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.dao.sql;

import static com.sjwi.catalog.model.KeySet.NUMBER_SYSTEM_KEY_CODE;

import com.sjwi.catalog.controller.ControllerHelper;
import com.sjwi.catalog.dao.SetListDao;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.model.SetList;
import com.sjwi.catalog.model.TransposableString;
import com.sjwi.catalog.model.song.SetListSong;
import com.sjwi.catalog.model.song.Song;
import com.sjwi.catalog.service.RecordingService;
import com.sjwi.catalog.service.UserService;
import com.sjwi.catalog.service.VersionService;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Repository;

@Repository
public class SqlSetListDao implements SetListDao {

  @Autowired private Map<String, String> queryStore;

  @Autowired RecordingService recordingService;

  @Autowired VersionService versionService;

  @Autowired JdbcTemplate jdbcTemplate;

  @Autowired ControllerHelper controllerHelper;

  @Autowired UserService userService;

  @Autowired CustomLogger log;

  @Override
  public List<SetList> getSetLists() {

    return jdbcTemplate.query(
        queryStore.get("getAllSetlistsOrderByCreatedOnDesc"),
        r -> {
          List<SetList> setLists = new ArrayList<>();
          while (r.next()) {
            setLists.add(
                new SetList(
                    r.getInt("ID"),
                    r.getString("SETLIST_NAME"),
                    r.getTimestamp("CREATED_ON"),
                    r.getTimestamp("LAST_UPDATED"),
                    r.getTimestamp("FLAGGED_AS_MOST_RECENT_ON"),
                    r.getString("CREATED_BY"),
                    r.getInt("ORGANIZATION"),
                    getSetSongs(r.getInt("ID"))));
          }
          return setLists;
        });
  }

  @Override
  public List<SetList> getSetLists(int qty) {
    return jdbcTemplate.query(
        queryStore.get("getXNumberOfSetlistsOrderByCreatedOnDesc"),
        new Object[] {qty},
        r -> {
          List<SetList> setLists = new ArrayList<>();
          while (r.next()) {
            setLists.add(
                new SetList(
                    r.getInt("ID"),
                    r.getString("SETLIST_NAME"),
                    r.getTimestamp("CREATED_ON"),
                    r.getTimestamp("LAST_UPDATED"),
                    r.getTimestamp("FLAGGED_AS_MOST_RECENT_ON"),
                    r.getString("CREATED_BY"),
                    r.getInt("ORGANIZATION"),
                    getSetSongs(r.getInt("ID"))));
          }
          return setLists;
        });
  }

  @Override
  public List<SetList> getSetListsByOrg(int id) {
    return jdbcTemplate.query(
        queryStore.get("getSetListsByOrg"),
        new Object[] {id},
        r -> {
          List<SetList> setLists = new ArrayList<>();
          while (r.next()) {
            setLists.add(
                new SetList(
                    r.getInt("ID"),
                    r.getString("SETLIST_NAME"),
                    r.getTimestamp("CREATED_ON"),
                    r.getTimestamp("LAST_UPDATED"),
                    r.getTimestamp("FLAGGED_AS_MOST_RECENT_ON"),
                    r.getString("CREATED_BY"),
                    r.getInt("ORGANIZATION"),
                    getSetSongs(r.getInt("ID"))));
          }
          return setLists;
        });
  }

  @Override
  public List<SetList> getSetListPage(int page, int cursor) {
    return jdbcTemplate.query(
        queryStore.get("getSetListsAfterX"),
        new Object[] {page * cursor, page},
        r -> {
          List<SetList> setLists = new ArrayList<>();
          while (r.next()) {
            setLists.add(
                new SetList(
                    r.getInt("ID"),
                    r.getString("SETLIST_NAME"),
                    r.getTimestamp("CREATED_ON"),
                    r.getTimestamp("LAST_UPDATED"),
                    r.getTimestamp("FLAGGED_AS_MOST_RECENT_ON"),
                    r.getString("CREATED_BY"),
                    r.getInt("ORGANIZATION"),
                    getSetSongs(r.getInt("ID"))));
          }
          return setLists;
        });
  }

  @Override
  public void addSongToSet(int songId, int setListId, String setDefaultKey, int sort) {
    if (sort != 0) {
      jdbcTemplate.update(
          queryStore.get("updateSetListSortForInsert"), new Object[] {setListId, sort});
    } else {
      sort = getMaxSortOrderFromSet(setListId);
    }
    if (setDefaultKey == null) {
      jdbcTemplate.update(
          queryStore.get("insertSongIntoSetDefaultKey"),
          new Object[] {setListId, sort, null, songId});
    } else {
      jdbcTemplate.update(
          queryStore.get("insertSongIntoSetOverrideKey"),
          new Object[] {setListId, setDefaultKey, sort, java.sql.Types.VARCHAR, null, songId});
    }
    jdbcTemplate.update(queryStore.get("updateSetlistLastUpdated"), new Object[] {setListId});
  }

  @Override
  public void addSongsToSet(List<Integer> songIds, int setListId) {
    jdbcTemplate.batchUpdate(
        queryStore.get("insertSongsIntoSetDefaultKey"),
        songIds,
        songIds.size(),
        new ParameterizedPreparedStatementSetter<Integer>() {
          public void setValues(PreparedStatement ps, Integer s) throws SQLException {
            ps.setInt(1, setListId);
            ps.setInt(2, setListId);
            ps.setInt(3, s);
          }
        });
    jdbcTemplate.update(queryStore.get("updateSetlistLastUpdated"), new Object[] {setListId});
  }

  private int getMaxSortOrderFromSet(int setId) {
    return jdbcTemplate.query(
        queryStore.get("getMaxSortValInSetlist"),
        new Object[] {setId},
        r -> {
          int sortOrder = 1;
          if (r.next()) {
            sortOrder = r.getInt("SORT_ORDER") + 1;
          }
          return sortOrder;
        });
  }

  @Override
  public int createSet(String setListName, String user, int unit, int subUnit) {
    jdbcTemplate.update(
        queryStore.get("createNewSet"), new Object[] {setListName, user, unit, subUnit});
    return jdbcTemplate.query(
        queryStore.get("getLatestSet"),
        r -> {
          if (r.next()) {
            return r.getInt("ID");
          } else {
            return null;
          }
        });
  }

  @Override
  public void deleteSet(int id) {
    jdbcTemplate.update(queryStore.get("deleteFromSetlistMaster"), new Object[] {id});
    jdbcTemplate.update(queryStore.get("insertIntoSetlistArchive"), new Object[] {id});
    jdbcTemplate.update(queryStore.get("deleteFromSetlist"), new Object[] {id});
  }

  @Override
  public SetList getSetListById(int id) {
    return jdbcTemplate.query(
        queryStore.get("getSetlistById"),
        new Object[] {id},
        r -> {
          if (r.next()) {
            return new SetList(
                r.getInt("ID"),
                r.getString("SETLIST_NAME"),
                r.getTimestamp("CREATED_ON"),
                r.getTimestamp("LAST_UPDATED"),
                r.getTimestamp("FLAGGED_AS_MOST_RECENT_ON"),
                r.getString("CREATED_BY"),
                r.getInt("ORGANIZATION"),
                getSetSongs(r.getInt("ID")));
          } else {
            return null;
          }
        });
  }

  @Override
  public void removeSongFromSet(int setId, int songId) {
    int sortOrder =
        jdbcTemplate.query(
            queryStore.get("getSortOrderForSongInSet"),
            new Object[] {songId},
            r -> {
              r.next();
              return r.getInt("SORT_ORDER");
            });
    jdbcTemplate.update(queryStore.get("decreaseSortOrder"), new Object[] {setId, sortOrder});
    jdbcTemplate.update(queryStore.get("deleteFromSetlistBySongId"), new Object[] {setId, songId});
    jdbcTemplate.update(queryStore.get("updateSetlistLastUpdated"), new Object[] {setId});
  }

  @Override
  public void setDefaultSetKey(String toKey, int songToTrans, int setListId) {
    jdbcTemplate.update(
        queryStore.get("updateDefaultKeyForSet"), new Object[] {toKey, songToTrans});
    jdbcTemplate.update(queryStore.get("updateSetlistLastUpdated"), new Object[] {setListId});
  }

  @Override
  public void sortSetList(List<Integer> sortedSongs) {
    AtomicInteger counter = new AtomicInteger(1);
    jdbcTemplate.batchUpdate(
        queryStore.get("updateSortOrderForSong"),
        sortedSongs,
        sortedSongs.size(),
        new ParameterizedPreparedStatementSetter<Integer>() {
          public void setValues(PreparedStatement ps, Integer s) throws SQLException {
            ps.setInt(1, counter.getAndIncrement());
            ps.setInt(2, s);
          }
        });
    jdbcTemplate.update(
        queryStore.get("updateSetlistLastUpdated"),
        new Object[] {getSetListIdForSetSong(sortedSongs.get(0))});
  }

  @Override
  public List<SetList> searchSetLists(String searchTerm) {
    if (searchTerm == null) {
      return getSetLists();
    } else {
      return jdbcTemplate.query(
          queryStore.get("searchSetlistsByTerm"),
          new Object[] {searchTerm, searchTerm, searchTerm},
          r -> {
            List<SetList> setLists = new ArrayList<>();
            while (r.next()) {
              setLists.add(
                  new SetList(
                      r.getInt("ID"),
                      r.getString("SETLIST_NAME"),
                      r.getTimestamp("CREATED_ON"),
                      r.getTimestamp("LAST_UPDATED"),
                      r.getTimestamp("FLAGGED_AS_MOST_RECENT_ON"),
                      r.getString("CREATED_BY"),
                      r.getInt("ORGANIZATION"),
                      getSetSongs(r.getInt("ID"))));
            }
            return setLists;
          });
    }
  }

  @Override
  public SetList getLatestSet() {
    return jdbcTemplate.query(
        queryStore.get("getLatestSet"),
        r -> {
          r.next();
          return getSetListById(r.getInt("ID"));
        });
  }

  @Override
  public List<Song> getSetSongs(int setListId) {
    try {
      return jdbcTemplate.query(
          queryStore.get("getSongsInSetBySetId"),
          new Object[] {setListId},
          r -> {
            List<Song> setSongs = new ArrayList<>();
            while (r.next()) {
              if (r.getInt("RELATED") == 0) {
                setSongs.add(
                    new SetListSong(
                        r.getInt("ID"),
                        r.getInt("SETLIST_ID"),
                        r.getInt("SORT_ORDER"),
                        r.getInt("SONG_ID"),
                        r.getString("NAME"),
                        new TransposableString(r.getString("BODY"), NUMBER_SYSTEM_KEY_CODE),
                        r.getString("DEFAULT_KEY"),
                        r.getString("ARTIST"),
                        r.getString("NOTES"),
                        userService.loadCfUserByUsername(r.getString("CREATED_BY")),
                        userService.loadCfUserByUsername(r.getString("MODIFIED_BY")),
                        r.getTimestamp("CHANGED_ON"),
                        r.getInt("RELATED"),
                        ("Y").equals(r.getString("PRIVATE")) ? true : false,
                        r.getInt("CATEGORY"),
                        recordingService.getRecordingBySongId(r.getInt("SONG_ID"))));
              } else {
                setSongs.add(versionService.getSetListVersionSongById(r.getInt("ID")));
              }
            }
            return setSongs;
          });
    } catch (Exception e) {
      controllerHelper.errorHandler(e);
      return null;
    }
  }

  @Override
  public void changeVersion(int setId, int oldVersion, int newVersion) {
    jdbcTemplate.update(
        queryStore.get("updateSetListVersion"), new Object[] {newVersion, setId, oldVersion});
    jdbcTemplate.update(queryStore.get("updateSetlistLastUpdated"), new Object[] {setId});
  }

  @Override
  public void changeVersion(int setSongId, int versionId) {
    jdbcTemplate.update(
        queryStore.get("updateSetListVersionBySetSongId"),
        new Object[] {versionId, versionId, setSongId});
    jdbcTemplate.update(
        queryStore.get("updateSetlistLastUpdated"),
        new Object[] {getSetListIdForSetSong(setSongId)});
  }

  @Override
  public void renameSet(int id, String setListName) {
    jdbcTemplate.update(queryStore.get("updateSetlistTitle"), new Object[] {setListName, id});
    jdbcTemplate.update(queryStore.get("updateSetlistLastUpdated"), new Object[] {id});
  }

  @Override
  public SetList getLatestSetListByOrg(int id) {
    return jdbcTemplate.query(
        queryStore.get("getLatestSetListByOrg"),
        new Object[] {id},
        r -> {
          if (r.next()) {
            return getSetListById(r.getInt("ID"));
          } else {
            return null;
          }
        });
  }

  @Override
  public int getSetListIdForSetSong(int setSongId) {
    return jdbcTemplate.query(
        queryStore.get("getSetListIdForSetSong"),
        new Object[] {setSongId},
        r -> {
          r.next();
          return r.getInt("ID");
        });
  }

  @Override
  public void flagSetListAsMostRecent(int id) {
    jdbcTemplate.update(queryStore.get("flagSetListAsMostRecent"), new Object[] {id});
  }
}
