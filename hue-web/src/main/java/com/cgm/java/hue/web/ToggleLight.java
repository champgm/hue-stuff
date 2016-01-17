package com.cgm.java.hue.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cgm.java.hue.utilities.LightUtil;
import com.cgm.java.hue.web.util.KnownParameterNames;

/**
 * Servlet implementation class HueServlet
 */
@WebServlet("/HueServlet")
public class ToggleLight extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * {@link javax.servlet.http.HttpServlet#HttpServlet()}
     */
    public ToggleLight() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * {@link javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     */
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String lightId = request.getParameter(KnownParameterNames.LIGHT_ID.getName());

        final String result = LightUtil.toggleLight(lightId);

        response.getWriter().write(result);
    }

    /**
     * {@link javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse )}
     */
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
