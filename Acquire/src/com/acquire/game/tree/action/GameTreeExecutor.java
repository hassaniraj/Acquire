package com.acquire.game.tree.action;

import java.util.List;

import com.acquire.game.tree.state.StateClient;
import com.acquire.player.Player;

public interface GameTreeExecutor {
	public void generate();
	public Player getCurrentPLayer();
	public StateClient setupTree();
	public void setUpIterator(List<Player> players);
	void rotate();
	void playGame(AdminTreeInspector adminTreeInspector,
			StateClient stateClient, PlayerTreeInspector playerTreeInspector);
	boolean isEnd();
	List<String> getHotels();
}
