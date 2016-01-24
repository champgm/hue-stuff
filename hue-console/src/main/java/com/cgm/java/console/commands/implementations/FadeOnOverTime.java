package com.cgm.java.console.commands.implementations;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgm.java.console.commands.BridgeCommand;
import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.models.State;
import com.cgm.java.hue.utilities.HueBridgeSetter;
import com.cgm.java.utilities.lambdas.Conversion;

/**
 * A command to turn on lights
 */
@Deprecated
public class FadeOnOverTime extends BridgeCommand {
    private static final String NAME_OPTION = "name";
    private static final String DURATION_IN_MINUTES_OPTION = "minutes";
    private static final Long ONE_SECOND = 1000L;
    private static final Logger LOGGER = LoggerFactory.getLogger(FadeOnOverTime.class);

    @Override
    public void usage() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(getName() + " <bridge IP> <hue ID> <light name>", "A command to turn on a specific light.", getOptions(), null);
    }

    @Override
    protected int run(final CommandLine line) throws RuntimeException, UnknownHostException {
        setIpAndId(line);
        final String[] lightNames = line.getOptionValues(NAME_OPTION);
        if (lightNames.length < 1) {
            usage();
            throw new IllegalArgumentException("You must specify at least one light to turn on.");
        }

        final String[] durations = line.getOptionValues(DURATION_IN_MINUTES_OPTION);
        if (durations == null || durations.length != 1) {
            usage();
            throw new IllegalArgumentException("You must specify one and only one duration option.");
        }
        final Integer durationInMinutes = Integer.valueOf(durations[0]);

        final List<Light> lights = HUE_BRIDGE_GETTER.getLights(bridgeIp, token);
        final Map<String, Light> nameToLightMap = lights.stream().collect(Collectors.toMap(Conversion.LIGHT_TO_NAME, (light) -> light));
        System.out.println("These lights were found: ");
        nameToLightMap.keySet().forEach(System.out::println);
        System.out.println();

        final List<String> lightsToTurnOn = Arrays.stream(lightNames).filter(nameToLightMap::containsKey).collect(Collectors.toList());
        System.out.println("These lights matched your input: ");
        lightsToTurnOn.forEach(System.out::println);

        final HueBridgeSetter hueBridgeSetter = new HueBridgeSetter();
        // Turn the lights off and set their brightness to 0
        lightsToTurnOn.forEach((lightName) -> {
            try {
                final Light light = nameToLightMap.get(lightName);

                final State.Builder newStateBuilder = State.newBuilder(light.getState()).setOn(false).setBri(0L);
                System.out.println("Turning '" + lightName + "' off.");
                hueBridgeSetter.setLightState(bridgeIp, token, light.getId().toString(), newStateBuilder.build());
                Thread.sleep(ONE_SECOND);
                hueBridgeSetter.setLightState(bridgeIp, token, light.getId().toString(), newStateBuilder.build());
                Thread.sleep(ONE_SECOND);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // We need to increment this many times before we reach 255 while counting from 0
        final int increment = 255 / durationInMinutes;
        long currentBrightness = 0;

        // Loop the outside once per minute
        for (int i = 0; i < durationInMinutes; i++) {
            try {
                // Loop the inside 6 times per minute
                for (int j = 0; j <= 5; j++) {
                    // It seems that any variable used in a lambda must be final. Interesting.
                    final long finalCurrentBrightness = currentBrightness;

                    // Update the light and increase the brightness a bit
                    lightsToTurnOn.forEach((lightName) -> {
                        final Light light = nameToLightMap.get(lightName);
                        System.out.println("Setting '" + lightName + "' to brightness: " + finalCurrentBrightness);
                        final State.Builder newStateBuilder = State.newBuilder(light.getState()).setOn(true).setBri(finalCurrentBrightness);
                        hueBridgeSetter.setLightState(bridgeIp, token, light.getId().toString(), newStateBuilder.build());
                    });

                    // Delay 10 seconds. If we do that 6 times it'll be about a minute. Close enough.
                    System.out.println("====Delaying 10 seconds====");
                    Thread.sleep(10000);
                }

                // Increase the brightness a bit. If we increase 255/durationInMinutes durationInMinutes times,
                // we should hit close to 255 barring some rounding errors.
                currentBrightness = currentBrightness + increment;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return 0;
    }

    @Override
    public String getName() {
        return "fadeon";
    }

    @Override
    public Options getOptions() {
        final Options options = super.getOptions();
        options.addOption(NAME_OPTION, true, "The name of the light to turn on.");
        options.addOption(DURATION_IN_MINUTES_OPTION, true, "The duration the fade should last.");
        return options;
    }
}
