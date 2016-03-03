package com.cgm.java.hue.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.models.Scene;
import com.cgm.java.hue.utilities.HueBridgeGetter;
import com.cgm.java.hue.utilities.HueConfiguration;
import com.cgm.java.hue.utilities.SceneUtil;
import com.cgm.java.hue.web.util.KnownParameterNames;
import com.google.common.collect.ImmutableList;

/**
 * This servlet will attempt to retrieve and return all {@link com.cgm.java.hue.models.Scene}s currently available on
 * the bridge
 */
@WebServlet("/HueServlet")
public class GetScenesServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetScenesServlet.class);
    private static final long serialVersionUID = 2L;
    private static final HueBridgeGetter HUE_BRIDGE_GETTER = new HueBridgeGetter();
    private static final HueConfiguration HUE_CONFIGURATION = new HueConfiguration();

    public GetScenesServlet() {
        super();
    }

    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final Boolean onlyV2 = Boolean.valueOf(request.getParameter(KnownParameterNames.SCENE_V2.getName()));

        LOGGER.info("Attempting to retrieve all scenes.");
        final List<Scene> allScenes = HUE_BRIDGE_GETTER.getScenes(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), onlyV2);
        LOGGER.info("Retrieved scenes: " + allScenes);

        LOGGER.info("Attempting to retrieve all lights.");
        final List<Light> allLights = HUE_BRIDGE_GETTER.getLights(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken());
        LOGGER.info("Retrieved lights: " + allLights);

        final ImmutableList<String> activeSceneIds = SceneUtil.determineActiveScenes(allScenes, allLights);
        request.setAttribute(KnownParameterNames.ACTIVE_SCENE.getName(), activeSceneIds);
        request.setAttribute(KnownParameterNames.SCENE_LIST.getName(), allScenes);
    }

    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
