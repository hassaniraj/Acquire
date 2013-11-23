package com.acquire.factory;

import com.acquire.board.Board;

public class BoardFactory {
	public static Board getBoard () {
		return Board.getInstance();
	}
}
