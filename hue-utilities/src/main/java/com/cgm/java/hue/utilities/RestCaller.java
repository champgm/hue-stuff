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
 * Created by mc023219 on 12/9/15.
 */
public class RestCaller {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestCaller.class);

    public static String rawGet(final String bridgeIP, final String hueID, final String rest) {
        CloseableHttpClient httpClient = null;
        InputStream contentStream = null;
        try {
            final String uri = "http://" + bridgeIP + "/" + hueID + "/" + rest;
            LOGGER.info("Will attempt to GET: " + uri);

            final HttpGet httpGet = new HttpGet(uri);
            httpClient = HttpClients.createDefault();
            final CloseableHttpResponse getResponse = httpClient.execute(httpGet);
            contentStream = getResponse.getEntity().getContent();
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
