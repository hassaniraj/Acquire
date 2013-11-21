package com.acquire.config;

public class Config {

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
