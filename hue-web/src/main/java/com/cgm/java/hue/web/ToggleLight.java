package com.cgm.java.hue.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cgm.java.hue.models.Light;
import com.cgm.java.hue.utilities.LightUtil;
import com.cgm.java.hue.web.util.KnownParameterNames;

@WebServlet("/HueServlet")
public class ToggleLight extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ToggleLight() {
        super();
    }

    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String lightId = request.getParameter(KnownParameterNames.LIGHT_ID.getName());
        final Light result = LightUtil.toggleLight(lightId);
        
        request.setAttribute(KnownParameterNames.LIGHT.getName(), result);
        RequestDispatcher view = request.getRequestDispatcher("index.jsp");
		view.forward(request, response);
    }

    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}