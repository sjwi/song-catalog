package com.sjwi.catalog.config;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.sjwi.catalog.model.KeySet;
import com.sjwi.catalog.service.SongService;

@Configuration
public class KeySetConfiguration {
	
	@Autowired
	private SongService songService;
	
	private static List<KeySet> keySetMaster;
	
	@PostConstruct
	public void initializeKeySet() {
		keySetMaster = songService.getKeySets();
	}

	public static List<KeySet> getKeySetMaster() {
		return keySetMaster;
	}
}