package com.cgm.java.console.commands.implementations;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.cgm.java.console.commands.BridgeCommand;
import com.cgm.java.hue.models.Scene;
import com.cgm.java.utilities.lambdas.Conversion;
import com.google.common.base.Preconditions;

/**
 * A command which will attempt to use POST to create a new, Version 2, {@link com.cgm.java.hue.models.Scene} on the
 * bridge. It will print the raw response from the bridge. Please note, it is not possible to set the
 * {@link com.cgm.java.hue.models.Light} {@link com.cgm.java.hue.models.State}s when creating a
 * {@link com.cgm.java.hue.models.Scene} in this manner. The CURRENT states of the specified
 * {@link com.cgm.java.hue.models.Light}s will be retrieved, stored in this {@link com.cgm.java.hue.models.Scene}, and
 * restored later when the {@link com.cgm.java.hue.models.Scene} is toggled on.
 * 
 * NOTE: If you create a {@link com.cgm.java.hue.models.Scene} with undesirable {@link com.cgm.java.hue.models.Light}
 * {@link com.cgm.java.hue.models.State}s, you SHOULD be able to set the lights and then run this command again with the
 * same parameters. It SHOULD overwrite the previously created {@link com.cgm.java.hue.models.Scene}
 */
public class AddScene extends BridgeCommand {
    private static final String NAME_OPTION = "name";
    private static final String LIGHT_ID = "lightId";

    @Override
    protected int run(final CommandLine line) throws UnknownHostException {
        setIpAndId(line);
        final String sceneName = Preconditions.checkNotNull(line.getOptionValue(NAME_OPTION), "You must enter a Scene name.");
        final String[] lightIds = Preconditions.checkNotNull(line.getOptionValues(LIGHT_ID), "You must enter at least one Light ID");
        final List<CharSequence> charSequenceLightIds = Arrays.stream(lightIds).map(Conversion.STRING_TO_SEQUENCE).collect(Collectors.toList());

        System.out.println("This will be the name of the new Scene: " + sceneName);
        System.out.println("These will be the light IDs in the new Scene: " + charSequenceLightIds);

        final Scene newScene = Scene.newBuilder()
                .setName(sceneName)
                .setLights(charSequenceLightIds).build();

        final String newSceneId = HUE_BRIDGE_SETTER.postNewScene(bridgeIp, token, newScene);

        System.out.println("The ID for the Scene you've just created is: " + newSceneId);
        System.out.println();

        final Scene scene = HUE_BRIDGE_GETTER.getScene(bridgeIp, token, newSceneId);

        System.out.println("Here are the contents of your new scene: " + scene.toString());

        return 0;
    }

    @Override
    public void usage() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(getName() + " <bridge IP> <hue ID>",
                "A command to create a new scene.",
                getOptions(), null);
    }

    @Override
    public String getName() {
        return "addscene";
    }

    @Override
    public Options getOptions() {
        final Options options = super.getOptions();
        options.addOption(NAME_OPTION, true, "The name of the scene to be created.");
        options.addOption(LIGHT_ID, true, "The id of one light add to this scene.");
        return options;
    }
}
