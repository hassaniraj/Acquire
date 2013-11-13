package com.acquire.game.tree.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.acquire.board.Board;
import com.acquire.board.Tile;
import com.acquire.config.Config;
import com.acquire.player.Player;

/**
 * Class that handles every new node created (flyweight pattern)
 * 
 */
public class StateClient {
	private Player player;

	public Player getPlayer() {
		if (player == null)
			player = new Player();
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	private String hotel;

	public String getHotel() {
		return hotel;
	}

	public void setHotel(String hotel) {
		this.hotel = hotel;
	}

	private List<String> shares;

	public List<String> getShares() {
		if (shares == null)
			shares = new ArrayList<>();
		return shares;
	}

	public void setShares(List<String> shares) {
		this.shares = shares;
	}

	/**
	 * List of all children
	 */
	private List<StateClient> children;

	public List<StateClient> getChildren() {
		if (children == null)
			children = new ArrayList<>();
		return children;
	}

	public void setChildren(List<StateClient> children) {
		this.children = children;
	}

	private Tile tile;

	public Tile getTile() {
		return tile;
	}

	public void setTile(Tile tile) {
		this.tile = tile;
	}

	State state = StateFactory.getInstance();
	String move;

	/**
	 * Inspect tile and set type of move in the node.
	 * @param board
	 * @param row
	 * @param column
	 * @return
	 */
	public String inspect(Board board, String row, String column) {
		ArrayList<String> hotels;
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
			position.add(column
					+ board.getRows().get(rowIndex - 1));
			if (board.getBoard().get(
					column
							+ board.getRows()
									.get(rowIndex - 1)) != null) {
				positions.add(column
						+ board.getRows().get(rowIndex - 1));
			}
		}
		if (rowIndex + 1 < board.getRows().size()) {
			position.add(column
					+ board.getRows().get(rowIndex + 1));
			if (board.getBoard().get(
					column
							+ board.getRows()
									.get(rowIndex + 1)) != null) {
				positions.add(column
						+ board.getRows().get(rowIndex + 1));
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

	public Map<String, Object> generate(Board board, String row, String column) {
		// state.generate(board, row, column, inspect (board, row, column));
		Map<String, Object> result = new HashMap<String, Object>();
		String move = inspect(board, row, column);
		result.put("move", move);
		Tile tile = new Tile();
		tile.setRow(row);
		tile.setColumn(column);
		result.put("tile", tile);
		return result;
	}

	public String getMove() {
		return move;
	}

	public void setMove(String move) {
		this.move = move;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

}
