package com.cgm.java.hue.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collection;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;

/**
 * A superclass for utility classes that interact with the bridge.
 */
public class HttpInteractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpInteractor.class);

    @VisibleForTesting
    protected String buildUri(final String bridgeIP, final String token, final String apiCall, final Collection<String> additionalParts) {
        final StringBuilder uriBuilder = new StringBuilder("http://" + bridgeIP + "/api/" + token + "/" + apiCall);
        for (final String additionalPart : additionalParts) {
            uriBuilder.append("/").append(additionalPart);
        }
        final String resultUri = uriBuilder.toString();
        LOGGER.debug("Built URI: " + resultUri);
        return resultUri;
    }

    @VisibleForTesting
    protected String buildUri(final String bridgeIP, final String token, final String apiCall) {
        return buildUri(bridgeIP, token, apiCall, ImmutableList.of());
    }

    @VisibleForTesting
    protected String postURI(final String uri, final String body) {
        final HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(new ByteArrayEntity(body.getBytes()));
        LOGGER.debug("Building POST with URI: " + uri);
        LOGGER.debug("Building POST with body: " + body);
        return hitURI(httpPost);
    }

    @VisibleForTesting
    protected String deleteURI(final String uri) {
        final HttpDelete httpDelete = new HttpDelete(uri);
        LOGGER.debug("Building DELETE with URI: " + uri);
        return hitURI(httpDelete);
    }

    @VisibleForTesting
    protected String putURI(final String uri, final String body) {
        final HttpPut httpPut = new HttpPut(uri);
        httpPut.setEntity(new ByteArrayEntity(body.getBytes()));
        LOGGER.debug("Building PUT with URI: " + uri);
        LOGGER.debug("Building PUT with body: " + body);
        return hitURI(httpPut);
    }

    @VisibleForTesting
    protected String getURI(final String uri) {
        final HttpGet httpGet = new HttpGet(uri);
        LOGGER.debug("Building GET with URI: " + uri);
        return hitURI(httpGet);
    }

    @VisibleForTesting
    protected String hitURI(final HttpRequestBase request) {
        LOGGER.debug("Attempting to hit URI: " + request.getURI());
        CloseableHttpClient httpClient = null;
        InputStream contentStream = null;
        try {
            // Apache httpclient is pretty handy
            httpClient = HttpClients.createDefault();
            final CloseableHttpResponse putResponse = httpClient.execute(request);
            contentStream = putResponse.getEntity().getContent();

            // But we just need the full contents of the stream, so dump it into a string
            final StringWriter fullContentWriter = new StringWriter();
            IOUtils.copy(contentStream, fullContentWriter, "UTF8");

            final String rawResponse = fullContentWriter.toString();
            LOGGER.debug("Raw response: " + rawResponse);
            return rawResponse;
        } catch (IOException e) {
            throw new RuntimeException("Error while executing an HTTP call to: " + request.getURI(), e);
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
