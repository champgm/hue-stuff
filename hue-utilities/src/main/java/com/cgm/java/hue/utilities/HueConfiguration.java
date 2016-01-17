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
    private final String fileName;
    private Properties bridgeProperties;

    public HueConfiguration() {
        this("bridge.properties");
    }

    public HueConfiguration(final String fileName) {
        Preconditions.checkArgument(StringUtils.isNotBlank(fileName), "fileName may not be null or empty.");
        this.fileName = fileName;

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

    public String getIP() {
        return bridgeProperties.getProperty("ip");
    }

    public String getToken() {
        return bridgeProperties.getProperty("token");
    }
}
