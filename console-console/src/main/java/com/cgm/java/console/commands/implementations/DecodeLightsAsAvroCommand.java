package com.cgm.java.console.commands.implementations;

import java.net.UnknownHostException;
import java.util.Collection;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;

import com.cgm.java.console.commands.BridgeCommand;
import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.utilities.HueBridgeGetter;

/**
 * A debugging command to retrieve the full set of {@link com.cgm.java.hue.models.Light}s and print them to console
 */
public class DecodeLightsAsAvroCommand extends BridgeCommand {

    @Override
    protected int run(final CommandLine line) throws UnknownHostException {
        setIpAndId(line);

        //This command got a lot smaller with some API...
        final Collection<Light> lightsSet = HueBridgeGetter.getLights(bridgeIp, hueId);

        System.out.println("=============================================================");
        System.out.println("Here is the light output after being decoded into avro models");
        System.out.println("=============================================================");
        for (final Light light : lightsSet) {
            System.out.println(light);
            System.out.println();
        }

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
        return "decodelightsasavro";
    }
}
