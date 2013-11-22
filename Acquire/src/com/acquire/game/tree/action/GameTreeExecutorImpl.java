package com.acquire.game.tree.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.acquire.board.Board;
import com.acquire.board.Chain;
import com.acquire.board.Game;
import com.acquire.board.Labels;
import com.acquire.board.Tile;
import com.acquire.game.tree.state.StateClient;
import com.acquire.player.Player;
import com.acquire.player.strategy.LargestAlphaStrategy;
import com.acquire.player.strategy.RandomPlayerStrategy;
import com.acquire.player.strategy.SequentialPlayerStrategy;
import com.acquire.player.strategy.SmallestAntiStrategy;

public class GameTreeExecutorImpl implements GameTreeExecutor {
	private static Iterator<Player> playerIterator;
	private Player currentPlayer;
	private StateClient root;
	private List<Player> playersList;

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

		AdminTreeInspector adminTreeInspector = new AdminTreeInspectorImpl();
		GameTreeExecutor gameTreeExecutor = new GameTreeExecutorImpl();
		PlayerTreeInspector playerTreeInspector = new PlayerTreeInspectorImpl();

		List<Player> players = new ArrayList<>();
		Player player1 = new Player();
		player1.setName("random");
		player1.setStrategy(new RandomPlayerStrategy());
		players.add(player1);

		Player player2 = new Player();
		player2.setName("sequential");
		player2.setStrategy(new SequentialPlayerStrategy());
		players.add(player2);

		Player player4 = new Player();
		player4.setName("smallest-anti");
		player4.setStrategy(new SmallestAntiStrategy());
		players.add(player4);

		Player player3 = new Player();
		player3.setName("largest-alpha");
		player3.setStrategy(new LargestAlphaStrategy());
		players.add(player3);

		Board board = adminTreeInspector.init(players);
		players = Game.getInstance().getGame(board);
		gameTreeExecutor = new GameTreeExecutorImpl();
		gameTreeExecutor.setUpIterator(players);
		StateClient stateClient = gameTreeExecutor.setupTree();
		
		

		for (Player player : players)
			stateClient.getState().setPlayerList(player, player.getTile());
		
		stateClient = gameTreeExecutor.generateTree(board, stateClient, players);
		gameTreeExecutor.playGame(board, adminTreeInspector, stateClient,
				playerTreeInspector);

	}

	@Override
	public void playGame(Board board, AdminTreeInspector adminTreeInspector,
			StateClient stateClient, PlayerTreeInspector playerTreeInspector) {
		playersList = new ArrayList<Player>(Game.getInstance().getGame(board));
		root = stateClient;
		
		while (true) {
//			root.setPlayer(currentPlayer);
//			List<Tile> tiles = root.getState().getPlayers()
//					.get(root.getPlayer());
//			for (Tile tile : tiles) {
//				Map<String, Object> result = root.generate(root.getState()
//						.getBoard(), tile.getRow(), tile.getColumn());
//
//				StateClient child = new StateClient();
//				child.setMove(result.get("move").toString());
//
//				child.setTile((Tile) result.get("tile"));
//				root.getChildren().add(child);
//			}

			StateClient state = playerTreeInspector.pickState(root,
					root.getPlayer());
			if (state == null)
				break;
			if (isEnd())
				break;
			// System.out.println(state.getMove());
			// System.out.println(state.getTile().getColumn()
			// + state.getTile().getRow());
			adminTreeInspector.place(state.getTile(), state.getMove(), state,
					this.root.getPlayer());
			System.out.println("Tile placed:" + state.getTile().getColumn() + state.getTile().getRow());
			root.getState().setShareCombinations();
			state.getState().setHotels(getHotels());
			root = state;
			root = generateTree(root.getState().getBoard(), root, playersList);
			rotate(root.getState().getBoard());
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
			if (Chain.getChain(chainName).size() >= 41) {
				return true;
			}
		}

		Map<Integer, String> chainLabelsSize = new TreeMap<>();
		for (String chainName : chainLabels.keySet()) {
			chainLabelsSize.put(chainLabels.get(chainName).size(), chainName);
		}
		if (!chainLabelsSize.isEmpty())
			if (chainLabelsSize.keySet().iterator().next() >= 11)
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
	public StateClient generateTree(Board board,
			StateClient stateClient, List<Player> players) {
		root = stateClient;
		StateClient state = root;
		root.setPath(new ArrayList<String>());
		
		//List<Player> players = Game.getInstance().getGame(board);
		root = generateChildren(state, players, 0);
		return root;
		//for (int i = 0; i < players.size(); i++) {
			
		//}
	}
	
	@Override
	public StateClient generateChildren(StateClient state, List<Player> players, int playerCount) {
		if (playerCount == 4) { return state;}
		state.setPlayer(players.get(playerCount));
		
		List<Tile> tiles = state.getState().getPlayers()
				.get(state.getPlayer());
		
		for(Tile tile: tiles){
//			System.out.print(tile.getColumn()+tile.getRow() + " ");
		}
		
		//playerCount++;
		for (Tile tile : tiles) {
			if (!state.getPath().contains(tile.getTileLabel(tile.getRow(), tile.getColumn()))){
//			System.out.println("\nPlayer " + playerCount);
//			
//			System.out.println("Current tile: " + tile.getColumn() + tile.getRow());
			Map<String, Object> result = state.generate(state.getState()
					.getBoard(), tile.getRow(), tile.getColumn());
			
			StateClient child = new StateClient();
			child.setMove(result.get("move").toString());

			child.setTile((Tile) result.get("tile"));
			state.getChildren().add(child);
			List<String> newPath = new ArrayList<>(state.getPath());
			if (state.getTile()!= null) 
			newPath.add(state.getTile().getTileLabel(state.getTile().getRow(), state.getTile().getColumn()));
			else 
				newPath.add("root");
			child.setPath(newPath);
//			System.out.println(child.getPath());
			
			generateChildren(child, players, playerCount+1);
//			System.out.println("\nPlayer Count:"+playerCount);
			}
		}
		return state;
	}
}
