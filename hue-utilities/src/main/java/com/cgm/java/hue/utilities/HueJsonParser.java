package com.cgm.java.hue.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgm.java.hue.models.Group;
import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.models.Scene;
import com.cgm.java.hue.models.Sensor;
import com.cgm.java.hue.models.State;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;

/**
 * A class which attempts to deal with Hue's JSON formatting quirks
 */
public class HueJsonParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(HueJsonParser.class);
    /**
     * Converts one JSON Scene into a {@link com.cgm.java.hue.models.Scene}
     */
    public static final BiFunction<String, String, Scene> JSON_TO_SCENE = (id, jsonString) -> {
        try {
            LOGGER.info("Attempting to parse one scene from raw JSON: " + jsonString);
            // The hue folks like to change the normal array format of "thing" : ["1","2","3"] into just "thing": {}
            // when it is empty, instead of []. The avro JSON converter hates this, so replace those {}'s with [].
            // TODO: this may or may not cause a bunch of issues in the future.
            final String nullifiedJson = HueJsonParser.replaceEmptyArray(jsonString);

            ObjectMapper objectMapper = new ObjectMapper();
            // Seems like the Scene model is a little crazy. Just ignore failures for now.
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            final Scene scene = objectMapper.readValue(nullifiedJson.getBytes(), Scene.class);
            final Scene sceneWithId = Scene.newBuilder(scene).setId(String.valueOf(id)).build();

            LOGGER.info("Successfully parsed a scene: " + sceneWithId);
            return sceneWithId;
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
            LOGGER.info("Attempting to parse one scene from raw JSON: " + jsonString);

            ObjectMapper objectMapper = new ObjectMapper();
            // The pointsymbol field has numerical field names. That's annoying, but it's also deprecated.
            // So, let's just drop it from the model and ignore it for now.
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            final Light light = objectMapper.readValue(jsonString.getBytes(), Light.class);

            final Light lightWithId = Light.newBuilder(light).setId(String.valueOf(id)).build();
            LOGGER.info("Successfully parsed a light: " + lightWithId);
            return lightWithId;
        } catch (IOException e) {
            LOGGER.error("Error reading JSON as Light: " + jsonString);
            e.printStackTrace();
        }
        return null;
    };
    /**
     * Converts one JSON Sensor into a {@link com.cgm.java.hue.models.Sensor}
     */
    public static final BiFunction<Integer, String, Sensor> JSON_TO_SENSOR = (id, jsonString) -> {
        try {
            LOGGER.info("Attempting to parse one sensor from raw JSON: " + jsonString);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            final Sensor sensor = objectMapper.readValue(jsonString.getBytes(), Sensor.class);

            final Sensor sensorWithId = Sensor.newBuilder(sensor).setId(String.valueOf(id)).build();
            LOGGER.info("Successfully parsed a sensor: " + sensorWithId);
            return sensorWithId;
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
            LOGGER.info("Attempting to parse one state from raw JSON: " + jsonString);
            ObjectMapper objectMapper = new ObjectMapper();
            final State state = objectMapper.readValue(jsonString.getBytes(), State.class);
            LOGGER.info("Successfully parsed a state: " + state);
            return state;
        } catch (IOException e) {
            LOGGER.error("Error reading JSON as State: " + jsonString);
            e.printStackTrace();
        }

        return null;
    };
    /**
     * Converts one JSON Group into a {@link com.cgm.java.hue.models.Group}
     */
    public static final BiFunction<Integer, String, Group> JSON_TO_GROUP = (id, jsonString) -> {
        try {
            LOGGER.info("Attempting to parse one group from raw JSON: " + jsonString);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            final Group group = objectMapper.readValue(jsonString.getBytes(), Group.class);
            final Group groupWithId = Group.newBuilder(group).setId(String.valueOf(id)).build();
            LOGGER.info("Successfully parsed a group: " + groupWithId);
            return groupWithId;
        } catch (IOException e) {
            LOGGER.error("Error reading JSON as Group: " + jsonString);
            e.printStackTrace();
        }
        return null;
    };
    private static final String NUMERICAL_ID_SPLIT_REGEX = ",\"\\d*\":";
    private static final String NUMERICAL_ID_REPLACE_REGEX = "\"\\d*\":";
    private static final String LIGHT_STATES_HEADER = ",\"lightstates\":";
    private static final String SCENE_ID_REGEX = "(\")[^\":,]*(\":\\{\"name)";

    /**
     * Attempts to parse Hue JSON into a {@link com.cgm.java.hue.models.State}
     *
     * @param rawJsonState
     *            the raw Hue JSON
     * @return a {@link com.cgm.java.hue.models.State}
     */
    public static State parseStateFromJson(final String rawJsonState) {
        final String jsonState = HueJsonParser.replaceEmptyArray(rawJsonState);
        return JSON_TO_STATE.apply(jsonState);
    }

    /**
     * Attempts to parse Hue JSON into a {@link com.cgm.java.hue.models.Light}
     *
     * @param lightId
     *            the light's ID
     * @param rawJsonLight
     *            the raw Hue JSON
     * @return a {@link com.cgm.java.hue.models.Light}
     */
    public static Light parseLightFromJson(final String lightId, final String rawJsonLight) {
        final String jsonLight = HueJsonParser.replaceEmptyArray(rawJsonLight);
        return JSON_TO_LIGHT.apply(Integer.valueOf(lightId), jsonLight);
    }

    /**
     * Attempts to parse Hue JSON into a collection of {@link com.cgm.java.hue.models.Scene}s
     *
     * @param rawJsonScenes
     *            the raw Hue JSON
     * @return a collection of {@link com.cgm.java.hue.models.Scene}s
     */
    public static Collection<Scene> parseScenesFromJson(final String rawJsonScenes) {
        final String jsonScenes = HueJsonParser.replaceEmptyArray(rawJsonScenes);

        // Items are separated by ,"the-scene-ID":
        final ArrayList<String> sceneJsonArrayList = new ArrayList<>(Arrays.asList(jsonScenes.split(SCENE_ID_REGEX)));
        // this seems hacky, but since the raw json will start with a scene ID, the first item in this array will be
        // blank, so...
        sceneJsonArrayList.remove(0);

        final Pattern lightIdPattern = Pattern.compile(SCENE_ID_REGEX);
        final Matcher patternMatcher = lightIdPattern.matcher(jsonScenes);
        final ArrayList<String> sceneIds = new ArrayList<>(sceneJsonArrayList.size());
        while (patternMatcher.find()) {
            final String untrimmedSceneId = patternMatcher.group();
            sceneIds.add(untrimmedSceneId.substring(1, untrimmedSceneId.length() - 8));
        }

        final ImmutableList.Builder<Scene> resultBuilder = ImmutableList.builder();
        for (int i = 0; i < sceneIds.size(); i++) {
            final String sceneId = sceneIds.get(i);
            final String sceneJson = "{\"name" + sceneJsonArrayList.get(i);
            LOGGER.debug("Attempting to convert: " + sceneJson);
            final Scene parsedScene = JSON_TO_SCENE.apply(sceneId, sceneJson);
            resultBuilder.add(parsedScene);
        }
        return resultBuilder.build();
    }

    /**
     * Parses a {@link com.cgm.java.hue.models.Scene} from Hue JSON
     *
     * @param sceneId
     *            the ID of the scene
     * @param rawJsonScene
     *            the raw JSON from the hue bridge
     * @return a parsed {@link com.cgm.java.hue.models.Scene}
     */
    public static Scene parseSceneFromJson(final String sceneId, final String rawJsonScene) {
        final String jsonScene = replaceEmptyArray(rawJsonScene);

        // Grab the first part before the light states
        final int beginPositionOfLightStatesHeader = jsonScene.indexOf(LIGHT_STATES_HEADER);
        final int beginHeader = beginPositionOfLightStatesHeader + LIGHT_STATES_HEADER.length();
        final String beforeLightStatesHeader = jsonScene.substring(0, beginHeader);
        // Append a bracket
        final StringBuilder resultBuilder = new StringBuilder(beforeLightStatesHeader).append("[");

        // Grab the light states collection, removing the curly braces
        final String afterLightStatesHeader = jsonScene.substring(beginHeader);
        final String bracedStateCollection = afterLightStatesHeader.substring(0, afterLightStatesHeader.length() - 1);
        final String bracesRemoved = bracedStateCollection.substring(1, bracedStateCollection.length() - 1);

        // Split, remove IDs, and join
        final String[] split = bracesRemoved.split(NUMERICAL_ID_SPLIT_REGEX);
        for (int i = 0; i < split.length; i++) {
            final String idStripped = split[i].replaceAll(NUMERICAL_ID_REPLACE_REGEX, "");
            split[i] = idStripped;
        }
        final String joined = StringUtils.join(split, ",");
        resultBuilder.append(joined);

        // Add the last bracket and brace
        resultBuilder.append("]}");
        return JSON_TO_SCENE.apply(sceneId, resultBuilder.toString());
    }

    /**
     * Parses the JSON response from the bridge for all {@link com.cgm.java.hue.models.Group}s
     *
     * @param rawJsonGroups
     *            the raw JSON for the group
     * @return a collection of {@link com.cgm.java.hue.models.Group}s
     */
    public static Collection<Group> parseGroupsFromJson(final String rawJsonGroups) {
        final String jsonGroups = replaceEmptyArray(rawJsonGroups);
        // Strip this off: '{"1":'
        final String jsonLightsString = jsonGroups.substring(5);

        // Items are separated by ,"#":
        final String[] jsonGroupsArray = jsonLightsString.split(NUMERICAL_ID_SPLIT_REGEX);

        // Convert the array to an ArrayList
        final ArrayList<String> jsonGroupsArrayList = new ArrayList<>(Arrays.asList(jsonGroupsArray));

        // Use a BiFunction and .indexOf to gather each light's json and its id (its index in the array + 1)
        return jsonGroupsArrayList.stream().map(jsonString -> JSON_TO_GROUP.apply(jsonGroupsArrayList.indexOf(jsonString) + 1, jsonString)).collect(Collectors.toList());
    }

    /**
     * Parses the JSON response from the bridge for a single {@link com.cgm.java.hue.models.Group}
     *
     * @param groupId
     *            the id the group should have
     * @param rawJsonGroup
     *            the raw JSON for the group
     * @return a {@link com.cgm.java.hue.models.Group}
     */
    public static Group parseGroupFromJson(final String groupId, final String rawJsonGroup) {
        final String jsonGroup = HueJsonParser.replaceEmptyArray(rawJsonGroup);
        return JSON_TO_GROUP.apply(Integer.valueOf(groupId), jsonGroup);
    }

    /**
     * Attempts to parse Hue JSON into a collection of {@link com.cgm.java.hue.models.Light}s
     *
     * @param rawJsonLights
     *            the raw Hue JSON
     * @return a collection of {@link com.cgm.java.hue.models.Light}s
     */
    public static Collection<Light> parseLightsFromJson(final String rawJsonLights) {
        final String jsonLights = replaceEmptyArray(rawJsonLights);
        // Strip this off: '{"1":'
        final String jsonLightsString = jsonLights.substring(5);

        // Items are separated by ,"#":
        final String[] jsonLightsArray = jsonLightsString.split(NUMERICAL_ID_SPLIT_REGEX);

        // Convert the array to an ArrayList
        final ArrayList<String> jsonLightsArrayList = new ArrayList<>(Arrays.asList(jsonLightsArray));

        // Use a BiFunction and .indexOf to gather each light's json and its id (its index in the array + 1)
        return jsonLightsArrayList.stream().map(jsonString -> JSON_TO_LIGHT.apply(jsonLightsArrayList.indexOf(jsonString) + 1, jsonString)).collect(Collectors.toList());
    }

    /**
     * Parses the JSON response from the bridge for all {@link com.cgm.java.hue.models.Sensor}s
     *
     * @param rawJsonSensors
     *            the raw JSON for the sensor
     * @return a collection of {@link com.cgm.java.hue.models.Sensor}s
     */
    public static Collection<Sensor> parseSensorsFromJson(final String rawJsonSensors) {
        final String jsonSensors = replaceEmptyArray(rawJsonSensors);
        // Strip this off: '{"1":'
        final String jsonSensorString = jsonSensors.substring(5);

        // Items are separated by ,"#":
        final String[] jsonSensorArray = jsonSensorString.split(NUMERICAL_ID_SPLIT_REGEX);

        // Convert the array to an ArrayList
        final ArrayList<String> jsonSensorsArrayList = new ArrayList<>(Arrays.asList(jsonSensorArray));

        // Use a BiFunction and .indexOf to gather each light's json and its id (its index in the array + 1)
        return jsonSensorsArrayList.stream().map(jsonString -> JSON_TO_SENSOR.apply(jsonSensorsArrayList.indexOf(jsonString) + 1, jsonString)).collect(Collectors.toList());
    }

    /**
     * Parses the JSON response from the bridge for a single {@link com.cgm.java.hue.models.Sensor}
     *
     * @param groupId
     *            the id the group should have
     * @param rawJsonSensor
     *            the raw JSON for the sensor
     * @return a {@link com.cgm.java.hue.models.Sensor}
     */
    public static Sensor parseSensorFromJson(final String groupId, final String rawJsonSensor) {
        final String jsonSensor = HueJsonParser.replaceEmptyArray(rawJsonSensor);
        return JSON_TO_SENSOR.apply(Integer.valueOf(groupId), jsonSensor);
    }


    private static String replaceEmptyArray(final String inputJson) {
        // The hue folks like to change the normal array format of "thing" : ["1","2","3"] into just "thing": {}
        // when it is empty, instead of []. The avro JSON converter hates this, so replace those {}'s with [].
        return inputJson.replace("{}", "[]");
    }
}
