package com.cgm.java.hue.utilities;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class HueConfigurationTest {
    private static final String MOCK_IP = "999.999.999.999";
    private static final String MOCK_TOKEN = "1234a5678b";

    @Test
    public void testConfigurationRead() throws Exception {
        final HueConfiguration hueConfiguration = new HueConfiguration();
        Assert.assertEquals(MOCK_IP, hueConfiguration.getIP());
        Assert.assertEquals("1234a5678b", hueConfiguration.getToken());
    }

    @Test
    public void testInvalidFileAndNoEnvironmentConfig() {
        final String fileName = "garbage";
        try {
            final HueConfiguration garbage = new HueConfiguration(fileName);
            Assert.assertNotNull(garbage);
        } catch (Exception e) {
            final String message = e.getMessage();
            Assert.assertTrue(message.contains("Failed to import configuration from file or environment."));
        }
    }


    @Test
    public void testInvalidFileAndEnvironmentConfig() throws Exception {
        final String fileName = "garbage2";

        final HueConfiguration environmentConfigured = Mockito.spy(new HueConfiguration(fileName));
        Mockito.doReturn(MOCK_IP)
                .when(environmentConfigured)
                .getEnvironmentConfiguredIp();
        Mockito.doReturn(MOCK_TOKEN)
                .when(environmentConfigured)
                .getEnvironmentConfiguredToken();
        Assert.assertNotNull(environmentConfigured);
        Assert.assertEquals(MOCK_IP, environmentConfigured.getIP());
        Assert.assertEquals(MOCK_TOKEN, environmentConfigured.getToken());
    }
}
