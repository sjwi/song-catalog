package com.sjwi.cfsongs.model.song;

import java.util.Date;

import com.sjwi.cfsongs.model.Recording;
import com.sjwi.cfsongs.model.TransposableString;

public class SetListVersionSong extends SetListSong {

	private final int versionId;
	private final String versionName;

	public SetListVersionSong(int versionId, String versionName, int setListSongId, int setListId, int sortOrder, int id, String name, TransposableString transposableString, int freq, String defaultKey, String artist, String notes,
			String createdBy, String modifiedBy, Date changedOn, int related, boolean priv, int category,
			Recording recording) {
		super(setListSongId,setListId,sortOrder,id, name, transposableString, freq, defaultKey, artist, notes, createdBy, modifiedBy, changedOn, related, priv, category, recording);
		this.versionId = versionId;
		this.versionName = versionName;
	}
	
	@Override
	public Song transpose(String targetTranspositionKey) {
		return new SetListVersionSong(versionId, versionName, setListSongId, setListId, sortOrder, id, name,
				transposableString,
				freq,targetTranspositionKey,artist,notes,createdBy,modifiedBy,changedOn,related,priv,category,recording);
	}
	
	public int getVersionId() {
		return versionId;
	}
	public String getVersionName() {
		return versionName;
	}
}
