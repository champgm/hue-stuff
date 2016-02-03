package com.cgm.java.hue.web;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgm.java.hue.models.Scene;
import com.cgm.java.hue.models.State;
import com.cgm.java.hue.utilities.HueBridgeGetter;
import com.cgm.java.hue.utilities.HueBridgeSetter;
import com.cgm.java.hue.utilities.HueConfiguration;
import com.cgm.java.hue.web.util.KnownParameterNames;
import com.google.common.base.Preconditions;

/**
 * This servlet will attempt to retrieve a {@link com.cgm.java.hue.models.Scene}, all of the
 * {@link com.cgm.java.hue.models.Light}s assigned to that scene, and then update each
 * {@link com.cgm.java.hue.models.Light} with the corresponding {@link com.cgm.java.hue.models.State} found in the
 * {@link com.cgm.java.hue.models.Scene}
 */
@WebServlet("/HueServlet")
public class ActivateScene extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivateScene.class);
    private static final long serialVersionUID = 2L;
    private static final HueBridgeGetter HUE_BRIDGE_GETTER = new HueBridgeGetter();
    private static final HueBridgeSetter HUE_BRIDGE_SETTER = new HueBridgeSetter();
    private static final HueConfiguration HUE_CONFIGURATION = new HueConfiguration();;

    public ActivateScene() {
        super();
    }

    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        // Grab the input scene ID and retrieve that scene from the bridge
        final String sceneId = request.getParameter(KnownParameterNames.SCENE_ID.getName());
        LOGGER.info("Attempting to activate scene with ID: " + sceneId);

        // Get the lights and their states from the scene
        final Scene scene = HUE_BRIDGE_GETTER.getScene(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), sceneId);
        final ArrayList<CharSequence> lightIds = new ArrayList<>(scene.getLights());
        final ArrayList<State> lightStates = new ArrayList<>(scene.getLightstates());

        // Iterate through each light and set its state to what the scene says it should be
        Preconditions.checkState(lightIds.size() == lightStates.size(), "The number of retrieved light IDs and light states do not match for the requested scene: \n" + scene);
        for (int i = 0; i < lightIds.size(); i++) {
            LOGGER.info("Attempting to activate one light with ID: " + lightIds.get(i));
            HUE_BRIDGE_SETTER.setLightState(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), lightIds.get(i).toString(), lightStates.get(i));
        }

        // Return the scene
        LOGGER.info("Successfully activated scene: " + scene);
        request.setAttribute(KnownParameterNames.SCENE.getName(), scene);
        final RequestDispatcher view = request.getRequestDispatcher("index.jsp");
        view.forward(request, response);
    }

    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
