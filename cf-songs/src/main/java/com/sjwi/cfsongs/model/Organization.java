package com.sjwi.cfsongs.model;

public class Organization {
	
	private int id;
	private String name;
	private String title;
	public Organization(int id, String name, String title) {
		super();
		this.id = id;
		this.name = name;
		this.title = title;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getTitle() {
		return title;
	}
	

}
