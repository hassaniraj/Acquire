package com.acquire.config;

public class Config {
		
	public static final int MAX_CHAIN_SIZE=41;
	
	public static final int SAFE_CHAIN_SIZE=11;
	
	public static enum Moves {
		SINGLETON("singleton"), FOUNDING("founding"), MERGING("merging"), GROWING(
				"growing"), NONE("none");

		String value;

		private Moves(String value) {
			this.value = value;
		}

		public String getMove() {
			return value;
		}
	}
}
