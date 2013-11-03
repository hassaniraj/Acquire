package com.acquire.player.strategy;

import java.util.List;
import java.util.Set;

import com.acquire.exception.AcquireException;
import com.acquire.game.tree.state.StateClient;
import com.acquire.player.Player;

public interface PlayerStrategy {

	/**
	 * Get shares in the specified strategy using controllers
	 * @return
	 * @throws AcquireException
	 */
	List<String> buyShare() throws AcquireException;
	
	/**
	 * Place tile according to specified strategies using controller
	 * @param player
	 * @param hotels
	 * @return
	 */
	List<Object> playTile(Player player, List<String> hotels);
	
	/**
	 * Play tile according to game tree
	 * @param children
	 * @return
	 */
	StateClient playTile(List<StateClient> children);
	
	/**
	 * Buy share using game tree
	 * @param stateClient
	 * @param shareCombinations
	 * @return
	 */
	List<String> buyShare(StateClient stateClient, Set<List<String>> shareCombinations);
}
