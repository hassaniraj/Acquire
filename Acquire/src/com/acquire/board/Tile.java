package com.acquire.board;

public class Tile {
	private String column;
	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getRow() {
		return row;
	}

	public void setRow(String row) {
		this.row = row;
	}

	private String row;
	
	public String getTileLabel(String row, String column) {
		return column+row;
	}
}
