package com.sjwi.catalog.model;

public class Organization {
	
	private final int id;
	private final String name;
	private final String title;
	private final String link;
	public Organization(int id, String name, String title, String link) {
		super();
		this.id = id;
		this.name = name;
		this.title = title;
		this.link = link;
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
	public String getLink() {
		return link;
	}
}
