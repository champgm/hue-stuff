package com.cgm.java.hue.utilities;

import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.models.Scene;
import com.cgm.java.hue.models.State;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Lambda converters for JSON -> Avro parsing
 */
public class HueConverters {
    private static final Logger LOGGER = LoggerFactory.getLogger(HueConverters.class);

    /**
     * Converts one JSON Scene into a {@link com.cgm.java.hue.models.Scene}
     */
    public static final BiFunction<String, String, Scene> JSON_TO_SCENE = (id, jsonString) -> {
        try {
            // The hue folks like to change the normal array format of "thing" : ["1","2","3"] into just "thing": {}
            // when it is empty, instead of []. The avro JSON converter hates this, so replace those {}'s with [].
            // TODO: this may or may not cause a bunch of issues in the future.
            final String nullifiedJson = jsonString.replace("{}", "[]");

            ObjectMapper objectMapper = new ObjectMapper();
            // Seems like the Scene model is a little crazy. Just ignore failures for now.
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            final Scene scene = objectMapper.readValue(nullifiedJson.getBytes(), Scene.class);
            return Scene.newBuilder(scene).setId(String.valueOf(id)).build();
        } catch (IOException e) {
            LOGGER.error("Error reading JSON as Scene: " + jsonString);
            e.printStackTrace();
        }
        return null;
    };

    /**
     * Converts one JSON Light into a {@link com.cgm.java.hue.models.Light}
     */
    public static final BiFunction<Integer, String, Light> JSON_TO_LIGHT = (id, jsonString) -> {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // The pointsymbol field has numerical field names. That's annoying, but it's also deprecated.
            // So, let's just drop it from the model and ignore it for now.
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            final Light light = objectMapper.readValue(jsonString.getBytes(), Light.class);
            return Light.newBuilder(light).setId(String.valueOf(id)).build();
        } catch (IOException e) {
            LOGGER.error("Error reading JSON as Light: " + jsonString);
            e.printStackTrace();
        }
        return null;
    };

    /**
     * Converts one JSON State into a {@link com.cgm.java.hue.models.State}
     */
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
