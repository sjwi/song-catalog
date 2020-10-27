package com.sjwi.cfsongs.model.song;

import java.util.Date;

import com.sjwi.cfsongs.model.Recording;
import com.sjwi.cfsongs.model.TransposableString;

public class VersionSong extends Song {

	private final int versionId;
	private final String versionName;

	public VersionSong(int versionId, String versionName, int id, String name, TransposableString transposableString, int freq, String defaultKey, String artist, String notes,
			String createdBy, String modifiedBy, Date changedOn, int related, boolean priv, int category,
			Recording recording) {
		super(id, name, transposableString, freq, defaultKey, artist, notes, createdBy, modifiedBy, changedOn, related, priv, category, recording);
		this.versionId = versionId;
		this.versionName = versionName;
	}
	
	public VersionSong(int versionId, String versionName, Song song) {
		super(song.getId(),song.getName(),song.getTransposableString(),song.getFreq(),song.getDefaultKey(),
				song.getArtist(),song.getNotes(),song.getCreatedBy(),song.getModifiedBy(),
				song.getChangedOn(),song.getRelated(),song.isPriv(),song.getCategory(),song.getRecording());
		this.versionId = versionId;
		this.versionName = versionName;
	}
	@Override
	public VersionSong transpose(String targetTranspositionKey) {
		return new VersionSong(versionId, versionName, id, name,transposableString,freq,targetTranspositionKey,artist,notes,createdBy,modifiedBy,changedOn,related,priv,category,recording);
	}
	public int getVersionId() {
		return versionId;
	}
	public String getVersionName() {
		return versionName;
	}

}
