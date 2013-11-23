package com.acquire.player;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.acquire.board.Board;
import com.acquire.board.Chain;
import com.acquire.board.Game;
import com.acquire.board.Hotel;
import com.acquire.board.Tile;
@RunWith (PowerMockRunner.class)
@PrepareForTest({Chain.class, Player.class, Tile.class,Game.class})

public class IPlayerTest {
	Player player;
    String Name = "Player1";
	String row = "A";
	String column = "1";

	public IPlayerTest(){
		
		player= new Player();
	}
	@Test
	public void testName() {
		player.setName(Name);
		Assert.assertEquals(Name, player.getName());
		}
	@Test
	public void testCash() {
		player.setCash(6000);
		Assert.assertEquals(6000, player.getCash());
		}
	@Test
	public void testShare() {
		player.setShare(Name, 300);
		Assert.assertEquals(300, player.getShare(Name));
		}
	@Test
	public void testShares() {
		assertNotNull(player.getShares());
		}
	@Test
	public void testSetTile() {
	    player.setTile(row, column);
		assertNotNull(player.getTile());
		}
    @Test
	public void trueRemoveTile() {
		Tile tile= Mockito.mock(Tile.class);
	    Mockito.when(tile.getTileLabel(row,column)).thenReturn("MockLabel");
	    player.removeTile(tile);
	    assertNotNull(player.getTile());
	    }
	
}
