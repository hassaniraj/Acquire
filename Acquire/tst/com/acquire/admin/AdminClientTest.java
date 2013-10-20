package com.acquire.admin;

import static org.junit.Assert.*;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.acquire.board.Board;
import com.acquire.board.Chain;
import com.acquire.board.Game;
import com.acquire.board.Tile;
import com.acquire.factory.PlayerFactory;
import com.acquire.player.Player;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Player.class, PlayerFactory.class, Administrator.class,
		IAdministrator.class, Board.class, Game.class, Chain.class })
public class AdminClientTest {
	AdminClient adminClient;

	@Before
	public void init() {
		adminClient = new AdminClient();
	}

	@Test
	public void testSetup() throws Exception {
		Administrator administrator = Mockito.mock(Administrator.class);
		PowerMockito.mockStatic(IAdministrator.class);
		PowerMockito.when(IAdministrator.getInstance()).thenReturn(
				administrator);
		Player player = Mockito.mock(Player.class);
		PowerMockito.mockStatic(PlayerFactory.class);
		PowerMockito.when(PlayerFactory.getInstance()).thenReturn(player);
		Game game = Mockito.mock(Game.class);
		PowerMockito.mockStatic(Game.class);
		PowerMockito.when(Game.getInstance()).thenReturn(game);
		Board board = Mockito.mock(Board.class);
		Mockito.when(
				administrator.startGame(Mockito.eq(game),
						Mockito.anyListOf(Player.class))).thenReturn(board);
		adminClient
				.start("<setup player1=\"NN\" player2=\"NN1\" player3=\"NN2\" />");
		assertSame("setup", adminClient.getCurrentStage());

	}

	@Test
	public void testPlaceSuccess() throws Exception {
		Administrator administrator = Mockito.mock(Administrator.class);
		PowerMockito.mockStatic(IAdministrator.class);
		PowerMockito.when(IAdministrator.getInstance()).thenReturn(
				administrator);
		Player player = Mockito.mock(Player.class);
		PowerMockito.mockStatic(PlayerFactory.class);
		PowerMockito.when(PlayerFactory.getInstance()).thenReturn(player);
		Game game = Mockito.mock(Game.class);
		PowerMockito.mockStatic(Game.class);
		PowerMockito.when(Game.getInstance()).thenReturn(game);
		Board board = Mockito.mock(Board.class);
		Mockito.when(
				administrator.startGame(Mockito.eq(game),
						Mockito.anyListOf(Player.class))).thenReturn(board);
		Tile tile = Mockito.mock(Tile.class);
		PowerMockito
				.doNothing()
				.when(administrator)
				.setTile(Mockito.eq(board), Mockito.eq(player),
						Mockito.eq(tile), Mockito.anyString());
		adminClient
				.start("<setup player1=\"NN\" player2=\"NN1\" player3=\"NN2\" />");
		adminClient
				.start("<place row=\"3\" column=\"A\"><state><board/><player/></state></place>");
		assertSame("place", adminClient.getCurrentStage());
	}

	@Test
	public void testPlaceFail() throws Exception {
		Administrator administrator = Mockito.mock(Administrator.class);
		PowerMockito.mockStatic(IAdministrator.class);
		PowerMockito.when(IAdministrator.getInstance()).thenReturn(
				administrator);
		Player player = Mockito.mock(Player.class);
		PowerMockito.mockStatic(PlayerFactory.class);
		PowerMockito.when(PlayerFactory.getInstance()).thenReturn(player);
		Game game = Mockito.mock(Game.class);
		PowerMockito.mockStatic(Game.class);
		PowerMockito.when(Game.getInstance()).thenReturn(game);
		Board board = Mockito.mock(Board.class);
		Mockito.when(
				administrator.startGame(Mockito.eq(game),
						Mockito.anyListOf(Player.class))).thenReturn(board);
		Tile tile = Mockito.mock(Tile.class);
		PowerMockito
				.doNothing()
				.when(administrator)
				.setTile(Mockito.eq(board), Mockito.eq(player),
						Mockito.eq(tile), Mockito.anyString());
		adminClient
				.start("<place row=\"3\" column=\"A\"><state><board/><player/></state></place>");
		assertNotSame("place", adminClient.getCurrentStage());
	}

