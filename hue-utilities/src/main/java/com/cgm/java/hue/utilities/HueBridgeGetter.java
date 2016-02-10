package com.cgm.java.hue.utilities;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgm.java.hue.models.Group;
import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.models.Rule;
import com.cgm.java.hue.models.Scene;
import com.cgm.java.hue.models.Sensor;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * This is a collection of static methods for calling Hue Bridge APIs.
 */
public class HueBridgeGetter extends HttpInteractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(HueBridgeGetter.class);

    /**
     * This method just does a get with hard coded commands. Good luck!
     *
     * @param bridgeIP
     *            the IP address of the bridge
     * @param token
     *            the user ID that should already be registered with the bridge
     * @param apiCall
     *            the api command to call
     * @return whatever the hue bridge returns. Ideally this will be a bunch of JSON, but it could be some HTML that
     *         says your URL is invalid
     */
    public String rawGet(final String bridgeIP, final String token, final String apiCall) {
        Preconditions.checkArgument(StringUtils.isNotBlank(bridgeIP), "bridgeIP may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(token), "token may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(apiCall), "apiCall may not be null or empty.");

        // Hue API calls are normally of the format
        // http://<bridge IP>/api/<user ID>/commands
        final String uri = buildUri(bridgeIP, token, apiCall);
        LOGGER.debug("Will attempt to raw GET: " + uri);
        return getURI(uri);
    }

    /**
     * Same as {@link com.cgm.java.hue.utilities.HueBridgeGetter#rawGet(String, String, String)} but with the ability to
     * add extra stuff to the URL
     *
     * @param bridgeIP
     *            the IP address of the bridge
     * @param token
     *            the user ID that should already be registered with the bridge
     * @param apiCall
     *            the api command to call
     * @param additional
     *            a list of stuff to add to the URI. For example, a normal call to LIGHTS will build a URI like
     *            "/api/1234/lights" but if you want a SPECIFIC light, you should add its ID into this list. Adding "1"
     *            to
     *            the list would make the requested URI "/api/77584ee540184794d3af91523c34302/lights/1". Adding
     *            "1","2","3","4" would make it "/api/77584ee540184794d3af91523c34302/lights/1/2/3/4"
     * @return the raw results of the get
     */
    public String rawGet(final String bridgeIP, final String token, final String apiCall, final List<String> additional) {
        Preconditions.checkArgument(StringUtils.isNotBlank(bridgeIP), "bridgeIP may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(token), "token may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(apiCall), "apiCall may not be null or empty.");
        Preconditions.checkNotNull(additional, "additional may not be null.");

        final String uri = buildUri(bridgeIP, token, apiCall);
        final StringBuilder newUriBuilder = new StringBuilder(uri);
        for (final String thing : additional) {
            newUriBuilder.append("/").append(thing);
        }

        final String newUri = newUriBuilder.toString();
        LOGGER.debug("Will attempt to raw GET: " + newUri);
        return getURI(newUri);
    }

    /**
     * Calls the bridge to get a list of currently configured lights and returns a collection of them parsed out into
     * {@link com.cgm.java.hue.models.Light} objects
     *
     * @param bridgeIp
     *            the IP address of the bridge
     * @param token
     *            the user ID that should already be registered with the bridge
     * @return an {@link java.util.ArrayList} of {@link com.cgm.java.hue.models.Light}s.
     */
    public List<Light> getLights(final String bridgeIp, final String token) {
        Preconditions.checkArgument(StringUtils.isNotBlank(bridgeIp), "bridgeIp may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(token), "token may not be null or empty.");

        LOGGER.debug("Attempting to get all lights.");
        final String rawJsonResults = rawGet(bridgeIp, token, HueBridgeCommands.LIGHTS);
        return new ArrayList<>(HueJsonParser.parseLightsFromJson(rawJsonResults));
    }

    /**
     * Retrieves one light from the bridge
     *
     * @param bridgeIp
     *            the IP address of the bridge
     * @param token
     *            the user ID that should already be registered with the bridge
     * @param lightId
     *            the ID of the light to retrieve
     * @return one {@link com.cgm.java.hue.models.Light} from the bridge
     */
    public Light getLight(final String bridgeIp, final String token, final String lightId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(bridgeIp), "bridgeIp may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(token), "token may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(lightId), "lightId may not be null or empty.");

        LOGGER.debug("Attempting to get light: " + lightId);
        final String rawJsonResult = rawGet(bridgeIp, token, HueBridgeCommands.LIGHTS, ImmutableList.of(lightId));
        final Light light = HueJsonParser.parseLightFromJson(lightId, rawJsonResult);
        return Light.newBuilder(light).setId(lightId).build();
    }

    /**
     * Calls the bridge to get a specific {@link com.cgm.java.hue.models.Scene}
     *
     * @param bridgeIp
     *            the IP address of the bridge
     * @param token
     *            the user ID that should already be registered with the bridge
     * @param sceneId
     *            the ID of the {@link com.cgm.java.hue.models.Scene} to retrieve
     * @return a {@link com.cgm.java.hue.models.Scene}
     */
    public Scene getScene(final String bridgeIp, final String token, final String sceneId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(bridgeIp), "bridgeIp may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(token), "token may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(sceneId), "sceneId may not be null or empty.");

        LOGGER.debug("Attempting to get scene: " + sceneId);
        // Getting scenes requires the format /api/<username>/scenes/<id>
        final String getSceneUri = buildUri(bridgeIp, token, HueBridgeCommands.SCENES, ImmutableList.of(sceneId));
        final String sceneJson = getURI(getSceneUri);
        return HueJsonParser.parseSceneFromJson(sceneId, sceneJson);
    }

    /**
     * Calls the bridge to get a list of currently configured scenes and returns a collection of them parsed out into
     * {@link com.cgm.java.hue.models.Scene} objects
     *
     * @param bridgeIp
     *            the IP address of the bridge
     * @param token
     *            the user ID that should already be registered with the bridge
     * @return a {@link java.util.List} of {@link com.cgm.java.hue.models.Scene}s
     */
    public List<Scene> getScenes(final String bridgeIp, final String token) {
        Preconditions.checkArgument(StringUtils.isNotBlank(bridgeIp), "bridgeIp may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(token), "token may not be null or empty.");

        LOGGER.debug("Attempting to get all scenes");
        final String rawJsonResults = rawGet(bridgeIp, token, HueBridgeCommands.SCENES);
        return new ArrayList<>(HueJsonParser.parseScenesFromJson(rawJsonResults));
    }

    /**
     * Calls the bridge to get a list of currently configured groups and returns a collection of them parsed out into
     * {@link com.cgm.java.hue.models.Group} objects
     *
     * @param bridgeIp
     *            the IP address of the bridge
     * @param token
     *            the user ID that should already be registered with the bridge
     * @return a {@link java.util.List} of {@link com.cgm.java.hue.models.Group}s
     */
    public List<Group> getGroups(final String bridgeIp, final String token) {
        Preconditions.checkArgument(StringUtils.isNotBlank(bridgeIp), "bridgeIp may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(token), "token may not be null or empty.");

        LOGGER.debug("Attempting to get all groups");
        final String rawJsonResults = rawGet(bridgeIp, token, HueBridgeCommands.GROUPS);
        return new ArrayList<>(HueJsonParser.parseGroupsFromJson(rawJsonResults));
    }

    /**
     * Calls the bridge to get a specific {@link com.cgm.java.hue.models.Group}
     *
     * @param bridgeIp
     *            the IP address of the bridge
     * @param token
     *            the user ID that should already be registered with the bridge
     * @param groupId
     *            the ID of the {@link com.cgm.java.hue.models.Group} to retrieve
     * @return a {@link com.cgm.java.hue.models.Group}
     */
    public Group getGroup(final String bridgeIp, final String token, final String groupId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(bridgeIp), "bridgeIp may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(token), "token may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(groupId), "groupId may not be null or empty.");

        LOGGER.debug("Attempting to get group: " + groupId);
        // Getting groups requires the format /api/<username>/groups/<id>
        final String getGroupUri = buildUri(bridgeIp, token, HueBridgeCommands.GROUPS, ImmutableList.of(groupId));
        final String sceneJson = getURI(getGroupUri);
        return HueJsonParser.parseGroupFromJson(groupId, sceneJson);
    }

    /**
     * Calls the bridge to get a list of currently configured sensors and returns a collection of them parsed out into
     * {@link com.cgm.java.hue.models.Sensor} objects
     *
     * @param bridgeIp
     *            the IP address of the bridge
     * @param token
     *            the user ID that should already be registered with the bridge
     * @return a {@link java.util.List} of {@link com.cgm.java.hue.models.Sensor}s
     */
    public List<Sensor> getSensors(final String bridgeIp, final String token) {
        Preconditions.checkArgument(StringUtils.isNotBlank(bridgeIp), "bridgeIp may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(token), "token may not be null or empty.");

        LOGGER.debug("Attempting to get all groups");
        final String rawJsonResults = rawGet(bridgeIp, token, HueBridgeCommands.SENSORS);
        return new ArrayList<>(HueJsonParser.parseSensorsFromJson(rawJsonResults));
    }

    /**
     * Calls the bridge to get a specific {@link com.cgm.java.hue.models.Sensor}
     *
     * @param bridgeIp
     *            the IP address of the bridge
     * @param token
     *            the user ID that should already be registered with the bridge
     * @param sensorId
     *            the ID of the {@link com.cgm.java.hue.models.Sensor} to retrieve
     * @return a {@link com.cgm.java.hue.models.Sensor}
     */
    public Sensor getSensor(final String bridgeIp, final String token, final String sensorId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(bridgeIp), "bridgeIp may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(token), "token may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(sensorId), "sensorId may not be null or empty.");

        LOGGER.debug("Attempting to get sensor: " + sensorId);
        // Getting groups requires the format /api/<username>/sensors/<id>
        final String getSensorUri = buildUri(bridgeIp, token, HueBridgeCommands.SENSORS, ImmutableList.of(sensorId));
        final String sceneJson = getURI(getSensorUri);
        return HueJsonParser.parseSensorFromJson(sensorId, sceneJson);
    }

    /**
     * Calls the bridge to get a list of currently configured sensors and returns a collection of them parsed out into
     * {@link com.cgm.java.hue.models.Rule} objects
     *
     * @param bridgeIp
     *            the IP address of the bridge
     * @param token
     *            the user ID that should already be registered with the bridge
     * @return a {@link java.util.List} of {@link com.cgm.java.hue.models.Rule}s
     */
    public List<Rule> getRules(final String bridgeIp, final String token) {
        Preconditions.checkArgument(StringUtils.isNotBlank(bridgeIp), "bridgeIp may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(token), "token may not be null or empty.");

        LOGGER.debug("Attempting to get all groups");
        final String rawJsonResults = rawGet(bridgeIp, token, HueBridgeCommands.RULES);
        return new ArrayList<>(HueJsonParser.parseRulesFromJson(rawJsonResults));
    }

    /**
     * Calls the bridge to get a specific {@link com.cgm.java.hue.models.Rule}
     *
     * @param bridgeIp
     *            the IP address of the bridge
     * @param token
     *            the user ID that should already be registered with the bridge
     * @param ruleId
     *            the ID of the {@link com.cgm.java.hue.models.Rule} to retrieve
     * @return a {@link com.cgm.java.hue.models.Rule}
     */
    public Rule getRule(final String bridgeIp, final String token, final String ruleId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(bridgeIp), "bridgeIp may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(token), "token may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(ruleId), "ruleId may not be null or empty.");

        LOGGER.debug("Attempting to get rule: " + ruleId);
        // Getting groups requires the format /api/<username>/rules/<id>
        final String getSensorUri = buildUri(bridgeIp, token, HueBridgeCommands.RULES, ImmutableList.of(ruleId));
        final String sceneJson = getURI(getSensorUri);
        return HueJsonParser.parseRuleFromJson(ruleId, sceneJson);
    }
}
