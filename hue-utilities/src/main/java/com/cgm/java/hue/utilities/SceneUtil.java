package com.cgm.java.hue.utilities;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.models.Scene;
import com.cgm.java.hue.models.State;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * Utility for interacting with {@link com.cgm.java.hue.models.Scene}s
 */
public class SceneUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(SceneUtil.class);

    /**
     * Given a list of scenes and lights, this method will compare the current light states to the expected states in
     * the scenes and figure out which scenes currently have all of their lights in the correct states.
     *
     * @param scenes
     *         the scenes to check for active state
     * @param lights
     *         the current state of the lights
     * @return the IDs for each {@link com.cgm.java.hue.models.Scene} that is currently active.
     */
    public static ImmutableList<String> determineActiveScenes(final List<Scene> scenes, final List<Light> lights) {
        Preconditions.checkArgument(scenes != null, "scenes cannot be null.");
        Preconditions.checkArgument(lights != null, "lights cannot be null.");
        if (scenes.isEmpty() || lights.isEmpty()) {
            return ImmutableList.of();
        }

        // Map each light state to its light ID.
        final Map<String, State> currentLightStates = lights.parallelStream()
                .collect(Collectors.toMap(light -> light.getId().toString(), Light::getState));

        // Iterate through the scenes
        final List<String> activeScenes = scenes.parallelStream().filter(scene -> {
            final List<State> possibleLightStates = scene.getLightstates();
            final List<CharSequence> sceneLightIds = scene.getLights();
            boolean allLightStatesMatch = true;

            //Iterate through all states for the current scene and see if they match the current light states
            if (possibleLightStates != null && possibleLightStates.size() == sceneLightIds.size()) {
                final boolean foundMismatchedSceneStates = IntStream.range(0, possibleLightStates.size()).parallel()
                        .anyMatch(index -> {
                            final State sceneState = possibleLightStates.get(index);
                            final String sceneLightId = sceneLightIds.get(index).toString();
                            final State currentLightState = currentLightStates.get(sceneLightId);
                            return currentLightState != null && currentLightState != sceneState;
                        });
                allLightStatesMatch = !foundMismatchedSceneStates;
            } else {
                LOGGER.error("Found a scene with unequal number of lights and light states: "
                        + scene.getId() + " : " + scene.getName());
            }
            return allLightStatesMatch;
        }).map(scene -> scene.getId().toString()).collect(Collectors.toList());

        return ImmutableList.copyOf(activeScenes);
    }
}
