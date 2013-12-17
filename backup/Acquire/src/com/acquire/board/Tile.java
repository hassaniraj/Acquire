package com.acquire.board;

public class Tile {
	public String column;
	public String row;
	
	public String getTileLabel(String row, String column) {
		return column+row;
	}
}
