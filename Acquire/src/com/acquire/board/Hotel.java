package com.acquire.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Hotel {
	private String label;
	private int share;
	private Properties properties;

	public Hotel() {
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setShare(int share) {
		this.share = share;
	}

	public int getShare() {
		return share;
	}

	public List<String> getLabels() {
		return Labels.getLabels();
	}
}
