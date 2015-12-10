package com.cgm.java.hue.utilities;

import java.io.IOException;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.models.State;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by mc023219 on 12/10/15.
 */
public class HueConverters {
    public static final Function<String, Light> JSON_TO_LIGHT = (jsonString) -> {

        return null;
    };
    private static final Logger LOGGER = LoggerFactory.getLogger(HueConverters.class);
    public static final Function<String, State> JSON_TO_STATE = (jsonString) -> {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString.getBytes(), State.class);
        } catch (IOException e) {
            LOGGER.error("Error reading JSON as State: " + jsonString);
            e.printStackTrace();
        }

        return null;
    };
}
