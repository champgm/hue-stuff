package com.cgm.java.hue.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgm.java.hue.models.State;

/**
 * Created by mc023219 on 12/17/15.
 */
public class HueBridgePutter {
    private static final Logger LOGGER = LoggerFactory.getLogger(HueBridgePutter.class);

    public static String setLightState(final String bridgeIp, final String hueId, final int lightIndex, final State state) {
        final int baseOneIndex = lightIndex + 1;
        // State calls look like this:
        // http://<bridge ip address>/api/1028d66426293e821ecfd9ef1a0731df/lights/1/state
        final String uri = "http://" + bridgeIp + "/api/" + hueId + "/lights/" + baseOneIndex + "/state";
        LOGGER.debug("Attempting put to PUT: " + uri);
        LOGGER.debug("Body: " + state);
        return hitURI(uri, state.toString());
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

            final String result = fullContentWriter.toString();
            LOGGER.debug("Result of PUT: "+result);
            return result;
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
