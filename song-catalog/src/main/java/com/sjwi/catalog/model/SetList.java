package com.sjwi.catalog.model;

import static com.sjwi.catalog.model.KeySet.LYRICS_ONLY_KEY_CODE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sjwi.catalog.model.song.Song;

public class SetList {
	private final int id;
	private final String setListName;
	private final Date createdOn;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy HH:mm:ss", timezone="US/New York")
	private final Date lastModifiedOn;
	private final Date flaggedAsMostRecentOn;
	private final String createdBy;
	private final int organization;
	private final List<Song> songs;
	
	public SetList(int id, String setListName, Date createdOn, Date lastModifiedOn, Date flaggedAsMostRecentOn, String createdBy, int organization, List<Song> songs) {
		super();
		this.id = id;
		this.setListName = setListName;
		this.createdOn = createdOn;
		this.lastModifiedOn = lastModifiedOn;
		this.flaggedAsMostRecentOn = flaggedAsMostRecentOn;
		this.createdBy = createdBy;
		this.organization = organization;
		this.songs = songs;
	}
	public SetList(String setListName, List<Song> songs){
		this.id = 0;
		this.setListName = setListName;
		this.createdOn = new Date();
		this.lastModifiedOn = new Date();
		this.flaggedAsMostRecentOn = new Date();
		this.createdBy = "automated";
		this.organization = 0;
		this.songs = songs;
	}
	public SetList transposeToLyrics() {
		return new SetList(id,setListName,createdOn,lastModifiedOn,flaggedAsMostRecentOn,createdBy,organization,songs.stream()
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
		return new SetList(id,setListName,createdOn,lastModifiedOn,flaggedAsMostRecentOn,createdBy,organization,transposedSongs.isEmpty() ? songs : transposedSongs);
	}
	public SetList setSetListName(String name){
		return new SetList(id,name == null? setListName: name,createdOn,lastModifiedOn,flaggedAsMostRecentOn,createdBy,organization,songs);
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
	public Date getFlaggedAsMostRecentOn() {
		return flaggedAsMostRecentOn;
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
	public int getOrganization() {
		return organization;
	}
	
}
