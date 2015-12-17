package com.cgm.java.console.commands.implementations;

import java.net.UnknownHostException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;

import com.cgm.java.console.commands.BridgeCommand;
import com.cgm.java.hue.utilities.HueBridgeCommands;
import com.cgm.java.hue.utilities.HueBridgeGetter;

/**
 * Prints the raw JSON for all available lights
 */
public class ListLightsCommand extends BridgeCommand {

    @Override
    protected int run(final CommandLine line) throws UnknownHostException {
        setIpAndId(line);
        final String fullLightsList = HueBridgeGetter.rawGet(bridgeIp, hueId, HueBridgeCommands.LIGHTS);
        System.out.println("Here is all available information on currently connected lights:");
        System.out.println(fullLightsList);

        return 0;
    }

    @Override
    public void usage() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(getName() + " <bridge IP> <hue ID>",
                "A command to print a JSON dump of all connected lights.",
                getOptions(), null);
    }

    @Override
    public String getName() {
        return "listlights";
    }
}
