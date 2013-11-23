package com.acquire.board;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

public class TileTest {
    Tile tile;
    
    public TileTest(){
    	tile = new Tile();
    }
	@Test
	public void test() {
		Assert.assertEquals("1A",tile.getTileLabel("A", "1"));
	}

}
