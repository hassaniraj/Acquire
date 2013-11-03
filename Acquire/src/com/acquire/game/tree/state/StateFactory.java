package com.acquire.game.tree.state;

/**
 * 
 * Create new state instance
 *
 */
public class StateFactory {
	static State state;
	
	public static State getInstance() {
		if (state == null) 
			state = new StateImpl();
		return state;
	}
}
