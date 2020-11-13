package com.sjwi.catalog.dao.sql;

import static com.sjwi.catalog.model.KeySet.LYRICS_ONLY_KEY_CODE;
import static com.sjwi.catalog.model.KeySet.NUMBER_SYSTEM_KEY_CODE;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sjwi.catalog.controller.ControllerHelper;
import com.sjwi.catalog.dao.SongDao;
import com.sjwi.catalog.model.KeySet;
import com.sjwi.catalog.model.TransposableString;
import com.sjwi.catalog.model.song.MasterSong;
import com.sjwi.catalog.model.song.Song;
import com.sjwi.catalog.service.RecordingService;
import com.sjwi.catalog.service.VersionService;

@Repository
public class SqlSongDao implements SongDao {

	@Autowired
	private Map<String,String> queryStore;
	
	@Autowired
	RecordingService recordingService;
	
	@Autowired
	VersionService versionService;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	ControllerHelper controllerHelper;
	
	@Value("{server.servlet.contextPath}")
	private String contextPath;
	
	@Override
	public List<Song> getMySongs(String user) {
		return jdbcTemplate.query(queryStore.get("getMySongsByUserName"), new Object[] {user}, r -> {
			return buildSongsFromResultSet(r);
		});
	}	
	
	@Override
	public List<Song> getSongs() {
		return jdbcTemplate.query(queryStore.get("getAllSongsOrderByNameAsc"), r -> {
			return buildSongsFromResultSet(r);
		});
	}
	
	@Override
	public List<Song> getSongs(int qty) {
		return jdbcTemplate.query(queryStore.get("getAllSongsOrderByNameAsc"), new Object[] {qty}, r -> {
			return buildSongsFromResultSet(r);
		});
	}
	
	@Override
	public int addSong(Song song) {
		jdbcTemplate.update(queryStore.get("insertNewSong"),new Object[] {song.getName(),
				song.getTransposableString().getTransposedString(NUMBER_SYSTEM_KEY_CODE),
				song.getDefaultKey(),
				song.getFreq(),
				song.getCreatedBy(),
				song.getModifiedBy(),
				song.isPriv()? "Y":null,
				song.getCategory()});
		return jdbcTemplate.query(queryStore.get("getLatestSongId"), r -> {
			r.next();
			return r.getInt("ID");
		});
	}

	@Override
	public Song getSongById(int id) {
		return jdbcTemplate.query(queryStore.get("getSongById"),new Object[] {id}, r -> {
			return buildSongsFromResultSet(r).get(0);
		});
	}

	@Override
	public KeySet getKey(String keyId) {
		return jdbcTemplate.query(queryStore.get("getKeysetByKeyId"), new Object[] {keyId}, r -> {
			r.next();
			return new KeySet(keyId, r);
		});
	}
	
	@Override
	public List<KeySet> getKeySets() {
		return jdbcTemplate.query(queryStore.get("getAllKeysets"),r -> {
			List<KeySet> keySets = new ArrayList<KeySet>();
			while (r.next()) {
				keySets.add(new KeySet(r.getString("keyId"),r));
			}
			keySets.add(new KeySet(LYRICS_ONLY_KEY_CODE,null));
			return keySets;
		});
	}
	
	@Override
	public void updateSong(Song theSong, String user) {
		insertSongIntoArchive(theSong.getId());
		jdbcTemplate.update(queryStore.get("updateSongById"),new Object[] {
				theSong.getName(),
				theSong.getTransposableString().getTransposedString(NUMBER_SYSTEM_KEY_CODE),
				theSong.getFreq(),
				theSong.getDefaultKey(),
				theSong.getArtist(),
				user,
				theSong.getCategory(),
				theSong.getId()
		});
	}
	
	@Override
	public void updateSongTitleAndBody(int songId, String name, String body, String user) {
		insertSongIntoArchive(songId);
		jdbcTemplate.update(queryStore.get("updateSongTitleAndBody"),new Object[] {
				name, body, user, songId
		});
	}

	@Override
	public void deleteSong(int id) {
		insertSongIntoArchive(id);
		jdbcTemplate.update(queryStore.get("deleteSongBySongId"), new Object[] {id});
		jdbcTemplate.update(queryStore.get("deleteVersionsByRelatedId"), new Object[] {id});
		jdbcTemplate.update(queryStore.get("deleteSongByRelatedId"), new Object[] {id});
		jdbcTemplate.update(queryStore.get("deleteFromAllSetlistsBySongId"), new Object[] {id});
	}
	
	private void insertSongIntoArchive(int id) {
		jdbcTemplate.update(queryStore.get("insertSongIntoArchive"), new Object[] {id});
	}
	
