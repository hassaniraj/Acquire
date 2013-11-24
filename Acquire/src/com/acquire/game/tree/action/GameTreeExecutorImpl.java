package com.acquire.game.tree.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import com.acquire.board.Board;
import com.acquire.board.Chain;
import com.acquire.board.Game;
import com.acquire.board.Labels;
import com.acquire.board.Tile;
import com.acquire.config.Config;
import com.acquire.game.tree.state.StateClient;
import com.acquire.player.Player;
import com.acquire.player.strategy.LargestAlphaStrategy;
import com.acquire.player.Share;
import com.acquire.player.strategy.MaximumSharesStrategy;
import com.acquire.player.strategy.MaximumWorthStrategy;
import com.acquire.player.strategy.RandomPlayerStrategy;
import com.acquire.player.strategy.SequentialPlayerStrategy;
import com.acquire.player.strategy.SmallestAntiStrategy;

public class GameTreeExecutorImpl implements GameTreeExecutor {
	private static Iterator<Player> playerIterator;
	private Player currentPlayer;
	private StateClient root;
	private List<Player> playersList;
	private List<Tile> allTiles;

	@Override
	public void generate() {
		// TODO Auto-generated method stub

	}

	@Override
	public Player getCurrentPLayer() {
		return currentPlayer;
	}

	@Override
	public StateClient setupTree() {
		StateClient stateClient = new StateClient();
		return stateClient;
	}

	public static void main(String[] args) {
		Map<String, Integer> results = new HashMap<>();
		
		results.put("random1", 0);
		results.put("random2", 0);
		results.put("worth", 0);
		results.put("shares", 0);
		
		
		AdminTreeInspector adminTreeInspector = new AdminTreeInspectorImpl();
		GameTreeExecutor gameTreeExecutor = new GameTreeExecutorImpl();
		PlayerTreeInspector playerTreeInspector = new PlayerTreeInspectorImpl();
		for (int i = 0; i < 1000; i++) {
			List<Player> playersList = new ArrayList<>();
			
			Player player1 = new Player();
			player1.setName("random1");
			player1.setStrategy(new RandomPlayerStrategy());
			playersList.add(player1);

			Player player2 = new Player();
			player2.setName("random2");
			player2.setStrategy(new RandomPlayerStrategy());
			playersList.add(player2);
			
			Player player4 = new Player();
			player4.setName("shares");
			player4.setStrategy(new MaximumSharesStrategy());
			playersList.add(player4);
			
			Player player3 = new Player();
			player3.setName("worth");
			player3.setStrategy(new MaximumWorthStrategy());
			playersList.add(player3);
			List<Player> players=new ArrayList<>();
			Random random=new Random();
			int size=playersList.size();
			for(int j=0;j<size;j++){
				int index=random.nextInt(playersList.size());
				players.add(playersList.remove(index));
			}
			System.out.println(players.size());
			Board board = adminTreeInspector.init(players);
			players = Game.getInstance().getGame(board);
			gameTreeExecutor = new GameTreeExecutorImpl();
			gameTreeExecutor.setUpIterator(players);
			gameTreeExecutor.setAllTiles(board);
			StateClient stateClient = gameTreeExecutor.setupTree();

			for (Player player : players)
				stateClient.getState().setPlayerList(player, player.getTile());

			stateClient = gameTreeExecutor.generateTree(board, stateClient,
					players, adminTreeInspector.emptyTiles());
			gameTreeExecutor.playGame(board, adminTreeInspector, stateClient,
					playerTreeInspector);

			// System.out.println(adminController.getWinner());
			// List<Player> playersFinalScore =
			 Game.getInstance().getGame(board);
			String winner = adminTreeInspector.getWinner(board);
			results.put(winner, results.get(winner) + 1);
			// System.out.println("Game ended! ");

		}
		// if (results.get(adminController.getWinner()) != null)
		// current += results.get(adminController.getWinner());

		System.out.println("Final Stats: " + results);

	}

	@Override
	public void playGame(Board board, AdminTreeInspector adminTreeInspector,
			StateClient stateClient, PlayerTreeInspector playerTreeInspector) {
		playersList = new ArrayList<Player>(Game.getInstance().getGame(board));
		root = stateClient;
		// List<Tile> tiles = root.getNextPlayer().getTile();
		int i = 0;
		while (!isEnd()) {
			System.out.println(i++);

			if (i == 54) {
				System.out.println(i);
			}
			// root.setPlayer(currentPlayer);
			// List<Tile> tiles = root.getState().getPlayers()
			// .get(root.getPlayer());
			// for (Tile tile : tiles) {
			// Map<String, Object> result = root.generate(root.getState()
			// .getBoard(), tile.getRow(), tile.getColumn());
			//
			// StateClient child = new StateClient();
			// child.setMove(result.get("move").toString());
			//
			// child.setTile((Tile) result.get("tile"));
			// root.getChildren().add(child);
			// }
			List<Tile> tiles = root.getNextPlayer().getTile();
			System.out.println(root.getNextPlayer().getName());
			for (Tile tile : tiles) {
				System.out.print(tile.getColumn() + tile.getRow() + " ");
			}
			System.out.println("");
			StateClient state = playerTreeInspector.pickState(root,
					root.getNextPlayer());
			if (state == null)
				break;
			if (isEnd())
				break;
			// System.out.println(state.getMove());
			// System.out.println(state.getTile().getColumn()
			// + state.getTile().getRow());
			adminTreeInspector.place(state.getTile(), state.getMove(), state,
					root.getNextPlayer());
			System.out.println("Tiles on board "
					+ state.getState().getBoard().getOccupiedTiles().size());
			System.out.println("Tile placed:" + state.getTile().getColumn()
					+ state.getTile().getRow());

			root.getState().setShareCombinations();
			state.getState().setHotels(getHotels());
			// root = state;
			rotate(root.getState().getBoard());
			root = generateTree(state.getState().getBoard(), state,
					playersList, adminTreeInspector.emptyTiles());

		}

		List<Player> playersFinalScore = Game.getInstance().getGame(board);
		String winner = adminTreeInspector.getWinner(board);
		System.out.println("The winner is " + winner);
	}

