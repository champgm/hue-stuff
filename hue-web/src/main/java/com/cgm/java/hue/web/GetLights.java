package com.cgm.java.hue.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.utilities.HueBridgeGetter;
import com.cgm.java.hue.utilities.HueConfiguration;
import com.cgm.java.hue.web.util.KnownParameterNames;

@WebServlet("/HueServlet")
public class GetLights extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final HueBridgeGetter HUE_BRIDGE_GETTER = new HueBridgeGetter();
    private static final HueConfiguration HUE_CONFIGURATION = new HueConfiguration();

    public GetLights() {
        super();
    }

    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        final String bridgeIP = HUE_CONFIGURATION.getIP();
        final String bridgeToken = HUE_CONFIGURATION.getToken();

        final List<Light> list = HUE_BRIDGE_GETTER.getLights(bridgeIP, bridgeToken);
        request.setAttribute(KnownParameterNames.LIGHT_LIST.getName(), list);
    }

    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
