package com.sjwi.catalog.dao.sql;

import static com.sjwi.catalog.model.KeySet.NUMBER_SYSTEM_KEY_CODE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sjwi.catalog.dao.VersionDao;
import com.sjwi.catalog.model.TransposableString;
import com.sjwi.catalog.model.song.MasterSong;
import com.sjwi.catalog.model.song.SetListVersionSong;
import com.sjwi.catalog.model.song.Song;
import com.sjwi.catalog.model.song.VersionSong;
import com.sjwi.catalog.service.RecordingService;
import com.sjwi.catalog.service.SongService;
import com.sjwi.catalog.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SqlVersionDao implements VersionDao {

	@Autowired
	private Map<String,String> queryStore;
	
	@Autowired
	RecordingService recordingService;
	
	@Lazy
	@Autowired
	SongService songService;

	@Autowired
	UserService userService;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public int createNewVersion(int id, String createdBy, String body, String defaultKey)  {
		jdbcTemplate.update(queryStore.get("createNewVersionForSong"),new Object[] {body,defaultKey,createdBy,createdBy,id,id});
		Song song =  jdbcTemplate.query(queryStore.get("getLatestSongId"), r -> {
			return songService.buildSongFromResultSet(r);
		});
		jdbcTemplate.update(queryStore.get("createNewVersion"),new Object[] {song.getId(),song.getRelated()});
		return song.getId();
	}	
	@Override
	public void changeMaster(int songId, int relatedId)  {
		jdbcTemplate.update(queryStore.get("swapMasterOfVersionByRelated"),new Object[] {songId,relatedId});
		jdbcTemplate.update(queryStore.get("nullifyRelatedBySongId"),new Object[] {songId});
		jdbcTemplate.update(queryStore.get("swapMasterOfVersionById"),new Object[] {songId,relatedId});
		jdbcTemplate.update(queryStore.get("swapVersionById"),new Object[] {relatedId,songId});
	}

	@Override
	public List<VersionSong> getVersionsByRelatedId(int id) {
		return jdbcTemplate.query(queryStore.get("getSongVersionsByRelatedIdExcludePrivate"), new Object[] {id}, r -> {
			List<VersionSong> versionSongs = new ArrayList<VersionSong>();
			while (r.next()) {
				versionSongs.add(new VersionSong(r.getInt("VERSION_ID"),
						r.getString("VERSION"),
						r.getInt("ID"),
						r.getString("NAME"),
						new TransposableString(r.getString("BODY"),NUMBER_SYSTEM_KEY_CODE),
						r.getString("DEFAULT_KEY"),
						r.getString("ARTIST"),
						r.getString("NOTES"),
						userService.loadCfUserByUsername(r.getString("CREATED_BY")),
						userService.loadCfUserByUsername(r.getString("MODIFIED_BY")),
						r.getTimestamp("CHANGED_ON"),
						r.getInt("RELATED"),
						"Y".equals(r.getString("PRIVATE"))? true: false,	
						r.getInt("CATEGORY"),
						recordingService.getRecordingBySongId(r.getInt("ID"))));
			}
			return versionSongs;
		});
	}	
	
	@Override
	public VersionSong getVersionById(int id) {
		return jdbcTemplate.query(queryStore.get("getSongVersionById"), new Object[] {id}, r -> {
			r.next();
				return new VersionSong(r.getInt("VERSION_ID"),
						r.getString("VERSION"),
						r.getInt("ID"),
						r.getString("NAME"),
						new TransposableString(r.getString("BODY"),NUMBER_SYSTEM_KEY_CODE),
						r.getString("DEFAULT_KEY"),
						r.getString("ARTIST"),
						r.getString("NOTES"),
						userService.loadCfUserByUsername(r.getString("CREATED_BY")),
						userService.loadCfUserByUsername(r.getString("MODIFIED_BY")),
						r.getTimestamp("CHANGED_ON"),
						r.getInt("RELATED"),
						"Y".equals(r.getString("PRIVATE"))? true: false,	
						r.getInt("CATEGORY"),
						recordingService.getRecordingBySongId(r.getInt("ID")));
		});
	}	
	
	@Override
	public SetListVersionSong getSetListVersionSongById(int id) {
		return jdbcTemplate.query(queryStore.get("getSetListVersionById"), new Object[] {id}, r -> {
			r.next();
				return new SetListVersionSong(
						r.getInt("VERSION_ID"),
						r.getString("VERSION"),
						r.getInt("SET_SONG_ID"),
						r.getInt("SETLIST_ID"),
						r.getInt("SORT_ORDER"),
						r.getInt("ID"),
						r.getString("NAME"),
						new TransposableString(r.getString("BODY"),NUMBER_SYSTEM_KEY_CODE),
						r.getString("DEFAULT_KEY"),
						r.getString("ARTIST"),
						r.getString("NOTES"),
						userService.loadCfUserByUsername(r.getString("CREATED_BY")),
						userService.loadCfUserByUsername(r.getString("MODIFIED_BY")),
						r.getTimestamp("CHANGED_ON"),
						r.getInt("RELATED"),
						"Y".equals(r.getString("PRIVATE"))? true: false,	
						r.getInt("CATEGORY"),
						recordingService.getRecordingBySongId(r.getInt("ID")));
		});
	}	

	
	@Override
	public List<Song> getAllRelatedSongs(int id){
		List<Song> versions = new ArrayList<Song>();
		Song song = songService.getSongById(id);
		if (song.getRelated() == 0) {
			versions.add(new VersionSong(0,"**",song));
			versions.addAll(((MasterSong)song).getVersions());
		} else {
			song = songService.getSongById(song.getRelated());
			versions.add(new VersionSong(0,"**",song));
			versions.addAll(((MasterSong)song).getVersions());
		}
		return versions;
	}
}
