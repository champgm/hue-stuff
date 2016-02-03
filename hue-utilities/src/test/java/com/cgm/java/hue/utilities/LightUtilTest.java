package com.cgm.java.hue.utilities;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.models.State;

public class LightUtilTest {
    @Test
    public void testToggleLightOff() {
        final HueConfiguration hueConfiguration = new HueConfiguration();
        final String lightId = "lightId";
        final HueBridgeGetter mockedHueBridgeGetter = Mockito.mock(HueBridgeGetter.class);
        final HueBridgeSetter mockedHueBridgeSetter = Mockito.mock(HueBridgeSetter.class);
        final State onState = State.newBuilder().setOn(true).build();
        final State offState = State.newBuilder().setOn(false).build();
        final Light lightOn = Light.newBuilder().setId(lightId).setState(onState).build();
        final Light lightOff = Light.newBuilder(lightOn).setState(offState).build();

        Mockito.doReturn(lightOn).doReturn(lightOff).doReturn(lightOn).when(mockedHueBridgeGetter).getLight(hueConfiguration.getIP(), hueConfiguration.getToken(), lightId);
        Mockito.doReturn("whatever").when(mockedHueBridgeSetter).setLightState(hueConfiguration.getIP(), hueConfiguration.getToken(), lightId, offState);

        final LightUtil lightUtil = Mockito.spy(new LightUtil());
        Mockito.doReturn(mockedHueBridgeGetter).when(lightUtil).getHueBridgeGetter();
        Mockito.doReturn(mockedHueBridgeSetter).when(lightUtil).getHueBridgeSetter();

        // Toggle off
        final Light newLight1 = lightUtil.toggleLight(hueConfiguration.getIP(), hueConfiguration.getToken(), lightId);
        Assert.assertEquals(lightOff, newLight1);

        // Toggle on
        final Light newLight2 = lightUtil.toggleLight(hueConfiguration.getIP(), hueConfiguration.getToken(), lightId);
        Assert.assertEquals(lightOn, newLight2);
    }
}
