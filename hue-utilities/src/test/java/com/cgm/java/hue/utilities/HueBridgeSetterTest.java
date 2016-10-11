package com.cgm.java.hue.utilities;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.models.Scene;
import com.cgm.java.hue.models.State;
import com.google.common.collect.ImmutableList;

public class HueBridgeSetterTest {
    private static final HueConfiguration HUE_CONFIGURATION = new HueConfiguration();

    @Test
    public void testSetLightStateSuccess() {
        final String lightId = "lightId";
        final State lightState = State.newBuilder().setOn(false).build();
        final HueBridgeGetter mockedGetter = Mockito.mock(HueBridgeGetter.class);
        final Light mockedLight = Mockito.mock(Light.class);
        Mockito.doReturn(lightState).when(mockedLight).getState();
        Mockito.doReturn(mockedLight).when(mockedGetter).getLight(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), lightId);
        final HueBridgeSetter hueBridgeSetter = Mockito.spy(new HueBridgeSetter(mockedGetter));
        final String expectedUri = "http://" + HUE_CONFIGURATION.getIP() + "/api/" + HUE_CONFIGURATION.getToken() + "/lights/lightId/state";
        final String expectedResult = "expected result";

        Mockito.doReturn(expectedResult).when(hueBridgeSetter).putURI(expectedUri, lightState.toString());

