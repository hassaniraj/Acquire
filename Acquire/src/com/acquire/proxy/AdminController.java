package com.acquire.proxy;

import java.util.List;
import java.util.Set;

import com.acquire.board.Tile;
import com.acquire.player.Player;

public interface AdminController {
	public void playerPlaceMove(Tile tile, String hotel);
	public void playerBuyMove(String hotel);
	public void playerDone();
	public Player init(List<Player> players);
	public List<String> getHotels();
	Player getCurrent();
	boolean checkIfEnd();
	public Player getWinner();
}
