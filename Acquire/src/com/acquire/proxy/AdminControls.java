package com.acquire.proxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import com.acquire.admin.Administrator;
import com.acquire.admin.IAdministrator;
import com.acquire.board.Board;
import com.acquire.board.Game;
import com.acquire.board.Labels;
import com.acquire.board.Tile;
import com.acquire.player.Player;

public class AdminControls implements AdminController {
	private Administrator admin;

	@Override
	public Player init(List<Player> players) {
		admin = IAdministrator.getInstance();
		admin.startGame(Game.getInstance(), players);
		return admin.getCurrentPlayer();
	}

	@Override
	public Player getCurrent() {
		return admin.getCurrentPlayer();
	}

	@Override
	public List<String> getHotels() {
		Set<String> hotels = Board.getInstance().getHotelTiles().keySet();
		List<String> available = new ArrayList<>(Labels.getLabels());
		available.removeAll(hotels);
		return available;
	}

	@Override
	public void playerPlaceMove(Tile tile, String hotel) {
		admin.setTile(Board.getInstance(), admin.getCurrentPlayer(), tile,
				hotel);
	}

	@Override
	public void playerBuyMove(String hotel) {
		admin.getHotelShares(Board.getInstance(), admin.getCurrentPlayer(),
				hotel);
	}

	@Override
	public void playerDone() {
		admin.done(admin.getCurrentPlayer(), Board.getInstance());
	}

	@Override
	public boolean checkIfEnd() {
		if (admin.isEnd())
			return true;
		return false;
	}

	@Override
	public String getWinner() {
		TreeMap<Integer,Player> playerCash=new TreeMap<>(Collections.reverseOrder());
		List<Player> players = Game.getInstance().getGame(Board.getInstance());
		
		for (Player player : players) {
			int finalWorth = admin.getWorth(player);
			player.setCash(finalWorth);
			playerCash.put(player.getCash(), player);
		}
		return playerCash.firstEntry().getValue().getName();
	}
}
