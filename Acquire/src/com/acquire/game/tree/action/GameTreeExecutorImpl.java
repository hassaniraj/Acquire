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
import com.acquire.factory.BoardFactory;
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

		players = adminTreeInspector.init(players);
		gameTreeExecutor = new GameTreeExecutorImpl();
		gameTreeExecutor.setUpIterator(players);
		StateClient stateClient = gameTreeExecutor.setupTree();

		for (Player player : players)
			stateClient.getState().setPlayerList(player, player.getTile());
		gameTreeExecutor.playGame(adminTreeInspector, stateClient,
				playerTreeInspector);

	}

	@Override
	public void playGame(AdminTreeInspector adminTreeInspector,
			StateClient stateClient, PlayerTreeInspector playerTreeInspector) {
		root = stateClient;
		while (true) {
			root.setPlayer(currentPlayer);
			List<Tile> tiles = root.getState().getPlayers()
					.get(root.getPlayer());
			for (Tile tile : tiles) {
				Map<String, Object> result = root.generate(root.getState()
						.getBoard(), tile.getRow(), tile.getColumn());
				
				StateClient child = new StateClient();
				child.setMove(result.get("move").toString());

				child.setTile((Tile) result.get("tile"));
				root.getChildren().add(child);
			}
			
			StateClient state = playerTreeInspector.pickState(root,
					root.getPlayer());
			if (state == null) break;
			if (isEnd()) break;
			System.out.println(state.getMove());
			System.out.println(state.getTile().getColumn()
					+ state.getTile().getRow());
			adminTreeInspector.place(state.getTile(), state.getMove(), state,
					this.root.getPlayer());
			root.getState().setShareCombinations();
			state.getState().setHotels(getHotels());
			root = state;
			rotate();
		}
		
		List<Player> playersFinalScore = Game.getInstance().getGame(BoardFactory.getBoard());
		String winner = adminTreeInspector.getWinner();
		for (Player p: playersFinalScore) 
			System.out.println(p.getName()+ " : " + p.getCash());
		System.out.println(winner);
	}

	@Override
	public void setUpIterator(List<Player> players) {
		playerIterator = players.iterator();
		currentPlayer = playerIterator.next();
	}

	@Override
	public void rotate() {
		if (playerIterator.hasNext()) {
			currentPlayer = playerIterator.next();
		} else {
			playerIterator = Game.getInstance()
					.getGame(BoardFactory.getBoard()).iterator();
			currentPlayer = playerIterator.next();
		}
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
}