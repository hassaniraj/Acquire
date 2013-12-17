package com.acquire.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.acquire.player.Player;

public class Game {
	private Map <Board, List<Player>> games;
	private static Game game;
	
	private Game() {
		games = new HashMap<Board, List<Player>>();
	}
	
	public static synchronized Game getInstance() {
		if (game == null) {
			game = new Game();
		}
		return game;
	}
	
	public void setGame(Board board, List<Player> playerList) {
		this.games.put(board, playerList);
	}
	
	public List<Player> getGame(Board board) {
		return games.get(board);
	}
	
	public void clear() {
		game = null;
	}
}
