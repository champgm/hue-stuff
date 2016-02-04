package com.cgm.java.hue.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.utilities.HueBridgeGetter;
import com.cgm.java.hue.utilities.HueConfiguration;
import com.cgm.java.hue.web.util.KnownParameterNames;

/**
 * This servlet will attempt to retrieve and return all {@link com.cgm.java.hue.models.Light}s currently available on
 * the bridge
 */
@WebServlet("/HueServlet")
public class GetLights extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetLights.class);
    private static final long serialVersionUID = 1L;
    private static final HueBridgeGetter HUE_BRIDGE_GETTER = new HueBridgeGetter();
    private static final HueConfiguration HUE_CONFIGURATION = new HueConfiguration();

    public GetLights() {
        super();
    }

    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("Attempting to retrieve all lights.");
        final List<Light> list = HUE_BRIDGE_GETTER.getLights(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken());
        request.setAttribute(KnownParameterNames.LIGHT_LIST.getName(), list);
    }

    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
