package com.cgm.java.hue.utilities;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.cgm.java.hue.models.Group;
import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.models.Scene;
import com.google.common.collect.ImmutableList;

public class HueBridgeGetterTest {
    private static final HueConfiguration HUE_CONFIGURATION = new HueConfiguration();

    @Test
    public void testProcessOfFullLightsDump() {
        final String fullLightDump = "{" +
                "\"1\":{\"state\": {\"on\":false,\"bri\":253,\"hue\":16048,\"sat\":72,\"effect\":\"none\",\"xy\":[0.4166,0.3961],\"ct\":302,\"alert\":\"none\",\"colormode\":\"ct\",\"reachable\":true}, \"type\": \"Extended color light\", \"name\": \"ColorLamp1\", \"modelid\": \"LCT007\", \"manufacturername\": \"Philips\",\"uniqueid\":\"00:17:88:01:10:5a:d0:e3-0b\", \"swversion\": \"66014919\"}," +
                "\"2\":{\"state\": {\"on\":false,\"bri\":253,\"hue\":34171,\"sat\":153,\"effect\":\"none\",\"xy\":[0.3395,0.3471],\"ct\":192,\"alert\":\"none\",\"colormode\":\"ct\",\"reachable\":true}, \"type\": \"Extended color light\", \"name\": \"ColorLamp2\", \"modelid\": \"LCT007\", \"manufacturername\": \"Philips\",\"uniqueid\":\"00:17:88:01:10:41:02:2d-0b\", \"swversion\": \"66014919\"}," +
                "\"3\":{\"state\": {\"on\":false,\"bri\":254,\"hue\":33946,\"sat\":5,\"effect\":\"none\",\"xy\":[0.3791,0.3759],\"ct\":248,\"alert\":\"none\",\"colormode\":\"ct\",\"reachable\":true}, \"type\": \"Extended color light\", \"name\": \"ColorLamp3\", \"modelid\": \"LCT007\", \"manufacturername\": \"Philips\",\"uniqueid\":\"00:17:88:01:10:5a:35:18-0b\", \"swversion\": \"66014919\"}," +
                "\"4\":{\"state\": {\"on\":false,\"bri\":1,\"alert\":\"none\",\"reachable\":true}, \"type\": \"Dimmable light\", \"name\": \"NaLamp\", \"modelid\": \"ZLL Light\", \"manufacturername\": \"GE_Appliances\",\"uniqueid\":\"7c:e5:24:00:00:0b:19:87-01\", \"swversion\": \"1197632\"}," +
                "\"5\":{\"state\": {\"on\":false,\"bri\":1,\"alert\":\"none\",\"reachable\":true}, \"type\": \"Dimmable light\", \"name\": \"MacLamp\", \"modelid\": \"ZLL Light\", \"manufacturername\": \"GE_Appliances\",\"uniqueid\":\"7c:e5:24:00:00:0a:eb:2e-01\", \"swversion\": \"1197632\"}," +
                "\"6\":{\"state\": {\"on\":false,\"bri\":3,\"alert\":\"none\",\"reachable\":true}, \"type\": \"Dimmable light\", \"name\": \"DenLamp1\", \"modelid\": \"ZLL Light\", \"manufacturername\": \"GE_Appliances\",\"uniqueid\":\"7c:e5:24:00:00:0b:2e:7d-01\", \"swversion\": \"1197632\"}," +
                "\"7\":{\"state\": {\"on\":false,\"bri\":3,\"alert\":\"none\",\"reachable\":true}, \"type\": \"Dimmable light\", \"name\": \"DenLamp2\", \"modelid\": \"ZLL Light\", \"manufacturername\": \"GE_Appliances\",\"uniqueid\":\"7c:e5:24:00:00:0a:de:f6-01\", \"swversion\": \"1197632\"}}";

        final HueBridgeGetter spiedBridgeGetter = Mockito.spy(new HueBridgeGetter());
        final String expectedUri = spiedBridgeGetter.buildUri(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), "lights");
        Mockito.doReturn(fullLightDump).when(spiedBridgeGetter).getURI(expectedUri);

