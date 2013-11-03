package com.acquire.game.tree.action;

import java.util.Map;

import com.acquire.game.tree.state.StateClient;
import com.acquire.player.Player;

public interface PlayerTreeInspector {

	StateClient pickState(StateClient root, Player player);
}
