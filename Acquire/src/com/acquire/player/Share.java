package com.acquire.player;

import java.util.HashMap;
import java.util.Map;

import com.acquire.board.Labels;
import com.acquire.board.SharePriceMapper;


public class Share {
	private static Map <String, Integer> shares;
	private static Map <String, Integer> sharePrice;
	private static SharePriceMapper priceMapper;
	
	public Share() {
		shares = new HashMap<String, Integer>();
		sharePrice = new HashMap<String, Integer>();
		for(String name : Labels.getLabels()) {
			shares.put(name, 25);
			if(name.equalsIgnoreCase("WorldWide")|| name.equalsIgnoreCase("Sackson")){
				sharePrice.put(name, priceMapper.getWorldwideSacksonPriceMap().firstEntry().getValue());
			}
			else if(name.equalsIgnoreCase("Festival")|| name.equalsIgnoreCase("Imperial") || name.equalsIgnoreCase("American")){
				sharePrice.put(name, priceMapper.getFestivalImperialAmericanPriceMap().firstEntry().getValue());
			}
			else
				sharePrice.put(name, priceMapper.getContinentalTowerPriceMap().firstEntry().getValue());
		}
	}
	
	public static synchronized int getShare(String name) {
		return shares.get(name);
	}
	
	public static synchronized Map<String, Integer> getShare() {
		return shares;
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