        final List<Light> lights = spiedBridgeGetter.getLights(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken());
        Assert.assertEquals(7, lights.size());
        final List<CharSequence> lightIds = lights.stream().map(Light::getId).map(String::valueOf).collect(Collectors.toList());
        Assert.assertTrue("The light IDs are base 0, so there should not be a light with ID 0", !lightIds.contains("0"));
        Assert.assertTrue(!lightIds.contains("8"));
        Assert.assertTrue(lightIds.contains("1"));
        Assert.assertTrue(lightIds.contains("3"));
        Assert.assertTrue(lightIds.contains("7"));
    }

    @Test
    public void testProcessOfFullScenesDump() {
        final String fullSceneDump = "{" +
                "\"738e3c2af-off-0\":{\"name\":\"BR - on off 1448596157000\",\"lights\":[\"4\",\"5\"],\"owner\":\"0000000077f6ae5e16f8583016f85830\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2016-01-12T04:36:52\",\"version\":1}," +
                "\"808140eef-on-0\":{\"name\":\"BR - on on 1448596157000\",\"lights\":[\"4\",\"5\"],\"owner\":\"0000000077f6ae5e16f8583016f85830\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2016-01-12T04:36:53\",\"version\":1}," +
                "\"087f88f52-on-0\":{\"name\":\"Sunset on 1448660085758\",\"lights\":[\"1\",\"2\",\"3\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"045fa99ae-on-0\":{\"name\":\"BR - off on 1448596158000\",\"lights\":[\"4\",\"5\"],\"owner\":\"0000000077f6ae5e16f8583016f85830\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2016-01-12T17:53:22\",\"version\":1}," +
                "\"ac637e2f0-on-0\":{\"name\":\"Relax on 1448660096855\",\"lights\":[\"1\",\"2\",\"3\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"4b964fde2-on-0\":{\"name\":\"White and Blue on 1452599617000\",\"lights\":[\"1\",\"2\",\"3\",\"6\",\"7\"],\"owner\":\"0000000077f6ae5e16f8583016f85830\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2016-01-12T17:53:55\",\"version\":1}," +
                "\"0a854c238-on-0\":{\"name\":\"White and Blue on 1452599617000\",\"lights\":[\"1\",\"2\",\"3\",\"6\",\"7\"],\"owner\":\"0000000077f6ae5e16f8583016f85830\",\"recycle\":true,\"locked\":true,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2016-01-12T17:55:14\",\"version\":1}," +
                "\"84fa88aa5-on-0\":{\"name\":\"Energize on 1448660214580\",\"lights\":[\"1\",\"2\",\"3\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"f5d73dbdd-on-0\":{\"name\":\"Concentrate on 1448595772000\",\"lights\":[\"1\",\"2\",\"3\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"ab00a355f-on-0\":{\"name\":\"Concentrate on 1448595772000\",\"lights\":[\"1\",\"2\",\"3\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"a1167aa91-on-0\":{\"name\":\"Concentrate on 1448596220000\",\"lights\":[\"1\",\"2\",\"3\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"00a658fd5-on-0\":{\"name\":\"Sunset on 1448596285000\",\"lights\":[\"1\",\"2\",\"3\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"085ed3167-off-0\":{\"name\":\"Sunset off 1448596285000\",\"lights\":[\"1\",\"2\",\"3\",\"6\",\"7\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"869e0d87c-on-0\":{\"name\":\"Sunset on 1448596285000\",\"lights\":[\"1\",\"2\",\"3\",\"6\",\"7\"],\"owner\":\"0000000077f6ae5e16f8583016f85830\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2015-12-17T19:09:48\",\"version\":1}," +
                "\"ef24cbac2-off-0\":{\"name\":\"Den - med off 1448661422110\",\"lights\":[\"6\",\"7\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"5e5e86da3-on-0\":{\"name\":\"Den - med on 1448661422110\",\"lights\":[\"6\",\"7\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"1cd5ac5ce-on-0\":{\"name\":\"Den - off on 1448661502040\",\"lights\":[\"6\",\"7\"],\"owner\":\"0000000077f6ae5e16f8583016f85830\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2015-12-23T12:37:13\",\"version\":1}," +
                "\"b79ee5717-on-0\":{\"name\":\"Den - full on 1448661564038\",\"lights\":[\"6\",\"7\"],\"owner\":\"0000000077f6ae5e16f8583016f85830\",\"recycle\":true,\"locked\":true,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2015-12-30T23:59:20\",\"version\":1}," +
                "\"0b10111d9-on-0\":{\"name\":\"Den - med on 1448596746000\",\"lights\":[\"6\",\"7\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"497b50d84-on-0\":{\"name\":\"Sunset on 0\",\"lights\":[\"1\",\"2\",\"3\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"acac20a4d-on-0\":{\"name\":\"Energize on 0\",\"lights\":[\"1\",\"2\",\"3\"],\"owner\":\"0000000077f6ae5e16f8583016f85830\",\"recycle\":true,\"locked\":true,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2015-12-30T23:59:21\",\"version\":1}," +
                "\"cd4f6fab3-on-0\":{\"name\":\"Sunset on 0\",\"lights\":[\"1\",\"2\",\"3\",\"6\",\"7\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"HuePro87358dbf\":{\"name\":\"Bedroom On from Hue Pro\",\"lights\":[\"4\",\"5\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"0b730f207-on-0\":{\"name\":\"Den - full on 0\",\"lights\":[\"6\",\"7\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"d350db76f-on-0\":{\"name\":\"BR - on on 0\",\"lights\":[\"4\",\"5\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"85385533d-on-0\":{\"name\":\"Full den off on 1448672842733\",\"lights\":[\"1\",\"2\",\"3\",\"6\",\"7\"],\"owner\":\"0000000077f6ae5e16f8583016f85830\",\"recycle\":true,\"locked\":true,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2015-12-30T23:59:22\",\"version\":1}," +
                "\"ffd8d7226-on-0\":{\"name\":\"Relax on 1448596286000\",\"lights\":[\"1\",\"2\",\"3\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"322142860-on-0\":{\"name\":\"Den - med on 0\",\"lights\":[\"6\",\"7\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"808140eef-on-30\":{\"name\":\"BR - on fon 1448596157000\",\"lights\":[\"4\",\"5\"],\"owner\":\"none\",\"recycle\":true,\"locked\":true,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"869e0d87c-on-30\":{\"name\":\"Sunset fon 1448596523000\",\"lights\":[\"1\",\"2\",\"3\",\"6\",\"7\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"28d83d8d1-on-0\":{\"name\":\"Sunset on 1448596523000\",\"lights\":[\"1\",\"2\",\"3\",\"6\",\"7\"],\"owner\":\"0000000077f6ae5e16f8583016f85830\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2015-12-17T19:10:21\",\"version\":1}," +
                "\"28098c28a-on-0\":{\"name\":\"Sunset on 1450520040000\",\"lights\":[\"1\",\"2\",\"3\",\"6\",\"7\"],\"owner\":\"0000000077f6ae5e16f8583016f85830\",\"recycle\":true,\"locked\":true,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2015-12-30T23:59:21\",\"version\":1}," +
                "\"434973f9f-on-0\":{\"name\":\"DenPair - MED on 1450874192099\",\"lights\":[\"6\",\"7\"],\"owner\":\"0000000077f6ae5e16f8583016f85830\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2015-12-23T12:36:32\",\"version\":1}}\n";

        final HueBridgeGetter spiedBridgeGetter = Mockito.spy(new HueBridgeGetter());
        final String expectedUri = spiedBridgeGetter.buildUri(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), "scenes");
        Mockito.doReturn(fullSceneDump).when(spiedBridgeGetter).getURI(expectedUri);

        final List<Scene> scenes = spiedBridgeGetter.getScenes(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken());
        Assert.assertEquals(33, scenes.size());
        final List<CharSequence> sceneIds = scenes.stream().map(Scene::getId).map(String::valueOf).collect(Collectors.toList());
        sceneIds.forEach(sceneId -> Assert.assertTrue(fullSceneDump.contains(sceneId)));
    }

    @Test
    public void testRawGet() {
        final String expectedUri = "http://" + HUE_CONFIGURATION.getIP() + "/api/" + HUE_CONFIGURATION.getToken() + "/lights";
        final HueBridgeGetter spiedBridgeGetter = Mockito.spy(new HueBridgeGetter());
        final String expected = "success";
        Mockito.doReturn(expected).when(spiedBridgeGetter).getURI(expectedUri);

        final String actual = spiedBridgeGetter.rawGet(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), HueBridgeCommands.LIGHTS);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRawGetWithExtras() {
        final String expectedUri = "http://" + HUE_CONFIGURATION.getIP() + "/api/" + HUE_CONFIGURATION.getToken() + "/lights/some/extra/stuff";
        final HueBridgeGetter spiedBridgeGetter = Mockito.spy(new HueBridgeGetter());
        final String expected = "success";
        Mockito.doReturn(expected).when(spiedBridgeGetter).getURI(expectedUri);

        final String actual = spiedBridgeGetter.rawGet(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), HueBridgeCommands.LIGHTS, ImmutableList.of("some", "extra", "stuff"));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetLight() {
        final String lightId = "4";
        final String rawLight = "{\"state\": {\"on\":false,\"bri\":246,\"hue\":34492,\"sat\":232,\"effect\":\"none\",\"xy\":[0.3151,0.3252],\"ct\":155,\"alert\":\"select\",\"colormode\":\"xy\",\"reachable\":true}, \"type\": \"Extended color light\", \"name\": \"ColorLamp2\", \"modelid\": \"LCT007\", \"manufacturername\": \"Philips\",\"uniqueid\":\"00:17:88:01:10:41:02:2d-0b\", \"swversion\": \"66014919\"}";
        final String expectedUri = "http://" + HUE_CONFIGURATION.getIP() + "/api/" + HUE_CONFIGURATION.getToken() + "/lights/4";
        final HueBridgeGetter spiedBridgeGetter = Mockito.spy(new HueBridgeGetter());

        Mockito.doReturn(rawLight).when(spiedBridgeGetter).getURI(expectedUri);
        final Light light = spiedBridgeGetter.getLight(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), lightId);
        Assert.assertEquals("ColorLamp2", light.getName());
    }

    @Test
    public void testGetScene() {
        final String sceneId = "1234test";
        final String rawScene = "{\"name\":\"All Off v2\",\"lights\":[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\"],\"owner\":\"1234\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2016-01-23T17:49:54\",\"version\":2,\"lightstates\":{\"1\":{\"on\":false},\"2\":{\"on\":false},\"3\":{\"on\":false},\"4\":{\"on\":false},\"5\":{\"on\":false},\"6\":{\"on\":false},\"7\":{\"on\":false}}}";
        final String expectedUri = "http://" + HUE_CONFIGURATION.getIP() + "/api/" + HUE_CONFIGURATION.getToken() + "/scenes/1234test";
        final HueBridgeGetter spiedBridgeGetter = Mockito.spy(new HueBridgeGetter());

        Mockito.doReturn(rawScene).when(spiedBridgeGetter).getURI(expectedUri);

        final Scene scene = spiedBridgeGetter.getScene(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), sceneId);
        Assert.assertEquals("All Off v2", scene.getName());
    }

    @Test
    public void testProcessOfFullGroupsDump() {
        final String fullGroupDump = "{" +
                "\"1\":{\"name\":\"Bedroom\",\"lights\":[\"5\",\"4\"],\"type\":\"LightGroup\",\"action\": {\"on\":false,\"bri\":254,\"alert\":\"none\"}}," +
                "\"2\":{\"name\":\"Den\",\"lights\":[\"1\",\"2\",\"3\",\"6\",\"7\"],\"type\":\"LightGroup\",\"action\": {\"on\":false,\"bri\":1,\"hue\":34494,\"sat\":232,\"effect\":\"none\",\"xy\":[0.3151,0.3252],\"ct\":155,\"alert\":\"select\",\"colormode\":\"hs\"}}" +
                "}";

        final HueBridgeGetter spiedBridgeGetter = Mockito.spy(new HueBridgeGetter());
        final String expectedUri = spiedBridgeGetter.buildUri(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), "groups");
        Mockito.doReturn(fullGroupDump).when(spiedBridgeGetter).getURI(expectedUri);

        final List<Group> groups = spiedBridgeGetter.getGroups(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken());
        Assert.assertEquals(2, groups.size());
        final List<CharSequence> groupIds = groups.stream().map(Group::getId).map(String::valueOf).collect(Collectors.toList());
        Assert.assertTrue("The light IDs are base 0, so there should not be a light with ID 0", !groupIds.contains("0"));
        Assert.assertTrue(groupIds.contains("2"));
        Assert.assertTrue(groupIds.contains("1"));
        Assert.assertTrue(!groupIds.contains("3"));
    }

    @Test
    public void testProcessOfSingleGroup() {
        final String singleGroup = "{\"name\":\"Den\",\"lights\":[\"1\",\"2\",\"3\",\"6\",\"7\"],\"type\":\"LightGroup\",\"action\": {\"on\":false,\"bri\":1,\"hue\":34494,\"sat\":232,\"effect\":\"none\",\"xy\":[0.3151,0.3252],\"ct\":155,\"alert\":\"select\",\"colormode\":\"hs\"}}";

        final HueBridgeGetter spiedBridgeGetter = Mockito.spy(new HueBridgeGetter());
        final String expectedUri = spiedBridgeGetter.buildUri(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), "groups", ImmutableList.of("2"));
        Mockito.doReturn(singleGroup).when(spiedBridgeGetter).getURI(expectedUri);

        final Group group = spiedBridgeGetter.getGroup(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), "2");
        Assert.assertEquals("2", group.getId());
        Assert.assertEquals("Den", group.getName());
    }
}
