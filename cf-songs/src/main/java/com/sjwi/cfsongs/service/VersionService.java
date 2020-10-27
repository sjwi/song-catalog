package com.sjwi.cfsongs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sjwi.cfsongs.dao.VersionDao;
import com.sjwi.cfsongs.model.song.Song;
import com.sjwi.cfsongs.model.song.VersionSong;

@Component
public class VersionService {

	@Autowired
	VersionDao versionDao;

	public int createNewVersion(int songId, String user, String transposedSongBody, String key) {
		return versionDao.createNewVersion(songId, user, transposedSongBody, key);
	}

	public List<Song> getAllRelatedSongs(int id) {
		return versionDao.getAllRelatedSongs(id);
	}

	public VersionSong getVersionById(int id) {
		return (VersionSong) versionDao.getVersionById(id);
	}

	public void changeMaster(int newId, int id) {
		versionDao.changeMaster(newId, id);
	}

	public Song getSetListVersionSongById(int id) {
		return versionDao.getSetListVersionSongById(id);
	}

	public List<VersionSong> getVersionsByRelatedId(int id) {
		return versionDao.getVersionsByRelatedId(id);
	}
}
