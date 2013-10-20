package com.acquire.player;

import java.util.HashMap;
import java.util.Map;

import com.acquire.board.Labels;


public class Share {
	private static Map <String, Integer> shares;
	private static Map <String, Integer> sharePrice;
	
	public Share() {
		shares = new HashMap<String, Integer>();
		sharePrice = new HashMap<String, Integer>();
		for(String name : Labels.getLabels()) {
			shares.put(name, 25);
			sharePrice.put(name, 300);
		}
	}
	
	public static synchronized int getShare(String name) {
		return shares.get(name);
	}
	
	public static synchronized void setShare(String name, int share) {
		shares.put(name, share);
	}
	
	public static synchronized int getSharePrice(String name) {
		return sharePrice.get(name);
	}
	
	public static synchronized void setSharePrice(String name, int share) {
		sharePrice.put(name, share);
	}
	
}
