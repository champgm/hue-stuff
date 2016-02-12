package com.cgm.java.hue.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.cgm.java.hue.models.Group;
import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.models.Rule;
import com.cgm.java.hue.models.Scene;
import com.cgm.java.hue.models.Schedule;
import com.cgm.java.hue.models.Sensor;
import com.cgm.java.hue.models.State;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class HueJsonParserTest {

    @Test
    public void testConversionOfSingleScene() {
        final String actualSceneJson =
                "{" +
                "\"name\":\"Test Scene\"," +
                "\"lights\":[\"1\",\"2\"]," +
                "\"owner\":\"1234\"," +
                "\"recycle\":true," +
                "\"locked\":false," +
                "\"appdata\":{}," +
                "\"picture\":\"\"," +
                "\"lastupdated\":\"2016-01-23T01:32:32\"," +
                "\"version\":2,\"lightstates\":{" +
                "\"1\":{\"on\":false,\"bri\":198,\"effect\":\"none\",\"hue\":0,\"sat\":254}," +
                "\"2\":{\"on\":true,\"bri\":198,\"effect\":\"none\",\"hue\":0,\"sat\":254}" +
                "}}";

        final String acceptableFormatJson =
                "{" +
                "\"id\":null," +
                "\"name\":\"scenename\"," +
                "\"lights\":[\"1\",\"2\"]," +
                "\"owner\":null," +
                "\"recycle\":null," +
                "\"locked\":null," +
                "\"appdata\":[]," +
                "\"picture\":null," +
                "\"lastupdated\":null," +
                "\"version\":null," +
                "\"lightstates\":[" +
                "{\"on\":false,\"bri\":null,\"alert\":null,\"reachable\":null,\"colormode\":null,\"hue\":null,\"sat\":null,\"effect\":null,\"xy\":null}," +
                "{\"on\":true,\"bri\":null,\"alert\":null,\"reachable\":null,\"colormode\":null,\"hue\":null,\"sat\":null,\"effect\":null,\"xy\":null}" +
                "]}";

        final State state1 = State.newBuilder().setOn(false).build();
        final State state2 = State.newBuilder().setOn(true).build();
        final Scene properScene = Scene.newBuilder().setName("scene name").setLights(ImmutableList.of("1", "2")).setLightstates(ImmutableList.of(state1, state2)).build();
        System.out.println("Proper Scene: " + properScene);

        final Scene acceptableScene = HueJsonParser.parseSceneFromJson("ID", acceptableFormatJson);
        Assert.assertNotNull(acceptableScene);

        final Scene actualScene = HueJsonParser.parseSceneFromJson("ID", actualSceneJson);
        Assert.assertNotNull(actualScene);
        final List<State> lightStates = actualScene.getLightstates();
        final ArrayList<State> lightStatesArray = new ArrayList<>(lightStates);
        Assert.assertFalse(lightStatesArray.get(0).getOn());
        Assert.assertTrue(lightStatesArray.get(1).getOn());
    }

    @Test
    public void testConversionOfFullSceneDump() {
        final String fullDump =
                "{" +
                "\"738e3c2af-off-0\":{\"name\":\"BR - on off 1448596157000\",\"lights\":[\"4\",\"5\"],\"owner\":\"\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2016-01-12T04:36:52\",\"version\":1}," +
                "\"808140eef-on-0\":{\"name\":\"BR - on on 1448596157000\",\"lights\":[\"4\",\"5\"],\"owner\":\"\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2016-01-16T01:50:17\",\"version\":1}," +
                "\"087f88f52-on-0\":{\"name\":\"Sunset on 1448660085758\",\"lights\":[\"1\",\"2\",\"3\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"045fa99ae-on-0\":{\"name\":\"BR - off on 1448596158000\",\"lights\":[\"4\",\"5\"],\"owner\":\"\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2016-01-12T17:53:22\",\"version\":1}," +
                "\"ac637e2f0-on-0\":{\"name\":\"Relax on 1448660096855\",\"lights\":[\"1\",\"2\",\"3\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"4b964fde2-on-0\":{\"name\":\"White and Blue on 1452599617000\",\"lights\":[\"1\",\"2\",\"3\",\"6\",\"7\"],\"owner\":\"\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2016-01-12T17:53:55\",\"version\":1}," +
                "\"0a854c238-on-0\":{\"name\":\"White and Blue on 1452599617000\",\"lights\":[\"1\",\"2\",\"3\",\"6\",\"7\"],\"owner\":\"\",\"recycle\":true,\"locked\":true,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2016-01-12T17:55:14\",\"version\":1}," +
                "\"8fd0f66aa-on-0\":{\"name\":\"BR - med on 1452844228000\",\"lights\":[\"4\",\"5\"],\"owner\":\"\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2016-01-16T01:50:48\",\"version\":1}," +
                "\"84fa88aa5-on-0\":{\"name\":\"Energize on 1448660214580\",\"lights\":[\"1\",\"2\",\"3\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"VVxFJ9bJErVFuVe\":{\"name\":\"Test Scene\",\"lights\":[\"1\"],\"owner\":\"\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2016-01-22T19:01:43\",\"version\":2}," +
                "\"Q05zE5Lv0ila9FV\":{\"name\":\"Test Scene\",\"lights\":[\"1\"],\"owner\":\"\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2016-01-22T19:02:37\",\"version\":2}," +
                "\"OldHdfRFof1xLNJ\":{\"name\":\"Test Scene\",\"lights\":[\"1\"],\"owner\":\"\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2016-01-22T19:03:14\",\"version\":2}," +
                "\"v64yP-QRuJ-OWS7\":{\"name\":\"Test Scene\",\"lights\":[\"1\"],\"owner\":\"\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2016-01-22T19:14:17\",\"version\":2}," +
                "\"f5d73dbdd-on-0\":{\"name\":\"Concentrate on 1448595772000\",\"lights\":[\"1\",\"2\",\"3\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"t08JdkUEckxUdOz\":{\"name\":\"Test Scene\",\"lights\":[\"1\",\"2\"],\"owner\":\"\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2016-01-23T01:32:32\",\"version\":2}," +
                "\"ab00a355f-on-0\":{\"name\":\"Concentrate on 1448595772000\",\"lights\":[\"1\",\"2\",\"3\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"a1167aa91-on-0\":{\"name\":\"Concentrate on 1448596220000\",\"lights\":[\"1\",\"2\",\"3\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"00a658fd5-on-0\":{\"name\":\"Sunset on 1448596285000\",\"lights\":[\"1\",\"2\",\"3\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"085ed3167-off-0\":{\"name\":\"Sunset off 1448596285000\",\"lights\":[\"1\",\"2\",\"3\",\"6\",\"7\"],\"owner\":\"none\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":null,\"version\":1}," +
                "\"869e0d87c-on-0\":{\"name\":\"Sunset on 1448596285000\",\"lights\":[\"1\",\"2\",\"3\",\"6\",\"7\"],\"owner\":\"\",\"recycle\":true,\"locked\":false,\"appdata\":{},\"picture\":\"\",\"lastupdated\":\"2015-12-17T19:09:48\",\"version\":1}" +
                "}";

        final ImmutableSet<String> expectedIds = ImmutableSet.of("738e3c2af-off-0", "808140eef-on-0", "087f88f52-on-0", "045fa99ae-on-0", "ac637e2f0-on-0", "4b964fde2-on-0", "0a854c238-on-0", "8fd0f66aa-on-0", "84fa88aa5-on-0", "VVxFJ9bJErVFuVe", "Q05zE5Lv0ila9FV", "OldHdfRFof1xLNJ", "v64yP-QRuJ-OWS7", "f5d73dbdd-on-0", "t08JdkUEckxUdOz", "ab00a355f-on-0", "a1167aa91-on-0", "00a658fd5-on-0", "085ed3167-off-0", "869e0d87c-on-0");

        final Collection<Scene> scenes = HueJsonParser.parseScenesFromJson(fullDump);
        final List<CharSequence> sceneIds = scenes.stream().map(Scene::getId).collect(Collectors.toList());
        for (final String expectedId : expectedIds) {
            Assert.assertTrue("Expected ID, '" + expectedId + "' not found.", sceneIds.contains(expectedId));
        }
        Assert.assertEquals(20, scenes.size());
    }

    @Test
    public void testConversionOfSingleJsonToLight() throws Exception {
        final String lightJson =
                "{" +
                "\"state\": {\"on\":false," +
                "\"bri\":254," +
                "\"alert\":\"none\"," +
                "\"reachable\":true}, " +
                "\"type\": \"Dimmable light\", " +
                "\"name\": \"Na's lamp\", " +
                "\"modelid\": \"ZLL Light\", " +
                "\"manufacturername\": \"GE_Appliances\"," +
                "\"uniqueid\":\"7c:e5:24:00:00:0b:19:87-01\", " +
                "\"swversion\": \"1197632\", " +
                "\"pointsymbol\": { \"1\":\"none\", \"2\":\"none\", \"3\":\"none\", \"4\":\"none\", " +
                "\"5\":\"none\", \"6\":\"none\", \"7\":\"none\", \"8\":\"none\" }" +
                "}";

        final Light lightAvro = HueJsonParser.parseLightFromJson("1", lightJson);
        Assert.assertEquals("1", lightAvro.getId());
        Assert.assertEquals("Na's lamp", lightAvro.getName());
    }

    @Test
    public void testConversionOfSingleJsonToGroup() throws Exception {
        final String lightJson =
                "{" +
                "\"name\":\"Bedroom\"," +
                "\"lights\":[\"5\",\"4\"]," +
                "\"type\":\"LightGroup\"," +
                "\"action\": {\"on\":false,\"bri\":254,\"alert\":\"none\"}" +
                "}";

        final Group groupAvro = HueJsonParser.parseGroupFromJson("1", lightJson);
        Assert.assertEquals("1", groupAvro.getId());
        Assert.assertEquals("Bedroom", groupAvro.getName());
    }

    @Test
    public void testConversionOfFullGroupDump() {
        final String fullDump =
                "{" +
                "\"1\":{\"name\":\"Bedroom\",\"lights\":[\"5\",\"4\"],\"type\":\"LightGroup\",\"action\": {\"on\":false,\"bri\":254,\"alert\":\"none\"}}," +
                "\"2\":{\"name\":\"Den\",\"lights\":[\"1\",\"2\",\"3\",\"6\",\"7\"],\"type\":\"LightGroup\",\"action\": {\"on\":false,\"bri\":1,\"hue\":34494,\"sat\":232,\"effect\":\"none\",\"xy\":[0.3151,0.3252],\"ct\":155,\"alert\":\"select\",\"colormode\":\"hs\"}}" +
                "}";

        final ImmutableSet<String> expectedIds = ImmutableSet.of("1", "2");

        final Collection<Group> groups = HueJsonParser.parseGroupsFromJson(fullDump);
        final List<CharSequence> groupIds = groups.stream().map(Group::getId).collect(Collectors.toList());
        for (final String expectedId : expectedIds) {
            Assert.assertTrue("Expected ID, '" + expectedId + "' not found.", groupIds.contains(expectedId));
        }
        Assert.assertEquals(2, groups.size());
    }

    @Test
    public void testConversionOfJsonToState() throws Exception {
        final String stateJson =
                "{" +
                "\"on\":false," +
                "\"bri\":254," +
                "\"alert\":\"none\"," +
                "\"reachable\":true" +
                "}";

        final State stateAvro = HueJsonParser.parseStateFromJson(stateJson);
        Assert.assertEquals(Boolean.FALSE, stateAvro.getOn());
        Assert.assertEquals(Long.valueOf(254), stateAvro.getBri());
        Assert.assertEquals("none", stateAvro.getAlert());
        Assert.assertEquals(Boolean.TRUE, stateAvro.getReachable());
    }

    @Test
    public void testConversionOfJsonToSensor() throws Exception {
        final String sensorJson = "{" +
                                  "\"state\": {" +
                                  "\"buttonevent\":4002," +
                                  "\"lastupdated\":\"2016-02-02T04:27:34\"}," +
                                  "\"config\":{" +
                                  "\"on\":true," +
                                  "\"battery\":100," +
                                  "\"reachable\":true}," +
                                  "\"name\":\"Hue dimmer switch 1\"," +
                                  "\"type\":\"ZLLSwitch\"," +
                                  "\"modelid\":\"RWL020\"," +
                                  "\"manufacturername\":\"Philips\"," +
                                  "\"swversion\":\"5.45.1.16265\"," +
                                  "\"uniqueid\":\"00:17:88:01:10:32:72:4e-02-fc00\"" +
                                  "}";

        final Sensor sensorAvro = HueJsonParser.parseSensorFromJson("1", sensorJson);
        Assert.assertEquals(Boolean.TRUE, sensorAvro.getConfig().getOn());
        Assert.assertEquals("Hue dimmer switch 1", sensorAvro.getName());
        Assert.assertEquals("00:17:88:01:10:32:72:4e-02-fc00", sensorAvro.getUniqueid());
    }

    @Test
    public void testConversionOfFullSensorDump() throws Exception {
        final String sensorJson = "{" +
                                  "\"1\":{\"state\": {\"daylight\":null,\"lastupdated\":\"none\"},\"config\":{\"on\":true,\"long\":\"none\",\"lat\":\"none\",\"sunriseoffset\":30,\"sunsetoffset\":-30},\"name\":\"Daylight\",\"type\":\"Daylight\",\"modelid\":\"PHDL00\",\"manufacturername\":\"Philips\",\"swversion\":\"1.0\"}," +
                                  "\"2\":{\"state\": {\"buttonevent\":34,\"lastupdated\":\"2016-02-02T13:10:54\"},\"config\":{\"on\":true},\"name\":\"Hue tap switch 1\",\"type\":\"ZGPSwitch\",\"modelid\":\"ZGPSWITCH\",\"manufacturername\":\"Philips\",\"uniqueid\":\"00:00:00:00:00:43:c5:ea-f2\"}," +
                                  "\"3\":{\"state\": {\"buttonevent\":4002,\"lastupdated\":\"2016-02-02T04:27:34\"},\"config\":{\"on\":true,\"battery\":100,\"reachable\":true},\"name\":\"Hue dimmer switch 1\",\"type\":\"ZLLSwitch\",\"modelid\":\"RWL020\",\"manufacturername\":\"Philips\",\"swversion\":\"5.45.1.16265\",\"uniqueid\":\"00:17:88:01:10:32:72:4e-02-fc00\"}," +
                                  "\"4\":{\"state\": {\"status\":0,\"lastupdated\":\"none\"},\"config\":{\"on\":true,\"battery\":0,\"reachable\":true},\"name\":\"Light status\",\"type\":\"CLIPGenericStatus\",\"modelid\":\"Model 2015\",\"manufacturername\":\"all 4 hue\",\"swversion\":\"1.0\",\"uniqueid\":\"PFHS-LIGHT-STATE\"}" +
                                  "}";
        final Collection<Sensor> sensors = HueJsonParser.parseSensorsFromJson(sensorJson);
        Assert.assertEquals(4, sensors.size());
        for (final Sensor sensor : sensors) {
            if (sensor.getId().equals("1")) {
                Assert.assertEquals("Daylight", sensor.getName());
            } else if (sensor.getId().equals("2")) {
                Assert.assertEquals("Hue tap switch 1", sensor.getName());
            } else if (sensor.getId().equals("3")) {
                Assert.assertEquals("Hue dimmer switch 1", sensor.getName());
            } else if (sensor.getId().equals("4")) {
                Assert.assertEquals("Light status", sensor.getName());
            }
        }
    }

    @Test
    public void testConversionOfJsonToRule() throws Exception {
        final String ruleJson = "{\"name\":\"Tap 2.4 DenPair -ON\"," +
                                "\"owner\":\"1234\"," +
                                "\"created\":\"2015-12-30T23:59:20\"," +
                                "\"lasttriggered\":\"2016-02-04T23:16:38\"," +
                                "\"timestriggered\": 32," +
                                "\"status\": \"enabled\"," +
                                "\"conditions\":[{\"address\":\"/sensors/2/state/buttonevent\",\"operator\":\"eq\",\"value\":\"18\"},{\"address\":\"/sensors/2/state/lastupdated\",\"operator\":\"dx\"}]," +
                                "\"actions\":[{\"address\":\"/groups/0/action\",\"method\":\"PUT\",\"body\":{\"scene\":\"b79ee5717-on-0\"}}]" +
                                "}";

        final Rule ruleAvro = HueJsonParser.parseRuleFromJson("1", ruleJson);
        Assert.assertEquals(2, ruleAvro.getConditions().size());
        Assert.assertEquals("Tap 2.4 DenPair -ON", ruleAvro.getName());
        Assert.assertEquals(1, ruleAvro.getActions().size());
        Assert.assertEquals("{\"scene\":\"b79ee5717-on-0\"}", ruleAvro.getActions().get(0).getBody());
    }

    @Test
    public void testConversionOfFullRuleDump() throws Exception {
        final String rulesJson = "{" +
                                 "\"1\":{\"name\":\"Tap 2.4 DenPair -ON\",\"owner\":\"1234\",\"created\":\"2015-12-30T23:59:20\",\"lasttriggered\":\"2016-02-04T23:16:38\",\"timestriggered\": 32,\"status\": \"enabled\",\"conditions\":[{\"address\":\"/sensors/2/state/buttonevent\",\"operator\":\"eq\",\"value\":\"18\"},{\"address\":\"/sensors/2/state/lastupdated\",\"operator\":\"dx\"}],\"actions\":[{\"address\":\"/groups/0/action\",\"method\":\"PUT\",\"body\":{\"scene\":\"b79ee5717-on-0\"}}]}," +
                                 "\"2\":{\"name\":\"Tap 2.1 Full den off\",\"owner\":\"1234\",\"created\":\"2015-12-30T23:59:22\",\"lasttriggered\":\"2016-02-07T18:27:44\",\"timestriggered\": 76,\"status\": \"enabled\",\"conditions\":[{\"address\":\"/sensors/2/state/buttonevent\",\"operator\":\"eq\",\"value\":\"34\"},{\"address\":\"/sensors/2/state/lastupdated\",\"operator\":\"dx\"}],\"actions\":[{\"address\":\"/groups/0/action\",\"method\":\"PUT\",\"body\":{\"scene\":\"85385533d-on-0\"}}]}," +
                                 "\"3\":{\"name\":\"Tap 2.2 Sunset\",\"owner\":\"1234\",\"created\":\"2015-12-30T23:59:21\",\"lasttriggered\":\"2016-02-07T00:27:17\",\"timestriggered\": 36,\"status\": \"enabled\",\"conditions\":[{\"address\":\"/sensors/2/state/buttonevent\",\"operator\":\"eq\",\"value\":\"16\"},{\"address\":\"/sensors/2/state/lastupdated\",\"operator\":\"dx\"}],\"actions\":[{\"address\":\"/groups/0/action\",\"method\":\"PUT\",\"body\":{\"scene\":\"28098c28a-on-0\"}}]}," +
                                 "\"4\":{\"name\":\"Tap 2.3 White and Blue\",\"owner\":\"1234\",\"created\":\"2016-01-12T17:55:15\",\"lasttriggered\":\"2016-02-06T15:21:05\",\"timestriggered\": 27,\"status\": \"enabled\",\"conditions\":[{\"address\":\"/sensors/2/state/buttonevent\",\"operator\":\"eq\",\"value\":\"17\"},{\"address\":\"/sensors/2/state/lastupdated\",\"operator\":\"dx\"}],\"actions\":[{\"address\":\"/groups/0/action\",\"method\":\"PUT\",\"body\":{\"scene\":\"0a854c238-on-0\"}}]}," +
                                 "\"5\":{\"name\":\"Dimmer 3.1 \",\"owner\":\"1234\",\"created\":\"2015-12-31T04:16:00\",\"lasttriggered\":\"2016-02-07T03:06:48\",\"timestriggered\": 91,\"status\": \"enabled\",\"conditions\":[{\"address\":\"/sensors/3/state/buttonevent\",\"operator\":\"eq\",\"value\":\"1000\"},{\"address\":\"/sensors/3/state/lastupdated\",\"operator\":\"dx\"}],\"actions\":[{\"address\":\"/groups/1/action\",\"method\":\"PUT\",\"body\":{\"on\":true}}]}," +
                                 "\"6\":{\"name\":\"Dimmer 3.2 \",\"owner\":\"1234\",\"created\":\"2015-12-31T04:16:00\",\"lasttriggered\":\"2016-02-06T03:49:50\",\"timestriggered\": 75,\"status\": \"enabled\",\"conditions\":[{\"address\":\"/sensors/3/state/buttonevent\",\"operator\":\"eq\",\"value\":\"2001\"},{\"address\":\"/sensors/3/state/lastupdated\",\"operator\":\"dx\"}],\"actions\":[{\"address\":\"/groups/1/action\",\"method\":\"PUT\",\"body\":{\"transitiontime\":9,\"bri_inc\":56}}]}," +
                                 "\"7\":{\"name\":\"Dimmer 3.3 \",\"owner\":\"1234\",\"created\":\"2015-12-31T04:16:00\",\"lasttriggered\":\"2016-01-11T02:48:22\",\"timestriggered\": 11,\"status\": \"enabled\",\"conditions\":[{\"address\":\"/sensors/3/state/buttonevent\",\"operator\":\"eq\",\"value\":\"3000\"},{\"address\":\"/sensors/3/state/lastupdated\",\"operator\":\"dx\"}],\"actions\":[{\"address\":\"/groups/1/action\",\"method\":\"PUT\",\"body\":{\"transitiontime\":9,\"bri_inc\":-30}}]}," +
                                 "\"8\":{\"name\":\"Dimmer 3.3 \",\"owner\":\"1234\",\"created\":\"2015-12-31T04:16:00\",\"lasttriggered\":\"2016-01-07T03:33:26\",\"timestriggered\": 14,\"status\": \"enabled\",\"conditions\":[{\"address\":\"/sensors/3/state/buttonevent\",\"operator\":\"eq\",\"value\":\"3001\"},{\"address\":\"/sensors/3/state/lastupdated\",\"operator\":\"dx\"}],\"actions\":[{\"address\":\"/groups/1/action\",\"method\":\"PUT\",\"body\":{\"transitiontime\":9,\"bri_inc\":-56}}]}," +
                                 "\"9\":{\"name\":\"Dimmer 3.3 \",\"owner\":\"1234\",\"created\":\"2015-12-31T04:16:00\",\"lasttriggered\":\"2015-12-31T04:18:53\",\"timestriggered\": 3,\"status\": \"enabled\",\"conditions\":[{\"address\":\"/sensors/3/state/buttonevent\",\"operator\":\"eq\",\"value\":\"3003\"},{\"address\":\"/sensors/3/state/lastupdated\",\"operator\":\"dx\"}],\"actions\":[{\"address\":\"/groups/1/action\",\"method\":\"PUT\",\"body\":{\"bri_inc\":0}}]}," +
                                 "\"10\":{\"name\":\"Dimmer 3.2 \",\"owner\":\"1234\",\"created\":\"2015-12-31T04:16:01\",\"lasttriggered\":\"2016-02-06T03:49:39\",\"timestriggered\": 29,\"status\": \"enabled\",\"conditions\":[{\"address\":\"/sensors/3/state/buttonevent\",\"operator\":\"eq\",\"value\":\"2000\"},{\"address\":\"/sensors/3/state/lastupdated\",\"operator\":\"dx\"}],\"actions\":[{\"address\":\"/groups/1/action\",\"method\":\"PUT\",\"body\":{\"transitiontime\":9,\"bri_inc\":30}}]}," +
                                 "\"11\":{\"name\":\"Dimmer 3.2 \",\"owner\":\"1234\",\"created\":\"2015-12-31T04:16:01\",\"lasttriggered\":\"2016-02-06T03:49:51\",\"timestriggered\": 16,\"status\": \"enabled\",\"conditions\":[{\"address\":\"/sensors/3/state/buttonevent\",\"operator\":\"eq\",\"value\":\"2003\"},{\"address\":\"/sensors/3/state/lastupdated\",\"operator\":\"dx\"}],\"actions\":[{\"address\":\"/groups/1/action\",\"method\":\"PUT\",\"body\":{\"bri_inc\":0}}]}," +
                                 "\"12\":{\"name\":\"Dimmer 3.4 \",\"owner\":\"1234\",\"created\":\"2015-12-31T04:16:01\",\"lasttriggered\":\"2016-02-04T13:17:49\",\"timestriggered\": 73,\"status\": \"enabled\",\"conditions\":[{\"address\":\"/sensors/3/state/buttonevent\",\"operator\":\"eq\",\"value\":\"4000\"},{\"address\":\"/sensors/3/state/lastupdated\",\"operator\":\"dx\"}],\"actions\":[{\"address\":\"/groups/1/action\",\"method\":\"PUT\",\"body\":{\"on\":false}}]}" +
                                 "}";

        final ImmutableList<String> expectedIds = ImmutableList.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");

        final Collection<Rule> rules = HueJsonParser.parseRulesFromJson(rulesJson);
        Assert.assertEquals(12, rules.size());
        rules.forEach(rule -> Assert.assertTrue(expectedIds.contains(rule.getId().toString())));
    }

    @Test
    public void testConversionOfJsonToSchedule() throws Exception {
        final String scheduleJson = "{" +
                                "\"name\":\"wakeup - bedroom\"," +
                                "\"description\":\"BR - on\"," +
                                "\"command\":{" +
                                "\"address\":\"/api/0000000077f6ae5e16f8583016f85830/groups/0/action\"," +
                                "\"body\":{\"scene\":\"808140eef-on-30\"}," +
                                "\"method\":\"PUT\"}," +
                                "\"localtime\":\"W124/T05:30:00\"," +
                                "\"time\":\"W124/T11:30:00\"," +
                                "\"created\":\"2015-12-04T12:01:11\"," +
                                "\"status\":\"disabled\"" +
                                "}";

        final Schedule scheduleAvro = HueJsonParser.parseScheduleFromJson("1482820976701523", scheduleJson);
        Assert.assertEquals("1482820976701523", scheduleAvro.getId());
        Assert.assertEquals("wakeup - bedroom", scheduleAvro.getName());
        Assert.assertEquals("{\"scene\":\"808140eef-on-30\"}", scheduleAvro.getCommand().getBody());
    }

    @Test
    public void testConversionOfFullScheduleDump() throws Exception {
        final String schedulesJson = "{" +
                                 "\"1482820976701523\":{\"name\":\"wakeup - bedroom\",\"description\":\"BR - on\",\"command\":{\"address\":\"/api/0000000077f6ae5e16f8583016f85830/groups/0/action\",\"body\":{\"scene\":\"808140eef-on-30\"},\"method\":\"PUT\"},\"localtime\":\"W124/T05:30:00\",\"time\":\"W124/T11:30:00\",\"created\":\"2015-12-04T12:01:11\",\"status\":\"disabled\"}," +
                                 "\"7303628988606452\":{\"name\":\"wakeup - den pair\",\"description\":\"Den - full\",\"command\":{\"address\":\"/api/0000000077f6ae5e16f8583016f85830/groups/0/action\",\"body\":{\"scene\":\"b79ee5717-on-0\"},\"method\":\"PUT\"},\"localtime\":\"W124/T05:30:00\",\"time\":\"W124/T11:30:00\",\"created\":\"2015-12-17T19:12:03\",\"status\":\"enabled\"}," +
                                 "\"9608249029237889\":{\"name\":\"Alarm\",\"description\":\"Energize\",\"command\":{\"address\":\"/api/0000000077f6ae5e16f8583016f85830/groups/0/action\",\"body\":{\"scene\":\"acac20a4d-on-0\"},\"method\":\"PUT\"},\"localtime\":\"W124/T05:30:00\",\"time\":\"W124/T11:30:00\",\"created\":\"2015-12-23T12:38:28\",\"status\":\"enabled\"}" +
                                 "}";

        final ImmutableList<String> expectedIds = ImmutableList.of(
                "1482820976701523",
                "7303628988606452",
                "9608249029237889");

        final Collection<Schedule> schedules = HueJsonParser.parseSchedulesFromJson(schedulesJson);
        schedules.forEach(schedule -> Assert.assertTrue(expectedIds.contains(schedule.getId().toString())));
        Assert.assertEquals(3, schedules.size());
    }

    @Test
    public void testConversionFailures() throws Exception {
        final Scene badScene = HueJsonParser.JSON_TO_SCENE.apply("junk", "data");
        Assert.assertNull(badScene);
        final Light badLight = HueJsonParser.JSON_TO_LIGHT.apply(1L, "data");
        Assert.assertNull(badLight);
        final State badState = HueJsonParser.JSON_TO_STATE.apply("bad data");
        Assert.assertNull(badState);
        final Group badGroup = HueJsonParser.JSON_TO_GROUP.apply(1L, "bad data");
        Assert.assertNull(badGroup);
        final Rule badRule = HueJsonParser.JSON_TO_RULE.apply(1L, "bad data");
        Assert.assertNull(badRule);
        final Schedule badSchedule = HueJsonParser.JSON_TO_SCHEDULE.apply(1L, "bad data");
        Assert.assertNull(badSchedule);
    }
}
