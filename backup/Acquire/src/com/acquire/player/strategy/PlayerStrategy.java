package com.acquire.player.strategy;

import java.util.List;

import com.acquire.exception.AcquireException;
import com.acquire.player.Player;

public interface PlayerStrategy {

	List<String> buyShare() throws AcquireException;
	List<Object> playTile(Player player, List<String> hotels);
}
