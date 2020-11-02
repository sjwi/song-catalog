package com.sjwi.cfsongs.model;

import static com.sjwi.cfsongs.model.KeySet.LYRICS_ONLY_KEY_CODE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.sjwi.cfsongs.model.song.Song;

public class SetList {
	private final int id;
	private final String setListName;
	private final Date createdOn;
	private final Date lastModifiedOn;
	private final String createdBy;
	private final List<Song> songs;
	
	public SetList(int id, String setListName, Date createdOn, Date lastModifiedOn, String createdBy, List<Song> songs) {
		super();
		this.id = id;
		this.setListName = setListName;
		this.createdOn = createdOn;
		this.lastModifiedOn = lastModifiedOn;
		this.createdBy = createdBy;
		this.songs = songs;
	}
	public SetList transposeToLyrics() {
		return new SetList(id,setListName,createdOn,lastModifiedOn,createdBy,songs.stream()
				.map(s -> s.transpose(LYRICS_ONLY_KEY_CODE))
				.collect(Collectors.toList()));
	}
	public SetList transpose(List<String> keys) {
		List<Song> transposedSongs = new ArrayList<Song>();
		if(keys != null && keys.size() == songs.size()) {
			for (int i = 0; i < songs.size(); i++){
				transposedSongs.add(songs.get(i).transpose(keys.get(i)));
			}
		}
		return new SetList(id,setListName,createdOn,lastModifiedOn,createdBy,transposedSongs.isEmpty() ? songs : transposedSongs);
	}
	public int getId() {
		return id;
	}
	public String getSetListName() {
		return setListName;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public Date getLastModifiedOn() {
		return lastModifiedOn;
	}
	public List<Song> getSongs(){
		return songs;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public String getNormalizedSetListName() {
		return setListName.replace("/", "_").replace("\\", "_").replace(";", ".");
	}
	
}