	@Override
	public void setUpIterator(List<Player> players) {
		playerIterator = players.iterator();
		currentPlayer = playerIterator.next();
	}

	@Override
	public void rotate(Board board) {
		playersList.remove(0);
		playersList.add(currentPlayer);
		currentPlayer = playersList.get(0);
	}

	@Override
	public boolean isEnd() {
		Map<String, List<String>> chainLabels = new HashMap<>(Chain.getChain());
		for (String chain : Chain.getChain().keySet()) {
			if (Chain.getChain(chain).isEmpty())
				chainLabels.remove(chain);
		}
		for (String chainName : chainLabels.keySet()) {
			if (Chain.getChain(chainName).size() >= Config.MAX_CHAIN_SIZE) {
				return true;
			}
		}

		Map<Integer, String> chainLabelsSize = new TreeMap<>();
		for (String chainName : chainLabels.keySet()) {
			chainLabelsSize.put(chainLabels.get(chainName).size(), chainName);
		}
		if (!chainLabelsSize.isEmpty())
			if (chainLabelsSize.keySet().iterator().next() >= Config.SAFE_CHAIN_SIZE)
				return true;
		return false;
	}

	@Override
	public List<String> getHotels() {
		Set<String> hotels = Board.getInstance().getHotelTiles().keySet();
		List<String> available = new ArrayList<>(Labels.getLabels());
		available.removeAll(hotels);
		return available;
	}

	@Override
	public StateClient generateTree(Board board, StateClient stateClient,
			List<Player> players, List<String> remainingTiles) {
		root = stateClient;
		stateClient.getState().setBoard(board);
		StateClient state = root;
		root.setPath(new ArrayList<String>());
		// for (Player name: players) {
		// System.out.println(name.getName());
		// }
		// List<Player> players = Game.getInstance().getGame(board);
		root = generateChildren(state, players, 0, remainingTiles);
		return root;
		// for (int i = 0; i < players.size(); i++) {

		// }
	}

	@Override
	public StateClient generateChildren(StateClient state,
			List<Player> players, int playerCount, List<String> remainingTiles) {
		if (playerCount == 2) {
			return state;
		}

		state.setNextPlayer(players.get(playerCount));
		state.setChildren(new ArrayList<StateClient>());
		List<Tile> tiles = state.getState().getPlayers()
				.get(state.getNextPlayer());

		// for (Tile tile : tiles) {
		// System.out.print(tile.getColumn()+tile.getRow() + " ");
		// }

		// playerCount++;
		if (playerCount < 1) {
			for (Tile tile : tiles) {
				if (!state.getPath().contains(
						tile.getTileLabel(tile.getRow(), tile.getColumn()))) {
					// System.out.println("\nPlayer " + playerCount);
					//
					// System.out.println("Current tile: " + tile.getColumn() +
					// tile.getRow());
					Map<String, Object> result = state.generate(state
							.getState().getBoard(), tile.getRow(), tile
							.getColumn());

					StateClient child = new StateClient();
					child.setMove(result.get("move").toString());

					child.setTile((Tile) result.get("tile"));
					state.getChildren().add(child);
					child.setPlayer(players.get(playerCount));
					List<String> newPath = new ArrayList<>(state.getPath());
					if (state.getTile() != null)
						newPath.add(state.getTile().getTileLabel(
								state.getTile().getRow(),
								state.getTile().getColumn()));
					else
						newPath.add("root");
					child.setPath(newPath);
					// System.out.println(child.getPath());

					generateChildren(child, players, playerCount + 1,
							remainingTiles);
					// System.out.println("\nPlayer Count:"+playerCount);
				}
			}
		} else {
			List<Tile> remaining = getAllTiles(state.getState().getBoard(),
					state.getNextPlayer().getTile());
			for (Tile tile : remaining) {

				if (!state.getPath().contains(
						tile.getTileLabel(tile.getRow(), tile.getColumn()))) {
					// System.out.println("\nPlayer " + playerCount);
					//
					// System.out.println("Current tile: " + tile.getColumn() +
					// tile.getRow());
					Map<String, Object> result = state.generate(state
							.getState().getBoard(), tile.getRow(), tile
							.getColumn());

					StateClient child = new StateClient();
					child.setMove(result.get("move").toString());

					child.setTile((Tile) result.get("tile"));
					state.getChildren().add(child);
					child.setPlayer(players.get(playerCount));
					List<String> newPath = new ArrayList<>(state.getPath());
					if (state.getTile() != null)
						newPath.add(state.getTile().getTileLabel(
								state.getTile().getRow(),
								state.getTile().getColumn()));
					else
						newPath.add("root");
					child.setPath(newPath);
					// System.out.println(child.getPath());

					generateChildren(child, players, playerCount + 1,
							remainingTiles);
					// System.out.println("\nPlayer Count:"+playerCount);
				}
			}
		}
		return state;
	}

