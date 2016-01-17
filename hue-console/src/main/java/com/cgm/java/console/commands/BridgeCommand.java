package com.cgm.java.console.commands;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.cli.CommandLine;

import com.cgm.java.hue.utilities.HueBridgeGetter;

/**
 * A super class
 */
public abstract class BridgeCommand extends Command {
    /**
     * The instance of {@link com.cgm.java.hue.utilities.HueBridgeGetter} used to retrieve data from the Hue Bridge
     */
    protected static final HueBridgeGetter HUE_BRIDGE_GETTER = new HueBridgeGetter();

    /**
     * The IP address for the bridge
     */
    protected String bridgeIp;
    /**
     * The ID to use when calling APIs
     */
    protected String hueId;

    /**
     * Parses the ID and IP
     * 
     * @param line
     *            the input to the command
     * @throws UnknownHostException
     */
    protected void setIpAndId(final CommandLine line) {
        final String[] args = line.getArgs();
        if (args.length != 2) {
            throw new RuntimeException("You must specify both a bridge IP and a hue ID.");
        }
        try {
            bridgeIp = InetAddress.getByName(args[0]).getHostAddress();
            hueId = args[1];
        } catch (UnknownHostException uhe) {
            throw new RuntimeException("Unknown host from IP: " + args[0]);
        }
    }
}
