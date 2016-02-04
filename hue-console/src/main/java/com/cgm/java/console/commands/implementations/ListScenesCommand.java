package com.cgm.java.console.commands.implementations;

import java.net.UnknownHostException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;

import com.cgm.java.console.commands.BridgeCommand;
import com.cgm.java.hue.utilities.HueBridgeCommands;

/**
 * Prints the raw JSON for all available Scenes
 */
public class ListScenesCommand extends BridgeCommand {

    @Override
    protected int run(final CommandLine line) throws UnknownHostException {
        setIpAndId(line);
        final String fullScenesList = HUE_BRIDGE_GETTER.rawGet(bridgeIp, token, HueBridgeCommands.SCENES);
        System.out.println("Here is the raw JSON for currently available scenes: ");
        System.out.println(fullScenesList);

        System.out.println("Here are those scenes converted into AVRO: ");
        HUE_BRIDGE_GETTER.getScenes(bridgeIp, token).forEach(System.out::println);

        return 0;
    }

    @Override
    public void usage() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(getName() + " <bridge IP> <hue ID>",
                "A command to print a JSON dump of all available scenes.",
                getOptions(), null);
    }

    @Override
    public String getName() {
        return "listscenes";
    }
}
