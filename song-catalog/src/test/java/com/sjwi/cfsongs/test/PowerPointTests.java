package com.sjwi.cfsongs.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sjwi.catalog.exception.FileUtilityException;
import com.sjwi.catalog.file.FileGenerator;
import com.sjwi.catalog.file.pdf.PdfFileGenerator;
import com.sjwi.catalog.file.ppt.PptFileGenerator;
import com.sjwi.catalog.service.SetListService;
import com.sjwi.catalog.service.SongService;
import com.sjwi.cfsongs.test.config.SpringTestConfiguration;

@EnableAutoConfiguration
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringTestConfiguration.class})		
public class PowerPointTests {
	
	@Autowired
	SongService songService;
	
	@Autowired
	SetListService setListService;
	
	@Before
	public void setUp() {
	}

	@Test
	public void generateMasterPowerPoint() throws FileUtilityException, InterruptedException {
		int setListId = setListService.createSet("Master SetList", "sjwi", 1);
		songService.getSongs().stream().forEach(s -> setListService.addSongToSet(s.getId(), setListId, s.getDefaultKey(), 0 ));
		FileGenerator fileGenerator = new PptFileGenerator(false,0,"C:/");
		fileGenerator.buildFile(setListService.getLatestSet().transposeToLyrics());
		fileGenerator = new PdfFileGenerator(0,null);
		fileGenerator.buildFile(setListService.getLatestSet().transposeToLyrics());
		fileGenerator = new PdfFileGenerator(0,null);
		fileGenerator.buildFile(setListService.getLatestSet());
//		fileGenerator.removeFile();
	}
}