package com.cgm.java.hue.utilities;

import org.junit.Assert;
import org.junit.Test;

import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.models.Scene;
import com.cgm.java.hue.models.State;
import com.google.common.collect.ImmutableList;

public class SceneUtilTest {

    @Test
    public void testDetermineActiveScene() {
        final String lightId1 = "1";
        final State lightState1a = State.newBuilder().setAlert("alert1a").setOn(true).setEffect("effect1a").build();
        final State lightState1b = State.newBuilder().setAlert("alert1b").setOn(true).setEffect("effect1b").build();
        final Light light1 = Light.newBuilder().setId(lightId1).setState(lightState1a).setName("light1").build();

        final String lightId2 = "2";
        final State lightState2a = State.newBuilder().setAlert("alert2a").setOn(true).setEffect("effect2a").build();
        final State lightState2b = State.newBuilder().setAlert("alert2b").setOn(true).setEffect("effect2b").build();
        final Light light2 = Light.newBuilder().setId(lightId2).setState(lightState2a).setName("light2").build();

        final String lightId3 = "3";
        final State lightState3a = State.newBuilder().setAlert("alert3a").setOn(true).setEffect("effect3a").build();
        final State lightState3b = State.newBuilder().setAlert("alert3b").setOn(true).setEffect("effect3b").build();
        final Light light3 = Light.newBuilder().setId(lightId3).setState(lightState3a).setName("light3").build();

        final String allCorrectId = "allCorrect";
        final Scene allCorrect = Scene.newBuilder()
                .setId(allCorrectId)
                .setLights(ImmutableList.of(lightId1, lightId2, lightId3))
                .setLightstates(ImmutableList.of(lightState1a, lightState2a, lightState3a))
                .build();

        final String allWrongId = "allWrong";
        final Scene allWrong = Scene.newBuilder()
                .setId(allWrongId)
                .setLights(ImmutableList.of(lightId1, lightId2, lightId3))
                .setLightstates(ImmutableList.of(lightState1b, lightState2b, lightState3b))
                .build();

        final String someId = "some";
        final Scene some = Scene.newBuilder()
                .setId(someId)
                .setLights(ImmutableList.of(lightId1, lightId2, lightId3))
                .setLightstates(ImmutableList.of(lightState1b, lightState2a, lightState3a))
                .build();

        final String subsetCorrectId = "subsetCorrect";
        final Scene subsetCorrect = Scene.newBuilder()
                .setId(subsetCorrectId)
                .setLights(ImmutableList.of(lightId1))
                .setLightstates(ImmutableList.of(lightState1a))
                .build();

        final String subsetWrongId = "subsetWrong";
        final Scene subsetWrong = Scene.newBuilder()
                .setId(subsetWrongId)
                .setLights(ImmutableList.of(lightId2))
                .setLightstates(ImmutableList.of(lightState2b))
                .build();

        final String subsetSomeId = "subsetSome";
        final Scene subsetSome = Scene.newBuilder()
                .setId(subsetSomeId)
                .setLights(ImmutableList.of(lightId2, lightId3))
                .setLightstates(ImmutableList.of(lightState2a, lightState3b))
                .build();

        final ImmutableList<String> activeScenes = SceneUtil.determineActiveScenes(
                ImmutableList.of(allCorrect, allWrong, some, subsetCorrect, subsetWrong, subsetSome),
                ImmutableList.of(light1, light2, light3));

        Assert.assertTrue(activeScenes.contains(allCorrectId));
        Assert.assertTrue(activeScenes.contains(subsetCorrectId));
        Assert.assertTrue(!activeScenes.contains(allWrongId));
        Assert.assertTrue(!activeScenes.contains(someId));
        Assert.assertTrue(!activeScenes.contains(subsetSomeId));
        Assert.assertTrue(!activeScenes.contains(subsetWrongId));
    }
}
