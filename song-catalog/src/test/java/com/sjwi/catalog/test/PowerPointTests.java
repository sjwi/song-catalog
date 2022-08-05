/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.test;

import com.sjwi.catalog.exception.FileUtilityException;
import com.sjwi.catalog.file.FileGenerator;
import com.sjwi.catalog.file.pdf.PdfFileGenerator;
import com.sjwi.catalog.file.ppt.PptFileGenerator;
import com.sjwi.catalog.model.SetList;
import com.sjwi.catalog.model.api.setlist.NewSetList;
import com.sjwi.catalog.service.SetListService;
import com.sjwi.catalog.service.SongService;
import com.sjwi.catalog.test.config.SpringTestConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@EnableAutoConfiguration
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringTestConfiguration.class})
public class PowerPointTests {

  @Autowired SongService songService;

  @Autowired SetListService setListService;

  @Before
  public void setUp() {}

  @Test
  public void generateMasterPowerPoint() throws FileUtilityException, InterruptedException {
    NewSetList newSet = new NewSetList();
    newSet.setUnit(1);
    newSet.setSubUnit(1);
    SetList setList = setListService.createSet("Master SetList", newSet, "sjwi");
    songService.getSongs().stream()
        .forEach(
            s -> setListService.addSongToSet(s.getId(), setList.getId(), s.getDefaultKey(), 0));
    FileGenerator fileGenerator = new PptFileGenerator(false, 0, false, false);
    fileGenerator.buildFile(setListService.getLatestSet().transposeToLyrics());
    fileGenerator = new PdfFileGenerator(0, null);
    fileGenerator.buildFile(setListService.getLatestSet().transposeToLyrics());
    fileGenerator = new PdfFileGenerator(0, null);
    fileGenerator.buildFile(setListService.getLatestSet());
    fileGenerator.removeFile();
  }
}
