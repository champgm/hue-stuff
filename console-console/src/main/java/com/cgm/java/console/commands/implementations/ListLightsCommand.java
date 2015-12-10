package com.cgm.java.console.commands.implementations;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;

import com.cgm.java.console.commands.Command;
import com.cgm.java.hue.utilities.HueBridgeCaller;
import com.cgm.java.hue.utilities.HueCommands;

/**
 * Prints the raw JSON for all available lights
 */
public class ListLightsCommand extends Command {

    @Override
    protected int run(final CommandLine line) throws UnknownHostException {

        final String[] args = line.getArgs();
        if (args.length != 2) {
            System.out.println("You must specify both a bridge IP and a hue ID.");
        }

        final InetAddress ipAddress = InetAddress.getByName(args[0]);

        final String fullLightsList = HueBridgeCaller.rawGet(ipAddress.getHostAddress(), args[1], HueCommands.LIGHTS);
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
