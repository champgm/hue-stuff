package com.cgm.java.hue.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cgm.java.hue.models.Scene;
import com.cgm.java.hue.utilities.HueBridgeGetter;
import com.cgm.java.hue.utilities.HueConfiguration;

@WebServlet("/HueServlet")
public class GetScenes extends HttpServlet {
    private static final long serialVersionUID = 2L;
    private static final HueBridgeGetter HUE_BRIDGE_GETTER = new HueBridgeGetter();

    public GetScenes() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final PrintWriter writer = response.getWriter();
        final HueConfiguration hueConfiguration = new HueConfiguration();
        final String bridgeIP = hueConfiguration.getIP();
        final String bridgeToken = hueConfiguration.getToken();

        final List<Scene> list = HUE_BRIDGE_GETTER.getScenes(bridgeIP, bridgeToken);
        for (final Scene light : list) {
            writer.append(light.toString()).append("\n");
        }
    }

    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}
