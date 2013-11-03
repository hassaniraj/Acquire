package com.acquire.player.strategy;

import java.util.List;
import java.util.Set;

import com.acquire.exception.AcquireException;
import com.acquire.game.tree.state.StateClient;
import com.acquire.player.Player;

public interface PlayerStrategy {

	List<String> buyShare() throws AcquireException;
	List<Object> playTile(Player player, List<String> hotels);
	StateClient playTile(List<StateClient> children);
	List<String> buyShare(StateClient stateClient, Set<List<String>> shareCombinations);
}
