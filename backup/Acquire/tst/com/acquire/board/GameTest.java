package com.acquire.board;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import com.acquire.player.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
@RunWith(PowerMockRunner.class)
@PrepareForTest({Game.class})
public class GameTest {
    Game game;
	
	public GameTest(){
		game = Game.getInstance();
	}
	@Test
	public void testGame() {
		Board board = Mockito.mock(Board.class);
		List<Player> playerList=new ArrayList<Player>();		
		game.setGame(board, playerList);
		assertNotNull(game.getGame(board));
	}


}
