package com.acquire.actions;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.acquire.board.Board;
import com.acquire.board.Chain;
import com.acquire.board.Hotel;

@RunWith (PowerMockRunner.class)
@PrepareForTest({Chain.class, Board.class, Hotel.class})
public class IAcquireActionsTest {
	AcquireActions acquireActions;
	String row = "A";
	String column = "1";
	@Mock
	Hotel hotel;
	
	public IAcquireActionsTest() {
		acquireActions = new IAcquireActions();
		PowerMockito.mockStatic(Chain.class);
	}
	
	@Test
	public void falseSingleton() {
		Board board = Mockito.mock(Board.class);
		Hotel hotel = Mockito.mock(Hotel.class);
		Map<String, Hotel> list = Mockito.mock(HashMap.class);
		list.put("1B", new Hotel());
		Mockito.when(board.getBoard()).thenReturn(list);
		Mockito.when(list.get("1B")).thenReturn(hotel);
		Mockito.when(hotel.getLabel()).thenReturn("singleton");
		
		assertFalse(acquireActions.singleton(board, row, column));
	}
	
	@Test
	public void trueSingleton() {
		Board board = Mockito.mock(Board.class);
		Map<String, Hotel> list = Mockito.mock(HashMap.class);
		Mockito.when(board.getBoard()).thenReturn(list);
		assertNotNull(acquireActions.inspect(board, row, column));
	}
	
	@Test
	public void trueFounding() throws Exception {
		Board board = Mockito.mock(Board.class);
		Hotel hotel = Mockito.mock(Hotel.class);
		Map<String, Hotel> list = Mockito.mock(HashMap.class);
		list.put("1B", new Hotel());
		Mockito.when(board.getBoard()).thenReturn(list);
		Mockito.when(list.get("1B")).thenReturn(hotel);
		Mockito.when(hotel.getLabel()).thenReturn("singleton");
		PowerMockito.doNothing().when(Chain.class, "setChain", Mockito.anyString(), Mockito.anyString());
		assertTrue(acquireActions.founding(board, row, column, "MockLabel"));
	}
	
	@Test
	public void falseFounding() {
		Board board = Mockito.mock(Board.class);
		Hotel hotel = Mockito.mock(Hotel.class);
		Map<String, Hotel> list = Mockito.mock(HashMap.class);
		list.put("1B", new Hotel());
		Mockito.when(board.getBoard()).thenReturn(list);
		Mockito.when(list.get("1B")).thenReturn(hotel);
		Mockito.when(hotel.getLabel()).thenReturn("MockHotel");
		assertFalse(acquireActions.founding(board, row, column, "MockLabel"));
	}
	
	@Test
	public void trueGrowing() throws Exception {
		Board board = Mockito.mock(Board.class);
		Hotel hotel = Mockito.mock(Hotel.class);
		Map<String, Hotel> list = Mockito.mock(HashMap.class);
		list.put("1B", new Hotel());
		Mockito.when(board.getBoard()).thenReturn(list);
		Mockito.when(list.get("1B")).thenReturn(hotel);
		Mockito.when(hotel.getLabel()).thenReturn("MockHotel");
		PowerMockito.doNothing().when(Chain.class, "setChain", Mockito.anyString(), Mockito.anyString());
		assertNotNull(acquireActions.growing(board, row, column));
	}
	
	@Test
	public void falseGrowing() {
		Board board = Mockito.mock(Board.class);
		Hotel hotel = Mockito.mock(Hotel.class);
		Map<String, Hotel> list = Mockito.mock(HashMap.class);
		list.put("1B", new Hotel());
		Mockito.when(board.getBoard()).thenReturn(list);
		Mockito.when(list.get("1B")).thenReturn(hotel);
		Mockito.when(hotel.getLabel()).thenReturn("singleton");
		
		assertNull(acquireActions.growing(board, row, column));
	}
	
	@Test
	public void trueMerging() throws Exception {
		Board board = Mockito.mock(Board.class);
		Hotel hotel1 = Mockito.mock(Hotel.class);
		Hotel hotel2 = Mockito.mock(Hotel.class);
		Hotel hotel3 = Mockito.mock(Hotel.class);
		Hotel hotel4 = Mockito.mock(Hotel.class);
		Map<String, Hotel> list = Mockito.mock(HashMap.class);
		list.put("1B", new Hotel());
		list.put("2C", new Hotel());
		list.put("2A", new Hotel());
		list.put("3B", new Hotel());
		Mockito.when(board.getBoard()).thenReturn(list);
		Mockito.when(list.get("1B")).thenReturn(hotel1);
		Mockito.when(hotel1.getLabel()).thenReturn("singleton");
		Mockito.when(list.get("3B")).thenReturn(hotel3);
		Mockito.when(hotel3.getLabel()).thenReturn("singleton");
		Mockito.when(list.get("2A")).thenReturn(hotel2);
		Mockito.when(hotel2.getLabel()).thenReturn("MockHotel");
		Mockito.when(list.get("2C")).thenReturn(hotel4);
		Mockito.when(hotel4.getLabel()).thenReturn("MockHotel2");
		
		PowerMockito.doNothing().when(Chain.class, "setChain", Mockito.anyString(), Mockito.anyString());
		assertNotNull(acquireActions.merging(board, "B", "2", "MockLabel"));
	}
	
	@Test
	public void falseMerging() {
		Board board = Mockito.mock(Board.class);
		Hotel hotel = Mockito.mock(Hotel.class);
		Map<String, Hotel> list = Mockito.mock(HashMap.class);
		list.put("1B", new Hotel());
		list.put("1D", Mockito.mock(Hotel.class));
		Mockito.when(board.getBoard()).thenReturn(list);
		Mockito.when(list.get("1B")).thenReturn(hotel);
		Mockito.when(hotel.getLabel()).thenReturn("singleton");
		Mockito.when(list.get("1D")).thenReturn(hotel);
		Mockito.when(hotel.getLabel()).thenReturn("singleton");
		assertNull(acquireActions.merging(board, row, column, "MockHotel"));
	}
}
