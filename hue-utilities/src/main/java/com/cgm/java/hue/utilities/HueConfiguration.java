package com.cgm.java.hue.utilities;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * A class to aid with the retrieval and parsing of the bridge.properties file.
 */
public class HueConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(HueConfiguration.class);
    private Properties bridgeProperties;

    /**
     * Reads configuration from the default bridge.properties file
     */
    public HueConfiguration() {
        this("bridge.properties");
    }

    /**
     * Reads a specified configuration file for bridge properties
     * @param fileName the file to read
     */
    public HueConfiguration(final String fileName) {
        Preconditions.checkArgument(StringUtils.isNotBlank(fileName), "fileName may not be null or empty.");

        InputStream inputStream = null;
        try {
            bridgeProperties = new Properties();
            inputStream = getClass().getClassLoader().getResourceAsStream(fileName);

            if (inputStream != null) {
                bridgeProperties.load(inputStream);
            } else {
                throw new FileNotFoundException("Properties file '" + fileName + "' not found in the classpath");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to import configuration from file: " + fileName);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
     * @return the IP address configured for the bridge
     */
    public String getIP() {
        return bridgeProperties.getProperty("ip");
    }

    /**
     * @return the API token configured for the bridge
     */
    public String getToken() {
        return bridgeProperties.getProperty("token");
    }
}
