package com.acquire.game.starter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.acquire.board.Board;
import com.acquire.board.Chain;
import com.acquire.board.Tile;
import com.acquire.exception.AcquireException;
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
//			for (int i = 0; i < 1000; i++) {
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

				// System.out.println(adminController.getWinner());
				while (true) {
					// System.out.println("\n" +
					// Board.getInstance().getBoard().keySet());

					Player player = adminController.getCurrent();
					 List<Tile> tiles = player.getTile();
					 System.out.println("\nBefore place:");
					 for (Tile tile : tiles)
					 System.out.print(" "
					 + tile.getTileLabel(tile.getRow(), tile.getColumn()));
					List<Object> response = playerController.playPlace(player,
							adminController.getHotels());
					if (response.isEmpty())
						break;
					if (adminController.checkIfEnd()) {
						if (playerController.askEndGame()) break; 
					}
					Tile t = (Tile) response.get(0);
					// System.out.println("\nTile : " + t.getTileLabel(t.row,
					// t.column));
					adminController.playerPlaceMove((Tile) response.get(0),
							response.get(1).toString());
					// System.out.println("\nAfter place:");
					// for (Tile tile : tiles)
					// System.out.print(" "
					// + tile.getTileLabel(tile.row, tile.column));
					System.out.println(Board.getInstance().getHotelTiles());
					List<String> hotels = playerController.playBuy(player);
					for (String hotel : hotels) {
						// System.out.println("\nBuying .." + hotel +
						// " Share : " + Share.getSharePrice(hotel));

						adminController.playerBuyMove(hotel);
					}
					adminController.playerDone();

				}
				System.out.println(adminController.getWinner());
				results.put(adminController.getWinner(),
						results.get(adminController.getWinner()) + 1);

				// if (results.get(adminController.getWinner()) != null)
				// current += results.get(adminController.getWinner());

//			}
			System.out.println(results);
		} catch (AcquireException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
