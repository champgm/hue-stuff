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
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.models.Scene;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;

/**
 * This is a collection of static methods for calling Hue Bridge APIs.
 */
public class HueBridgeGetter {
    private static final Logger LOGGER = LoggerFactory.getLogger(HueBridgeGetter.class);
    private final String lightIdRegex = ",\"\\d*\":";
    private final String sceneIdRegex = "\"\\w*-\\w*-\\w*\":";

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
        // Hue API calls are normally of the format
        // http://<bridge IP>/api/<user ID>/commands
        final String uri = buildUri(bridgeIP, token, apiCall);
        LOGGER.info("Will attempt to GET: " + uri);
        return hitURI(uri);
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
        final String rawJsonResults = rawGet(bridgeIp, hueId, HueBridgeCommands.LIGHTS);

        // Strip this off: '{"1":'
        final String jsonLightsString = rawJsonResults.substring(5);

        // Items are separated by ,"#":
        final String[] jsonLightsArray = jsonLightsString.split(lightIdRegex);

        // Convert the array to an ArrayList
        final ArrayList<String> jsonLightsArrayList = new ArrayList<>(Arrays.asList(jsonLightsArray));

        // Use a BiFunction and .indexOf to gather each light's json and its id (its index in the array + 1)
        final List<Light> resultList = jsonLightsArrayList.stream()
                .map(jsonString -> HueConverters.JSON_TO_LIGHT.apply(jsonLightsArrayList.indexOf(jsonString) + 1, jsonString))
                .collect(Collectors.toList());

        return new ArrayList<>(resultList);
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
        final String rawJsonResults = rawGet(bridgeIp, hueId, HueBridgeCommands.SCENES);

        // Strip this off: '{'
        final String scenesJsonString = rawJsonResults.substring(1);

        // Items are separated by ,"the-scene-ID":
        final ArrayList<String> sceneJsonArrayList = new ArrayList<>(Arrays.asList(scenesJsonString.split(sceneIdRegex)));
        // this seems hacky, but since the raw json will start with a scene ID, the first item in this array will be
        // blank, so...
        sceneJsonArrayList.remove(0);

        final Pattern lightIdPattern = Pattern.compile(sceneIdRegex);
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
