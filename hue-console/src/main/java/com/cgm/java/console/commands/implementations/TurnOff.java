package com.cgm.java.console.commands.implementations;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.cgm.java.console.commands.BridgeCommand;
import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.models.State;
import com.cgm.java.hue.utilities.HueBridgePutter;
import com.cgm.java.utilities.lambdas.Conversion;

/**
 * A command to turn off lights.
 */
public class TurnOff extends BridgeCommand {
    private static final String NAME_OPTION = "name";

    @Override
    public void usage() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(getName() + " <bridge IP> <hue ID> <light name>",
                "A command to turn off a specific light.",
                getOptions(), null);
    }

    @Override
    protected int run(final CommandLine line) throws RuntimeException, UnknownHostException {
        setIpAndId(line);
        final String[] lightNames = line.getOptionValues(NAME_OPTION);
        if (lightNames.length < 1) {
            usage();
            throw new IllegalArgumentException("You must specify at least one light to turn on.");
        }

        final List<Light> lights = HUE_BRIDGE_GETTER.getLights(bridgeIp, hueId);
        final Map<String, Light> nameToLightMap = lights.stream().collect(Collectors.toMap(Conversion.LIGHT_TO_NAME, (light) -> light));
        System.out.println("These lights were found: ");
        nameToLightMap.keySet().forEach(System.out::println);
        System.out.println();

        final HueBridgePutter hueBridgePutter = new HueBridgePutter();
        final List<String> lightsToTurnOff = Arrays.stream(lightNames).filter(nameToLightMap::containsKey).collect(Collectors.toList());
        System.out.println("These lights matched your input: ");
        lightsToTurnOff.forEach((lightName) -> {
            System.out.println(lightName);
            final Light light = nameToLightMap.get(lightName);

            final State.Builder newStateBuilder = State.newBuilder(light.getState()).setOn(false);
            hueBridgePutter.setLightState(bridgeIp, hueId, lights.indexOf(light), newStateBuilder.build());
        });

        return 0;
    }

    @Override
    public String getName() {
        return "turnoff";
    }

    @Override
    public Options getOptions() {
        final Options options = super.getOptions();
        options.addOption(NAME_OPTION, true,
                "The name of the light to turn on.");
        return options;
    }
}
