package com.acquire.proxy;

import java.util.List;

import com.acquire.exception.AcquireException;
import com.acquire.player.Player;

public class PlayerControls implements PlayerController {
	@Override
	public List<Object> playPlace(Player player, List<String> hotels) {
		return player.getStrategy().playTile(player, hotels);
	}

	@Override
	public List<String> playBuy(Player player) throws AcquireException {
		return player.getStrategy().buyShare();
	}
}
