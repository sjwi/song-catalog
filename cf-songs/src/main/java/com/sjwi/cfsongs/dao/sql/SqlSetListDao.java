package com.sjwi.cfsongs.dao.sql;

import static com.sjwi.cfsongs.model.KeySet.NUMBER_SYSTEM_KEY_CODE;

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

import com.sjwi.cfsongs.controller.ControllerHelper;
import com.sjwi.cfsongs.dao.SetListDao;
import com.sjwi.cfsongs.log.CustomLogger;
import com.sjwi.cfsongs.model.SetList;
import com.sjwi.cfsongs.model.TransposableString;
import com.sjwi.cfsongs.model.song.SetListSong;
import com.sjwi.cfsongs.model.song.Song;
import com.sjwi.cfsongs.service.RecordingService;
import com.sjwi.cfsongs.service.VersionService;

@Repository
public class SqlSetListDao implements SetListDao {

	@Autowired
	private Map<String,String> queryStore;
	
	@Autowired
	RecordingService recordingService;
	
	@Autowired
	VersionService versionService;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	ControllerHelper controllerHelper;
	
	@Autowired
	CustomLogger log;
	
	@Override
	public List<SetList> getSetLists() {

		return jdbcTemplate.query(queryStore.get("getAllSetlistsOrderByCreatedOnDesc"), r -> {
			List<SetList> setLists = new ArrayList<>();
			while(r.next()) {
					setLists.add(new SetList(
					r.getInt("ID"),
					r.getString("SETLIST_NAME"),
					r.getDate("CREATED_ON"),
					r.getDate("LAST_UPDATED"),
					r.getString("CREATED_BY"),
					getSetSongs(r.getInt("ID"))
					));
			}
			return setLists;
		});
	}
	
	@Override
	public List<SetList> getSetLists(int qty) {
		return jdbcTemplate.query(queryStore.get("getXNumberOfSetlistsOrderByCreatedOnDesc"),new Object[] {qty}, r -> {
			List<SetList> setLists = new ArrayList<>();
			while(r.next()) {
					setLists.add(new SetList(
					r.getInt("ID"),
					r.getString("SETLIST_NAME"),
					r.getDate("CREATED_ON"),
					r.getDate("LAST_UPDATED"),
					r.getString("CREATED_BY"),
					getSetSongs(r.getInt("ID"))
					));
			}
			return setLists;
		});
	}

	@Override
	public List<SetList> getSetListPage(int page) {
		return jdbcTemplate.query(queryStore.get("getSetListsAfterX"),new Object[] {page}, r -> {
			List<SetList> setLists = new ArrayList<>();
			while(r.next()) {
					setLists.add(new SetList(
					r.getInt("ID"),
					r.getString("SETLIST_NAME"),
					r.getDate("CREATED_ON"),
					r.getDate("LAST_UPDATED"),
					r.getString("CREATED_BY"),
					getSetSongs(r.getInt("ID"))
					));
			}
			return setLists;
		});
	}

	@Override
	public void addSongToSet(int songId, int setListId, String setDefaultKey, int sort) {
		if (sort != 0) {
			jdbcTemplate.update(queryStore.get("updateSetListSortForInsert"), new Object[] {setListId, sort});
		} else {
			sort = getMaxSortOrderFromSet(setListId);
		}
		if (setDefaultKey == null) {
			jdbcTemplate.update(queryStore.get("insertSongIntoSetDefaultKey"), new Object[] {setListId, sort,null,songId});
		} else {
			jdbcTemplate.update(queryStore.get("insertSongIntoSetOverrideKey"), new Object[] {setListId, setDefaultKey,sort, java.sql.Types.VARCHAR,null,songId});
		}
		jdbcTemplate.update(queryStore.get("updateSongFrequencyById"), new Object[] {songId});
		jdbcTemplate.update(queryStore.get("updateSetlistLastUpdated"), new Object[] {setListId});
	}

	@Override
	public void addSongsToSet(List<Integer> songIds, int setListId) {
		jdbcTemplate.batchUpdate(queryStore.get("insertSongsIntoSetDefaultKey"), songIds, songIds.size(), new ParameterizedPreparedStatementSetter<Integer>() {
			public void setValues(PreparedStatement ps, Integer s) throws SQLException {
				ps.setInt(1, setListId);
				ps.setInt(2, setListId);
				ps.setInt(3, s);
			}
		});
		jdbcTemplate.batchUpdate(queryStore.get("updateSongFrequencyById"), songIds, songIds.size(), new ParameterizedPreparedStatementSetter<Integer>() {
			public void setValues(PreparedStatement ps, Integer s) throws SQLException {
				ps.setInt(1, s);
			}
		});
		jdbcTemplate.update(queryStore.get("updateSetlistLastUpdated"), new Object[] {setListId});
		
	}	
	
	private int getMaxSortOrderFromSet(int setId) {
		return jdbcTemplate.query(queryStore.get("getMaxSortValInSetlist"), new Object[] {setId}, r -> {
			int sortOrder = 1;
			if (r.next()) {
				sortOrder = r.getInt("SORT_ORDER") + 1;
			}
			return sortOrder;
		});
	}

	@Override
	public int createSet(String setListName, String user, int unit) {
		jdbcTemplate.update(queryStore.get("createNewSet"), new Object[] {setListName,user,unit});
		return jdbcTemplate.query(queryStore.get("getLatestSet"), r -> {
			r.next();
			return r.getInt("ID");
		});
	}

