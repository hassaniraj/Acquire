package com.acquire.game.tree.action;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.acquire.admin.Administrator;
import com.acquire.admin.IAdministrator;
import com.acquire.board.Board;
import com.acquire.board.Chain;
import com.acquire.board.Game;
import com.acquire.board.Tile;
import com.acquire.config.Config;
import com.acquire.game.tree.state.StateClient;
import com.acquire.player.Player;

public class AdminTreeInspectorImpl implements AdminTreeInspector {
	private Administrator admin;
	private Board board;
	private StateClient root;
	
	@Override
	public Board init(List<Player> players) {
		admin = IAdministrator.getInstance();
		board = admin.startGame(Game.getInstance(), players);
		return board;
	}

	@Override 
	public void place(Tile tile, String type, StateClient state, Player player) {
		Board board = state.getState().getBoard();
		admin.setTile(board, player, tile, state.getHotel());
		for (String label: state.getShares()){ 
			admin.getHotelShares(board, player, label);
		}
		admin.done(player, board);
	}
	
	@Override
	public String getWinner(Board board) {
		TreeMap<Integer,Player> playerCash=new TreeMap<>(Collections.reverseOrder());
		List<Player> players = Game.getInstance().getGame(board);
		
		for (Player player : players) {
			int finalWorth = admin.getWorth(player);
			player.setCash(finalWorth);
			playerCash.put(player.getCash(), player);
		}
		return playerCash.firstEntry().getValue().getName();
	}
	
	@Override
	public List<String> emptyTiles() {
		return admin.getEmptyTiles();
	}

	@Override
	public StateClient getRoot() {
		// TODO Auto-generated method stub
		return root;
	}

	@Override
	public StateClient setRoot(StateClient stateClient) {
		root = stateClient;
		return root;
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
}
