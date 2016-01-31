package com.cgm.java.hue.web;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cgm.java.hue.models.Scene;
import com.cgm.java.hue.models.State;
import com.cgm.java.hue.utilities.HueBridgeGetter;
import com.cgm.java.hue.utilities.HueBridgeSetter;
import com.cgm.java.hue.utilities.HueConfiguration;
import com.cgm.java.hue.web.util.KnownParameterNames;
import com.google.common.base.Preconditions;

@WebServlet("/HueServlet")
public class ActivateScene extends HttpServlet {
    private static final long serialVersionUID = 2L;
    private static final HueBridgeGetter HUE_BRIDGE_GETTER = new HueBridgeGetter();
    private static final HueBridgeSetter HUE_BRIDGE_SETTER = new HueBridgeSetter();

    public ActivateScene() {
        super();
    }

    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String sceneId = request.getParameter(KnownParameterNames.SCENE_ID.getName());
        final HueConfiguration hueConfiguration = new HueConfiguration();
        final String bridgeIP = hueConfiguration.getIP();
        final String bridgeToken = hueConfiguration.getToken();
        final Scene scene = HUE_BRIDGE_GETTER.getScene(bridgeIP, bridgeToken, sceneId);

        final ArrayList<CharSequence> lightIds = new ArrayList<>(scene.getLights());
        final ArrayList<State> lightStates = new ArrayList<>(scene.getLightstates());

        Preconditions.checkState(lightIds.size() == lightStates.size(), "The number of retrieved light IDs and light states do not match for the requested scene: \n" + scene);
        for (int i = 0; i < lightIds.size(); i++) {
            HUE_BRIDGE_SETTER.setLightState(bridgeIP, bridgeToken, lightIds.get(i).toString(), lightStates.get(i));
        }

        request.setAttribute(KnownParameterNames.SCENE.getName(), scene);
        final RequestDispatcher view = request.getRequestDispatcher("index.jsp");
        view.forward(request, response);
    }

    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
