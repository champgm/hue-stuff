package com.cgm.java.hue.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
//        writer.append("Served at: ").append(request.getContextPath());
//        writer.append("\n")
//        writer.append("Blah blah blah blah blah.");

        

    }

    /**
     * {@link HttpServlet#doPost(HttpServletRequest, HttpServletResponse )}
     */
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
