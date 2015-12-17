package com.cgm.java.hue.utilities;

import java.io.IOException;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.models.State;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by mc023219 on 12/10/15.
 */
public class HueConverters {
    private static final Logger LOGGER = LoggerFactory.getLogger(HueConverters.class);
    public static final Function<String, Light> JSON_TO_LIGHT = (jsonString) -> {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //The pointsymbol field has numerical field names. That's annoying, but it's also deprecated.
            //So, let's just drop it from the model and ignore it for now.
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(jsonString.getBytes(), Light.class);
        } catch (IOException e) {
            LOGGER.error("Error reading JSON as State: " + jsonString);
            e.printStackTrace();
        }
        return null;
    };
    public static final Function<String, State> JSON_TO_STATE = (jsonString) -> {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString.getBytes(), State.class);
        } catch (IOException e) {
            LOGGER.error("Error reading JSON as State: " + jsonString);
            e.printStackTrace();
        }

        return null;
    };
}
