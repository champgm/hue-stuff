package com.cgm.java.hue.utilities;

import java.util.stream.Stream;

import junit.framework.Assert;

import org.junit.Test;

import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.models.Scene;
import com.cgm.java.hue.models.State;
import com.google.common.collect.ImmutableList;

/**
 * Created by mc023219 on 12/10/15.
 */
public class HueConvertersTest {

    @Test
    public void testConversionOfSingleJsonToScene() throws Exception {
        final String sceneJson = "{\"name\":\"BR - on off 1448596157000\"," +
                "\"lights\":[\"4\",\"5\"]," +
                "\"owner\":\"0000000077f6ae5e16f8583016f85830\"," +
                "\"recycle\":true," +
                "\"locked\":false," +
                "\"appdata\":[]," +
                "\"picture\":\"\"," +
                "\"lastupdated\":\"2016-01-12T04:36:52\"," +
                "\"version\":1}";

        final Scene sceneAvro = HueConverters.JSON_TO_SCENE.apply("ID", sceneJson);

        final ImmutableList<String> expectedLights = ImmutableList.of("4", "5");
        Assert.assertEquals("ID", sceneAvro.getId());
        Assert.assertEquals(expectedLights, sceneAvro.getLights());
    }

    @Test
    public void testConversionOfSingleJsonToLight() throws Exception {
        final String lightJson =
                "{\"state\": {\"on\":false," +
                        "\"bri\":254," + "" +
                        "\"alert\":\"none\"," + "" +
                        "\"reachable\":true}, " + "" +
                        "\"type\": \"Dimmable light\", " + "" +
                        "\"name\": \"Na's lamp\", " + "" +
                        "\"modelid\": \"ZLL Light\", " + "" +
                        "\"manufacturername\": \"GE_Appliances\"," + "" +
                        "\"uniqueid\":\"7c:e5:24:00:00:0b:19:87-01\", " + "" +
                        "\"swversion\": \"1197632\", " + "" +
                        "\"pointsymbol\": { \"1\":\"none\", \"2\":\"none\", \"3\":\"none\", \"4\":\"none\", \"5\":\"none\", \"6\":\"none\", \"7\":\"none\", \"8\":\"none\" }}";

        final Light lightAvro = HueConverters.JSON_TO_LIGHT.apply(1, lightJson);
        Assert.assertEquals("1", lightAvro.getId());
        Assert.assertEquals("Na's lamp", lightAvro.getName());
    }

    @Test
    public void testConversionOfJsonToState() throws Exception {
        final String stateJson =
                " {\"on\":false," +
                        "\"bri\":254," + "" +
                        "\"alert\":\"none\"," + "" +
                        "\"reachable\":true}";
        final Stream<String> geStream = Stream.of(stateJson);

        final State stateAvro = HueConverters.JSON_TO_STATE.apply(stateJson);
        Assert.assertEquals(Boolean.FALSE, stateAvro.getOn());
        Assert.assertEquals(Long.valueOf(254), stateAvro.getBri());
        Assert.assertEquals("none", stateAvro.getAlert());
        Assert.assertEquals(Boolean.TRUE, stateAvro.getReachable());
    }

}
