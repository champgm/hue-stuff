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
import com.cgm.java.hue.models.State;
import com.cgm.java.hue.utilities.HueBridgeGetter;
import com.cgm.java.hue.utilities.HueBridgeSetter;
import com.cgm.java.hue.utilities.HueConfiguration;
import com.cgm.java.hue.web.util.KnownParameterNames;

/**
 * This servlet will attempt to directly write an input {@link com.cgm.java.hue.models.State} to an input
 * {@link com.cgm.java.hue.models.Light} ID.
 */
@WebServlet("/HueServlet")
public class SetLightStateServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(SetLightStateServlet.class);
    private static final long serialVersionUID = 1L;
    private static final HueConfiguration HUE_CONFIGURATION = new HueConfiguration();
    private static final HueBridgeSetter HUE_BRIDGE_SETTER = new HueBridgeSetter();
    private static final HueBridgeGetter HUE_BRIDGE_GETTER = new HueBridgeGetter();

    public SetLightStateServlet() {
        super();
    }

    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        // Grab the input
        final String lightId = request.getParameter(KnownParameterNames.LIGHT_ID.getName());
        final State inputState = (State) request.getAttribute(KnownParameterNames.LIGHT_STATE.getName());
        LOGGER.info("Attempting to set state of light with ID: " + lightId);
        LOGGER.info("And state: " + inputState);

        // Attempt to write the input state to the input light ID
        HUE_BRIDGE_SETTER.setLightState(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), lightId, inputState);
        final Light updatedLight = HUE_BRIDGE_GETTER.getLight(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), lightId);

        LOGGER.info("State successfully set. Resulting state: " + updatedLight.getState());
        // Attempt to get the light with the input ID and return it
        request.setAttribute(KnownParameterNames.LIGHT.getName(), updatedLight);
        final RequestDispatcher view = request.getRequestDispatcher("index.jsp");
        view.forward(request, response);
    }

    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
