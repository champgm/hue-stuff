package com.cgm.java.hue.utilities;

import org.junit.Assert;

import org.junit.Test;

public class HueConfigurationTest {

    @Test
    public void testConfigurationRead() throws Exception {
        final HueConfiguration hueConfiguration = new HueConfiguration();
        Assert.assertEquals("999.999.999.999", hueConfiguration.getIP());
        Assert.assertEquals("1234a5678b", hueConfiguration.getToken());
    }

    @Test
    public void testInvalidFiles() {
        final String fileName = "garbage";
        try {
            final HueConfiguration garbage = new HueConfiguration(fileName);
            Assert.assertNotNull(garbage);
        } catch (Exception e) {
            final String message = e.getMessage();
            Assert.assertTrue(message.contains("Failed to import configuration from file: " + fileName));
        }
    }
}
