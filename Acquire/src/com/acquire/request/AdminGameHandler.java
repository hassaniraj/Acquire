package com.acquire.request;

import java.util.ArrayList;
import java.util.List;

import com.acquire.board.Board;
import com.acquire.player.Player;
import com.acquire.player.strategy.LargestAlphaStrategy;
import com.acquire.player.strategy.RandomPlayerStrategy;
import com.acquire.player.strategy.SequentialPlayerStrategy;
import com.acquire.player.strategy.SmallestAntiStrategy;
import com.acquire.proxy.AdminController;
import com.acquire.proxy.AdminProxy;

public class AdminGameHandler {
	AdminController adminController = new AdminProxy();
	
	public Board initializeGame(){
		List<Player> players = new ArrayList<>();
		Player player1 = new Player();
		player1.setName("random");
		player1.setStrategy(new RandomPlayerStrategy());
		players.add(player1);

		Player player2 = new Player();
		player2.setName("sequential");
		player2.setStrategy(new SequentialPlayerStrategy());
		players.add(player2);
		
		Player player3 = new Player();
		player3.setName("largest-alpha");
		player3.setStrategy(new LargestAlphaStrategy());
		players.add(player3);

		Player player4 = new Player();
		player4.setName("smallest-anti");
		player4.setStrategy(new SmallestAntiStrategy());
		players.add(player4);

		

		Board board = adminController.init(players);
		return board;
	}

}
