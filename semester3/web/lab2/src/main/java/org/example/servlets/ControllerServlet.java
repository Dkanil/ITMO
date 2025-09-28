package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/controller")
public class ControllerServlet extends HttpServlet {
    final String JSON_ERROR = """
            { "error": "%s" }
            """;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) {
        try {
            System.out.println("----- ControllerServlet Starts -----");
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            if (req.getParameter("x") == null ||
                    req.getParameter("y") == null ||
                    req.getParameter("r") == null ||
                    req.getParameter("x").isEmpty() ||
                    req.getParameter("y").isEmpty() ||
                    req.getParameter("r").isEmpty()) {
                System.err.println("One of parameters is null or empty");
                String json = String.format(JSON_ERROR, "One of parameters is null or empty");
                res.getWriter().write(json);
            } else {
                System.out.println("All parameters are valid, forwarding to /areaCheck");
                req.getRequestDispatcher("/areaCheck").forward(req, res);
            }
            System.out.println("----- ControllerServlet Ends -----");
        } catch (IOException | ServletException e) {
            System.err.println(e.getMessage());
            String json = String.format(JSON_ERROR, e.getMessage());
            try {
                res.getWriter().write(json);
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}
