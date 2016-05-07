package com.cgm.java.hue.utilities;

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
import com.cgm.java.hue.models.Rule;
import com.cgm.java.hue.models.Scene;
import com.cgm.java.hue.models.Schedule;
import com.cgm.java.hue.models.Sensor;
import com.cgm.java.hue.models.State;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;

/**
 * A class which attempts to deal with Hue's JSON formatting quirks.
 * <p/>
 * The hue folks like to change the normal array format of "thing" : ["1","2","3"] into just "thing": {} when it is
 * empty, instead of "[]". They ALSO replace any instance of optional values (appdata, for instance) with this same
 * "{}", creating some ambiguity. To handle this, I've made all fields on the models optional and replace all instances
 * of "{}" with "null".
 */
public class HueJsonParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(HueJsonParser.class);
    /**
     * Converts one JSON Scene into a {@link com.cgm.java.hue.models.Scene}
     */
    public static final BiFunction<String, String, Scene> JSON_TO_SCENE = (id, jsonString) -> {
        try {
            LOGGER.debug("Attempting to parse one scene from raw JSON: " + jsonString);
            // The hue folks like to change the normal array format of "thing" : ["1","2","3"] into just "thing": {}
            // when it is empty, instead of []. To fit in the models, I think we're just going to try to set {} to null
            // TODO: this may or may not cause a bunch of issues in the future.
            final String nullifiedJson = HueJsonParser.replaceEmptyArray(jsonString);

            ObjectMapper objectMapper = new ObjectMapper();
            // Seems like the Scene model is a little crazy. Just ignore failures for now.
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            final Scene scene = objectMapper.readValue(nullifiedJson.getBytes(), Scene.class);
            final Scene sceneWithId = Scene.newBuilder(scene).setId(String.valueOf(id)).build();

            LOGGER.debug("Successfully parsed a scene: " + sceneWithId);
            return sceneWithId;
        } catch (Exception e) {
            LOGGER.error("Error reading JSON as Scene: " + jsonString);
            e.printStackTrace();
        }
        return null;
    };
    /**
     * Converts one JSON Light into a {@link com.cgm.java.hue.models.Light}
     */
    public static final BiFunction<Long, String, Light> JSON_TO_LIGHT = (id, jsonString) -> {
        try {
            LOGGER.debug("Attempting to parse one scene from raw JSON: " + jsonString);

            ObjectMapper objectMapper = new ObjectMapper();
            // The pointsymbol field has numerical field names. That's annoying, but it's also deprecated.
            // So, let's just drop it from the model and ignore it for now.
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            final Light light = objectMapper.readValue(jsonString.getBytes(), Light.class);

            final Light lightWithId = Light.newBuilder(light).setId(String.valueOf(id)).build();
            LOGGER.debug("Successfully parsed a light: " + lightWithId);
            return lightWithId;
        } catch (Exception e) {
            LOGGER.error("Error reading JSON as Light: " + jsonString);
            e.printStackTrace();
        }
        return null;
    };
    /**
     * Converts one JSON Sensor into a {@link com.cgm.java.hue.models.Sensor}
     */
    public static final BiFunction<Long, String, Sensor> JSON_TO_SENSOR = (id, jsonString) -> {
        try {
            LOGGER.debug("Attempting to parse one sensor from raw JSON: " + jsonString);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            final Sensor sensor = objectMapper.readValue(jsonString.getBytes(), Sensor.class);

            final Sensor sensorWithId = Sensor.newBuilder(sensor).setId(String.valueOf(id)).build();
            LOGGER.debug("Successfully parsed a sensor: " + sensorWithId);
            return sensorWithId;
        } catch (Exception e) {
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
            LOGGER.debug("Attempting to parse one state from raw JSON: " + jsonString);
            ObjectMapper objectMapper = new ObjectMapper();
            final State state = objectMapper.readValue(jsonString.getBytes(), State.class);
            LOGGER.debug("Successfully parsed a state: " + state);
            return state;
        } catch (Exception e) {
            LOGGER.error("Error reading JSON as State: " + jsonString);
            e.printStackTrace();
        }

        return null;
    };
    /**
     * Converts one JSON Group into a {@link com.cgm.java.hue.models.Group}
     */
    public static final BiFunction<Long, String, Group> JSON_TO_GROUP = (id, jsonString) -> {
        try {
            LOGGER.debug("Attempting to parse one group from raw JSON: " + jsonString);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            final Group group = objectMapper.readValue(jsonString.getBytes(), Group.class);
            final Group groupWithId = Group.newBuilder(group).setId(String.valueOf(id)).build();
            LOGGER.debug("Successfully parsed a group: " + groupWithId);
            return groupWithId;

        } catch (Exception e) {
            LOGGER.error("Error reading JSON as Group: " + jsonString);
            e.printStackTrace();
        }
        return null;
    };
    /**
     * Converts one JSON Group into a {@link com.cgm.java.hue.models.Rule}
     */
    public static final BiFunction<Long, String, Rule> JSON_TO_RULE = (id, jsonString) -> {
        String jsonStringWithQuoteWrappedBody = null;
        try {
            LOGGER.debug("Attempting to parse one rule from raw JSON: " + jsonString);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // The "body" attribute is written by the Hue bridge as its own object. However, this object seems to be
            // able to contain anything at all. Some examples are "on", "bri_inc", "transformationtime", etc. If I
            // try to make a model for this, it will be absolutely ridiculous. Instead, I will attempt to wrap the
            // body in quotes so that the JSON parser treats it as a string.
            final int bodyStart = jsonString.indexOf("\"body\":") + 7;
            final int bodyEnd = jsonString.length() - 3;
            final String body = jsonString.substring(bodyStart, bodyEnd);
            final String bodyWithEscapedQuotes = body.replace("\"", "\\\"");

            jsonStringWithQuoteWrappedBody = jsonString.substring(0, bodyStart) +
                                             "\"" + bodyWithEscapedQuotes + "\"" +
                                             jsonString.substring(bodyEnd, jsonString.length());

            final Rule rule = objectMapper.readValue(jsonStringWithQuoteWrappedBody.getBytes(), Rule.class);
            final Rule ruleWithId = Rule.newBuilder(rule).setId(String.valueOf(id)).build();
            LOGGER.debug("Successfully parsed a Rule: " + ruleWithId);
            return ruleWithId;
        } catch (Exception e) {
            LOGGER.error("Error reading JSON as Rule.");
            LOGGER.error("Original String: " + jsonString);
            LOGGER.error("Quote wrapped body: " + jsonStringWithQuoteWrappedBody);
            e.printStackTrace();
        }
        return null;
    };
    /**
     * Converts one JSON Group into a {@link com.cgm.java.hue.models.Schedule}
     */
    public static final BiFunction<Long, String, Schedule> JSON_TO_SCHEDULE = (id, jsonString) -> {
        String jsonStringWithQuoteWrappedBody = null;
        try {
            LOGGER.debug("Attempting to parse one schedule from raw JSON: " + jsonString);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // The "body" attribute is written by the Hue bridge as its own object. However, this object seems to be
            // able to contain anything at all. Some examples are "on", "bri_inc", "transformationtime", etc. If I
            // try to make a model for this, it will be absolutely ridiculous. Instead, I will attempt to wrap the
            // body in quotes so that the JSON parser treats it as a string.
            final int bodyStart = jsonString.indexOf("\"body\":") + 7;
            final int bodyEnd = findEndOfBody(bodyStart, jsonString);
            final String body = jsonString.substring(bodyStart, bodyEnd);
            final String bodyWithEscapedQuotes = body.replace("\"", "\\\"");

            jsonStringWithQuoteWrappedBody = jsonString.substring(0, bodyStart) +
                                             "\"" + bodyWithEscapedQuotes + "\"" +
                                             jsonString.substring(bodyEnd, jsonString.length());

            final Schedule schedule = objectMapper.readValue(jsonStringWithQuoteWrappedBody.getBytes(), Schedule.class);
            final Schedule scheduleWithId = Schedule.newBuilder(schedule).setId(String.valueOf(id)).build();
            LOGGER.debug("Successfully parsed a Schedule: " + scheduleWithId);
            return scheduleWithId;
        } catch (Exception e) {
            LOGGER.error("Error reading JSON as Schedule.");
            LOGGER.error("Original String: " + jsonString);
            LOGGER.error("Quote wrapped body: " + jsonStringWithQuoteWrappedBody);
            e.printStackTrace();
        }
        return null;
    };
    private static final String NUMERICAL_ID_SPLIT_REGEX = ",\"\\d*\":";
    private static final String NUMERICAL_ID_REPLACE_REGEX = "\"\\d*\":";
    private static final String LIGHT_STATES_HEADER = ",\"lightstates\":";
    private static final String ID_REGEX = "(\")[^\":,]*(\":\\{\"name)";

    /**
     * Attempts to parse Hue JSON into a {@link com.cgm.java.hue.models.State}
     *
     * @param rawJsonState
     *         the raw Hue JSON
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
     *         the light's ID
     * @param rawJsonLight
     *         the raw Hue JSON
     * @return a {@link com.cgm.java.hue.models.Light}
     */
    public static Light parseLightFromJson(final String lightId, final String rawJsonLight) {
        final String jsonLight = HueJsonParser.replaceEmptyArray(rawJsonLight);
        return JSON_TO_LIGHT.apply(Long.valueOf(lightId), jsonLight);
    }

    /**
     * Attempts to parse Hue JSON into a collection of {@link com.cgm.java.hue.models.Scene}s
     *
     * @param rawJsonScenes
     *         the raw Hue JSON
     * @return a collection of {@link com.cgm.java.hue.models.Scene}s
     */
    public static Collection<Scene> parseScenesFromJson(final String rawJsonScenes) {
        final String jsonScenes = HueJsonParser.replaceEmptyArray(rawJsonScenes);

        // Items are separated by ,"the-scene-ID":
        final ArrayList<String> sceneJsonArrayList = new ArrayList<>(Arrays.asList(jsonScenes.split(ID_REGEX)));
        // this seems hacky, but since the raw json will start with a scene ID, the first item in this array will be
        // blank, so...
        sceneJsonArrayList.remove(0);

        final Pattern scheduleIdPattern = Pattern.compile(ID_REGEX);
        final Matcher patternMatcher = scheduleIdPattern.matcher(jsonScenes);
        final ArrayList<String> sceneIds = new ArrayList<>(sceneJsonArrayList.size());
        while (patternMatcher.find()) {
            final String untrimmedSceneId = patternMatcher.group();
            //We removed {"name" from the json when we were aggressively hunting for IDs. Remove it.
            sceneIds.add(untrimmedSceneId.substring(1, untrimmedSceneId.length() - 8));
        }

        final ImmutableList.Builder<Scene> resultBuilder = ImmutableList.builder();
        for (int i = 0; i < sceneIds.size(); i++) {
            final String sceneId = sceneIds.get(i);
            //We removed {"name" from the json when we were aggressively hunting for IDs. Restore it.
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
     *         the ID of the scene
     * @param rawJsonScene
     *         the raw JSON from the hue bridge
     * @return a parsed {@link com.cgm.java.hue.models.Scene}
     */
    public static Scene parseSceneFromJson(final String sceneId, final String rawJsonScene) {
        LOGGER.debug("Full raw scene: " + rawJsonScene);
        final String sceneWithEmptyDataNulled = replaceEmptyArray(rawJsonScene);
        LOGGER.debug("after nulled: " + sceneWithEmptyDataNulled);

        // If the lightstates array has already been blanked out by the null-replacement method, just return it.
        if (sceneWithEmptyDataNulled.contains("lightstates\":null")) {
            return JSON_TO_SCENE.apply(sceneId, sceneWithEmptyDataNulled);
        } else {
            // Otherwise, we will have to replace the curly braces surrounding the lightstates array with square brackets
            // so that it can be parsed into an AVRO later.

            // Grab the first part before the light states
            final int beginPositionOfLightStatesHeader = sceneWithEmptyDataNulled.indexOf(LIGHT_STATES_HEADER);
            final int beginHeader = beginPositionOfLightStatesHeader + LIGHT_STATES_HEADER.length();
            final String beforeLightStatesHeader = sceneWithEmptyDataNulled.substring(0, beginHeader);
            LOGGER.debug("beforeLightStatesHeader: " + beforeLightStatesHeader);

            // Append a bracket
            final StringBuilder resultBuilder = new StringBuilder(beforeLightStatesHeader).append("[");

            LOGGER.debug("With bracket: " + resultBuilder);

            // Grab the light states collection, removing the curly braces
            final String afterLightStatesHeader = sceneWithEmptyDataNulled.substring(beginHeader);
            final String bracedStateCollection = afterLightStatesHeader.substring(0,
                    afterLightStatesHeader.length() - 1);
            final String bracesRemoved = bracedStateCollection.substring(1, bracedStateCollection.length() - 1);

            // Split, remove IDs, and join
            final String[] split = bracesRemoved.split(NUMERICAL_ID_SPLIT_REGEX);
            for (int i = 0; i < split.length; i++) {
                final String idStripped = split[i].replaceAll(NUMERICAL_ID_REPLACE_REGEX, "");
                split[i] = idStripped;
            }

            final String joined = StringUtils.join(split, ",");
            resultBuilder.append(joined);
            LOGGER.debug("After lightstates: " + resultBuilder);

            // Add the last bracket and brace
            resultBuilder.append("]}");
            LOGGER.debug("After manipulation: " + resultBuilder.toString());
            return JSON_TO_SCENE.apply(sceneId, resultBuilder.toString());
        }

    }

    /**
     * Parses the JSON response from the bridge for all {@link com.cgm.java.hue.models.Group}s
     *
     * @param rawJsonGroups
     *         the raw JSON for the group
     * @return a collection of {@link com.cgm.java.hue.models.Group}s
     */
    public static Collection<Group> parseGroupsFromJson(final String rawJsonGroups) {
        final ArrayList<String> jsonGroupsArrayList = parseNumericalItems(rawJsonGroups);

        // Use a BiFunction and .indexOf to gather each light's json and its id (its index in the array + 1)
        return jsonGroupsArrayList.stream()
                .map(jsonString -> JSON_TO_GROUP.apply((long) (jsonGroupsArrayList.indexOf(jsonString) +
                                                               1), jsonString))
                .collect(Collectors.toList());
    }

    /**
     * Parses the JSON response from the bridge for a single {@link com.cgm.java.hue.models.Group}
     *
     * @param groupId
     *         the id the group should have
     * @param rawJsonGroup
     *         the raw JSON for the group
     * @return a {@link com.cgm.java.hue.models.Group}
     */
    public static Group parseGroupFromJson(final String groupId, final String rawJsonGroup) {
        final String jsonGroup = HueJsonParser.replaceEmptyArray(rawJsonGroup);
        return JSON_TO_GROUP.apply(Long.valueOf(groupId), jsonGroup);
    }

    /**
     * Attempts to parse Hue JSON into a collection of {@link com.cgm.java.hue.models.Light}s
     *
     * @param rawJsonLights
     *         the raw Hue JSON
     * @return a collection of {@link com.cgm.java.hue.models.Light}s
     */
    public static Collection<Light> parseLightsFromJson(final String rawJsonLights) {
        final ArrayList<String> jsonLightsArrayList = parseNumericalItems(rawJsonLights);

        // Use a BiFunction and .indexOf to gather each light's json and its id (its index in the array + 1)
        return jsonLightsArrayList.stream()
                .map(jsonString -> JSON_TO_LIGHT.apply((long) (jsonLightsArrayList.indexOf(jsonString) +
                                                               1), jsonString))
                .collect(Collectors.toList());
    }

    /**
     * Parses the JSON response from the bridge for all {@link com.cgm.java.hue.models.Sensor}s
     *
     * @param rawJsonSensors
     *         the raw JSON for the sensor
     * @return a collection of {@link com.cgm.java.hue.models.Sensor}s
     */
    public static Collection<Sensor> parseSensorsFromJson(final String rawJsonSensors) {
        final ArrayList<String> jsonSensorsArrayList = parseNumericalItems(rawJsonSensors);

        // Use a BiFunction and .indexOf to gather each light's json and its id (its index in the array + 1)
        return jsonSensorsArrayList.stream()
                .map(jsonString -> JSON_TO_SENSOR.apply((long) (jsonSensorsArrayList.indexOf(jsonString) +
                                                                1), jsonString))
                .collect(Collectors.toList());
    }

    /**
     * Parses the JSON response from the bridge for a single {@link com.cgm.java.hue.models.Sensor}
     *
     * @param sensorId
     *         the id the group should have
     * @param rawJsonSensor
     *         the raw JSON for the sensor
     * @return a {@link com.cgm.java.hue.models.Sensor}
     */
    public static Sensor parseSensorFromJson(final String sensorId, final String rawJsonSensor) {
        final String jsonSensor = HueJsonParser.replaceEmptyArray(rawJsonSensor);
        return JSON_TO_SENSOR.apply(Long.valueOf(sensorId), jsonSensor);
    }

    /**
     * Parses the JSON response from the bridge for all {@link com.cgm.java.hue.models.Rule}s
     *
     * @param rawJsonRules
     *         the raw JSON for the rule
     * @return a collection of {@link com.cgm.java.hue.models.Rule}s
     */
    public static Collection<Rule> parseRulesFromJson(final String rawJsonRules) {
        final ArrayList<String> jsonRulesArrayList = parseNumericalItems(rawJsonRules);

        // Use a BiFunction and .indexOf to gather each light's json and its id (its index in the array + 1)
        return jsonRulesArrayList.stream()
                .map(jsonString -> JSON_TO_RULE.apply((long) (jsonRulesArrayList.indexOf(jsonString) + 1), jsonString))
                .collect(Collectors.toList());
    }

    /**
     * Parses the JSON response from the bridge for a single {@link com.cgm.java.hue.models.Schedule}
     *
     * @param scheduleId
     *         the id the group should have
     * @param rawJsonSchedule
     *         the raw JSON for the sensor
     * @return a {@link com.cgm.java.hue.models.Schedule}
     */
    public static Schedule parseScheduleFromJson(final String scheduleId, final String rawJsonSchedule) {
        final String jsonSensor = HueJsonParser.replaceEmptyArray(rawJsonSchedule);
        return JSON_TO_SCHEDULE.apply(Long.valueOf(scheduleId), jsonSensor);
    }

    /**
     * Parses the JSON response from the bridge for all {@link com.cgm.java.hue.models.Schedule}s
     *
     * @param rawJsonSchedules
     *         the raw JSON for the rule
     * @return a collection of {@link com.cgm.java.hue.models.Schedule}s
     */
    public static Collection<Schedule> parseSchedulesFromJson(final String rawJsonSchedules) {
        final String jsonSchedules = HueJsonParser.replaceEmptyArray(rawJsonSchedules);

        // Schedules have crazy IDs, just long strings of numbers.
        // The pattern we created for scenes matches here too though, so reuse that.
        final ArrayList<String> sceneJsonArrayList = new ArrayList<>(Arrays.asList(jsonSchedules.split(ID_REGEX)));
        // this seems hacky, but since the raw json will start with an ID, so the first item in this array will be blank
        sceneJsonArrayList.remove(0);

        final Pattern scheduleIdPattern = Pattern.compile(ID_REGEX);
        final Matcher patternMatcher = scheduleIdPattern.matcher(jsonSchedules);
        final ArrayList<String> scheduleIds = new ArrayList<>(sceneJsonArrayList.size());
        while (patternMatcher.find()) {
            final String untrimmedSceneId = patternMatcher.group();
            //We removed {"name" from the json when we were aggressively hunting for IDs. Remove it.
            scheduleIds.add(untrimmedSceneId.substring(1, untrimmedSceneId.length() - 8));
        }

        final ImmutableList.Builder<Schedule> resultBuilder = ImmutableList.builder();
        for (int i = 0; i < scheduleIds.size(); i++) {
            final String scheduleId = scheduleIds.get(i);
            //We removed {"name" from the json when we were aggressively hunting for IDs. Restore it.
            final String scheduleJson = "{\"name" + sceneJsonArrayList.get(i);
            LOGGER.debug("Attempting to convert: " + scheduleJson);
            final Schedule parsedScene = JSON_TO_SCHEDULE.apply(Long.valueOf(scheduleId), scheduleJson);
            resultBuilder.add(parsedScene);
        }
        return resultBuilder.build();
    }

    /**
     * Parses the JSON response from the bridge for a single {@link com.cgm.java.hue.models.Rule}
     *
     * @param ruleId
     *         the id the group should have
     * @param rawJsonRule
     *         the raw JSON for the sensor
     * @return a {@link com.cgm.java.hue.models.Rule}
     */
    public static Rule parseRuleFromJson(final String ruleId, final String rawJsonRule) {
        final String jsonSensor = HueJsonParser.replaceEmptyArray(rawJsonRule);
        return JSON_TO_RULE.apply(Long.valueOf(ruleId), jsonSensor);
    }

    private static String replaceEmptyArray(final String inputJson) {
        // SEE JAVADOC
        return inputJson.replaceAll("\\{\\}", "null");
    }

    private static int findEndOfBody(final int bodyStart, final String jsonString) {
        boolean haveOpen = false;
        for (int index = bodyStart + 1; index < jsonString.length(); index++) {
            final char currentCharacter = jsonString.charAt(index);
            if ('{' == currentCharacter) {
                haveOpen = true;
            } else if ('}' == currentCharacter) {
                if (haveOpen) {
                    haveOpen = false;
                } else {
                    return index + 1;
                }
            }
        }
        return -1;
    }

    private static ArrayList<String> parseNumericalItems(final String rawJson) {
        final String nonEmptyJson = replaceEmptyArray(rawJson);
        // Strip this off: '{"1":'
        final String strippedJson = nonEmptyJson.substring(5, nonEmptyJson.length() - 1);

        // Items are separated by ,"#":
        final String[] jsonArray = strippedJson.split(NUMERICAL_ID_SPLIT_REGEX);

        // Convert the array to an ArrayList
        return new ArrayList<>(Arrays.asList(jsonArray));
    }
}
