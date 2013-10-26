package com.acquire.proxy;

import java.util.List;

import com.acquire.exception.AcquireException;
import com.acquire.player.Player;

public interface PlayerController {
	public List<String> playBuy(Player player) throws AcquireException;
	public List<Object> playPlace(Player player, List<String> hotels);
}