	@Override
	public void deleteSet(int id) {
		jdbcTemplate.update(queryStore.get("deleteFromSetlistMaster"),new Object[] {id});
		jdbcTemplate.update(queryStore.get("insertIntoSetlistArchive"),new Object[] {id});
		jdbcTemplate.update(queryStore.get("deleteFromSetlist"),new Object[] {id});
	}

	@Override
	public SetList getSetListById(int id) {
		return jdbcTemplate.query(queryStore.get("getSetlistById"), new Object[] {id}, r -> {
			r.next();
			return new SetList(r.getInt("ID"),
					r.getString("SETLIST_NAME"),
					r.getDate("CREATED_ON"),
					r.getDate("LAST_UPDATED"),
					r.getString("CREATED_BY"),
					getSetSongs(r.getInt("ID")));
		});
	}	
	
	
	@Override
	public void removeSongFromSet(int setId, int songId) {
		int sortOrder = jdbcTemplate.query(queryStore.get("getSortOrderForSongInSet"),new Object[] {songId}, r -> {r.next(); return r.getInt("SORT_ORDER");});
		jdbcTemplate.update(queryStore.get("decreaseSortOrder"),new Object[] {setId, sortOrder});
		jdbcTemplate.query(queryStore.get("getSongFromSetlistBySongId"),new Object[] {songId}, r -> {
				if(r.next()) {
					jdbcTemplate.update(queryStore.get("decreaseFrequencyBySongId"),new Object[] {r.getInt("SONG_ID")});
				}
			});
		jdbcTemplate.update(queryStore.get("deleteFromSetlistBySongId"),new Object[] {setId,songId});
		jdbcTemplate.update(queryStore.get("updateSetListMasterLastModified"),new Object[] {setId});
	}
	

	@Override
	public void setDefaultSetKey(String toKey, int songToTrans) {
		jdbcTemplate.update(queryStore.get("updateDefaultKeyForSet"),new Object[] {toKey, songToTrans});
	}

	@Override
	public void sortSetList(List<Integer> sortedSongs) {
		AtomicInteger counter = new AtomicInteger(1);
		jdbcTemplate.batchUpdate(queryStore.get("updateSortOrderForSong"),sortedSongs, sortedSongs.size(), new ParameterizedPreparedStatementSetter<Integer>() {
			public void setValues(PreparedStatement ps, Integer s) throws SQLException {
				ps.setInt(1, counter.getAndIncrement());
				ps.setInt(2, s);
			}
		});
	}

	@Override
	public List<SetList> searchSetLists(String searchTerm) {
		if (searchTerm == null) {
			return getSetLists();
		} else {
			return jdbcTemplate.query(queryStore.get("searchSetlistsByTerm"), new Object[] {searchTerm,searchTerm,searchTerm},r -> {
				List<SetList> setLists = new ArrayList<>();
				while (r.next()) {
					setLists.add(new SetList(r.getInt("ID"),
							r.getString("SETLIST_NAME"),
							r.getDate("CREATED_ON"),
							r.getDate("LAST_UPDATED"),
							r.getString("CREATED_BY"),
							getSetSongs(r.getInt("ID"))));
				}
				return setLists;
			});
		}
	}
	
	@Override
	public int getLatestSet()  {
		return jdbcTemplate.query(queryStore.get("getLatestSet"), r -> {
			r.next();
			return r.getInt("ID");
		});
	}

	@Override
	public void updateSetTitle(int id, String title)  {
		jdbcTemplate.update(queryStore.get("updateSetlistTitle"), new Object[] {title,id});
	}
	
	@Override
	public List<Song> getSetSongs(int setListId) {
		try {
			return jdbcTemplate.query(queryStore.get("getSongsInSetBySetId"), new Object[] {setListId},r -> {
				List<Song> setSongs = new ArrayList<>();
				while(r.next()) {
					if (r.getInt("RELATED") == 0) {
						setSongs.add(new SetListSong(
							r.getInt("ID"),
							r.getInt("SETLIST_ID"),
							r.getInt("SORT_ORDER"),
							r.getInt("SONG_ID"),
							r.getString("NAME"),
							new TransposableString(r.getString("BODY"),NUMBER_SYSTEM_KEY_CODE),
							r.getInt("FREQUENCY"),
							r.getString("DEFAULT_KEY"),
							r.getString("ARTIST"),
							r.getString("NOTES"),
							r.getString("CREATED_BY"),
							r.getString("MODIFIED_BY"),
							r.getDate("CHANGED_ON"),
							r.getInt("RELATED"),
							("Y").equals(r.getString("PRIVATE"))? true: false,
							r.getInt("CATEGORY"),
							recordingService.getRecordingBySongId(r.getInt("SONG_ID"))));
					} else {
						setSongs.add(versionService.getSetListVersionSongById(r.getInt("ID")));
					}
				}
				return setSongs;
			});
		}
		catch (Exception e) {
			controllerHelper.errorHandler(e);
			return null;
		}
	}

	@Override
	public void changeVersion(int setId, int oldVersion, int newVersion) {
		jdbcTemplate.update(queryStore.get("updateSetListVersion"),new Object[] {newVersion,setId,oldVersion});
	}

	@Override
	public void changeVersion(int setSongId, int versionId) {
		jdbcTemplate.update(queryStore.get("updateSetListVersionBySetSongId"),new Object[] {versionId,versionId,setSongId});
	}

	@Override
	public void renameSet(int id, String setListName) {
		jdbcTemplate.update(queryStore.get("updateSetlistTitle"),new Object[] {setListName,id});
	}
}
