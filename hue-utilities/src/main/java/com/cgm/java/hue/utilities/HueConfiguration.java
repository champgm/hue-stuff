package com.cgm.java.hue.utilities;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

/**
 * A class to aid with the retrieval and parsing of the bridge.properties file.
 */
public class HueConfiguration {
    public static final String BRIDGE_IP_ENVIRONMENT_VARIABLE_NAME = "HUE_BRIDGE_IP";
    public static final String BRIDGE_TOKEN_ENVIRONMENT_VARIABLE_NAME = "HUE_BRIDGE_TOKEN";
    private static final Logger LOGGER = LoggerFactory.getLogger(HueConfiguration.class);
    private final String fileName;
    private Properties bridgeProperties;

    /**
     * Reads configuration from the default bridge.properties file
     */
    public HueConfiguration() {
        this("bridge.properties");
    }

    /**
     * Reads a specified configuration file for bridge properties
     *
     * @param fileName the file to read
     */
    public HueConfiguration(final String fileName) {
        Preconditions.checkArgument(StringUtils.isNotBlank(fileName), "fileName may not be null or empty.");
        this.fileName = fileName;
    }

    @VisibleForTesting
    protected String getEnvironmentConfiguredIp() {
        final String ip = System.getenv(BRIDGE_IP_ENVIRONMENT_VARIABLE_NAME);
        LOGGER.info("Retrieved IP from environment config: " + ip);
        Preconditions.checkNotNull(ip, BRIDGE_IP_ENVIRONMENT_VARIABLE_NAME + " environment variable was not set.");
        return ip;
    }

    @VisibleForTesting
    protected String getEnvironmentConfiguredToken() {
        final String token = System.getenv(BRIDGE_TOKEN_ENVIRONMENT_VARIABLE_NAME);
        LOGGER.info("Retrieved token from environment config: " + token);
        Preconditions.checkNotNull(token, BRIDGE_TOKEN_ENVIRONMENT_VARIABLE_NAME + " environment variable was not set.");
        return token;
    }

    /**
     * @return the IP address configured for the bridge
     */
    public String getIP() {
        if (bridgeProperties == null) {
            configureProperties();
        }
        return bridgeProperties.getProperty("ip");
    }

    /**
     * @return the API token configured for the bridge
     */
    public String getToken() {
        if (bridgeProperties == null) {
            configureProperties();
        }
        return bridgeProperties.getProperty("token");
    }

    private void configureProperties() {
        InputStream inputStream = null;
        try {
            final Properties newProperties = new Properties();
            inputStream = getClass().getClassLoader()
                    .getResourceAsStream(fileName);

            if (inputStream != null) {
                newProperties.load(inputStream);
            } else {
                LOGGER.error("Properties file '" + fileName + "' not found in the classpath");

                LOGGER.error("Will attempt to read config from environment variables...");
                newProperties.setProperty("ip", getEnvironmentConfiguredIp());
                newProperties.setProperty("token", getEnvironmentConfiguredToken());
                LOGGER.error("IP read: " + newProperties.getProperty("ip"));
                LOGGER.error("Token read: " + newProperties.getProperty("token"));
            }
            bridgeProperties = newProperties;
        } catch (Exception e) {
            throw new RuntimeException("Failed to import configuration from file or environment.", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
}
