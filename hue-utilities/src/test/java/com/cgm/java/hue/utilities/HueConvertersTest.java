package com.cgm.java.hue.utilities;

import java.util.stream.Stream;

import junit.framework.Assert;

import org.junit.Test;

import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.models.State;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

/**
 * Created by mc023219 on 12/10/15.
 */
public class HueConvertersTest {

    @Test
    public void testConversionOfJsonToLight() throws Exception {
        final String geJson =
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
        final Stream<String> geStream = Stream.of(geJson);

        final ImmutableSet.Builder<Light> builder = ImmutableSet.builder();
        geStream.map(HueConverters.JSON_TO_LIGHT).forEach(builder::add);
        final ImmutableSet<Light> geLights = builder.build();

        Assert.assertEquals(1, geLights.size());

    }

    @Test
    public void testConversionOfJsonToState() throws Exception {
        final String geJson =
                " {\"on\":false," +
                        "\"bri\":254," + "" +
                        "\"alert\":\"none\"," + "" +
                        "\"reachable\":true}";
        final Stream<String> geStream = Stream.of(geJson);

        final ImmutableSet.Builder<State> builder = ImmutableSet.builder();
        geStream.map(HueConverters.JSON_TO_STATE).forEach(builder::add);
        final ImmutableSet<State> geLights = builder.build();

        final State state = Iterables.getOnlyElement(geLights);
       Assert.assertEquals("false",state.getOn());
       Assert.assertEquals("254",state.getBri());
       Assert.assertEquals("none",state.getAlert());
       Assert.assertEquals("true",state.getReachable());

        //
        //
        //
        //
    }

}
