package com.acquire.actions;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.acquire.board.Board;
import com.acquire.board.Hotel;

public interface AcquireActions {
	String inspect (Board board, String row, String column);
	boolean singleton (Board board, String row, String column);
	Hotel growing (Board board, String row, String column);
	boolean founding (Board board, String row, String column, String label);
	Set <String> merging (Board board, String row, String column, String label);
	Map<String, List<String>> getLabel(Board board, String row, String column);
}
