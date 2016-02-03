package com.cgm.java.hue.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.utilities.HueConfiguration;
import com.cgm.java.hue.utilities.LightUtil;
import com.cgm.java.hue.web.util.KnownParameterNames;

/**
 * This servlet will attempt to toggle a {@link com.cgm.java.hue.models.Light}'s state. That is, it will attempt to turn
 * the light ON or OFF.
 */
@WebServlet("/HueServlet")
public class ToggleLight extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(ToggleLight.class);
    private static final long serialVersionUID = 1L;
    private static final HueConfiguration HUE_CONFIGURATION = new HueConfiguration();
    private static final LightUtil LIGHT_UTIL = new LightUtil();

    public ToggleLight() {
        super();
    }

    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        // Get the input light ID, toggle it
        final String lightId = request.getParameter(KnownParameterNames.LIGHT_ID.getName());
        LOGGER.info("Attempting to toggle the light with ID: " + lightId);
        final Light toggledLight = LIGHT_UTIL.toggleLight(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), lightId);
        LOGGER.info("State successfully toggled to ON = " + toggledLight.getState().getOn());

        // Return the result
        request.setAttribute(KnownParameterNames.LIGHT.getName(), toggledLight);
        final RequestDispatcher view = request.getRequestDispatcher("index.jsp");
        view.forward(request, response);
    }

    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
