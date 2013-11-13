package com.acquire.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.acquire.board.Board;
import com.acquire.board.Chain;
import com.acquire.board.Game;
import com.acquire.board.Hotel;
import com.acquire.board.Labels;
import com.acquire.config.Config;
import com.acquire.factory.BoardFactory;
import com.acquire.player.Player;
import com.acquire.player.Share;

public class IAcquireActions implements AcquireActions {

	private List<String> hotels;
	
	private int SAFE_CHAIN_SIZE = 11;

	@Override
	public String inspect(Board board, String row, String column) {
		hotels = new ArrayList<>();

		List<String> neighbors = checkNeighbor(row, column, board);
		for (String n : neighbors) {
			if (!hotels.contains(board.getBoard().get(n).getLabel()))
				hotels.add(board.getBoard().get(n).getLabel());
		}

		if (neighbors.size() > 1) {
			Set<String> set = new HashSet<>(hotels);
			// for (String h : hotels) {
			if (set.size() == 1) {
				if (hotels.get(0).equals(Config.Moves.SINGLETON.getMove())) {
					return Config.Moves.FOUNDING.getMove();
				} else
					return Config.Moves.GROWING.getMove();
			} else
				return Config.Moves.MERGING.getMove();
			// }
		} else if (neighbors.size() == 1 && hotels.get(0).equals(Config.Moves.SINGLETON.getMove())) {
			return Config.Moves.FOUNDING.getMove();
		} else if (neighbors.size() == 1 && !hotels.isEmpty()) {
			return Config.Moves.GROWING.getMove();
		} else if (neighbors.size() == 0) {
			return Config.Moves.SINGLETON.getMove();
		}

		return null;
	}

	/**
	 * Method to get neighbors for a tile
	 * 
	 * @param r
	 * @param prevCol
	 * @param nextCol
	 * @param row
	 * @param board
	 * @return List<String> consisting of the neighboring tiles
	 */
	private List<String> checkNeighbor(String row, String column, Board board) {
		List<String> positions = new ArrayList<>();
		List<String> position = new ArrayList<>();
		int rowIndex = board.getRows().indexOf(row);
		int columnIndex = board.getColumns().indexOf(column);
		if (rowIndex - 1 > 0) {
			position.add(column + board.getRows()
					.get(rowIndex - 1));
			if (board.getBoard().get(column +
					board.getRows().get(rowIndex - 1)) != null) {
				positions.add(column + board.getRows()
						.get(rowIndex - 1));
			}
		}
		if (rowIndex + 1 < board.getRows().size()) {
			position.add(column + board.getRows()
					.get(rowIndex + 1));
			if (board.getBoard().get(column +
					board.getRows().get(rowIndex + 1)) != null) {
				positions.add(column + board.getRows()
						.get(rowIndex + 1));
			}
		}
		if (columnIndex - 1 > 0) {
			position.add(board.getColumns()
					.get(columnIndex - 1)
					+ row);
			if (board.getBoard().get(
					board.getColumns().get(columnIndex - 1)
							+ row) != null) {
				positions.add(board.getColumns()
						.get(columnIndex - 1)
						+ row);
			}
		}
		if (columnIndex + 1 < board.getColumns().size()) {
			position.add(board.getColumns()
					.get(columnIndex + 1)
					+ row);
			if (board.getBoard().get(
					board.getColumns().get(columnIndex + 1)
							+ row) != null) {
				positions.add(board.getColumns()
						.get(columnIndex + 1)
						+ row);
			}
		}

		return positions; 
	}

	@Override
	public boolean singleton(Board board, String row, String column) {
		String type = inspect(board, row, column);
		if (type.equalsIgnoreCase(Config.Moves.SINGLETON.getMove())) {
			Hotel hotel = new Hotel();
			hotel.setLabel(Config.Moves.SINGLETON.getMove());
			board.getBoard().put(column + row, hotel);
			return true;
		}

		return false;
	}

	@Override
	public Hotel growing(Board board, String row, String column) {
		String type = inspect(board, row, column);
		if (type.equalsIgnoreCase(Config.Moves.GROWING.getMove())) {
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
		List<String> neighbors = checkNeighbor(row, column, board);
		if (type.equalsIgnoreCase(Config.Moves.FOUNDING.getMove())) {
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
		if (type.equalsIgnoreCase(Config.Moves.MERGING.getMove())) {
			int max = 0;

			if (checkIfEqual()) {
				maxLabel = label;
			} else if (!impossible()) {
				for (String hotel : hotels) {
					if (!hotel.equalsIgnoreCase(Config.Moves.SINGLETON.getMove())) {
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
					if (!hotelName.equalsIgnoreCase(Config.Moves.SINGLETON.getMove())) {
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

			}
			List<String> neighbours = checkNeighbor(row, column, board);

			for (String neighbour : neighbours) {
				if (board.getBoard().get(neighbour).getLabel()
						.equals(Config.Moves.SINGLETON.getMove())) {
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
			if (!hotel.equals(Config.Moves.SINGLETON.getMove()))
				chains.put(hotel, Chain.getChain(hotel));
		}
		return chains;
	}

	/**
	 * Method to check if the chains are equal
	 * 
	 * @return true if of equal size
	 */
	public boolean checkIfEqual() {
		int max = 0;
		for (String hotel : hotels) {
			if (!hotel.equals(Config.Moves.SINGLETON.getMove())) {
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

	/**
	 * Method to check if the impossible condition occurs for the chain size >
	 * 41
	 * 
	 * @return true if chain size is greater than 41
	 */
	public boolean impossible() {
		int max = 0;
		for (String hotel : hotels) {
			if (!hotel.equals(Config.Moves.SINGLETON.getMove())) {
				List<String> chain = Chain.getChain(hotel);
				if (max != chain.size() && max != 0) {
					max = chain.size();
				} else {
					if (max >= SAFE_CHAIN_SIZE && max == chain.size())
						return true;
					max = chain.size();
				}
			}
		}
		return false;
	}

	

}