	@Test
	public void testBuySuccess() throws Exception {
		Administrator administrator = Mockito.mock(Administrator.class);
		PowerMockito.mockStatic(IAdministrator.class);
		PowerMockito.when(IAdministrator.getInstance()).thenReturn(
				administrator);
		Player player = Mockito.mock(Player.class);
		PowerMockito.mockStatic(PlayerFactory.class);
		PowerMockito.when(PlayerFactory.getInstance()).thenReturn(player);
		Game game = Mockito.mock(Game.class);
		PowerMockito.mockStatic(Game.class);
		PowerMockito.when(Game.getInstance()).thenReturn(game);
		Board board = Mockito.mock(Board.class);
		Mockito.when(
				administrator.startGame(Mockito.eq(game),
						Mockito.anyListOf(Player.class))).thenReturn(board);
		Tile tile = Mockito.mock(Tile.class);
		PowerMockito
				.doNothing()
				.when(administrator)
				.setTile(Mockito.eq(board), Mockito.eq(player),
						Mockito.eq(tile), Mockito.anyString());
		
		PowerMockito.doNothing().when(administrator).getHotelShares(Mockito.eq(board), Mockito.eq(player),
				Mockito.anyString());
		PowerMockito.when(administrator.getCurrentPlayer()).thenReturn(player);
		PowerMockito.mockStatic(Chain.class);
		List <String> list = Mockito.mock(ArrayList.class);
		PowerMockito.when(Chain.getChain(Mockito.anyString())).thenReturn(list);
		adminClient
				.start("<setup player1=\"NN\" player2=\"NN1\" player3=\"NN2\" />");
		adminClient
				.start("<place row=\"3\" column=\"A\"><state><board/><player/></state></place>");
		adminClient
				.start("<buy share1=\"American\" share2=\"American\"><state><board></board><player></player></state></buy>");
		assertSame("buy", adminClient.getCurrentStage());
	}
	
	@Test
	public void testBuyFail() throws Exception {
		Administrator administrator = Mockito.mock(Administrator.class);
		PowerMockito.mockStatic(IAdministrator.class);
		PowerMockito.when(IAdministrator.getInstance()).thenReturn(
				administrator);
		Player player = Mockito.mock(Player.class);
		PowerMockito.mockStatic(PlayerFactory.class);
		PowerMockito.when(PlayerFactory.getInstance()).thenReturn(player);
		Game game = Mockito.mock(Game.class);
		PowerMockito.mockStatic(Game.class);
		PowerMockito.when(Game.getInstance()).thenReturn(game);
		Board board = Mockito.mock(Board.class);
		Mockito.when(
				administrator.startGame(Mockito.eq(game),
						Mockito.anyListOf(Player.class))).thenReturn(board);
		Tile tile = Mockito.mock(Tile.class);
		PowerMockito
				.doNothing()
				.when(administrator)
				.setTile(Mockito.eq(board), Mockito.eq(player),
						Mockito.eq(tile), Mockito.anyString());
		
		PowerMockito.doNothing().when(administrator).getHotelShares(Mockito.eq(board), Mockito.eq(player),
				Mockito.anyString());
		PowerMockito.when(administrator.getCurrentPlayer()).thenReturn(player);
		PowerMockito.mockStatic(Chain.class);
		List <String> list = Mockito.mock(ArrayList.class);
		PowerMockito.when(Chain.getChain(Mockito.anyString())).thenReturn(list);
		adminClient
				.start("<setup player1=\"NN\" player2=\"NN1\" player3=\"NN2\" />");
		
		adminClient
				.start("<buy share1=\"American\" share2=\"American\"><state><board></board><player></player></state></buy>");
		assertNotSame("buy", adminClient.getCurrentStage());
	}
	
	@Test
	public void testDoneSuccess() throws Exception {
		Administrator administrator = Mockito.mock(Administrator.class);
		PowerMockito.mockStatic(IAdministrator.class);
		PowerMockito.when(IAdministrator.getInstance()).thenReturn(
				administrator);
		Player player = Mockito.mock(Player.class);
		PowerMockito.mockStatic(PlayerFactory.class);
		PowerMockito.when(PlayerFactory.getInstance()).thenReturn(player);
		Game game = Mockito.mock(Game.class);
		PowerMockito.mockStatic(Game.class);
		PowerMockito.when(Game.getInstance()).thenReturn(game);
		Board board = Mockito.mock(Board.class);
		Mockito.when(
				administrator.startGame(Mockito.eq(game),
						Mockito.anyListOf(Player.class))).thenReturn(board);
		Tile tile = Mockito.mock(Tile.class);
		PowerMockito
				.doNothing()
				.when(administrator)
				.setTile(Mockito.eq(board), Mockito.eq(player),
						Mockito.eq(tile), Mockito.anyString());
		
		PowerMockito.doNothing().when(administrator).getHotelShares(Mockito.eq(board), Mockito.eq(player),
				Mockito.anyString());
		PowerMockito.when(administrator.getCurrentPlayer()).thenReturn(player);
		PowerMockito.mockStatic(Chain.class);
		List <String> list = Mockito.mock(ArrayList.class);
		PowerMockito.when(Chain.getChain(Mockito.anyString())).thenReturn(list);
		PowerMockito.doNothing().when(administrator).done(player, board);
		adminClient
				.start("<setup player1=\"NN\" player2=\"NN1\" player3=\"NN2\" />");
		adminClient
		.start("<place row=\"3\" column=\"A\"><state><board/><player/></state></place>");
		adminClient
				.start("<buy share1=\"American\" share2=\"American\"><state><board/><player/></state></buy>");
		adminClient
		.start("<done><state><board/><player/></state></done>");
		assertSame("done", adminClient.getCurrentStage());
	}
	
