package com.cgm.java.console.commands.implementations;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.collections.ListUtils;

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
    private static final String ID_OPTION = "id";

    @Override
    public void usage() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(getName() + " <bridge IP> <hue ID>", "A command to turn off a specific light.", getOptions(), null);
    }

    @Override
    protected int run(final CommandLine line) throws RuntimeException, UnknownHostException {
        setIpAndId(line);
        final String[] lightNames = line.hasOption(NAME_OPTION) ? line.getOptionValues(NAME_OPTION) : new String[] {};
        final String[] lightIds = line.hasOption(ID_OPTION) ? line.getOptionValues(ID_OPTION) : new String[] {};
        if ((lightNames == null || lightNames.length < 1) && (lightIds == null || lightIds.length < 1)) {
            usage();
            throw new IllegalArgumentException("You must specify at least one light to turn on.");
        }

        // Collect all lights and keep them in a map
        final List<Light> lights = HUE_BRIDGE_GETTER.getLights(bridgeIp, hueId);
        final Map<String, Light> nameToLightMap = lights.stream().collect(Collectors.toMap(Conversion.LIGHT_TO_NAME, (light) -> light));
        final Map<String, Light> idToLightMap = lights.stream().collect(Collectors.toMap(Conversion.LIGHT_TO_ID, (light) -> light));
        System.out.println("These lights were found: ");
        lights.forEach(System.out::println);
        System.out.println();

        // Figure out which requested lights actually match the available lights, and turn them on
        final HueBridgePutter hueBridgePutter = new HueBridgePutter();
        final List<String> lightNamesToTurnOn = Arrays.stream(lightNames).filter(idToLightMap::containsKey).collect(Collectors.toList());
        final List<String> lightIdsToTurnOn = Arrays.stream(lightIds).filter(idToLightMap::containsKey).collect(Collectors.toList());
        final List<String> union = ListUtils.union(lightNamesToTurnOn, lightIdsToTurnOn);
        System.out.println("These lights matched your input: ");
        union.forEach((lightIdentifier) -> {
            System.out.println(lightIdentifier);
            Light light = idToLightMap.get(lightIdentifier);
            if (light == null) {
                light = nameToLightMap.get(lightIdentifier);
            }

            final State.Builder newStateBuilder = createNewState(light);

            hueBridgePutter.setLightState(bridgeIp, hueId, light.getId().toString(), newStateBuilder.build());
        });

        return 0;
    }

    private State.Builder createNewState(final Light light) {
        return State.newBuilder(light.getState()).setOn(false);
    }

    @Override
    public String getName() {
        return "turnoff";
    }

    @Override
    public Options getOptions() {
        final Options options = super.getOptions();
        options.addOption(NAME_OPTION, true, "The name of the light to turn on.");
        options.addOption(ID_OPTION, true, "The id of the light to turn on.");
        return options;
    }
}
