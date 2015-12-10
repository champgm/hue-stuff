package com.cgm.java.console.commands.implementations;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;

import com.cgm.java.console.commands.Command;
import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.utilities.HueBridgeCaller;
import com.cgm.java.hue.utilities.HueCommands;
import com.cgm.java.hue.utilities.HueConverters;
import com.google.common.collect.ImmutableSet;

/**
 * Attempts to put the JSOn
 */
public class DecodeLightsAsAvroCommand extends Command {

    @Override
    protected int run(final CommandLine line) throws UnknownHostException {

        final String[] args = line.getArgs();
        if (args.length != 2) {
            System.out.println("You must specify both a bridge IP and a hue ID.");
        }

        final InetAddress ipAddress = InetAddress.getByName(args[0]);

        final String fullLightsList = HueBridgeCaller.rawGet(ipAddress.getHostAddress(), args[1], HueCommands.LIGHTS);
        System.out.println("Here is the string representation of the list of lights:");
        System.out.println(fullLightsList);
        System.out.println();

        // ,"2":
        final String regex = ",\"\\d*\":";
        System.out.println("==================================================");
        System.out.println("Here is the light output split by, <" + regex + ">");
        System.out.println("==================================================");
        final String[] splitLights = fullLightsList.split(regex);
        for (final String light : splitLights) {
            System.out.println(light);
            System.out.println();
        }

        final ImmutableSet.Builder<Light> builder = ImmutableSet.builder();
        Arrays.stream(splitLights).map(HueConverters.JSON_TO_LIGHT).forEach(builder::add);
        final ImmutableSet<Light> lightsSet = builder.build();

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
