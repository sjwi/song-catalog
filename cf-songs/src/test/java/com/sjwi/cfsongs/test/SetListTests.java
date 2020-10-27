package com.sjwi.cfsongs.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sjwi.cfsongs.service.SetListService;
import com.sjwi.cfsongs.test.config.SpringTestConfiguration;

@EnableAutoConfiguration
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringTestConfiguration.class})		
public class SetListTests {
	
	@Autowired
	SetListService setListService;

	@Test
	public void whenXNumberOfSetsRequested_SizeOfSetsListIsX() {
		final int requestSize = 20;
		assertEquals(requestSize, setListService.getSetLists(requestSize).size());
	}
}