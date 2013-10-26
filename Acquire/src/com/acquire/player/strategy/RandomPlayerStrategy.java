package com.acquire.player.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.acquire.admin.Administrator;
import com.acquire.admin.IAdministrator;
import com.acquire.board.Board;
import com.acquire.board.Chain;
import com.acquire.board.Labels;
import com.acquire.board.Tile;
import com.acquire.exception.AcquireException;
import com.acquire.factory.BoardFactory;
import com.acquire.player.Player;

public class RandomPlayerStrategy implements PlayerStrategy{

	@Override
	public List<Object> playTile(Player player, List<String> hotels) {
//		Administrator administrator = IAdministrator.getInstance();
//		Board board = BoardFactory.getBoard();
//		administrator.setCurrentPlayer(player);
		List<Tile> tiles = player.getTile();
		String label = "";
		if (hotels.size() > 0)
			label = pickHotel(hotels);
		if (!tiles.isEmpty()) {
			Tile tile = pickTile(tiles);
		
//		String type = administrator.setTile(board, player, tile, label);
//		if (type.equals("founding") || type.equals("merge")) {
//			return Arrays.asList(tile, label);
//		}
		return new ArrayList<Object>(Arrays.asList(tile, label));
		}
		return new ArrayList<Object>();
	}

	@Override
	public List<String> buyShare() throws AcquireException {
//		Administrator administrator = IAdministrator.getInstance();
//		Board board = BoardFactory.getBoard();
//		Player player = administrator.getCurrentPlayer();
		List <String> hotels = new ArrayList<>();
		int number = new Random().nextInt(2);
		for (int i = 0; i < number + 1; i++) {
			List <String> labels = new ArrayList<>();
			labels.addAll(Labels.getLabels());
			
			labels.removeAll(Chain.getChainLabel());
			if (labels.size() > 0) {
			String hotel = pickHotel(labels);
			if (!Chain.getChain(hotel).isEmpty()) {
				hotels.add(hotel);
//				administrator.getHotelShares(board, player, hotel);
			}
			}
		}
		return hotels;
	}
	
	public String pickHotel(List<String> hotels) {
		Random random = new Random();
		int i = random.nextInt(hotels.size());
		return hotels.get(i);
	}
	
	public Tile pickTile(List <Tile> tiles ) {
		Random random = new Random();
		
		return tiles.get(random.nextInt(tiles.size()));
	}

}
