package com.acquire.game.starter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.acquire.board.Board;
import com.acquire.board.Chain;
import com.acquire.board.Game;
import com.acquire.board.Tile;
import com.acquire.exception.AcquireException;
import com.acquire.factory.BoardFactory;
import com.acquire.player.Player;
import com.acquire.player.strategy.LargestAlphaStrategy;
import com.acquire.player.strategy.RandomPlayerStrategy;
import com.acquire.player.strategy.SequentialPlayerStrategy;
import com.acquire.player.strategy.SmallestAntiStrategy;
import com.acquire.proxy.AdminController;
import com.acquire.proxy.AdminProxy;
import com.acquire.proxy.PlayerController;
import com.acquire.proxy.PlayerProxy;

public class GameController {

	public static void main(String args[]) {

		AdminController adminController = new AdminProxy();
		PlayerController playerController = new PlayerProxy();
		Map<String, Integer> results = new HashMap<>();
		
		results.put("random", 0);
		results.put("sequential", 0);
		results.put("largest-alpha", 0);
		results.put("smallest-anti", 0);
		
		try {
			for (int k = 0; k < 100; k++) {
				System.out.println("\nGame " + k);
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

				adminController.init(players);
				while (true) {
					Player player = adminController.getCurrent();
					List<Object> response = playerController.playPlace(player,
							adminController.getHotels());
					if (response.isEmpty())
						break;
					if (adminController.checkIfEnd()) {
						if (playerController.askEndGame()) break;
					}
					Tile t = (Tile) response.get(0);
					adminController.playerPlaceMove((Tile) response.get(0),
							response.get(1).toString());
					Tile tile = (Tile) response.get(0);
					List<String> hotels = playerController.playBuy(player);
					
					for (String hotel : hotels) {
						adminController.playerBuyMove(hotel);
					}

					adminController.playerDone();

				}
				
				System.out.println(adminController.getWinner());
				List<Player> playersFinalScore = Game.getInstance().getGame(BoardFactory.getBoard());
				for (Player p: playersFinalScore) 
					System.out.println(p.getName()+ " : " + p.getCash());
				String winner = adminController.getWinner();
//				System.out.println(results.get(winner));
				results.put(winner,
						results.get(winner) + 1);
//				System.out.println("Game ended! ");
				System.out.println("Stats: " + results);
		}
				// if (results.get(adminController.getWinner()) != null)
				// current += results.get(adminController.getWinner());

			
			System.out.println("Final Stats: " + results);
		
		} catch (AcquireException e) {
			System.out.println(e.getMessage());
		}
	}
}
