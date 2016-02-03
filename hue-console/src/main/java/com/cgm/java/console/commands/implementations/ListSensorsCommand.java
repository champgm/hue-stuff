package com.cgm.java.console.commands.implementations;

import java.net.UnknownHostException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;

import com.cgm.java.console.commands.BridgeCommand;
import com.cgm.java.hue.utilities.HueBridgeCommands;

/**
 * Prints the raw JSON for all available {@link com.cgm.java.hue.models.Sensor}s
 */
public class ListSensorsCommand extends BridgeCommand {

    @Override
    protected int run(final CommandLine line) throws UnknownHostException {
        setIpAndId(line);
        final String fullSensorsList = HUE_BRIDGE_GETTER.rawGet(bridgeIp, token, HueBridgeCommands.SENSORS);
        System.out.println("Here is the raw JSON for currently available sensors: ");
        System.out.println(fullSensorsList);

        System.out.println("Here are those sensors converted into AVRO: ");
        HUE_BRIDGE_GETTER.getSensors(bridgeIp, token).forEach(System.out::println);

        return 0;
    }

    @Override
    public void usage() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(getName() + " <bridge IP> <hue ID>",
                "A command to print a JSON dump of all available sensors.",
                getOptions(), null);
    }

    @Override
    public String getName() {
        return "listsensors";
    }
}
