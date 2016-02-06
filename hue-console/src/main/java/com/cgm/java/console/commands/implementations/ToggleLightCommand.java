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
import com.cgm.java.hue.utilities.LightUtil;
import com.cgm.java.utilities.lambdas.Conversion;

/**
 * A command to turn on lights
 */
public class ToggleLightCommand extends BridgeCommand {
    private static final String NAME_OPTION = "name";
    private static final String ID_OPTION = "id";

    @Override
    public void usage() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(getName() + " <bridge IP> <hue ID> <light name>", "A command to turn on a specific light.", getOptions(), null);
    }

    @Override
    protected int run(final CommandLine line) throws RuntimeException, UnknownHostException {
        setIpAndId(line);
        final String[] lightNames = line.getOptionValues(NAME_OPTION) == null ? new String[]{} : line.getOptionValues(NAME_OPTION);
        final String[] lightIds = line.getOptionValues(ID_OPTION) == null ? new String[]{} : line.getOptionValues(ID_OPTION);
        if (lightNames.length < 1 && lightIds.length < 1) {
            usage();
            throw new IllegalArgumentException("You must specify at least one light to turn on.");
        }

        // Collect all lights and keep them in a map
        final List<Light> lights = HUE_BRIDGE_GETTER.getLights(bridgeIp, token);
        final Map<String, Light> nameToLightMap = lights.stream().collect(Collectors.toMap(Conversion.LIGHT_TO_NAME, (light) -> light));
        final Map<String, Light> idToLightMap = lights.stream().collect(Collectors.toMap(Conversion.LIGHT_TO_ID, (light) -> light));
        System.out.println("These lights were found: ");
        nameToLightMap.keySet().forEach(System.out::println);
        System.out.println();

        // Figure out which requested lights actually match the available lights, and turn them on
        final List<String> lightNamesToggle = Arrays.stream(lightNames).filter(nameToLightMap::containsKey).collect(Collectors.toList());
        final List<String> lightIdsToToggle = Arrays.stream(lightIds).filter(idToLightMap::containsKey).collect(Collectors.toList());
        final List<String> union = ListUtils.union(lightNamesToggle, lightIdsToToggle);

        final LightUtil lightUtil = new LightUtil();
        System.out.println("These lights matched your input: ");
        union.forEach((lightIdentifier) -> {
            System.out.println(lightIdentifier);
            Light light = nameToLightMap.get(lightIdentifier);
            if(light==null){
                light = idToLightMap.get(lightIdentifier);
            }

            lightUtil.toggleLight(bridgeIp,token,light.getId().toString());
        });

        return 0;
    }

    @Override
    public String getName() {
        return "togglelight";
    }

    @Override
    public Options getOptions() {
        final Options options = super.getOptions();
        options.addOption(NAME_OPTION, true, "The name of the light to toggle.");
        options.addOption(ID_OPTION, true, "The name of the light to toggle.");
        return options;
    }
}
