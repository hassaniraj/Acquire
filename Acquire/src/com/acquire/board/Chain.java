package com.acquire.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Chain {
	private static Map <String, List<String>> chains;
	
	public Chain() {
		chains = new HashMap<String, List<String>>();
		for(String name : Labels.getLabels()) {
			chains.put(name, new ArrayList<String>());
		}
	}
	
	public static synchronized List<String> getChain(String name) {
		return chains.get(name);
	}
	
	public static synchronized int getChainSize() {
		return chains.size();
	}
	
	public static synchronized Set<String> getChainLabel() {
		Set <String> labels = new HashSet<>();
		for (String label: chains.keySet())
			if (chains.get(label).isEmpty())
				labels.add(label);
		return labels;
	}
	
	public static synchronized void setChain(String name, String tile) {
		List<String> chain = chains.get(name);
		chain.add(tile);
		chains.put(name, chain);
	}
}
