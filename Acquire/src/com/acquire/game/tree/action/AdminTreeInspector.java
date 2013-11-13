package com.acquire.game.tree.action;

import java.util.List;

import com.acquire.board.Board;
import com.acquire.board.Tile;
import com.acquire.game.tree.state.StateClient;
import com.acquire.player.Player;

public interface AdminTreeInspector {
	
	/**
	 * Initialize game
	 * @param players
	 * @return
	 */
	Board init(List<Player> players);

	/**
	 * Place tile on given board
	 * @param tile
	 * @param type
	 * @param root
	 * @param player
	 */
	void place(Tile tile, String type, StateClient root, Player player);

	/**
	 * Get the winner of the game
	 * @return
	 */
	String getWinner(Board board);
}
