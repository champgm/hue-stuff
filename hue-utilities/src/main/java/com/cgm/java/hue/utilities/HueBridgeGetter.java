package com.cgm.java.hue.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.models.Scene;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * This is a collection of static methods for calling Hue Bridge APIs.
 */
public class HueBridgeGetter {
    private static final Logger LOGGER = LoggerFactory.getLogger(HueBridgeGetter.class);
    private static final String LIGHT_ID_REGEX = ",\"\\d*\":";
    private static final String SCENE_ID_REGEX = "\"\\w*-\\w*-\\w*\":";

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
        LOGGER.info("Will attempt to GET: " + uri);
        return hitURI(uri);
    }

    /**
     * Same as {@link HueBridgeGetter#rawGet(String, String, String)} but with the ability to
     * add extra stuff to the URL
     * 
     * @param bridgeIP
     * @param token
     * @param apiCall
     * @param additional
     *            a list of stuff to add to the URI. For example, a normal call to LIGHTS will build a URI like
     *            "/api/1234/lights" but if you want a SPECIFIC light, you should add its ID into this list. Adding "1"
     *            to the list would make the requested URI "/api/77584ee540184794d3af91523c34302/lights/1". Adding
     *            "1","2","3","4" would make it "/api/77584ee540184794d3af91523c34302/lights/1/2/3/4"
     * @return
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
        return hitURI(newUriBuilder.toString());
    }

    /**
     * Calls the bridge to get a list of currently configured lights and returns a collection of them parsed out into
     * {@link com.cgm.java.hue.models.Light} objects
     * 
     * @param bridgeIp
     *            the IP address of the bridge
     * @param hueId
     *            the user ID that should already be registered with the bridge
     * @return an {@link java.util.ArrayList} of {@link com.cgm.java.hue.models.Light}s.
     */
    public List<Light> getLights(final String bridgeIp, final String hueId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(bridgeIp), "bridgeIp may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(hueId), "hueId may not be null or empty.");

        final String rawJsonResults = rawGet(bridgeIp, hueId, HueBridgeCommands.LIGHTS);

        // Strip this off: '{"1":'
        final String jsonLightsString = rawJsonResults.substring(5);

        // Items are separated by ,"#":
        final String[] jsonLightsArray = jsonLightsString.split(LIGHT_ID_REGEX);

        // Convert the array to an ArrayList
        final ArrayList<String> jsonLightsArrayList = new ArrayList<>(Arrays.asList(jsonLightsArray));

        // Use a BiFunction and .indexOf to gather each light's json and its id (its index in the array + 1)
        final List<Light> resultList = jsonLightsArrayList.stream().map(jsonString -> HueConverters.JSON_TO_LIGHT.apply(jsonLightsArrayList.indexOf(jsonString) + 1, jsonString)).collect(Collectors.toList());

        return new ArrayList<>(resultList);
    }

    public Light getLight(final String bridgeIp, final String hueId, final String lightIdString) {
        Preconditions.checkArgument(StringUtils.isNotBlank(bridgeIp), "bridgeIp may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(hueId), "hueId may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(lightIdString), "lightIdString may not be null or empty.");
        final Integer lightId = Integer.valueOf(lightIdString);

        final String rawJsonResult = rawGet(bridgeIp, hueId, HueBridgeCommands.LIGHTS, ImmutableList.of(lightIdString));
        final Light light = HueConverters.JSON_TO_LIGHT.apply(lightId, rawJsonResult);

        return Light.newBuilder(light).setId(lightIdString).build();
    }

    /**
     * Calls the bridge to get a list of currently configured scenes and returns a collection of them parsed out into
     * {@link com.cgm.java.hue.models.Scene} objects
     *
     * @param bridgeIp
     *            the IP address of the bridge
     * @param hueId
     *            the user ID that should already be registered with the bridge
     * @return an {@link java.util.ArrayList} of {@link com.cgm.java.hue.models.Scene}s
     */
    public List<Scene> getScenes(final String bridgeIp, final String hueId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(bridgeIp), "bridgeIp may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(hueId), "hueId may not be null or empty.");

        final String rawJsonResults = rawGet(bridgeIp, hueId, HueBridgeCommands.SCENES);

        // Strip this off: '{'
        final String scenesJsonString = rawJsonResults.substring(1);

        // Items are separated by ,"the-scene-ID":
        final ArrayList<String> sceneJsonArrayList = new ArrayList<>(Arrays.asList(scenesJsonString.split(SCENE_ID_REGEX)));
        // this seems hacky, but since the raw json will start with a scene ID, the first item in this array will be
        // blank, so...
        sceneJsonArrayList.remove(0);

        final Pattern lightIdPattern = Pattern.compile(SCENE_ID_REGEX);
        final Matcher patternMatcher = lightIdPattern.matcher(scenesJsonString);
        final ArrayList<String> sceneIds = new ArrayList<>(sceneJsonArrayList.size());
        while (patternMatcher.find()) {
            final String untrimmedSceneId = patternMatcher.group();
            sceneIds.add(untrimmedSceneId.substring(1, untrimmedSceneId.length() - 2));
        }

        final ImmutableList.Builder<Scene> resultBuilder = ImmutableList.builder();
        for (int i = 0; i < sceneIds.size(); i++) {
            final String sceneId = sceneIds.get(i);
            final String sceneJson = sceneJsonArrayList.get(i);
            System.out.println("Attempting to convert: " + sceneJson);
            final Scene parsedScene = HueConverters.JSON_TO_SCENE.apply(sceneId, sceneJson);
            resultBuilder.add(parsedScene);
        }
        return resultBuilder.build();
    }

    @VisibleForTesting
    protected String buildUri(final String bridgeIP, final String token, final String apiCall) {
        return "http://" + bridgeIP + "/api/" + token + "/" + apiCall;
    }

    @VisibleForTesting
    protected String hitURI(final String uri) {
        CloseableHttpClient httpClient = null;
        InputStream contentStream = null;
        try {
            // Apache httpclient is pretty handy
            final HttpGet httpGet = new HttpGet(uri);
            httpClient = HttpClients.createDefault();
            final CloseableHttpResponse getResponse = httpClient.execute(httpGet);
            contentStream = getResponse.getEntity().getContent();

            // But we just need the full contents of the stream, so dump it into a string
            final StringWriter fullContentWriter = new StringWriter();
            IOUtils.copy(contentStream, fullContentWriter, "UTF8");

            return fullContentWriter.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error while executing a raw HTTP get.", e);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
                if (contentStream != null) {
                    contentStream.close();
                }
            } catch (IOException e) {
                LOGGER.error("Error closing HTTP client after call.");
            }
        }
    }
}
