package com.cgm.java.hue.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HueServlet
 */
@WebServlet("/HueServlet")
public class HueServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * {@link HttpServlet#HttpServlet()}
     */
    public HueServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * {@link HttpServlet#doGet(HttpServletRequest, HttpServletResponse)}
     */
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    /**
     * {@link HttpServlet#doPost(HttpServletRequest, HttpServletResponse )}
     */
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
