package com.acquire.admin;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import com.acquire.board.Board;
import com.acquire.board.Game;
import com.acquire.board.Hotel;
import com.acquire.board.Tile;
import com.acquire.player.Player;

public interface Administrator {
	
	//Start game by initiating board state and players
	Board startGame(Game game, List <Player> players);
	
	//Get all occupied tiles on board
	Map<String, Hotel> getOccupiedTiles();
	
	//Get all empty tiles on board
//	List<String> getEmptyTiles();
	
	//Get random tiles from empty tiles
	String pickTiles();
	
	//Get money from bank and distribute to players
//	double distributeMoney();
	
	//Get player info
	List<Player> getPlayers(Game game, Board board);
	
	//get counter of tiles owned by player for a hotel
//	int getHotelCounter(String hotel, String player);
	
	//set counter of tiles owned by pplayer for a hotel
//	int setHotelCounter(String hotel, String player);
	
	//get list of hotels and remaining stock
	void getHotelShares(Board board, Player player, String label);
	
	//set stock price for a hotel
	void setStockPrice (String hotel, int price);
	
	//get stock price for a hotel
	int getStockPrice (String hotel);
	
	//get bonus for each player for hotel
	//double getBonus(String player, String hotel);
	
	//compute final score of each player
	//void getFinalScore(List<String> players, List <String> hotels);

	String setTile(Board board, Player player, Tile tile, String label);
	
	public void done(Player player, Board board);
	
	public Player getCurrentPlayer();

	void setCurrentPlayer(Player player);

	void initTiles(Random random, List<String> tiles);

	void setAllTiles(Board board);

	boolean isEnd();
	
	int getWorth(Player player);

	void setPlayersContainingShares(List<String> hotels, List<Player> players);

	void setCash(TreeMap<Integer, List<Player>> playerShareMap, String hotel);

	void sell(Board board, Set<String> acquired);

	List<String> getEmptyTiles();
	
	void removeTile(Board board, Tile tile);

	void addAllTiles(String tile);
}
