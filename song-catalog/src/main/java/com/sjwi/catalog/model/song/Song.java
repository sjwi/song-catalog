package com.sjwi.catalog.model.song;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import com.sjwi.catalog.model.Recording;
import com.sjwi.catalog.model.TransposableString;
import com.sjwi.catalog.model.user.CfUser;

public abstract class Song {
	
	protected final int id;
	protected final String name;
	protected final TransposableString transposableString;
	protected final String body;
	protected final String defaultKey;
	protected final String artist;
	protected final String notes;
	protected final CfUser createdBy;
	protected final CfUser modifiedBy;
	protected final Date changedOn;
	protected final int related;
	protected final boolean priv;
	protected final int category;
	protected final Recording recording;
	
	public Song(int id, String name, TransposableString transposableString, String defaultKey, String artist, String notes,
			CfUser createdBy, CfUser modifiedBy, Date changedOn, int related, boolean priv, int category,
			Recording recording) {
		this.id = id;
		this.name = name;
		this.transposableString = transposableString;
		this.defaultKey = defaultKey;
		this.artist = artist;
		this.notes = notes;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.changedOn = changedOn;
		this.related = related;
		this.priv = priv;
		this.recording = recording;
		this.category = category;
		this.body = transposableString.getTransposedString(defaultKey);
}
	
	public abstract Song transpose(String targetTranspositionKey); 
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getBody() {
		return body;
	}
	public String[] getBodyAsChunks() {
		String[] chunks = Arrays.stream(body.split("\n"))
				.map(s -> s.trim().isEmpty()? s.trim(): s) 
				.collect(Collectors.joining("\n"))
				.split("[\n]{2,}");
		//filter CCLI footer
		return Arrays.stream(chunks)
			.filter(c -> !c.startsWith("CCLI"))
			.toArray(String[]::new);
	}

	public String getDefaultKey() {
		return defaultKey;
	}
	public String getArtist() {
		return artist;
	}
	public String getNotes() {
		return notes;
	}
	public CfUser getCreatedBy() {
		return createdBy;
	}
	public CfUser getModifiedBy() {
		return modifiedBy;
	}
	public Date getChangedOn() {
		return changedOn;
	}
	public int getRelated() {
		return related;
	}
	public boolean isPriv() {
		return priv;
	}
	public int getCategory() {
		return category;
	}
	public Recording getRecording() {
		return recording;
	}
	public String getNormalizedName() {
		return name.replace("/", "_").replace("\"", "_").replace(";", ".");
	}
	public TransposableString getTransposableString() {
		return transposableString;
	}
}
