package com.acquire.admin;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.acquire.actions.AcquireActions;
import com.acquire.actions.IAcquireActions;
import com.acquire.board.Board;
import com.acquire.board.Chain;
import com.acquire.board.Game;
import com.acquire.board.Tile;
import com.acquire.factory.AcquireActionsFactory;
import com.acquire.factory.BoardFactory;
import com.acquire.player.Player;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Board.class, Game.class, Player.class, BoardFactory.class,
		Random.class, AcquireActions.class, AcquireActionsFactory.class })
public class IAdministratorTest {
	Administrator admin;

	@Before
	public void init() {
		admin = IAdministrator.getInstance();
	}

	@Test
	public void startGameTestSuccess() throws Exception {
		Random random = Mockito.mock(Random.class);
		PowerMockito.whenNew(Random.class).withNoArguments().thenReturn(random);
		Game game = Mockito.mock(Game.class);
		List<Player> players = new ArrayList<Player>();
		Player player = Mockito.mock(Player.class);
		players.add(player);

		Board board = Mockito.mock(Board.class);
		PowerMockito.mockStatic(BoardFactory.class);
		PowerMockito.doReturn(board).when(BoardFactory.class, "getBoard");
		List<String> row = new ArrayList<>();
		row.add("A");
		row.add("B");
		row.add("C");
		row.add("D");
		List<String> col = new ArrayList<>();
		col.add("1");
		col.add("2");
		col.add("3");
		col.add("4");
		Mockito.when(board.getColumns()).thenReturn(col);
		Mockito.when(board.getRows()).thenReturn(row);
		PowerMockito.doNothing().when(player)
				.setTile(Mockito.anyString(), Mockito.anyString());
		assertNotNull(admin.startGame(game, players));
	}

	@Test
	public void pickTilesSuccess() throws Exception {
		Random random = Mockito.mock(Random.class);
		PowerMockito.whenNew(Random.class).withNoArguments().thenReturn(random);
		Game game = Mockito.mock(Game.class);
		List<Player> players = new ArrayList<Player>();
		Player player = Mockito.mock(Player.class);
		players.add(player);

		Board board = Mockito.mock(Board.class);
		PowerMockito.mockStatic(BoardFactory.class);
		PowerMockito.doReturn(board).when(BoardFactory.class, "getBoard");
		List<String> row = new ArrayList<>();
		row.add("A");
		row.add("B");
		row.add("C");
		row.add("D");
		List<String> col = new ArrayList<>();
		col.add("1");
		col.add("2");
		col.add("3");
		col.add("4");
		Mockito.when(board.getColumns()).thenReturn(col);
		Mockito.when(board.getRows()).thenReturn(row);
		PowerMockito.doNothing().when(player)
				.setTile(Mockito.anyString(), Mockito.anyString());
		admin.startGame(game, players);
		assertNotNull(admin.pickTiles());
		assertNotNull(admin.pickTiles());
	}
	
	@Test 
	public void pickTilesFail() throws Exception {
		Random random = Mockito.mock(Random.class);
		PowerMockito.whenNew(Random.class).withNoArguments().thenReturn(random);
		Game game = Mockito.mock(Game.class);
		List<Player> players = new ArrayList<Player>();
		Player player = Mockito.mock(Player.class);
		players.add(player);

		Board board = Mockito.mock(Board.class);
		PowerMockito.mockStatic(BoardFactory.class);
		PowerMockito.doReturn(board).when(BoardFactory.class, "getBoard");
		List<String> row = new ArrayList<>();
		row.add("A");
		row.add("B");
		row.add("C");
		List<String> col = new ArrayList<>();
		col.add("1");
		col.add("2");
		Mockito.when(board.getColumns()).thenReturn(col);
		Mockito.when(board.getRows()).thenReturn(row);
		PowerMockito.doNothing().when(player)
				.setTile(Mockito.anyString(), Mockito.anyString());
		admin.startGame(game, players);
		assertNull(admin.pickTiles());
		
	}

	
	@Test
	public void setTilesSuccess() throws Exception {
		Board board = BoardFactory.getBoard();
		board.createBoard();
		Player player = new Player();
		player.setTile("A", "1");
		player.setTile("B", "1");
		Tile tile = new Tile();
		tile.setRow("A");
		tile.setColumn("1");

		AcquireActions acquireActions = Mockito.mock(AcquireActions.class);
		PowerMockito.mockStatic(AcquireActionsFactory.class);
		PowerMockito.when(AcquireActionsFactory.getInstance()).thenReturn(
				acquireActions);
		Mockito.when(acquireActions.inspect(board, tile.getRow(), tile.getColumn()))
				.thenReturn("singleton");
		Mockito.when(acquireActions.singleton(board, tile.getRow(),
		tile.getColumn())).thenReturn(true);
		admin.setTile(board, player, tile, "MockHotel");
		assertEquals(player.getTile().size(), 1);
	}
	
	@Test
	public void doneTest() throws Exception {
//		Board board = BoardFactory.getBoard();
//		board.createBoard();
		
		Board board = Mockito.mock(Board.class);
		PowerMockito.mockStatic(BoardFactory.class);
		PowerMockito.doReturn(board).when(BoardFactory.class, "getBoard");
		List<String> row = new ArrayList<>();
		row.add("A");
		row.add("B");
		row.add("C");
		row.add("D");
		List<String> col = new ArrayList<>();
		col.add("1");
		col.add("2");
		col.add("3");
		col.add("4");
		Mockito.when(board.getColumns()).thenReturn(col);
		Mockito.when(board.getRows()).thenReturn(row);
		
		Player player = new Player();
		player.setTile("A", "1");
		player.setTile("B", "1");
		Tile tile = new Tile();
		tile.setRow("A");
		tile.setColumn("1");

		AcquireActions acquireActions = Mockito.mock(AcquireActions.class);
		PowerMockito.mockStatic(AcquireActionsFactory.class);
		PowerMockito.when(AcquireActionsFactory.getInstance()).thenReturn(
				acquireActions);
		Mockito.when(acquireActions.inspect(board, tile.getRow(), tile.getColumn()))
				.thenReturn("singleton");
		Mockito.when(acquireActions.singleton(board, tile.getRow(),
		tile.getColumn())).thenReturn(true);
		admin.setCurrentPlayer(player);
		Game game = Mockito.mock(Game.class);
		PowerMockito.mockStatic(Game.class);
		
		PowerMockito.when(Game.getInstance()).thenReturn(game);
		PowerMockito.when(game.getGame(board)).thenReturn(Arrays.asList(player));
		
		admin.initTiles(new Random(), new ArrayList<String>());
		admin.setAllTiles(board);
		
		admin.startGame(game, Arrays.asList(player));
		admin.done(player, board);
		assertSame(admin.getCurrentPlayer(), player);
		
	}
}
