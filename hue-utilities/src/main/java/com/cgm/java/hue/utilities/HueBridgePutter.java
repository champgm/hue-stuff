package com.cgm.java.hue.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgm.java.hue.models.State;
import com.google.common.base.Preconditions;

/**
 * A utility class for writing data to the hue bridge
 */
public class HueBridgePutter {
    private static final Logger LOGGER = LoggerFactory.getLogger(HueBridgePutter.class);

    /**
     * Sets one light's state
     * 
     * @param bridgeIp
     *            IP for the hue bridge
     * @param hueId
     *            API token for the bridge
     * @param lightId
     *            the ID of the light to modify
     * @param state
     *            the desired state for that light
     * @return whatever the response from the bridge is
     */
    public String setLightState(final String bridgeIp, final String hueId, final String lightId, final State state) {
        Preconditions.checkArgument(StringUtils.isNotBlank(bridgeIp), "bridgeIp may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(hueId), "hueId may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(lightId), "lightId may not be null or empty.");
        Preconditions.checkNotNull(state, "state may not be null.");

        // State calls look like this:
        // http://<bridge ip address>/api/1028d66426293e821ecfd9ef1a0731df/lights/1/state
        final String uri = "http://" + bridgeIp + "/api/" + hueId + "/lights/" + lightId + "/state";
        return hitURI(uri, state.toString());
    }

    /**
     * Use
     * {@link com.cgm.java.hue.utilities.HueBridgePutter#setLightState(String, String, String, com.cgm.java.hue.models.State)}
     * instead.
     */
    @Deprecated
    public String setLightState(final String bridgeIp, final String hueId, final int lightIndex, final State state) {
        Preconditions.checkArgument(StringUtils.isNotBlank(bridgeIp), "bridgeIp may not be null or empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(hueId), "hueId may not be null or empty.");
        Preconditions.checkNotNull(state, "state may not be null.");

        final int baseOneIndex = lightIndex + 1;
        return setLightState(bridgeIp, hueId, String.valueOf(baseOneIndex), state);
    }

    private static String hitURI(final String uri, final String body) {
        CloseableHttpClient httpClient = null;
        InputStream contentStream = null;
        try {
            // Apache httpclient is pretty handy
            final HttpPut httpPut = new HttpPut(uri);
            httpPut.setEntity(new ByteArrayEntity(body.getBytes()));
            httpClient = HttpClients.createDefault();
            final CloseableHttpResponse putResponse = httpClient.execute(httpPut);
            contentStream = putResponse.getEntity().getContent();

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
