package com.acquire.factory;

import com.acquire.player.Player;

public class PlayerFactory {
	public static Player getInstance() {
		return new Player();
	}
}
