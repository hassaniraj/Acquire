package com.acquire.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.mockito.asm.tree.IntInsnNode;

import com.acquire.actions.AcquireActions;
import com.acquire.actions.IAcquireActions;
import com.acquire.board.Board;
import com.acquire.board.Chain;
import com.acquire.board.Game;
import com.acquire.board.Hotel;
import com.acquire.board.Labels;
import com.acquire.board.Tile;
import com.acquire.factory.AcquireActionsFactory;
import com.acquire.factory.BoardFactory;
import com.acquire.player.Player;
import com.acquire.player.Share;

public class IAdministrator implements Administrator {

	private List<String> allTiles;
	private Random randomTileGenerator;
	private Iterator<Player> playerIterator;
	private Player currentPlayer;
	private static Administrator administrator;

	@Override
	public void initTiles(Random random, List<String> tiles) {
		randomTileGenerator = random;
		allTiles = tiles;
	}
	
	public static synchronized Administrator getInstance() {
		if (administrator == null) {
			administrator = new IAdministrator();
		}
		return administrator;
	}

	@Override
	public void setAllTiles(Board board) {

		for (String column : board.getColumns()) {
			for (String row : board.getRows()) {
				allTiles.add(new Tile().getTileLabel(row, column));
			}
		}
	}

	@Override
	public Board startGame(Game game, List<Player> players) {
		Board board = BoardFactory.getBoard();
		board.getBoard().clear();
		Random rand = new Random();
		List<String> tiles = new ArrayList<String>();
		this.initTiles(rand, tiles);
		setAllTiles(board);
		new Share();
		new Chain();
		for (Player player : players) {
			for (int i = 0; i < 6; i++) {
				if (pickTiles() != null) {
					String tile = pickTiles();
					player.setTile(tile.substring(tile.length() - 1),
							tile.substring(0, tile.length() - 1));
				}
			}
			player.setCash(6000);
		}
		game.setGame(board, players);
		playerIterator = players.iterator();
		currentPlayer = playerIterator.next();
		return board;
	}

	@Override
	public Map<String, Hotel> getOccupiedTiles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String setTile(Board board, Player player, Tile tile, String label) {
		AcquireActions acquireActions = AcquireActionsFactory.getInstance();
		String type = acquireActions.inspect(board, tile.row, tile.column);
		if (type.equals("singleton")) {
			acquireActions.singleton(board, tile.row, tile.column);
		} else if (type.equals("growing")) {
			acquireActions.growing(board, tile.row, tile.column);
		} else if (type.equals("founding")) {
			acquireActions.founding(board, tile.row, tile.column, label);
			player.setShare(label, player.getShare(label) + 1);
			Share.setShare(label, Share.getShare(label) - 1);
		} else if (type.equals("merging")) {
			Map<String, List<String>> hotelList = acquireActions.getLabel(
					board, tile.row, tile.column);
			List<String> h = new ArrayList<String>(hotelList.keySet());
			acquireActions.merging(board, tile.row, tile.column, h.get(0));
		}
		player.removeTile(tile);
		return type;
	}

	@Override
	public List<String> getEmptyTiles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String pickTiles() {
		if (!allTiles.isEmpty()) {
			int index = randomTileGenerator.nextInt(allTiles.size());
			String tile = allTiles.get(index);
			allTiles.remove(index);
			return tile;
		}
		return null;
	}

	@Override
	public double distributeMoney() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Player> getPlayers(Game game, Board board) {
		return game.getGame(board);
	}

	@Override
	public int getHotelCounter(String hotel, String player) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int setHotelCounter(String hotel, String player) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void getHotelShares(Board board, Player player, String label) {
		int share = Share.getShare(label);
		if (Chain.getChain(label).size() > 0) {
		if (share > 0 && player.getCash() > Share.getSharePrice(label)) {
			Share.setShare(label, --share);
			player.setShare(label, player.getShare(label) + 1);
			player.setCash(player.getCash() - 300);
		}
		}
	}

	@Override
	public void setStockPrice(String hotel, int price) {
		Share.setShare(hotel, price);

	}

	@Override
	public int getStockPrice(String hotel) {
		return Share.getSharePrice(hotel);
	}

	@Override
	public double getBonus(String player, String hotel) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void getFinalScore(List<String> players, List<String> hotels) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public Player getCurrentPlayer() {
		return currentPlayer;

	}
	
	@Override
	public void setCurrentPlayer(Player player) {
		currentPlayer = player;

	}
	
	@Override
	public void done(Player player, Board board) {
		if (pickTiles() != null) {
			String newTile = pickTiles();
			player.setTile(newTile.substring(newTile.length() - 1),
					newTile.substring(0, newTile.length() - 1));
		}
		if (playerIterator.hasNext()) {
			currentPlayer = playerIterator.next();
		} else {
			playerIterator = Game.getInstance().getGame(board).iterator();
			currentPlayer = playerIterator.next();
		}
		
	}

}