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

import com.cgm.java.hue.models.Schedule;
import com.cgm.java.hue.utilities.HueBridgeGetter;
import com.cgm.java.hue.utilities.HueConfiguration;
import com.cgm.java.hue.web.util.KnownParameterNames;
import com.google.gson.Gson;

/**
 * This servlet will attempt to retrieve and return all {@link com.cgm.java.hue.models.Scene}s currently available on
 * the bridge
 */
@WebServlet("/HueServlet")
public class GetSchedulesServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetSchedulesServlet.class);
    private static final long serialVersionUID = 2L;
    private static final HueBridgeGetter HUE_BRIDGE_GETTER = new HueBridgeGetter();
    private static final HueConfiguration HUE_CONFIGURATION = new HueConfiguration();

    public GetSchedulesServlet() {
        super();
    }

    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("Attempting to retrieve all schedules.");
        final List<Schedule> list = HUE_BRIDGE_GETTER.getSchedules(HUE_CONFIGURATION.getIP(), HUE_CONFIGURATION.getToken());
        LOGGER.info("Retrieved schedules: " + list);
        
        final String scheduleListJson = new Gson().toJson(list);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        response.getWriter().write(scheduleListJson);
    }

    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