        final String actualResult = hueBridgeSetter.setLightState(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), lightId, lightState);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSetGroupStateSuccess() {
        final HueBridgeSetter hueBridgeSetter = Mockito.spy(new HueBridgeSetter(Mockito.mock(HueBridgeGetter.class)));
        final String groupId = "groupId";
        final State groupAction = State.newBuilder().setOn(false).build();
        final String expectedUri = "http://" + HUE_CONFIGURATION.getIP() + "/api/" + HUE_CONFIGURATION.getToken() + "/groups/groupId/action";
        final String expectedResult = "expected result";

        Mockito.doReturn(expectedResult).when(hueBridgeSetter).putURI(expectedUri, groupAction.toString());

        final String actualResult = hueBridgeSetter.setGroupState(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), groupId, groupAction);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testPostNewSceneSuccess() {
        final HueBridgeSetter hueBridgeSetter = Mockito.spy(new HueBridgeSetter(Mockito.mock(HueBridgeGetter.class)));
        final String expectedSceneId = "sceneId";
        final String sceneName = "sceneName";
        final Scene scene = Scene.newBuilder().setName(sceneName).setLights(ImmutableList.of("4", "9", "1")).build();
        final String expectedUri = "http://" + HUE_CONFIGURATION.getIP() + "/api/" + HUE_CONFIGURATION.getToken() + "/scenes";
        final String successJson = "[{\"success\":{\"id\":\"" + expectedSceneId + "\"}}]";
        final String expectedRequestBody = "{\"name\":\"" + sceneName + "\", \"lights\":[\"" + "4\",\"9\",\"1" + "\"]}";

        Mockito.doReturn(successJson).when(hueBridgeSetter).postURI(expectedUri, expectedRequestBody);

        final String resultSceneId = hueBridgeSetter.postNewScene(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), scene);
        Assert.assertEquals(expectedSceneId, resultSceneId);
    }

    @Test
    public void testPostNewSceneBadResponse() {
        final HueBridgeSetter hueBridgeSetter = Mockito.spy(new HueBridgeSetter(Mockito.mock(HueBridgeGetter.class)));
        final String sceneName = "sceneName";
        final Scene scene = Scene.newBuilder().setName(sceneName).setLights(ImmutableList.of("4", "9", "1")).build();
        final String expectedUri = "http://" + HUE_CONFIGURATION.getIP() + "/api/" + HUE_CONFIGURATION.getToken() + "/scenes";
        final String failureJson = "[{\"error\": {\"type\": 5,\"address\": \"/scenes/lights\",\"description\": \"invalid/missing parameters in body\"}}]";
        final String expectedRequestBody = "{\"name\":\"" + sceneName + "\", \"lights\":[\"" + "4\",\"9\",\"1" + "\"]}";

        Mockito.doReturn(failureJson).when(hueBridgeSetter).postURI(expectedUri, expectedRequestBody);

        try {
            hueBridgeSetter.postNewScene(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), scene);
        } catch (RuntimeException rte) {
            final String message = rte.getMessage();
            Assert.assertTrue(message, message.contains("POSTing of new scene failed."));
        }
    }

    @Test
    public void testPostNewSceneParseResponseFail() {
        final HueBridgeSetter hueBridgeSetter = Mockito.spy(new HueBridgeSetter(Mockito.mock(HueBridgeGetter.class)));
        final String sceneName = "sceneName";
        final Scene scene = Scene.newBuilder().setName(sceneName).setLights(ImmutableList.of("4", "9", "1")).build();
        final String expectedUri = "http://" + HUE_CONFIGURATION.getIP() + "/api/" + HUE_CONFIGURATION.getToken() + "/scenes";
        final String unparseableJson = "[{\"error\": {\"type\": 5,\"address\": \"/scenes/lights\",\"description\": \"success\"}}]";
        final String expectedRequestBody = "{\"name\":\"" + sceneName + "\", \"lights\":[\"" + "4\",\"9\",\"1" + "\"]}";

        Mockito.doReturn(unparseableJson).when(hueBridgeSetter).postURI(expectedUri, expectedRequestBody);

        try {
            hueBridgeSetter.postNewScene(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), scene);
        } catch (RuntimeException rte) {
            final String message = rte.getMessage();
            Assert.assertTrue(message, message.contains("Error while parsing response to a POST of a new scene."));
        }
    }

    @Test
    public void testDeleteSceneSuccess() {
        final HueBridgeSetter hueBridgeSetter = Mockito.spy(new HueBridgeSetter(Mockito.mock(HueBridgeGetter.class)));
        final String sceneId = "sceneId";
        final String expectedUri = "http://" + HUE_CONFIGURATION.getIP() + "/api/" + HUE_CONFIGURATION.getToken() + "/scenes/" + sceneId;
        final String expectedDeleteResult = "Delete Result";
        Mockito.doReturn(expectedDeleteResult).when(hueBridgeSetter).deleteURI(expectedUri);

        final String deleteResult = hueBridgeSetter.deleteScene(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), sceneId);

        Assert.assertEquals(expectedDeleteResult, deleteResult);
    }

    @Test
    public void testDeleteGroupSuccess() {
        final HueBridgeSetter hueBridgeSetter = Mockito.spy(new HueBridgeSetter(Mockito.mock(HueBridgeGetter.class)));
        final String groupId = "groupId";
        final String expectedUri = "http://" + HUE_CONFIGURATION.getIP() + "/api/" + HUE_CONFIGURATION.getToken() + "/groups/" + groupId;
        final String expectedDeleteResult = "Delete Result";
        Mockito.doReturn(expectedDeleteResult).when(hueBridgeSetter).deleteURI(expectedUri);

        final String deleteResult = hueBridgeSetter.deleteGroup(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), groupId);

        Assert.assertEquals(expectedDeleteResult, deleteResult);
    }

    @Test
    public void testDeleteScenePreconditions() {
        breakHueBridgeSetterDeleteScene("ip", "token", null);
        breakHueBridgeSetterDeleteScene("ip", null, "sceneId");
        breakHueBridgeSetterDeleteScene(null, "token", "sceneId");
        breakHueBridgeSetterDeleteScene("ip", "token", "");
        breakHueBridgeSetterDeleteScene("ip", "", "sceneId");
        breakHueBridgeSetterDeleteScene("", "token", "sceneId");
    }

    @Test
    public void testDeleteGroupPreconditions() {
        breakHueBridgeSetterDeleteGroup("ip", "token", null);
        breakHueBridgeSetterDeleteGroup("ip", null, "groupId");
        breakHueBridgeSetterDeleteGroup(null, "token", "groupId");
        breakHueBridgeSetterDeleteGroup("ip", "token", "");
        breakHueBridgeSetterDeleteGroup("ip", "", "groupId");
        breakHueBridgeSetterDeleteGroup("", "token", "groupId");
    }

    private void breakHueBridgeSetterDeleteGroup(final String ip, final String token, final String groupId) {
        final HueBridgeSetter hueBridgeSetter = new HueBridgeSetter(Mockito.mock(HueBridgeGetter.class));
        try {
            hueBridgeSetter.deleteGroup(ip, token, groupId);
            Assert.fail();
        } catch (IllegalArgumentException iae) {
            // Good.
        }
    }

    private void breakHueBridgeSetterDeleteScene(final String ip, final String token, final String sceneId) {
        final HueBridgeSetter hueBridgeSetter = new HueBridgeSetter(Mockito.mock(HueBridgeGetter.class));
        try {
            hueBridgeSetter.deleteScene(ip, token, sceneId);
            Assert.fail();
        } catch (IllegalArgumentException iae) {
            // Good.
        }
    }

    @Test
    public void testSetLightStatePreconditions() {
        final State state = State.newBuilder().setOn(false).build();
        breakHueBridgeSetterSetLightState("ip", "token", "lightId", null);
        breakHueBridgeSetterSetLightState("ip", "token", null, state);
        breakHueBridgeSetterSetLightState("ip", null, "lightId", state);
        breakHueBridgeSetterSetLightState(null, "token", "lightId", state);
        breakHueBridgeSetterSetLightState("ip", "token", "", state);
        breakHueBridgeSetterSetLightState("ip", "", "lightId", state);
        breakHueBridgeSetterSetLightState("", "token", "lightId", state);
    }

    private void breakHueBridgeSetterSetLightState(final String ip, final String token, final String lightId, final State state) {
        final HueBridgeSetter hueBridgeSetter = new HueBridgeSetter(Mockito.mock(HueBridgeGetter.class));
        try {
            hueBridgeSetter.setLightState(ip, token, lightId, state);
            Assert.fail();
        } catch (IllegalArgumentException iae) {
            // Good.
        }
    }

    @Test
    public void testPostNewScenePreconditions() {
        final Scene scene = Scene.newBuilder().setId("sceneId").build();
        breakHueBridgeSetterPost("ip", "token", null);
        breakHueBridgeSetterPost("ip", null, scene);
        breakHueBridgeSetterPost(null, "token", scene);
        breakHueBridgeSetterPost("ip", "", scene);
        breakHueBridgeSetterPost("", "token", scene);
    }

    private void breakHueBridgeSetterPost(final String ip, final String token, final Scene scene) {
        final HueBridgeSetter hueBridgeSetter = new HueBridgeSetter(Mockito.mock(HueBridgeGetter.class));
        try {
            hueBridgeSetter.postNewScene(ip, token, scene);
            Assert.fail();
        } catch (IllegalArgumentException iae) {
            // Good.
        }
    }
}
