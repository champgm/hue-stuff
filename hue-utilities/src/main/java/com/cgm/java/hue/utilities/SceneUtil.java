package com.cgm.java.hue.utilities;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.models.Scene;
import com.cgm.java.hue.models.State;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

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
        final ImmutableMap.Builder<String, State> idToLightStateMapBuilder = ImmutableMap.builder();
        lights.forEach(light -> idToLightStateMapBuilder.put(light.getId().toString(), light.getState()));
        final ImmutableMap<String, State> idToLightStateMap = idToLightStateMapBuilder.build();

        // Iterate through the scenes
        final ImmutableList.Builder<String> activeSceneIdsBuilder = ImmutableList.builder();
        for (final Scene scene : scenes) {
            final List<State> lightStates = scene.getLightstates();
            final List<CharSequence> sceneLightIds = scene.getLights();
            boolean allLightStatesMatch = true;

            //Iterate through all states for the current scene and see if they match the current light states
            if (lightStates!=null && lightStates.size() == sceneLightIds.size()) {
                for (int j = 0; j < lightStates.size(); j++) {
                    final State sceneState = lightStates.get(j);
                    final String sceneLightId = sceneLightIds.get(j).toString();
                    final State lightState = idToLightStateMap.get(sceneLightId);

                    //Abort if any do not match
                    if (lightState != null && lightState != sceneState) {
                        allLightStatesMatch = false;
                        break;
                    }
                }
            } else {
                LOGGER.error("Found a scene with unequal number of lights and light states: "
                             + scene.getId() + " : " + scene.getName());
            }
            if (allLightStatesMatch) {
                activeSceneIdsBuilder.add(scene.getId().toString());
            }
        }
        return activeSceneIdsBuilder.build();
    }
}
