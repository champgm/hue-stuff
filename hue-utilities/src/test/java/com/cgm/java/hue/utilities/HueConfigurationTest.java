package com.cgm.java.hue.utilities;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by mac on 1/2/16.
 */
public class HueConfigurationTest {


    @Test
    public void testConfigurationRead() throws Exception {
        final HueConfiguration hueConfiguration = new HueConfiguration();
        Assert.assertEquals("1.2.3.4",hueConfiguration.getIP());
        Assert.assertEquals( "1234a5678b",hueConfiguration.getToken());
    }
}
