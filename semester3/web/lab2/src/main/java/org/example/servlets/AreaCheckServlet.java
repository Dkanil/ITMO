package org.example.servlets;

import jakarta.servlet.ServletException;
import org.example.models.PointCords;
import org.example.models.ResultsBean;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/areaCheck")
public class AreaCheckServlet extends HttpServlet {
    final String JSON_ERROR = """
            { "error": "%s" }
            """;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) {
        try {
            System.out.println("----- AreaCheckServlet Starts -----");
            ResultsBean resultsBean = (ResultsBean) req.getSession().getAttribute("resultsBean");
            if (resultsBean == null) {
                resultsBean = new ResultsBean();
                req.getSession().setAttribute("resultsBean", resultsBean);
                System.out.println("Created resultsBean: " + resultsBean);
            }
            else {
                System.out.println("resultsBean already exists: " + resultsBean);
            }
            Long startTime = System.nanoTime();
            PointCords point = new PointCords(req.getQueryString());
            System.out.println("Processed point: " + point);
            resultsBean.addResult(point);
            Long endTime = System.nanoTime();
            point.setExecutionTime((endTime - startTime) / 1_000_000.0); // время в миллисекундах

            req.getRequestDispatcher("/result.jsp").forward(req, res);
            System.out.println("----- AreaCheckServlet Ends -----");
        } catch (IllegalArgumentException | IOException e) {
            System.err.println(e.getMessage());
            String json = String.format(JSON_ERROR, e.getMessage());
            try {
                res.getWriter().write(json);
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        } catch (ServletException e) {
            System.err.println(e.getMessage());
        }
    }
}
