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

import com.cgm.java.hue.models.Group;
import com.cgm.java.hue.models.State;
import com.cgm.java.hue.utilities.HueBridgeGetter;
import com.cgm.java.hue.utilities.HueBridgeSetter;
import com.cgm.java.hue.utilities.HueConfiguration;
import com.cgm.java.hue.web.util.KnownParameterNames;

/**
 * This servlet will attempt to toggle a {@link com.cgm.java.hue.models.Group}'s state. That is, it will attempt to turn
 * the group ON or OFF. Ostensibly this will turn all of the lights in the given group on or off, but this needs some
 * testing.
 */
@WebServlet("/HueServlet")
public class ToggleGroupServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(ToggleGroupServlet.class);
    private static final long serialVersionUID = 2L;
    private static final HueBridgeGetter HUE_BRIDGE_GETTER = new HueBridgeGetter();
    private static final HueBridgeSetter HUE_BRIDGE_SETTER = new HueBridgeSetter();
    private static final HueConfiguration HUE_CONFIGURATION = new HueConfiguration();

    public ToggleGroupServlet() {
        super();
    }

    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        // Grab the input and get the current state of the requested group.
        final String groupId = request.getParameter(KnownParameterNames.GROUP_ID.getName());
        LOGGER.info("Attempting to toggle the group with ID: " + groupId);
        final Group group = HUE_BRIDGE_GETTER.getGroup(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), groupId);
        final State currentState = group.getAction();
        LOGGER.info("Current state: " + currentState);

        // Toggle the state, then push the state to the bridge
        final State toggledState = State.newBuilder(currentState).setOn(!currentState.getOn()).build();
        HUE_BRIDGE_SETTER.setGroupState(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), groupId, toggledState);
    }

    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
