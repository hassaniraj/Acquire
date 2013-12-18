package com.acquire.game.tree.action;

import com.acquire.game.tree.state.StateClient;
import com.acquire.player.Player;

public class PlayerProxy implements PlayerTreeInspector{
	PlayerTreeInspector playerTreeInspector = new PlayerTreeInspectorImpl();
	
	@Override
	public StateClient pickState(StateClient root, Player player) {
		return playerTreeInspector.pickState(root, player);
	}

}
