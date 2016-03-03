package com.cgm.java.console.commands.implementations;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.cgm.java.console.commands.BridgeCommand;
import com.cgm.java.hue.models.Scene;

/**
 * A command which will attempt to retrieve a scene and then use POST to re-create a specified {@link
 * com.cgm.java.hue.models.Scene}. This is needed because it seems that {@link com.cgm.java.hue.models.Scene}s created
 * with the "Phillips Hue" app on Android always end up as "Version 1", which means they are missing some key attributes
 * like individual {@link com.cgm.java.hue.models.Light} {@link com.cgm.java.hue.models.State}s.
 */
public class UpgradeSceneVersionCommand extends BridgeCommand {
    private static final String ID_OPTION = "id";

    @Override
    protected int run(final CommandLine line) throws UnknownHostException {
        setIpAndId(line);
        final List<String> sceneIds = Arrays.asList(line.getOptionValues(ID_OPTION));

        System.out.println("Here is all available information on currently available scenes: ");
        final List<Scene> currentScenes = HUE_BRIDGE_GETTER.getScenes(bridgeIp, token, false);
        currentScenes.forEach(System.out::println);
        System.out.println();

        System.out.println("These scenes will be updated: ");
        currentScenes.stream().filter(currentScene -> sceneIds.contains(currentScene.getId().toString()))
                .forEach(currentScene -> {
                    System.out.println(currentScene.getId() + ": " + currentScene.getName());
                    // Don't do this yet... the app can't see non-V1 scenes.
                    // HUE_BRIDGE_SETTER.deleteScene(bridgeIp, token, currentScene.getId().toString());
                    HUE_BRIDGE_SETTER.postNewScene(bridgeIp, token, currentScene);
                });

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
    public Options getOptions() {
        final Options options = super.getOptions();
        options.addOption(ID_OPTION, true, "The ID of the scene to be updated.");
        return options;
    }

    @Override
    public String getName() {
        return "upgradescene";
    }
}
