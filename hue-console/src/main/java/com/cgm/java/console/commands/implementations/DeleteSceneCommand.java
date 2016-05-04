package com.cgm.java.console.commands.implementations;

import java.net.UnknownHostException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.cgm.java.console.commands.BridgeCommand;

/**
 * A command which will attempt to delete {@link com.cgm.java.hue.models.Scene}s with the input IDs.
 */
public class DeleteSceneCommand extends BridgeCommand {
    private static final String ID_OPTION = "sceneId";

    @Override
    protected int run(final CommandLine line) throws UnknownHostException {
        setIpAndId(line);
        final String[] sceneIds = line.getOptionValues(ID_OPTION);
        if (sceneIds == null || sceneIds.length < 1) {
            usage();
            throw new IllegalArgumentException("You must enter a Scene ID.");
        }

        for (final String sceneId : sceneIds) {
            System.out.println("Deleting scene: " + sceneId);
            System.out.println(HUE_BRIDGE_SETTER.deleteScene(bridgeIp, token, sceneId));
        }

        System.out.println();
        System.out.println("Remaining scenes: ");
        HUE_BRIDGE_GETTER.getScenes(bridgeIp, token, false).forEach(System.out::println);

        return 0;
    }

    @Override
    public void usage() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(getName() + " <bridge IP> <hue ID>",
                "A command to delete a scene.",
                getOptions(), null);
    }

    @Override
    public String getName() {
        return "deletescene";
    }

    @Override
    public Options getOptions() {
        final Options options = super.getOptions();
        options.addOption(ID_OPTION, true, "The ID of the scene to be deleted.");
        return options;
    }
}
