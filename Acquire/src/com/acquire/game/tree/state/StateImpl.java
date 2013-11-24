package com.acquire.game.tree.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.acquire.board.Board;
import com.acquire.board.Labels;
import com.acquire.board.Tile;
import com.acquire.factory.BoardFactory;
import com.acquire.player.Player;
import com.acquire.player.Share;

public class StateImpl implements State {
	private Board board;
	private List<String> hotels = new ArrayList<>();
	private Map<Player, List<Tile>> players = new HashMap<>();
	private String move;

	@Override
	public Board getBoard() {
		return board;
	}

	@Override
	public void setBoard(Board board) {
		this.board = board;
	}

	@Override
	public List<String> getHotels() {
		return hotels;
	}

	@Override
	public void setHotels(List<String> hotels) {
		this.hotels = hotels;
	}

	@Override
	public Map<Player, List<Tile>> getPlayers() {
		return players;
	}

	@Override
	public void setPlayers(Map<Player, List<Tile>> players) {
		this.players = players;
	}

	@Override
	public Set<List<String>> getShareCombinations() {
		return shareCombinations;
	}

	@Override
	public void setShareCombinations() {
		Map<String, Integer> shareCount = new HashMap<>(Share.getShare());
		shareCombinations.clear();
		hotels = new ArrayList<String>(board.getHotelTiles().keySet());
		System.out.println(hotels);
		if (Share.getShare("American") == 1)
			System.out.println("one left");
		for (int i = 0; i < hotels.size(); i++) {
			if (shareCount.get(hotels.get(i)) > 0) {
				shareCombinations.add(new ArrayList<>(Arrays.asList(hotels
						.get(i))));
				shareCount
						.put(hotels.get(i), shareCount.get(hotels.get(i)) - 1);
			}
			for (int j = 0; j < hotels.size(); j++) {
				if (shareCount.get(hotels.get(j)) > 0) {
					
						shareCombinations.add(new ArrayList<>(Arrays.asList(
								hotels.get(i), hotels.get(j))));
						shareCount.put(hotels.get(j),
								shareCount.get(hotels.get(j)) - 1);
					}
				
				for (int k = 0; k < hotels.size(); k++) {
					if (shareCount.get(hotels.get(k)) > 2) {
						
							shareCombinations.add(new ArrayList<>(Arrays
									.asList(hotels.get(i), hotels.get(j),
											hotels.get(k))));
							shareCount.put(hotels.get(k),
									shareCount.get(hotels.get(k)) - 1);
						}
					}
				}
			
		}
		List<List<String>> shares = new ArrayList<>(shareCombinations);
		Collections.sort(shares, new Comparator<List<String>>() {

			@Override
			public int compare(List<String> o1, List<String> o2) {
				Collections.sort(o1);
				Collections.sort(o2);
				if (o1.size() == o2.size()) {
					for (int i = 0; i < o1.size(); i++) {
						if (!o1.get(i).equals(o2.get(i))) {
							return o1.get(i).compareTo(o2.get(i));
						}
					}
				} else if (o1.size() > o2.size())
					return 1;
				else if (o1.size() < o2.size())
					return -1;
				else
					return 0;
				return 0;
			}
		});
		Set<List<String>> result = new LinkedHashSet<>(shares);
		shareCombinations = result;

	}

	Set<List<String>> shareCombinations = new LinkedHashSet<>();

	public StateImpl() {
		setup();
	}

	public void setup() {
		// board.clear();
		board = BoardFactory.getBoard();
		hotels = new ArrayList<String>(board.getHotelTiles().keySet());

		for (int i = 0; i < hotels.size(); i++) {
			shareCombinations
					.add(new ArrayList<>(Arrays.asList(hotels.get(i))));
			for (int j = 0; j < hotels.size(); j++) {
				shareCombinations.add(new ArrayList<>(Arrays.asList(
						hotels.get(i), hotels.get(j))));
				for (int k = 0; k < hotels.size(); k++) {
					shareCombinations.add(new ArrayList<>(Arrays.asList(
							hotels.get(i), hotels.get(j), hotels.get(k))));
				}
			}
		}
		List<List<String>> shares = new ArrayList<>(shareCombinations);
		Collections.sort(shares, new Comparator<List<String>>() {

			@Override
			public int compare(List<String> o1, List<String> o2) {
				Collections.sort(o1);
				Collections.sort(o2);
				if (o1.size() == o2.size()) {
					for (int i = 0; i < o1.size(); i++) {
						if (!o1.get(i).equals(o2.get(i))) {
							return o1.get(i).compareTo(o2.get(i));
						}
					}
				} else if (o1.size() > o2.size())
					return 1;
				else if (o1.size() < o2.size())
					return -1;
				else
					return 0;
				return 0;
			}
		});
		Set<List<String>> result = new LinkedHashSet<>(shares);
		shareCombinations = result;
		hotels = Labels.getLabels();
	}

	@Override
	public void setPlayerList(Player player, List<Tile> tiles) {
		this.players.put(player, tiles);
	}

}
