package com.acquire.game.tree.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.acquire.board.Tile;
import com.acquire.game.tree.state.StateClient;
import com.acquire.player.Player;

public class PlayerTreeInspectorImpl implements PlayerTreeInspector {

	@Override
	public StateClient pickState(StateClient root, Player player) {
		List <StateClient> children = root.getChildren();
		StateClient state = player.getStrategy().playTile(root, children);
		if (state != null) {
		List<String> shares = player.getStrategy().buyShare(state, root.getState().getShareCombinations());
		state.setShares(shares);
		return state;
		}
		return null;
	}

}
