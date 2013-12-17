package com.acquire.board;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
@RunWith(PowerMockRunner.class)
@PrepareForTest({Hotel.class})
public class HotelTest {

	Hotel hotel;
	
	public HotelTest(){
		hotel = new Hotel();
	}
	@Test
	public void testLabel() {
		hotel.setLabel("American");
		Assert.assertEquals("American",hotel.getLabel());
	}

	@Test
	public void testShare() {
		hotel.setShare(25);
		Assert.assertEquals(25,hotel.getShare());
	}
	
	@Test
	public void testGetLabels() {
		Assert.assertNotNull(hotel.getLabels());
	}
}
