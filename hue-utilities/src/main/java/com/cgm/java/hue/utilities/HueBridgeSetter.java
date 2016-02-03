package com.cgm.java.hue.utilities;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgm.java.hue.models.Scene;
import com.cgm.java.hue.models.State;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * A utility class for writing data to the hue bridge
 */
public class HueBridgeSetter extends HttpInteractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(HueBridgeSetter.class);

    /**
     * Sets one light's state
     *
     * @param bridgeIp
     *            IP for the hue bridge
     * @param token
     *            API token for the bridge
     * @param lightId
     *            the ID of the light to modify
     * @param state
     *            the desired state for that light
     * @return whatever the response from the bridge is
     */
    public String setLightState(final String bridgeIp, final String token, final String lightId, final State state) {
        Preconditions.checkArgument(StringUtils.isNotBlank(bridgeIp), "bridgeIp may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(token), "token may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(lightId), "lightId may not be null or empty.");
        Preconditions.checkArgument(state != null, "state may not be null.");

        // State calls look like this:
        // http://<bridge ip address>/api/1234/lights/1/state
        final String uri = buildUri(bridgeIp, token, HueBridgeCommands.LIGHTS, ImmutableList.of(lightId, "state"));
        return putURI(uri, state.toString());
    }

    /**
     * Attempts to use POST to add a new scene to the bridge.
     *
     * @param bridgeIp
     *            IP for the hue bridge
     * @param token
     *            API token for the bridge
     * @param scene
     *            the scene to POST
     * @return the ID of the newly created scene
     */
    public String postNewScene(final String bridgeIp, final String token, final Scene scene) {
        Preconditions.checkArgument(StringUtils.isNotBlank(bridgeIp), "bridgeIp may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(token), "token may not be null or empty.");
        Preconditions.checkArgument(scene != null, "scene may not be null.");

        // Scene post calls look like this:
        // http://<bridge ip address>/api/1234/scenes
        final String uri = buildUri(bridgeIp, token, HueBridgeCommands.SCENES);

        // Ok, the API is really strict about what you put in this request. The body should only contain "name" and
        // "lights" fields. If you include more, it will error out. So, let's build a nice one.
        // Expected format: {"name":"Romantic dinner", "lights":["1","2"]}
        final String requestBody =
                "{\"name\":\"" +
                        scene.getName() +
                        "\", \"lights\":[\"" +
                        String.join("\",\"", scene.getLights()) +
                        "\"]}";

        final String rawResponse = postURI(uri, requestBody);
        if (!rawResponse.contains("success")) {
            throw new RuntimeException("POSTing of new scene failed.\n" +
                    "Attempted URI was: " + uri + "\n" +
                    "Attempted body was: " + requestBody + "\n" +
                    "Raw response was: " + rawResponse);
        }
        try {
            // The response will look something like this:
            // [{"success":{"id":"Abc123Def456Ghi"}}]
            final String splitOnId = "\"id\":\"";
            final String[] splitResult = rawResponse.split(splitOnId);
            final String secondPart = splitResult[1];
            final String[] secondSplit = secondPart.split("\"");
            return secondSplit[0];
        } catch (Exception e) {
            throw new RuntimeException("Error while parsing response to a POST of a new scene.\n" +
                    "Attempted URI was: " + uri + "\n" +
                    "Attempted body was: " + requestBody + "\n" +
                    "Raw response was: " + rawResponse,
                    e);
        }
    }

    /**
     * Deletes the specified {@link com.cgm.java.hue.models.Scene}
     *
     * @param bridgeIp
     *            IP for the hue bridge
     * @param token
     *            API token for the bridge
     * @param sceneId
     *            the id of the {@link com.cgm.java.hue.models.Scene} to delete
     * @return the response from the bridge
     */
    public String deleteScene(final String bridgeIp, final String token, final String sceneId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(bridgeIp), "bridgeIp may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(token), "token may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(sceneId), "sceneId may not be null or empty.");

        final String uri = buildUri(bridgeIp, token, HueBridgeCommands.SCENES, ImmutableList.of(sceneId));
        return deleteURI(uri);
    }

    /**
     * Sets one group's state
     *
     * @param bridgeIp
     *            IP for the hue bridge
     * @param token
     *            API token for the bridge
     * @param groupId
     *            the ID of the light to modify
     * @param state
     *            the desired state for that group
     * @return whatever the response from the bridge is
     */
    public String setGroupState(final String bridgeIp, final String token, final String groupId, final State state) {
        Preconditions.checkArgument(StringUtils.isNotBlank(bridgeIp), "bridgeIp may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(token), "token may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(groupId), "groupId may not be null or empty.");
        Preconditions.checkNotNull(state, "state may not be null.");

        // set-action calls look like this:
        // http://<bridge ip address>/api/1234/groups/1/action
        final String uri = buildUri(bridgeIp, token, HueBridgeCommands.GROUPS, ImmutableList.of(groupId, "action"));
        return putURI(uri, state.toString());
    }

    /**
     * Deletes the specified {@link com.cgm.java.hue.models.Group}
     *
     * @param bridgeIp
     *            IP for the hue bridge
     * @param token
     *            API token for the bridge
     * @param groupId
     *            the id of the {@link com.cgm.java.hue.models.Group} to delete
     * @return the response from the bridge
     */
    public String deleteGroup(final String bridgeIp, final String token, final String groupId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(bridgeIp), "bridgeIp may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(token), "token may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(groupId), "groupId may not be null or empty.");

        final String uri = buildUri(bridgeIp, token, HueBridgeCommands.GROUPS, ImmutableList.of(groupId));
        return deleteURI(uri);
    }
}
