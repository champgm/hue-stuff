package com.cgm.java.hue.utilities;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.models.State;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

/**
 * Utility for interacting with lights
 */
public class LightUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(LightUtil.class);

    /**
     * Toggles the light with the given ID
     *
     * @param lightId
     *         the ID of the light to toggle
     * @return the light state, retrieved from the bridge after being toggled
     */
    public Light toggleLight(final String bridgeIp, final String token, final String lightId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(lightId), "lightId may not be null or empty.");
        LOGGER.info("Attempting to toggle one light with ID: " + lightId);

        // Get the light
        final Light light = getHueBridgeGetter().getLight(bridgeIp, token, lightId);

        // Create an opposite-on state
        final State state = light.getState();
        final Boolean onState = state.getOn();

        // Put the new state
        getHueBridgeSetter().setLightOnState(bridgeIp, token, String.valueOf(light.getId()), !onState);

        // Get the resulting light and return it
        final Light newLight = getHueBridgeGetter().getLight(bridgeIp, token, lightId);
        LOGGER.info("New light state after toggling: " + newLight.getState());
        return newLight;
    }

    @VisibleForTesting
    protected HueBridgeGetter getHueBridgeGetter() {
        return new HueBridgeGetter();
    }

    @VisibleForTesting
    protected HueBridgeSetter getHueBridgeSetter() {
        return new HueBridgeSetter();
    }
}
