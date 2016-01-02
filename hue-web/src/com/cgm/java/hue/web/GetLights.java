package com.cgm.java.hue.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

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
        // writer.append("Served at: ").append(request.getContextPath());
        // writer.append("\n")
        final HueConfiguration hueConfiguration = new HueConfiguration();
        final String bridgeIP = hueConfiguration.getIP();
        writer.append("IP: ").append(bridgeIP).append("/n");
        final String bridgeToken = hueConfiguration.getToken();
        writer.append("API token: ").append(bridgeToken).append("/n");

        final ArrayList<Light> list = HueBridgeGetter.getLights(bridgeIP, bridgeToken);
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
