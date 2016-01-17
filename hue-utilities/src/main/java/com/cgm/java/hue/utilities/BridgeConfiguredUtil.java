package com.cgm.java.hue.utilities;

/**
 * A common class for utilities that need configuration to interact with the bridge
 */
public class BridgeConfiguredUtil {
    protected static final HueBridgeGetter HUE_BRIDGE_GETTER = new HueBridgeGetter();
    protected static final HueBridgePutter HUE_BRIDGE_PUTTER = new HueBridgePutter();
    protected static final HueConfiguration HUE_CONFIGURATION = new HueConfiguration();
    protected static final String BRIDGE_TOKEN = HUE_CONFIGURATION.getToken();
    protected static final String BRIDGE_IP = HUE_CONFIGURATION.getIP();
}
