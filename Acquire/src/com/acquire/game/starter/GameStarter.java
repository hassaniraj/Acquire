package com.acquire.game.starter;

import java.util.ArrayList;
import java.util.List;

import com.acquire.board.Board;
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

public class GameStarter {
	
	public static void main(String args[]) {
		List<Player> players = new ArrayList<>();
		Player player1 = new Player();
		player1.setName("random");
		player1.setStrategy(new RandomPlayerStrategy());
		players.add(player1);
//		Player player2 = new Player();
//		player2.setName("sequential");
//		player2.setStrategy(new SequentialPlayerStrategy());
//		players.add(player2);
//		Player player3 = new Player();
//		player3.setName("largest-alpha");
//		player3.setStrategy(new LargestAlphaStrategy());
//		players.add(player3);
//		Player player4 = new Player();
//		player4.setName("smallest-anti");
//		player4.setStrategy(new SmallestAntiStrategy());
//		players.add(player4);
//		Administrator admin = IAdministrator.getInstance();
//		
//		admin.startGame(Game.getInstance(), new ArrayList<>(Arrays.asList(player1, player2, player3, player4)));
		AdminController adminController = new AdminProxy();
		PlayerController playerController = new PlayerProxy();
		adminController.init(players);
		
		
		try {
		while(!adminController.checkIfEnd()) {
			System.out.println("\n" + Board.getInstance().getBoard().keySet());
			
			Player player = adminController.getCurrent();
			List<Tile> tiles = player.getTile();
			System.out.println("\nBefore place:");
			for (Tile tile : tiles) System.out.print( " " + tile.getTileLabel(tile.row, tile.column));
			List<Object> response = playerController.playPlace(player, adminController.getHotels());
			if (response.isEmpty()) break;
			Tile t = (Tile) response.get(0);
			System.out.println("\nTile : " + t.getTileLabel(t.row, t.column));
			adminController.playerPlaceMove((Tile) response.get(0), response.get(1).toString());
			System.out.println("\nAfter place:");
			for (Tile tile : tiles) System.out.print( " " + tile.getTileLabel(tile.row, tile.column));
			System.out.println(Board.getInstance().getHotelTiles());
			List<String> hotels = playerController.playBuy(player);
			for (String hotel: hotels) {
				System.out.println("\nBuying .." + hotel);
				adminController.playerBuyMove(hotel);
			}
			adminController.playerDone();
			
		}
		} catch (AcquireException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
