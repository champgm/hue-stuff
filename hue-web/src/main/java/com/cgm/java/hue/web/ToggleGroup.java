package com.cgm.java.hue.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cgm.java.hue.models.Group;
import com.cgm.java.hue.models.State;
import com.cgm.java.hue.utilities.HueBridgeGetter;
import com.cgm.java.hue.utilities.HueBridgeSetter;
import com.cgm.java.hue.utilities.HueConfiguration;
import com.cgm.java.hue.web.util.KnownParameterNames;

@WebServlet("/HueServlet")
public class ToggleGroup extends HttpServlet {
    private static final long serialVersionUID = 2L;
    private static final HueBridgeGetter HUE_BRIDGE_GETTER = new HueBridgeGetter();
    private static final HueBridgeSetter HUE_BRIDGE_SETTER = new HueBridgeSetter();
    private static final HueConfiguration HUE_CONFIGURATION = new HueConfiguration();

    public ToggleGroup() {
        super();
    }

    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String groupId = request.getParameter(KnownParameterNames.GROUP_ID.getName());
        final Group group = HUE_BRIDGE_GETTER.getGroup(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), groupId);
        final State currentState = group.getAction();
        
        final State toggledState = State.newBuilder(currentState).setOn(!currentState.getOn()).build();
        HUE_BRIDGE_SETTER.setGroupState(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), groupId, toggledState);
        final Group toggledGroup = HUE_BRIDGE_GETTER.getGroup(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken(), groupId);

        request.setAttribute(KnownParameterNames.TOGGLE_GROUP_RESULT.getName(), toggledGroup);
        final RequestDispatcher view = request.getRequestDispatcher("index.jsp");
        view.forward(request, response);
    }

    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
