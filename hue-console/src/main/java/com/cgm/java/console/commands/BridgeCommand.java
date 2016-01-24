package com.cgm.java.console.commands;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgm.java.hue.utilities.HueBridgeGetter;
import com.cgm.java.hue.utilities.HueBridgeSetter;

/**
 * A super class
 */
public abstract class BridgeCommand extends Command {
    /**
     * The instance of {@link com.cgm.java.hue.utilities.HueBridgeGetter} used to retrieve data from the Hue Bridge
     */
    protected static final HueBridgeGetter HUE_BRIDGE_GETTER = new HueBridgeGetter();
    /**
     * The instance of {@link com.cgm.java.hue.utilities.HueBridgeSetter} used to send data to the Hue Bridge
     */
    protected static final HueBridgeSetter HUE_BRIDGE_SETTER = new HueBridgeSetter();
    /**
     * The IP address for the bridge
     */
    protected String bridgeIp;
    /**
     * The ID to use when calling APIs
     */
    protected String token;

    private static final Logger LOGGER = LoggerFactory.getLogger(BridgeCommand.class);

    /**
     * Parses the ID and IP
     * 
     * @param line
     *            the input to the command
     */
    protected void setIpAndId(final CommandLine line) {
        final String[] args = line.getArgs();
        if (args.length != 2) {
            throw new RuntimeException("You must specify both a bridge IP and a hue ID.");
        }
        try {
            bridgeIp = InetAddress.getByName(args[0]).getHostAddress();
        } catch (UnknownHostException uhe) {
            LOGGER.warn("Unable to determine if input IP, '" + args[0] + "' is valid. Will attempt to continue anyway.");
            bridgeIp = args[0];
        } finally {
            token = args[1];
        }
    }
}
