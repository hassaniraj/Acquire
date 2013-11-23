package com.acquire.board;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Tile.class})
public class BoardTest {
	Board b;
	public BoardTest() {
		b = Board.getInstance();
	}
	
	@Test
	public void createBoardSuccess() throws Exception {
		Tile tile = Mockito.mock(Tile.class);
		PowerMockito.whenNew(Tile.class).withNoArguments().thenReturn(tile);
		assertNotNull(b.createBoard());
		
	}
	
	@Test
	public void getOccupiedTilesSuccess() {
		assertNotNull(b.getOccupiedTiles());
	}
	
	@Test
	public void getHotelTilesSuccess() {
		assertNotNull(b.getHotelTiles());
	}
	
	
}
