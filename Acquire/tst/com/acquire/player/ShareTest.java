package com.acquire.player;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.acquire.board.Chain;
import com.acquire.board.Game;
import com.acquire.board.Tile;
import com.acquire.player.Player;
import com.acquire.player.Share;
@RunWith (PowerMockRunner.class)
@PrepareForTest({Chain.class, Player.class, Tile.class,Game.class})

public class ShareTest {
    Share share;
    String Label = "American";
    
    public ShareTest(){
    	share= new Share();
    }
	@Test
	public void testShare() {
		Share.setShare(Label, 25);
		Assert.assertEquals(25, Share.getShare(Label));
	}
	@Test
	public void testSharePrice() {
		Share.setSharePrice(Label, 300);
		Assert.assertEquals(300, Share.getSharePrice(Label));
	}

}
