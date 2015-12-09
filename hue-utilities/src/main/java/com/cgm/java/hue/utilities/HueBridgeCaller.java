package com.cgm.java.hue.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a collection of static methods for calling Hue Bridge APIs.
 */
public class HueBridgeCaller {
    private static final Logger LOGGER = LoggerFactory.getLogger(HueBridgeCaller.class);

    /**
     * This method just does a get with hard coded commands. Good luck!
     * 
     * @param bridgeIP
     *            the IP address of the bridge
     * @param hueID
     *            the user ID that should already be registered with the bridge
     * @param apiCall
     *            the api command to call
     * @return whatever the hue bridge returns. Ideally this will be a bunch of JSON, but it could be some HTML that
     *         says your URL is invalid
     */
    public static String rawGet(final String bridgeIP, final String hueID, final String apiCall) {
        CloseableHttpClient httpClient = null;
        InputStream contentStream = null;
        try {
            // Hue API calls are normally of the format
            // http://<bridge IP>/api/<user ID>/commands
            final String uri = "http://" + bridgeIP + "/api/" + hueID + "/" + apiCall;
            LOGGER.info("Will attempt to GET: " + uri);

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
