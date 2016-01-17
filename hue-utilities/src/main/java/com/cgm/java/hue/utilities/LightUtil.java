package com.cgm.java.hue.utilities;

import org.apache.commons.lang.StringUtils;

import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.models.State;
import com.google.common.base.Preconditions;

/**
 * Utility for interacting with lights
 */
public class LightUtil extends BridgeConfiguredUtil {
    /**
     * Toggles the light with the given ID
     * 
     * @param lightId
     *            the ID of the light to toggle
     * @return whatever the bridge's response was
     */
    public static String toggleLight(final String lightId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(lightId), "lightId may not be null or empty.");

        // Get the light
        final Light light = HUE_BRIDGE_GETTER.getLight(BRIDGE_IP, BRIDGE_TOKEN, lightId);

        // Create an opposite-on state
        final State state = light.getState();
        final Boolean onState = state.getOn();
        final State newState = State.newBuilder(state).setOn(!onState).build();

        // Put and return result.
        return HUE_BRIDGE_PUTTER.setLightState(BRIDGE_IP, BRIDGE_TOKEN, String.valueOf(light.getId()), newState);
    }
}
