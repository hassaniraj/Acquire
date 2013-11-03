package com.acquire.game.tree.state;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.acquire.board.Board;
import com.acquire.board.Tile;
import com.acquire.player.Player;

public interface State {
	
	void setPlayerList(Player player, List<Tile> tiles);

//	void generate(Tile tile, Player player, Board board);

	Set<List<String>> getShareCombinations();

	void setPlayers(Map<Player, List<Tile>> players);

	Map<Player, List<Tile>> getPlayers();

	void setHotels(List<String> hotels);

	List<String> getHotels();

	void setBoard(Board board);

	Board getBoard();

	void generate(Board board, String row, String column, String move);

	void setShareCombinations();
	
}