	@Test
	public void testDoneSuccessWithoutBuy() throws Exception {
		Administrator administrator = Mockito.mock(Administrator.class);
		PowerMockito.mockStatic(IAdministrator.class);
		PowerMockito.when(IAdministrator.getInstance()).thenReturn(
				administrator);
		Player player = Mockito.mock(Player.class);
		PowerMockito.mockStatic(PlayerFactory.class);
		PowerMockito.when(PlayerFactory.getInstance()).thenReturn(player);
		Game game = Mockito.mock(Game.class);
		PowerMockito.mockStatic(Game.class);
		PowerMockito.when(Game.getInstance()).thenReturn(game);
		Board board = Mockito.mock(Board.class);
		Mockito.when(
				administrator.startGame(Mockito.eq(game),
						Mockito.anyListOf(Player.class))).thenReturn(board);
		Tile tile = Mockito.mock(Tile.class);
		PowerMockito
				.doNothing()
				.when(administrator)
				.setTile(Mockito.eq(board), Mockito.eq(player),
						Mockito.eq(tile), Mockito.anyString());
		
		PowerMockito.doNothing().when(administrator).getHotelShares(Mockito.eq(board), Mockito.eq(player),
				Mockito.anyString());
		PowerMockito.when(administrator.getCurrentPlayer()).thenReturn(player);
		PowerMockito.mockStatic(Chain.class);
		List <String> list = Mockito.mock(ArrayList.class);
		PowerMockito.when(Chain.getChain(Mockito.anyString())).thenReturn(list);
		PowerMockito.doNothing().when(administrator).done(player, board);
		adminClient
				.start("<setup player1=\"NN\" player2=\"NN1\" player3=\"NN2\" />");
		adminClient
		.start("<place row=\"3\" column=\"A\"><state><board/><player/></state></place>");
		adminClient
		.start("<done><state><board/><player/></state></done>");
		assertSame("done", adminClient.getCurrentStage());
	}
	
	@Test
	public void testDoneFail() throws Exception {
		Administrator administrator = Mockito.mock(Administrator.class);
		PowerMockito.mockStatic(IAdministrator.class);
		PowerMockito.when(IAdministrator.getInstance()).thenReturn(
				administrator);
		Player player = Mockito.mock(Player.class);
		PowerMockito.mockStatic(PlayerFactory.class);
		PowerMockito.when(PlayerFactory.getInstance()).thenReturn(player);
		Game game = Mockito.mock(Game.class);
		PowerMockito.mockStatic(Game.class);
		PowerMockito.when(Game.getInstance()).thenReturn(game);
		Board board = Mockito.mock(Board.class);
		Mockito.when(
				administrator.startGame(Mockito.eq(game),
						Mockito.anyListOf(Player.class))).thenReturn(board);
		Tile tile = Mockito.mock(Tile.class);
		PowerMockito
				.doNothing()
				.when(administrator)
				.setTile(Mockito.eq(board), Mockito.eq(player),
						Mockito.eq(tile), Mockito.anyString());
		
		PowerMockito.doNothing().when(administrator).getHotelShares(Mockito.eq(board), Mockito.eq(player),
				Mockito.anyString());
		PowerMockito.when(administrator.getCurrentPlayer()).thenReturn(player);
		PowerMockito.mockStatic(Chain.class);
		List <String> list = Mockito.mock(ArrayList.class);
		PowerMockito.when(Chain.getChain(Mockito.anyString())).thenReturn(list);
		PowerMockito.doNothing().when(administrator).done(player, board);
		adminClient
				.start("<setup player1=\"NN\" player2=\"NN1\" player3=\"NN2\" />");
		adminClient
		.start("<done><state><board/><player/></state></done>");
		assertNotSame("done", adminClient.getCurrentStage());
	}
}
