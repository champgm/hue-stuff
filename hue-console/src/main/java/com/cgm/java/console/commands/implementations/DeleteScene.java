package com.cgm.java.console.commands.implementations;

import java.net.UnknownHostException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.cgm.java.console.commands.BridgeCommand;
import com.google.common.base.Preconditions;

/**
 * A command which will attempt to use POST to create a new, Version 2, {@link com.cgm.java.hue.models.Scene} on the
 * bridge. It will print the raw response from the bridge. Please note, it is not possible to set the {@link
 * com.cgm.java.hue.models.Light} {@link com.cgm.java.hue.models.State}s when creating a {@link
 * com.cgm.java.hue.models.Scene} in this manner. The CURRENT states of the specified {@link
 * com.cgm.java.hue.models.Light}s will be retrieved, stored in this {@link com.cgm.java.hue.models.Scene}, and restored
 * later when the {@link com.cgm.java.hue.models.Scene} is toggled on.
 * <p/>
 * NOTE: If you create a {@link com.cgm.java.hue.models.Scene} with undesirable {@link com.cgm.java.hue.models.Light}
 * {@link com.cgm.java.hue.models.State}s, you SHOULD be able to set the lights and then run this command again with the
 * same parameters. It SHOULD overwrite the previously created {@link com.cgm.java.hue.models.Scene}
 */
public class DeleteScene extends BridgeCommand {
    private static final String ID_OPTION = "sceneId";

    @Override
    protected int run(final CommandLine line) throws UnknownHostException {
        setIpAndId(line);
        final String[] sceneIds = Preconditions.checkNotNull(line.getOptionValues(ID_OPTION), "You must enter a Scene ID.");
        Preconditions.checkArgument(sceneIds.length > 0, "You must enter a Scene ID.");

        for (final String sceneId : sceneIds) {
            System.out.println("Deleting scene: " + sceneId);
            System.out.println(HUE_BRIDGE_SETTER.deleteScene(bridgeIp, token, sceneId));
        }

        System.out.println("Remaining scenes: ");
        HUE_BRIDGE_GETTER.getScenes(bridgeIp, token).forEach(System.out::println);

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
