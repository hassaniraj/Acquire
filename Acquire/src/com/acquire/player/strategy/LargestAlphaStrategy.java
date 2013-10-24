package com.acquire.player.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.sound.sampled.ReverbType;

import com.acquire.admin.Administrator;
import com.acquire.admin.IAdministrator;
import com.acquire.board.Board;
import com.acquire.board.Chain;
import com.acquire.board.Labels;
import com.acquire.board.Tile;
import com.acquire.exception.AcquireException;
import com.acquire.factory.BoardFactory;
import com.acquire.player.Player;
import com.acquire.player.Share;

public class LargestAlphaStrategy implements PlayerStrategy {

	@Override
	public List <Object> playTile(Player player, List<String> hotels) {
		Administrator administrator = IAdministrator.getInstance();
		Board board = BoardFactory.getBoard();
		administrator.setCurrentPlayer(player);
		List<Tile> tiles = player.getTile();
		String label = pickHotel(hotels);
		Tile tile = pickTile(tiles);
		
		String type = administrator.setTile(board, player, tile, label);
		if (type.equals("founding") || type.equals("merge")) {
			return Arrays.asList(tile, label);
		}
		
		else return new ArrayList<Object> (Arrays.asList(tile));
	}

	@Override
	public List <String> buyShare() {
		Administrator administrator = IAdministrator.getInstance();
		Board board = BoardFactory.getBoard();
		Player player = administrator.getCurrentPlayer();
		List <String> hotels = new ArrayList<>();
		List <String> labels = new ArrayList<>();
		labels.addAll(Labels.getLabels());
		for (int i = 0; i < labels.size(); i++) {
			if (Chain.getChain(labels.get(i)).isEmpty())
				labels.remove(i);
		}
		String hotel = pickHotel(labels);
		
		if (!Chain.getChain(hotel).isEmpty()) {
			hotels.add(hotel);
			administrator.getHotelShares(board, player, hotel);
		}
		hotel = pickHotel(labels);
		if (!Chain.getChain(hotel).isEmpty()) {
			hotels.add(hotel);
			administrator.getHotelShares(board, player, hotel);
		}
		hotel = pickHotel(labels);
		if (!Chain.getChain(hotel).isEmpty()) {
			hotels.add(hotel);
			administrator.getHotelShares(board, player, hotel);
		}
		return hotels;
	}
	
	public String pickHotel(List<String> labels) {
		Collections.sort(labels);
		for (String label: labels) {
			if (Share.getShare(label) > 0) {
				return label;
			}
		}
		return null;
	}
	
	public Tile pickTile(List <Tile> tiles ) {
		Collections.sort(tiles, new Comparator<Tile>() {

			@Override
			public int compare(Tile o1, Tile o2) {
				if (o1.row.equals(o2.row)) {
					return o2.column.compareTo(o1.column);
				}
				else
				return o2.row.compareTo(o1.row);
			}
		});
		
		return tiles.get(0);
	}

}
