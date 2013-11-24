package com.acquire.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import com.acquire.config.Config;
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
		System.out.println(board.getColumns());
		System.out.println(board.getRows());
		for (String column : board.getColumns()) {
			for (String row : board.getRows()) {
				allTiles.add(new Tile().getTileLabel(row, column));
			}
		}
		System.out.println(allTiles.size());
	}

	@Override
	public Board startGame(Game game, List<Player> players) {		
		Board board = BoardFactory.getBoard();
//		board.clear();
//		board = BoardFactory.getBoard();		
		
		Random rand = new Random();
		List<String> tiles = new ArrayList<String>();
		this.initTiles(rand, tiles);
		setAllTiles(board);
		new Share();
		new Chain();
		for (Player player : players) {
			for (int i = 0; i < 6; i++) {
				String t = pickTiles();
				if (t != null) {
					String tile = t;
					player.setTile(tile.substring(tile.length() - 1),
							tile.substring(0, tile.length() - 1));
				}
			}
			player.setCash(6000);
		}
		if (game.getGame(board) != null)
		game.getGame(board).clear();
		game.setGame(board, players);
//		playerIterator = players.iterator();
//		currentPlayer = playerIterator.next();
		return board;
	}

	@Override
	public Map<String, Hotel> getOccupiedTiles() {
		return null;
	}

	@Override
	public String setTile(Board board, Player player, Tile tile, String label) {
		AcquireActions acquireActions = AcquireActionsFactory.getInstance();
		String type = acquireActions.inspect(board, tile.getRow(), tile.getColumn());
		System.out.println(type);
		if (type == null) {
			System.out.println(type);
		}
		if (type.equals("merging")) {
			System.out.println("merging case");
		}
		if (type.equals(Config.Moves.SINGLETON.getMove())) {
			acquireActions.singleton(board, tile.getRow(), tile.getColumn());
			player.removeTile(tile);
			return type;
		} else if (type.equals(Config.Moves.GROWING.getMove())) {
			acquireActions.growing(board, tile.getRow(), tile.getColumn());
			player.removeTile(tile);
			return type;
		} else if (label != null) {
			if (type.equals(Config.Moves.FOUNDING.getMove()) && !label.equals("")) {
			acquireActions.founding(board, tile.getRow(), tile.getColumn(), label);
			player.setShare(label, player.getShare(label) + 1);
			Share.setShare(label, Share.getShare(label) - 1);
			player.removeTile(tile);
			return type;
		
		} else if (type.equals(Config.Moves.MERGING.getMove())) {
			Map<String, List<String>> hotelList = acquireActions.getLabel(
					board, tile.getRow(), tile.getColumn());
			List<String> h = new ArrayList<String>(hotelList.keySet());
			Set<String> acquired = acquireActions.merging(board,tile.getRow(), tile.getColumn(), h.get(0));
			if (acquired.size() > 1) {
				setBonus(board, acquired);
				sell(board, acquired);
			}
			player.removeTile(tile);
			return type;
		}} else {
			Hotel hotel = new Hotel();
			hotel.setLabel(Config.Moves.SINGLETON.getMove());
			board.getBoard().put(tile.getColumn() + tile.getRow(), hotel);
			player.removeTile(tile);
			return Config.Moves.SINGLETON.getMove();
		}
		System.out.println("No match");
		return null;
	}

	@Override
	public List<String> getEmptyTiles() {
		// TODO Auto-generated method stub
		return allTiles;
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

//	@Override
//	public double distributeMoney() {
//		return 0;
//	}

	@Override
	public List<Player> getPlayers(Game game, Board board) {
		return game.getGame(board);
	}

//	@Override
//	public int getHotelCounter(String hotel, String player) {
//		return 0;
//	}
//
//	@Override
//	public int setHotelCounter(String hotel, String player) {
//		// TODO Auto-generated method stub
//		return 0;
//	}

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
				} else if (label.equalsIgnoreCase("Continental")
							|| label.equalsIgnoreCase("Tower")){
					TreeMap<Integer, Integer> priceMapper=SharePriceMapper.getContinentalTowerPriceMap();
					if (SharePriceMapper.getContinentalTowerPriceMap()
							.containsKey(chainSize)) {
						setSharePriceAndCashWhenExactSharesAvailable(player, label, chainSize,priceMapper);
					} else {
						setSharePriceAndCashWhenExactSharesNotAvailable(player,
								label, chainSize,priceMapper);
					}
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

//
//	@Override
//	public void getFinalScore(List<String> players, List<String> hotels) {
//		// TODO Auto-generated method stub
//
//	}

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
		String tile = pickTiles();
		if (tile != null) {
			String newTile = tile;
			player.setTile(newTile.substring(newTile.length() - 1),
					newTile.substring(0, newTile.length() - 1));
		}
//		if (playerIterator.hasNext()) {
//			currentPlayer = playerIterator.next();
//		} else {
//			playerIterator = Game.getInstance().getGame(board).iterator();
//			currentPlayer = playerIterator.next();
//		}

	}
	
	@Override
	public boolean isEnd() {
		Map<String, List<String>> chainLabels = new HashMap<>(Chain.getChain());
		for (String chain: Chain.getChain().keySet()) {
			if (Chain.getChain(chain).isEmpty()) chainLabels.remove(chain);
		}
		for (String chainName: chainLabels.keySet()) {
			if (Chain.getChain(chainName).size() >= Config.MAX_CHAIN_SIZE) {
				return true;
			}
		}
		
		Map <Integer, String> chainLabelsSize = new TreeMap<>();
		for (String chainName: chainLabels.keySet()) {
			chainLabelsSize.put(chainLabels.get(chainName).size(), chainName);
		}
		if (!chainLabelsSize.isEmpty())
		if (chainLabelsSize.keySet().iterator().next() >= Config.SAFE_CHAIN_SIZE)
			return true;
		
		if (allTiles.isEmpty()) 
			return true;
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
			worthOfAPlayer+=(sharePrice*numberOfShares);
		}
		worthOfAPlayer += cash;
		return worthOfAPlayer;
	}
	
	/**
	 * Method to set the bonus of the player
	 * 
	 * @param acquired
	 */
	public void setBonus(Board board, Set<String> acquired) {
		List<Player> players = Game.getInstance().getGame(board);
		List<String> hotels = new ArrayList<>(acquired);
		hotels.remove(0);
		setPlayersContainingShares(hotels, players);
	}

	/**
	 * Method to set the players containing the cash in a map
	 * 
	 * @param hotels
	 * @param players
	 */

	@Override
	public void setPlayersContainingShares(List<String> hotels,
			List<Player> players) {
		TreeMap<Integer, List<Player>> playerShareMap = new TreeMap<>(
				Collections.reverseOrder());
		for (String hotel : hotels) {
			for (Player player : players) {
				if (player.getShares().containsKey(hotel)) {
					int playerShares = player.getShare(hotel);
					if (playerShareMap.containsKey(playerShares)) {
						playerShareMap.get(playerShares).add(player);
					} else {
						List<Player> playerList = new ArrayList<>();
						playerList.add(player);
						playerShareMap.put(playerShares, playerList);
					}
				}
			}
			setCash(playerShareMap, hotel);
		}
	}

	/**
	 * Method to set cash for the player for the bonus
	 * 
	 * @param playerShareMap
	 * @param hotel
	 */
	@Override
	public void setCash(TreeMap<Integer, List<Player>> playerShareMap,
			String hotel) {
		boolean isFirstPlayer = true;
		for (Map.Entry<Integer, List<Player>> entry : playerShareMap.entrySet()) {
			if (entry.getValue().size() > 1) {
				if (isFirstPlayer) {
					int price = Share.getSharePrice(hotel);
					price = price * 15;
					isFirstPlayer = false;
					for (Player player : entry.getValue()) {
						player.setCash(player.getCash()
								+ (price / entry.getValue().size()));
					}
					break;
				} else {
					int price = Share.getSharePrice(hotel);
					price = price * 5;
					for (Player player : entry.getValue()) {
						player.setCash(player.getCash()
								+ (price / entry.getValue().size()));
					}
					break;
				}

			} else if (entry.getValue().size() == 1) {
				if (isFirstPlayer) {
					int price = Share.getSharePrice(hotel);
					price = price * 10;
					isFirstPlayer = false;
					for (Player player : entry.getValue()) {
						player.setCash(player.getCash() + price);
					}
				} else {
					int price = Share.getSharePrice(hotel);
					price = price * 5;
					for (Player player : entry.getValue()) {
						player.setCash(player.getCash()
								+ (price / entry.getValue().size()));
					}
					break;
				}
			}
		}
	}

	/**
	 * Method to sell the shares for the player after getting the bonus
	 * 
	 * @param acquired
	 */
	@Override
	public void sell(Board board, Set<String> acquired) {
		List<String> hotels = new ArrayList<>(acquired);
		hotels.remove(0);
		List<Player> players = Game.getInstance().getGame(board);
		for (String hotel : hotels) {
			for (Player player : players) {
				if (player.getShares().containsKey(hotel)) {
					player.setCash(player.getCash()
							+ (player.getShare(hotel) * Share
									.getSharePrice(hotel)));
					player.setShare(hotel, 0);
					Share.setShare(hotel, 25);
					
				}
			}

		}
	}

}
