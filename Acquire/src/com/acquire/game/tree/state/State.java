package com.acquire.game.tree.state;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.acquire.board.Board;
import com.acquire.board.Tile;
import com.acquire.player.Player;

/**
 * Define state (node) of tree
 * @author Shreya
 *
 */
public interface State {
	/**
	 * Set the list of players in node
	 * @param player
	 * @param tiles
	 */
	void setPlayerList(Player player, List<Tile> tiles);

	/**
	 * Get all share combinations
	 * @return
	 */
	Set<List<String>> getShareCombinations();

	/**
	 * Set players in the node
	 * @param players
	 */
	void setPlayers(Map<Player, List<Tile>> players);

	/**
	 * get all players
	 * @return
	 */
	Map<Player, List<Tile>> getPlayers();

	/**
	 * Set all hotels
	 * @param hotels
	 */
	void setHotels(List<String> hotels);

	/**
	 * Get all hotels
	 * @return
	 */
	List<String> getHotels();

	/**
	 * Set the board
	 * @param board
	 */
	void setBoard(Board board);

	/**
	 * Get the board
	 * @return
	 */
	Board getBoard();

	/**
	 * Set all combination of shares
	 */
	void setShareCombinations();
	
}
