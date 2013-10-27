package com.acquire.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.acquire.board.Tile;
import com.acquire.player.strategy.PlayerStrategy;

public class Player {
	private String name;
	private int cash;
	private Map <String, Integer> share;
	private List<Tile> tiles;
	private PlayerStrategy strategy;
	
	public Player() {
		this.cash = 0;
		share = new HashMap<>();
		tiles = new ArrayList<>();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName () {
		return name;
	}
	
	public void setCash(int cash) {
		this.cash = cash;
	}
	
	public int getCash () {
		return cash;
	}
	
	public void setShare(String name, int share) {
		this.share.put(name, share);
	}
	
	public int getShare (String name) {
		if (share.containsKey(name)) return share.get(name);
		return 0;
	}
	
	public Map <String, Integer> getShares () {
		return share;
	}
	
	public void setTile(String row, String column) {
		Tile tile = new Tile();
		tile.setRow(row);
		tile.setColumn(column);
		tiles.add(tile);
	}
	
	public List<Tile> getTile() {
		return tiles;
	}
	
	public void removeTile(Tile tile) {
		for (Tile t: tiles) {
			if (t.getTileLabel(t.getRow(), t.getColumn()).equals(tile.getTileLabel(tile.getRow(), tile.getColumn()))) {
				tiles.remove(tiles.indexOf(t));
				break;
			}
		}
	}
	
	public void setStrategy(PlayerStrategy strategy) {
		this.strategy = strategy;
	}
	
	public PlayerStrategy getStrategy() {
		return this.strategy;
	}
}
