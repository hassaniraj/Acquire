package com.acquire.board;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class LabelsTest {
    List<String> labels;
    
    public LabelsTest(){
    	labels = Labels.getLabels();
    }
	@Test
	public void testLabels() {
		Assert.assertSame(labels, Labels.getLabels());
	}

}
