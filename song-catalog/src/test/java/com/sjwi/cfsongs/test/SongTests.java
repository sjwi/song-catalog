package com.sjwi.cfsongs.test;

import static com.sjwi.catalog.model.KeySet.LYRICS_ONLY_KEY_CODE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sjwi.catalog.model.TransposableString;
import com.sjwi.catalog.service.SetListService;
import com.sjwi.catalog.service.SongService;
import com.sjwi.cfsongs.test.config.SpringTestConfiguration;

@EnableAutoConfiguration
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringTestConfiguration.class})		
public class SongTests {
	
	@Autowired
	SongService songService;
	
	@Autowired
	SetListService setListService;
	
	@Before
	public void setUp() {
	}

	@Test
	public void whenSongBodyTransposedToDefaultKey_IsEqualToDefaultBody() {
		songService.getSongs().stream().forEach(s -> {
			assertEquals(s.getBody(), s.transpose(s.getDefaultKey()).getBody());
		});
	}

	@Test
	public void whenSongBodyTransposedToNumberSystem_IsEqualToDatabaseString() {
		songService.getSongs().stream().forEach(s -> {
			assertEquals(s.getTransposableString().getSourceString().trim(), s.transpose("NumSys").getBody().trim());
		});
	}

	@Test
	public void whenBodyTransposedToLyrics_StringIsNotTransposable() {
		songService.getSongs().stream().forEach(s -> {
			assertFalse(new TransposableString(s.transpose(LYRICS_ONLY_KEY_CODE).getBody(),"G").isStringIsTransposable());
		});
	}

	@Test
	public void whenSongAddedToSet_FrequencyIsIncrimentedByOne() {
		final int id = 13;
		int initialFrequency = songService.getSongById(id).getFreq();
		setListService.addSongToSet(13, setListService.getLatestSet().getId(), LYRICS_ONLY_KEY_CODE, 0);
		assertEquals(initialFrequency + 1, songService.getSongById(id).getFreq());
	}
}