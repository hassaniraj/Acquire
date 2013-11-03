package com.acquire.game.tree.action;

import java.util.Map;

import com.acquire.game.tree.state.StateClient;
import com.acquire.player.Player;

public interface PlayerTreeInspector {

	/**
	 * Pick each node from tree and decide move
	 * @param root
	 * @param player
	 * @return
	 */
	StateClient pickState(StateClient root, Player player);
}
