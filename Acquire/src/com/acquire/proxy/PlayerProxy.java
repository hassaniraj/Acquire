package com.acquire.proxy;

import java.util.List;

import com.acquire.exception.AcquireException;
import com.acquire.player.Player;

public class PlayerProxy implements PlayerController {
	private PlayerController playerController;
	
	public PlayerProxy() {
		playerController = new PlayerControls();
	}
	
	@Override
	public List<Object> playPlace(Player player, List<String> hotels) {
		return playerController.playPlace(player, hotels);
	}

	@Override
	public List<String> playBuy(Player player) throws AcquireException {
		return playerController.playBuy(player);
	}

	@Override
	public boolean askEndGame() {
		return playerController.askEndGame();
	}
}
