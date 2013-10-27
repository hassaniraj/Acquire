package com.acquire.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import com.acquire.actions.AcquireActions;
import com.acquire.board.Board;
import com.acquire.board.Chain;
import com.acquire.board.Game;
import com.acquire.board.Hotel;
import com.acquire.board.SharePriceMapper;
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
		board.clear();
		board = BoardFactory.getBoard();		
		
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
		if (game.getGame(board) != null)
		game.getGame(board).clear();
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
		String type = acquireActions.inspect(board, tile.getRow(), tile.getColumn());
		if (type.equals("singleton")) {
			acquireActions.singleton(board, tile.getRow(), tile.getColumn());
			player.removeTile(tile);
		} else if (type.equals("growing")) {
			acquireActions.growing(board, tile.getRow(), tile.getColumn());
			player.removeTile(tile);
		} else if (type.equals("founding") && !label.equals("") && label != null) {
			acquireActions.founding(board, tile.getRow(), tile.getColumn(), label);
			player.setShare(label, player.getShare(label) + 1);
			Share.setShare(label, Share.getShare(label) - 1);
			player.removeTile(tile);
			
		} else if (type.equals("merging")) {
			Map<String, List<String>> hotelList = acquireActions.getLabel(
					board, tile.getRow(), tile.getColumn());
			List<String> h = new ArrayList<String>(hotelList.keySet());
			acquireActions.merging(board,tile.getRow(), tile.getColumn(), h.get(0));
			player.removeTile(tile);
		} else {
			Hotel hotel = new Hotel();
			hotel.setLabel("singleton");
			board.getBoard().put(tile.getColumn() + tile.getRow(), hotel);
			player.removeTile(tile);
		}
		
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

	//Get the hotel shares and set the new share price
	@Override
	public void getHotelShares(Board board, Player player, String label) {
		int share = Share.getShare(label);
		if (Chain.getChain(label).size() > 0) {
			if (share > 0 && player.getCash() > Share.getSharePrice(label)) {
				int chainSize = Chain.getChain(label).size();
				Share.setShare(label, --share);
				if (label.equalsIgnoreCase("WorldWide")
						|| label.equalsIgnoreCase("Sackson")) {
					TreeMap<Integer, Integer> priceMapper=SharePriceMapper.getWorldwideSacksonPriceMap();
					if (SharePriceMapper.getWorldwideSacksonPriceMap()
							.containsKey(chainSize)) {
						setSharePriceAndCashWhenExactSharesAvailable(player, label, chainSize,priceMapper);
					} else {
						setSharePriceAndCashWhenExactSharesNotAvailable(player,
								label, chainSize,priceMapper);
					}
				} else if (label.equalsIgnoreCase("Festival")
						|| label.equalsIgnoreCase("Imperial")
						|| label.equalsIgnoreCase("American")) {
					TreeMap<Integer, Integer> priceMapper=SharePriceMapper.getFestivalImperialAmericanPriceMap();
					if (priceMapper
							.containsKey(chainSize)) {
						setSharePriceAndCashWhenExactSharesAvailable(player, label, chainSize,priceMapper);
					} else {
						setSharePriceAndCashWhenExactSharesNotAvailable(player,
								label, chainSize,priceMapper);
					}
				} else if (SharePriceMapper.getContinentalTowerPriceMap()
						.containsKey(chainSize)) {
					TreeMap<Integer, Integer> priceMapper=SharePriceMapper.getContinentalTowerPriceMap();
					setSharePriceAndCashWhenExactSharesAvailable(player, label, chainSize,priceMapper);
				} else {
					TreeMap<Integer, Integer> priceMapper=SharePriceMapper.getContinentalTowerPriceMap();
					setSharePriceAndCashWhenExactSharesNotAvailable(player,
							label, chainSize,priceMapper);
				}
				player.setShare(label, player.getShare(label) + 1);
				// player.setCash(player.getCash() - 300);
			}
		}
	}

	private void setSharePriceAndCashWhenExactSharesNotAvailable(Player player,
			String label, int chainSize,TreeMap<Integer, Integer> priceMapper) {
		Share.setSharePrice(label,
				priceMapper
						.lowerEntry(chainSize).getValue());
		player.setCash(player.getCash()
				- priceMapper
						.lowerEntry(chainSize).getValue());
	}

	private void setSharePriceAndCashWhenExactSharesAvailable(Player player, String label, int chainSize,TreeMap<Integer, Integer> priceMapper) {
		Share.setSharePrice(label, priceMapper.get(chainSize));
		player.setCash(player.getCash()
				- priceMapper.get(
								chainSize));
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
	
	@Override
	public boolean isEnd() {
		Set<String> chainLabels = Chain.getChainLabel();
		for (String chainName: chainLabels) {
			if (Chain.getChain(chainName).size() >= 41) return true;
		}
		return false;
	}

	@Override
	public int getWorth(Player player) {
		Map<String, Integer> playerShares = player.getShares();
		int cash = player.getCash();
		int	worthOfAPlayer=0;
		for (Map.Entry<String, Integer> playerSharesEntry : playerShares
				.entrySet()) {
			String label = playerSharesEntry.getKey();
			int numberOfShares = playerSharesEntry.getValue();
			int sharePrice = Share.getSharePrice(label);
			worthOfAPlayer=(sharePrice*numberOfShares)+cash;
		}
		return worthOfAPlayer;
	}

}
