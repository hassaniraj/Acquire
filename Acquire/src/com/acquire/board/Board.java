package com.acquire.board;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.acquire.admin.IAdministrator;

public class Board {
	private Map <String, Hotel> board;
	private List <String> rows;
	private List <String> columns;
	private Map <String, Set<String>> hotelTiles;	
	private static Board b;
	
	private Board() {
		rows = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I");
		columns = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
		board = new HashMap<String, Hotel>();
		hotelTiles = new HashMap<String, Set<String>>();
	}
	
	public static Board getInstance() {
		if(b == null) {
			b = new Board();
		}
		return b;
	}
	
	public Board createBoard() {
		for (String row: rows) {
			for (String column: columns) {
				Tile tile = new Tile();
				tile.row = row;
				tile.column = column;
				this.board.put(tile.getTileLabel(row, column), null);
			}
		}
		return this;
	}
	
	public Map<String, Hotel> getOccupiedTiles() {
		Map <String, Hotel> occupiedTiles = new HashMap<>();
		for (String tile : board.keySet()) {
			if (board.get(tile) != null) {
				occupiedTiles.put(tile, board.get(tile));
			}
		}
		return occupiedTiles;
	}
	
	public Map<String, Set<String>> getHotelTiles() {
		Map <String, Hotel> occupiedTiles = getOccupiedTiles();
		//Map <String, List<String>> hotelTiles = new HashMap<>();
		for (String tile : occupiedTiles.keySet()) {
			if (!occupiedTiles.get(tile).getLabel().equals("singleton")) {
				if (!hotelTiles.containsKey(occupiedTiles.get(tile).getLabel())) {
					hotelTiles.put(occupiedTiles.get(tile).getLabel(), new HashSet<>(Arrays.asList(tile)));
				} else {
					Set<String> tiles = hotelTiles.get(occupiedTiles.get(tile).getLabel());
					tiles.add(tile);
					hotelTiles.put(occupiedTiles.get(tile).getLabel(), tiles);
				}
				
				
			}
		}
		return hotelTiles;
		
	}
	
	public List<String> getRows(){
		return rows;
	}
	
	public List<String> getColumns(){
		return columns;
	}
	
	public Map <String, Hotel> getBoard() {
		return board;
	}
	
	public void clear() {
		b = null;
	}
}
