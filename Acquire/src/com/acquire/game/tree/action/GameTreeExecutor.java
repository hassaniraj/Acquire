package com.acquire.game.tree.action;

import java.util.List;

import com.acquire.game.tree.state.StateClient;
import com.acquire.player.Player;

public interface GameTreeExecutor {
	
	/**
	 * Generate next level information of tree
	 */
	public void generate();
	
	/**
	 * Get the current player from nodes
	 * @return
	 */
	public Player getCurrentPLayer();
	
	/**
	 * Set up tree for the first time
	 * @return
	 */
	public StateClient setupTree();
	
	/**
	 * Set up player iterator
	 * @param players
	 */
	public void setUpIterator(List<Player> players);
	
	/**
	 * rotate player after one complete turn
	 */
	void rotate();
	
	/**
	 * play the game
	 * @param adminTreeInspector
	 * @param stateClient
	 * @param playerTreeInspector
	 */
	void playGame(AdminTreeInspector adminTreeInspector,
			StateClient stateClient, PlayerTreeInspector playerTreeInspector);
	
	/**
	 * Check if reached game end state
	 * @return
	 */
	boolean isEnd();
	
	/**
	 * Get hotel list
	 * @return
	 */
	List<String> getHotels();
}
