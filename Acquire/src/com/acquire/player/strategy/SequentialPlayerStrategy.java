package com.acquire.player.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.acquire.board.Chain;
import com.acquire.board.Labels;
import com.acquire.board.Tile;
import com.acquire.game.tree.state.StateClient;
import com.acquire.player.Player;
import com.acquire.player.Share;

public class SequentialPlayerStrategy implements PlayerStrategy {

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
	public List<String> buyShare() {
		// Administrator administrator = IAdministrator.getInstance();
		// Board board = BoardFactory.getBoard();
		// Player player = administrator.getCurrentPlayer();
		List<String> hotels = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		labels.addAll(Labels.getLabels());
		for (int i = 0; i < labels.size(); i++) {
			if (Chain.getChain(labels.get(i)).isEmpty()
					|| Share.getShare(labels.get(i)) == 0)
				labels.remove(i);
		}
		String hotel = pickHotel(labels);

		if (!Chain.getChain(hotel).isEmpty()) {
			hotels.add(hotel);
			// administrator.getHotelShares(board, player, hotel);
		}
		hotel = pickHotel(labels);
		if (!Chain.getChain(hotel).isEmpty()) {
			hotels.add(hotel);
			// administrator.getHotelShares(board, player, hotel);
		}
		hotel = pickHotel(labels);
		if (!Chain.getChain(hotel).isEmpty()) {
			hotels.add(hotel);
			// administrator.getHotelShares(board, player, hotel);
		}
		return hotels;
	}

	public String pickHotel(List<String> labels) {
		Collections.sort(labels);
		for (String label : labels) {
			if (Share.getShare(label) > 0) {
				return label;
			}
		}
		return "";
	}

	public Tile pickTile(List<Tile> tiles) {
		Collections.sort(tiles, new Comparator<Tile>() {

			@Override
			public int compare(Tile o1, Tile o2) {
				if (o1.getRow().equals(o2.getRow())) {
					return o1.getColumn().compareTo(o2.getColumn());
				} else
					return o1.getRow().compareTo(o2.getRow());
			}
		});
		
		return tiles.get(0);
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
			List<String> nextELement = iter.next();
			if (nextELement.size() == 3)
			{
				return nextELement;
			}
		}
		
		return new ArrayList<>();
	}

}
