package com.acquire.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.acquire.board.Board;
import com.acquire.board.Game;
import com.acquire.board.Labels;
import com.acquire.board.Tile;
import com.acquire.player.Player;

public class AdminProxy implements AdminController {
	private AdminController adminController;
	
	public AdminProxy() {
		 adminController = new AdminControls();
	}

	@Override
	public Player init(List<Player> players) {
		Player player = adminController.init(players);
		return player;
	}

	@Override
	public Player getCurrent() {
		return adminController.getCurrent();
	}

	@Override
	public List<String> getHotels() {
		return adminController.getHotels();
	}

	@Override
	public void playerPlaceMove(Tile tile, String hotel) {
		adminController.playerPlaceMove(tile, hotel);
	}

	@Override
	public void playerBuyMove(String hotel) {
		adminController.playerBuyMove(hotel);
	}

	@Override
	public void playerDone() {
		adminController.playerDone();
	}

	@Override
	public boolean checkIfEnd() {
		return adminController.checkIfEnd();
	}

	@Override
	public String getWinner() {
		return adminController.getWinner();
	}
}
