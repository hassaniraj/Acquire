package com.acquire.player.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.acquire.admin.Administrator;
import com.acquire.admin.IAdministrator;
import com.acquire.board.Board;
import com.acquire.board.Chain;
import com.acquire.board.Labels;
import com.acquire.board.Tile;
import com.acquire.exception.AcquireException;
import com.acquire.factory.BoardFactory;
import com.acquire.game.tree.state.StateClient;
import com.acquire.player.Player;

public class RandomPlayerStrategy implements PlayerStrategy {

	@Override
	public List<Object> playTile(Player player, List<String> hotels) {
		// Administrator administrator = IAdministrator.getInstance();
		// Board board = BoardFactory.getBoard();
		// administrator.setCurrentPlayer(player);
		List<Tile> tiles = player.getTile();
		String label = "";
		if (hotels.size() > 0)
			label = pickHotel(hotels);
		if (!tiles.isEmpty()) {
			Tile tile = pickTile(tiles);

			// String type = administrator.setTile(board, player, tile, label);
			// if (type.equals("founding") || type.equals("merge")) {
			// return Arrays.asList(tile, label);
			// }
			return new ArrayList<Object>(Arrays.asList(tile, label));
		}
		return new ArrayList<Object>();
	}

	@Override
	public List<String> buyShare() throws AcquireException {
		// Administrator administrator = IAdministrator.getInstance();
		// Board board = BoardFactory.getBoard();
		// Player player = administrator.getCurrentPlayer();
		List<String> hotels = new ArrayList<>();
		int number = new Random().nextInt(3);
		for (int i = 0; i < number + 1; i++) {
			List<String> labels = new ArrayList<>();
			labels.addAll(Labels.getLabels());

			labels.removeAll(Chain.getChainLabel());
			if (labels.size() > 0) {
				String hotel = pickHotel(labels);
				if (!Chain.getChain(hotel).isEmpty()) {
					hotels.add(hotel);
				}
			}
		}
		return hotels;
	}

	public String pickHotel(List<String> hotels) {
		Random random = new Random();
		if (hotels.size() > 0) {
		int i = random.nextInt(hotels.size());
		return hotels.get(i);
		}
		return "";
	}

	public Tile pickTile(List<Tile> tiles) {
		Random random = new Random();

		return tiles.get(random.nextInt(tiles.size()));
	}

	@Override
	public StateClient playTile(List<StateClient> children) {
		// Administrator administrator = IAdministrator.getInstance();
		// Board board = BoardFactory.getBoard();
		// administrator.setCurrentPlayer(player);
		if (children.size() > 0) {
		String label = "";
		Map<Tile, StateClient> tiles = new HashMap<>();
		List<String> hotels = new ArrayList<>();
		//hotels=Labels.getLabels();
		hotels = children.get(0).getState().getHotels();
		for (int i = 0; i < children.size(); i++) {
			tiles.put(children.get(i).getTile(), children.get(i));
		}

		// String label = "";
		// if (hotels.size() > 0)
		// label = pickHotel(hotels);
		if (!tiles.isEmpty()) {
			Tile tile = pickTile(new ArrayList<>(tiles.keySet()));
			label = pickHotel(hotels);
			tiles.get(tile).setHotel(label);

			return tiles.get(tile);
		}
		}
		return null;

	}

	@Override
	public List<String> buyShare(StateClient state, Set<List<String>> shareCombinations) {
		// TODO Auto-generated method stub
		Iterator<List<String>> iter = shareCombinations.iterator();

		while (iter.hasNext() && shareCombinations.size() > 0) {
			List<String> nextELement = pickHotel(shareCombinations);
//			int random = new Random().nextInt(1);
			//if (nextELement.size() == 3)
				return nextELement;
		}
		
		return new ArrayList<>();
	}

	private List<String> pickHotel(Set<List<String>> shareCombinations) {
		List<List<String>> hotels = new ArrayList<>(shareCombinations);
		Random random = new Random();
		if (hotels.size() > 0) {
		int i = random.nextInt(hotels.size());
		return hotels.get(i);
		}
		return null;
	}

}
