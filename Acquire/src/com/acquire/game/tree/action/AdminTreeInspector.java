package com.acquire.game.tree.action;

import java.util.List;

import com.acquire.admin.Administrator;
import com.acquire.board.Tile;
import com.acquire.game.tree.state.StateClient;
import com.acquire.player.Player;

public interface AdminTreeInspector {
	
	List<Player> init(List<Player> players);

	void place(Tile tile, String type, StateClient root, Player player);

	String getWinner();
}
