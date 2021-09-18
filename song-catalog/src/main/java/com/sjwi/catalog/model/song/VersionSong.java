package com.sjwi.catalog.model.song;

import java.util.Date;

import com.sjwi.catalog.model.Recording;
import com.sjwi.catalog.model.TransposableString;
import com.sjwi.catalog.model.user.CfUser;

public class VersionSong extends Song {

	private final int versionId;
	private final String versionName;

	public VersionSong(int versionId, String versionName, int id, String name, TransposableString transposableString, String defaultKey, String artist, String notes,
			CfUser createdBy, CfUser modifiedBy, Date changedOn, int related, boolean priv, int category,
			Recording recording) {
		super(id, name, transposableString, defaultKey, artist, notes, createdBy, modifiedBy, changedOn, related, priv, category, recording);
		this.versionId = versionId;
		this.versionName = versionName;
	}
	
	public VersionSong(int versionId, String versionName, Song song) {
		super(song.getId(),song.getName(),song.getTransposableString(),song.getDefaultKey(),
				song.getArtist(),song.getNotes(),song.getCreatedBy(),song.getModifiedBy(),
				song.getChangedOn(),song.getRelated(),song.isPriv(),song.getCategory(),song.getRecording());
		this.versionId = versionId;
		this.versionName = versionName;
	}
	@Override
	public VersionSong transpose(String targetTranspositionKey) {
		return new VersionSong(versionId, versionName, id, name,transposableString,targetTranspositionKey,artist,notes,createdBy,modifiedBy,changedOn,related,priv,category,recording);
	}
	public int getVersionId() {
		return versionId;
	}
	public String getVersionName() {
		return versionName;
	}

}