	@Override
	public List<Song> searchSongs(String searchTerm) {
		if (searchTerm != null && searchTerm.trim().length() > 0) {
			Map<String,String> parameterMap = new HashMap<>();
			parameterMap.put("term", searchTerm);
			return namedParameterJdbcTemplate.query(queryStore.get("searchSongsByTerm"), parameterMap,r -> {
				return buildSongsFromResultSet(r);
			});
		} else {
			return getSongs();
		}
	}
	
	@Override
	public void setDefaultKey(String defaultKey, int id, String user) {
		jdbcTemplate.update(queryStore.get("setDefaultKeyForSong"), new Object[] {
				defaultKey, user, id
		});
	}
	
	@Override
	public void addRecording(String name, String ext, int id)  {
		jdbcTemplate.update(queryStore.get("addRecording"), new Object[] {
				id,
				contextPath + "/audio/" + name,
				ext});
	}
	
	@Override
	public void makeSongPublic(int id)  {
		jdbcTemplate.update(queryStore.get("makeSongPublic"),new Object[] {id});
	}

	@Override
	public void makeSongPrivate(int id)  {
		jdbcTemplate.update(queryStore.get("makeSongPrivate"), new Object[] {id});
	}
	private List<Song> buildSongsFromResultSet(ResultSet rs)  {
		List<Song> songs = new ArrayList<Song>();
		try {
			while (rs.next()) {
				songs.add(new MasterSong(
						versionService.getVersionsByRelatedId(rs.getInt("ID")),
						rs.getInt("ID"),
						rs.getString("NAME"),
						new TransposableString(rs.getString("BODY"),NUMBER_SYSTEM_KEY_CODE),
						rs.getInt("FREQUENCY"),
						rs.getString("DEFAULT_KEY"),
						rs.getString("ARTIST"),
						rs.getString("NOTES"),
						rs.getString("CREATED_BY"),
						rs.getString("MODIFIED_BY"),
						rs.getDate("CHANGED_ON"),
						rs.getInt("RELATED"),
						("Y").equals(rs.getString("PRIVATE"))? true: false,
						rs.getInt("CATEGORY"),
						recordingService.getRecordingBySongId(rs.getInt("ID"))));
			}
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
		}
		return songs;
	}

	@Override
	public Song buildSongFromResultSet(ResultSet rs) {
		try {
			rs.next(); 
			return new MasterSong(
					versionService.getVersionsByRelatedId(rs.getInt("ID")),
					rs.getInt("ID"),
					rs.getString("NAME"),
					new TransposableString(rs.getString("BODY"),NUMBER_SYSTEM_KEY_CODE),
					rs.getInt("FREQUENCY"),
					rs.getString("DEFAULT_KEY"),
					rs.getString("ARTIST"),
					rs.getString("NOTES"),
					rs.getString("CREATED_BY"),
					rs.getString("MODIFIED_BY"),
					rs.getDate("CHANGED_ON"),
					rs.getInt("RELATED"),
					("Y").equals(rs.getString("PRIVATE"))? true: false,
					rs.getInt("CATEGORY"),
					recordingService.getRecordingBySongId(rs.getInt("ID")));
		} catch (Exception e) {
			return null;
		}
	}

	public Song buildSongFromResultSetWithoutIteration(ResultSet rs) {
		try {
			return new MasterSong(
					versionService.getVersionsByRelatedId(rs.getInt("ID")),
					rs.getInt("ID"),
					rs.getString("NAME"),
					new TransposableString(rs.getString("BODY"),NUMBER_SYSTEM_KEY_CODE),
					rs.getInt("FREQUENCY"),
					rs.getString("DEFAULT_KEY"),
					rs.getString("ARTIST"),
					rs.getString("NOTES"),
					rs.getString("CREATED_BY"),
					rs.getString("MODIFIED_BY"),
					rs.getDate("CHANGED_ON"),
					rs.getInt("RELATED"),
					("Y").equals(rs.getString("PRIVATE"))? true: false,
					rs.getInt("CATEGORY"),
					recordingService.getRecordingBySongId(rs.getInt("ID")));
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Map<Integer, String> getSongCategories() {
		return jdbcTemplate.query(queryStore.get("getSongCategories"), r -> {
			Map<Integer, String> categories = new HashMap<Integer, String>();
			while (r.next()) {
				categories.put(r.getInt("ID"), r.getString("NAME"));
			}
			return categories;
		});
	}

	@Override
	public Map<Song, Integer> getFrequencyCountByOrg(int orgId) {
		return jdbcTemplate.query(queryStore.get("getFrequencyCountByOrg"), new Object[] {orgId}, r -> {
			Map<Song, Integer> songMap = new HashMap<>();
			while (r.next()) {
				songMap.put(buildSongFromResultSetWithoutIteration(r),r.getInt("CT"));
			}
			return songMap;
		});
	}
}
