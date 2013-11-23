package com.acquire.board;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.acquire.board.Chain;
@RunWith(PowerMockRunner.class)
@PrepareForTest({Chain.class})

public class ChainTest {
   Chain chain;
	public ChainTest(){
		chain = new Chain();
	}
	@Test
	public void testChain() {		
		Chain.setChain("American", "1A");
		assertNotNull(Chain.getChain("American"));
	}
	@Test
	public void testChainSize() {		
		assertNotNull(Chain.getChainSize());
	}

}
