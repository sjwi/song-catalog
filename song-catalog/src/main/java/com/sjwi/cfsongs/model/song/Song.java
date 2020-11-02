package com.sjwi.cfsongs.model.song;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import com.sjwi.cfsongs.model.Recording;
import com.sjwi.cfsongs.model.TransposableString;

public abstract class Song {
	
	protected final int id;
	protected final String name;
	protected final TransposableString transposableString;
	protected final String body;
	protected final int freq;
	protected final String defaultKey;
	protected final String artist;
	protected final String notes;
	protected final String createdBy;
	protected final String modifiedBy;
	protected final Date changedOn;
	protected final int related;
	protected final boolean priv;
	protected final int category;
	protected final Recording recording;
	
	public Song(int id, String name, TransposableString transposableString, int freq, String defaultKey, String artist, String notes,
			String createdBy, String modifiedBy, Date changedOn, int related, boolean priv, int category,
			Recording recording) {
		this.id = id;
		this.name = name;
		this.transposableString = transposableString;
		this.freq = freq;
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
		String arr = Arrays.stream(body.split("\n"))
				.map(s -> {
					if (s.trim().isEmpty()) {
						return s.trim();
					} else {
						return s;
					}
				})
				.collect(Collectors.joining("\n"));
		return arr.split("[\n]{2,}");
	}

	public int getFreq() {
		return freq;
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
	public String getCreatedBy() {
		return createdBy;
	}
	public String getModifiedBy() {
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
