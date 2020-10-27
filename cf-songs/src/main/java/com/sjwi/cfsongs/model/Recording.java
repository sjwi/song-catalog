package com.sjwi.cfsongs.model;

import java.io.InputStream;

public class Recording {
	private final int id;
	private final String path;
	private final String extension;
	private final InputStream file;

	public Recording(int id, String path, String ext) {
		this.path = path;
		this.extension = ext;
		this.id = id;
		this.file = null;
	}
	public Recording(int id, String path, String ext, InputStream file) {
		this.path = path;
		this.extension = ext;
		this.id = id;
		this.file = file;
	}
	public int getId() {
		return id;
	}
	public String getPath() {
		return path;
	}
	public String getExtension() {
		return extension;
	}
	public InputStream getFileInputStream() {
		return file;
	}
}
