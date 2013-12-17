package com.acquire.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.acquire.board.Board;
import com.acquire.board.Chain;
import com.acquire.board.Hotel;
import com.acquire.board.Tile;

public class IAcquireActions implements AcquireActions {

	private List<String> hotels;

	@Override
	public String inspect(Board board, String row, String column) {
		int r = Integer.parseInt(column);
		int charValue = row.charAt(0);
		String nextCol = String.valueOf((char) (charValue + 1));
		String prevCol = String.valueOf((char) (charValue - 1));
		hotels = new ArrayList<>();
		List<String> neighbors = checkNeighbor(r, prevCol, nextCol, row, board);

		for (String n : neighbors) {
			if (!hotels.contains(board.getBoard().get(n).getLabel()))
				hotels.add(board.getBoard().get(n).getLabel());
		}

		if (neighbors.size() > 1) {
			Set<String> set = new HashSet<>(hotels);
			// for (String h : hotels) {
			if (set.size() == 1) {
				if (hotels.get(0).equals("singleton")) {
					return "founding";
				} else
					return "growing";
			} else
				return "merging";
			// }
		} else if (neighbors.size() == 1 && hotels.get(0).equals("singleton")) {
			return "founding";
		} else if (neighbors.size() == 1 && !hotels.isEmpty()) {
			return "growing";
		} else if (neighbors.size() == 0) {
			return "singleton";
		}

		return null;
	}

	private List<String> checkNeighbor(int r, String prevCol, String nextCol,
			String row, Board board) {
		List<String> positions = new ArrayList<>();
		if (board.getBoard().get(r + nextCol) != null) {
			positions.add(r + nextCol);
		}
		if (board.getBoard().get(r + prevCol) != null) {
			positions.add(r + prevCol);
		}
		if (board.getBoard().get((r + 1) + row) != null) {
			positions.add((r + 1) + row);
		}
		if (board.getBoard().get((r - 1) + row) != null) {
			positions.add((r - 1) + row);
		}
		return positions;
	}

	@Override
	public boolean singleton(Board board, String row, String column) {
		String type = inspect(board, row, column);
		if (type.equalsIgnoreCase("singleton")) {
			Hotel hotel = new Hotel();
			hotel.setLabel("singleton");
			board.getBoard().put(column + row, hotel);
			return true;
		}

		return false;
	}

	@Override
	public Hotel growing(Board board, String row, String column) {
		String type = inspect(board, row, column);
		if (type.equalsIgnoreCase("growing")) {
			Hotel hotel = new Hotel();
			Set<String> set = new HashSet<>(hotels);
			hotel.setLabel(hotels.get(0));
			Chain.setChain(hotels.get(0), column + row);

			board.getBoard().put(column + row, hotel);
			return hotel;
		}
		return null;
	}

	@Override
	public boolean founding(Board board, String row, String column, String label) {
		String type = inspect(board, row, column);
		int r = Integer.parseInt(column);
		int charValue = row.charAt(0);
		String nextCol = String.valueOf((char) (charValue + 1));
		String prevCol = String.valueOf((char) (charValue - 1));
		List<String> neighbors = checkNeighbor(r, prevCol, nextCol, row, board);
		if (type.equalsIgnoreCase("founding")) {
			Hotel hotel = new Hotel();
			hotel.setLabel(label);
			for (String tile : neighbors) {
				Chain.setChain(label, tile);
				board.getBoard().put(tile, hotel);
			}

			Chain.setChain(label, column + row);
			board.getBoard().put(column + row, hotel);
			return true;
		}

		return false;
	}

	@Override
	public Set<String> merging(Board board, String row, String column,
			String label) {
		Set<String> acquired = new HashSet<>();
		String type = inspect(board, row, column);
		String maxLabel = "";
		if (type.equalsIgnoreCase("merging")) {
			int max = 0;

			if (checkIfEqual()) {
				maxLabel = label;
			} else if (!impossible()) {
				for (String hotel : hotels) {
					if (!hotel.equals("singleton")) {
					List<String> chain = Chain.getChain(hotel);
					if (max < chain.size()) {
						max = chain.size();
						maxLabel = hotel;
					}
					}
				}
			} else
				return null;

			Hotel hotel = new Hotel();
			hotel.setLabel(maxLabel);
			board.getBoard().put(column + row, hotel);
			Chain.setChain(maxLabel, column + row);
			acquired.add(maxLabel);
			for (String hotelName : hotels) {
				if (!hotelName.equals(maxLabel)) {
					acquired.add(hotelName);
					List<String> chain = Chain.getChain(hotelName);
					if (chain != null) {
						for (String tile : chain) {
							Hotel h = new Hotel();
							h.setLabel(maxLabel);
							board.getBoard().remove(tile);
							board.getBoard().put(tile, h);
							Chain.setChain(maxLabel, tile);
						}
						Chain.getChain(hotelName).clear();
					}
				}

			}
			int r = Integer.parseInt(column);
			int charValue = row.charAt(0);
			String nextCol = String.valueOf((char) (charValue + 1));
			String prevCol = String.valueOf((char) (charValue - 1));
			List<String> neighbours = checkNeighbor(r, prevCol, nextCol, row,
					board);

			for (String neighbour : neighbours) {
				if (board.getBoard().get(neighbour).getLabel()
						.equals("singleton")) {
					board.getBoard().put(neighbour, hotel);
					Chain.setChain(maxLabel, neighbour);
				}
			}
			return acquired;
		}
		return null;
	}

	@Override
	public Map<String, List<String>> getLabel(Board board, String row,
			String column) {
		inspect(board, row, column);
		Map<String, List<String>> chains = new HashMap<>();
		for (String hotel : hotels) {
			if (!hotel.equals("singleton"))
				chains.put(hotel, Chain.getChain(hotel));
		}
		return chains;
	}

	public boolean checkIfEqual() {
		int max = 0;
		for (String hotel : hotels) {
			if (!hotel.equals("singleton")) {
				List<String> chain = Chain.getChain(hotel);
				if (max != chain.size() && max != 0) {
					max = chain.size();
					return false;
				} else {
					max = chain.size();
				}
			}
		}
		return true;
	}

	public boolean impossible() {
		int max = 0;
		for (String hotel : hotels) {
			if (!hotel.equals("singleton")) {
				List<String> chain = Chain.getChain(hotel);
				if (max != chain.size() && max != 0) {
					max = chain.size();
				} else {
					if (max >= 11 && max == chain.size())
						return true;
					max = chain.size();
				}
			}
		}
		return false;
	}
}
