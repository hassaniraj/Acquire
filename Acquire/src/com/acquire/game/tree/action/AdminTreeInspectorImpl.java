package com.acquire.game.tree.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.acquire.actions.AcquireActions;
import com.acquire.admin.Administrator;
import com.acquire.admin.IAdministrator;
import com.acquire.board.Board;
import com.acquire.board.Game;
import com.acquire.board.Tile;
import com.acquire.game.tree.state.StateClient;
import com.acquire.player.Player;

public class AdminTreeInspectorImpl implements AdminTreeInspector {
	Administrator admin;
	
	@Override
	public List<Player> init(List<Player> players) {
		admin = IAdministrator.getInstance();
		admin.startGame(Game.getInstance(), players);
		return players;
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
	public String getWinner() {
		TreeMap<Integer,Player> playerCash=new TreeMap<>(Collections.reverseOrder());
		List<Player> players = Game.getInstance().getGame(Board.getInstance());
		
		for (Player player : players) {
			int finalWorth = admin.getWorth(player);
			player.setCash(finalWorth);
			playerCash.put(player.getCash(), player);
		}
		return playerCash.firstEntry().getValue().getName();
	}
}
