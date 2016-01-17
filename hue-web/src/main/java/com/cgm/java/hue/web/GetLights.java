package com.cgm.java.hue.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.utilities.HueBridgeGetter;
import com.cgm.java.hue.utilities.HueConfiguration;

/**
 * Servlet implementation class HueServlet
 */
@WebServlet("/HueServlet")
public class GetLights extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final HueBridgeGetter HUE_BRIDGE_GETTER = new HueBridgeGetter();

    /**
     * {@link HttpServlet#HttpServlet()}
     */
    public GetLights() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * {@link HttpServlet#doGet(HttpServletRequest, HttpServletResponse)}
     */
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final PrintWriter writer = response.getWriter();
        final HueConfiguration hueConfiguration = new HueConfiguration();
        final String bridgeIP = hueConfiguration.getIP();
        final String bridgeToken = hueConfiguration.getToken();

        final List<Light> list = HUE_BRIDGE_GETTER.getLights(bridgeIP, bridgeToken);
        for (final Light light : list) {
            writer.append(light.toString()).append("\n");
        }
    }

    /**
     * {@link HttpServlet#doPost(HttpServletRequest, HttpServletResponse )}
     */
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
