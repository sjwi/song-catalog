package com.sjwi.catalog.model.song;

import java.util.Date;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sjwi.catalog.model.Recording;
import com.sjwi.catalog.model.TransposableString;
import com.sjwi.catalog.model.user.CfUser;

public class MasterSong extends Song {

	private final List<VersionSong> versions;

	public MasterSong(List<VersionSong> versions, int id, String name, TransposableString transposableString, String defaultKey, String artist, String notes,
			CfUser createdBy, CfUser modifiedBy, Date changedOn, int related, boolean priv, int category,
			Recording recording) {
		super(id, name, transposableString, defaultKey, artist, notes, createdBy, modifiedBy, changedOn, related, priv, category, recording);
		this.versions = versions;
	}
	
	public MasterSong(List<VersionSong> versions, Song song) {
		super(song.getId(),song.getName(),song.getTransposableString(),song.getDefaultKey(),
				song.getArtist(),song.getNotes(),song.getCreatedBy(),song.getModifiedBy(),
				song.getChangedOn(),song.getRelated(),song.isPriv(),song.getCategory(),song.getRecording());
		this.versions = versions;
	}
	
	@Override
	public MasterSong transpose(String targetTranspositionKey) {
		return new MasterSong(versions, id, name, transposableString,targetTranspositionKey,artist,notes,createdBy,modifiedBy,changedOn,related,priv,category,recording);
	}
	
	public List<VersionSong> getVersions() {
		return versions;
	}
	
	public String getVersionsAsJson() {
		JsonArray jsonVersions = new JsonArray();
		versions.stream().forEach(v -> {
			JsonObject version = new JsonObject();
			version.addProperty("songId", v.getId());
			version.addProperty("name", v.getVersionName());
			jsonVersions.add(version);
		});
		return jsonVersions.toString();
	}
}