	@Override
	public List<Tile> getAllTiles(Board board, List<Tile> playerTiles) {
		List<Tile> allTilesClone = new ArrayList<>(allTiles);
		List<Tile> boardTiles = new ArrayList<Tile>();
		for (String tile : board.getOccupiedTiles().keySet()) {
			Tile t = new Tile();
			t.setTileLabel(tile);
			boardTiles.add(t);
		}

		for (Tile tile : playerTiles) {
			// System.out.println(tile.getTileLabel(tile.getRow(),
			// tile.getColumn()));
			allTilesClone.remove(allTilesClone.indexOf(tile));
		}

		for (Tile boardTile : boardTiles) {
			allTilesClone.remove(allTilesClone.indexOf(boardTile));
		}
		// System.out.println("SIze:" + allTilesClone.size());
		return allTilesClone;
	}

	@Override
	public void setAllTiles(Board board) {
		allTiles = new ArrayList<>();
		for (String col : board.getColumns())
			for (String row : board.getRows()) {
				Tile tile = new Tile();
				tile.setColumn(col);
				tile.setRow(row);
				allTiles.add(tile);
			}
	}

	public Map<StateClient, Integer> minimaxExecute(Board board,
			StateClient root, boolean MAX) {
		Map<StateClient, Integer> score = new HashMap<>();
		score.put(root, -9999999);
		minimax(board, score, MAX, -9999999);
		return null;
	}

	public Map<StateClient, Integer> minimax(Board board,
			Map<StateClient, Integer> scoreMap, boolean MAX, int alpha) {
		StateClient root = scoreMap.keySet().iterator().next();
		if (root.getNextPlayer() == null) {
			// return heuristic
			return null;
		}
		if (MAX) {
			List<StateClient> children = root.getChildren();
			alpha = -9999999;
			for (StateClient child : children) {
				Map<StateClient, Integer> nextScore = new HashMap<>();
				nextScore.put(child, alpha);
				Map<StateClient, Integer> value = minimax(board, nextScore,
						!MAX, alpha);
				alpha = alpha < value.get(child) ? nextScore.put(child,
						value.get(child)) : nextScore.put(child, alpha);
				return nextScore;
			}
		} else if (!MAX) {
			List<StateClient> children = root.getChildren();
			alpha = 9999999;
			for (StateClient child : children) {
				Map<StateClient, Integer> nextScore = new HashMap<>();
				nextScore.put(child, alpha);
				Map<StateClient, Integer> value = minimax(board, nextScore,
						!MAX, alpha);
				alpha = alpha > value.get(child) ? nextScore.put(child,
						value.get(child)) : nextScore.put(child, alpha);
				return nextScore;
			}
		}
		return null;
	}

	public List<Player> evaluationOnWorth(List<Player> players) {
		for (Player player : players) {
			Map<String, Integer> playerShares = player.getShares();
			int cash = player.getCash();
			int worthOfAPlayer = 0;
			for (Map.Entry<String, Integer> playerSharesEntry : playerShares
					.entrySet()) {
				String label = playerSharesEntry.getKey();
				int numberOfShares = playerSharesEntry.getValue();
				int sharePrice = Share.getSharePrice(label);
				worthOfAPlayer += (sharePrice * numberOfShares);
			}
			worthOfAPlayer += cash;
			player.setPresentWorth(worthOfAPlayer);
		}
		return players;
	}

	public Player evaluationOnShares(Player player) {
		Map<String, Integer> chainMap = new HashMap<>();
		Map<String, List<String>> chainLabels = new HashMap<>(Chain.getChain());
		int max = 0;
		String hotelName = "";
		for (String chain : Chain.getChain().keySet()) {
			if (Chain.getChain(chain).isEmpty())
				chainLabels.remove(chain);
		}
		for (Map.Entry<String, List<String>> entry : chainLabels.entrySet()) {
			int count = entry.getValue().size();
			chainMap.put(entry.getKey(), count);
		}
		for (Map.Entry<String, Integer> entry : chainMap.entrySet()) {
			if (entry.getValue() > max) {
				max = entry.getValue();
				hotelName = entry.getKey();
			}
		}
		if (player.getShares().containsKey(hotelName)) {
			int score = player.getShare(hotelName);
			player.setScore(score);

		} else {
			player.setScore(0);
		}
		return player;
	}

}
