package com.acquire.board;

import java.util.TreeMap;

public final class SharePriceMapper {
	private TreeMap<Integer, Integer> continentalTowerPriceMap = new TreeMap<>();

	private TreeMap<Integer, Integer> worldwideSacksonPriceMap = new TreeMap<>();

	private TreeMap<Integer, Integer> festivalImperialAmericanPriceMap = new TreeMap<>();

	private static SharePriceMapper priceMapper;

	private SharePriceMapper() {
		
		//For Continental & Tower
		continentalTowerPriceMap.put(0, 300);
		continentalTowerPriceMap.put(2, 400);
		continentalTowerPriceMap.put(3, 500);
		continentalTowerPriceMap.put(4, 600);
		continentalTowerPriceMap.put(5, 700);
		continentalTowerPriceMap.put(6, 800);
		continentalTowerPriceMap.put(11, 900);
		continentalTowerPriceMap.put(21, 1000);
		continentalTowerPriceMap.put(31, 1100);
		continentalTowerPriceMap.put(41, 1200);

		//For Worldwide and Sackson
		worldwideSacksonPriceMap.put(2, 200);
		worldwideSacksonPriceMap.put(3, 300);
		worldwideSacksonPriceMap.put(4, 400);
		worldwideSacksonPriceMap.put(5, 500);
		worldwideSacksonPriceMap.put(6, 600);
		worldwideSacksonPriceMap.put(11, 700);
		worldwideSacksonPriceMap.put(21, 800);
		worldwideSacksonPriceMap.put(31, 900);
		worldwideSacksonPriceMap.put(41, 1000);

		//For Festival, American and Imperial
		festivalImperialAmericanPriceMap.put(0, 200);
		festivalImperialAmericanPriceMap.put(2, 300);
		festivalImperialAmericanPriceMap.put(3, 400);
		festivalImperialAmericanPriceMap.put(4, 500);
		festivalImperialAmericanPriceMap.put(5, 600);
		festivalImperialAmericanPriceMap.put(6, 700);
		festivalImperialAmericanPriceMap.put(11, 800);
		festivalImperialAmericanPriceMap.put(21, 900);
		festivalImperialAmericanPriceMap.put(31, 1000);
		festivalImperialAmericanPriceMap.put(41, 1100);
	}

	public static synchronized TreeMap<Integer, Integer> getContinentalTowerPriceMap() {
		if (priceMapper == null) {
			priceMapper = new SharePriceMapper();
		}
		return priceMapper.continentalTowerPriceMap;
	}

	public static synchronized TreeMap<Integer, Integer> getWorldwideSacksonPriceMap() {
		if (priceMapper == null) {
			priceMapper = new SharePriceMapper();
		}
		return priceMapper.worldwideSacksonPriceMap;
	}

	public static synchronized TreeMap<Integer, Integer> getFestivalImperialAmericanPriceMap() {
		if (priceMapper == null) {
			priceMapper = new SharePriceMapper();
		}
		return priceMapper.festivalImperialAmericanPriceMap;
	}

}
