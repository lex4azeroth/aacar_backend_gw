package com.aawashcar.apigateway.util;

import org.junit.Assert;
import org.junit.Test;


public class ServiceUtilTest {
	
	@Test
	public void testGetServiceIdsWithOneService() {
		String[] ids = ServiceUtil.getServiceIDs("1");
		Assert.assertTrue(ids.length == 1);
		Assert.assertTrue("1".equals(ids[0]));
	}
	
	@Test
	public void testGetServiceIdsWithThreeServices() {
		String[] ids = ServiceUtil.getServiceIDs("1,5,7");
		Assert.assertTrue(ids.length == 3);
		Assert.assertTrue("1".equals(ids[0]));
		Assert.assertTrue("5".equals(ids[1]));
		Assert.assertTrue("7".equals(ids[2]));
	}

}
