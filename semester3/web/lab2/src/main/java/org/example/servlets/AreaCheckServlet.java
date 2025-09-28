package org.example.servlets;

import org.example.models.PointCords;
import org.example.models.ResultsBean;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

@WebServlet("/areaCheck")
public class AreaCheckServlet extends HttpServlet {
    final String JSON_RESPONSE = """
                { "x": %.15f,
                "y": %.15f,
                "r": %.1f,
                "hit": %s,
                "execution_time": %f,
                "timestamp": "%s" }
                """;
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

            // todo добавить формирование новой HTML страницы с результатами
            String json = String.format(Locale.US, JSON_RESPONSE,
                    point.getX(),
                    point.getY(),
                    point.getR(),
                    point.isHit(),
                    point.getExecutionTime(),
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(point.getTimestamp()));
            res.getWriter().write(json);
            System.out.println("----- AreaCheckServlet Ends -----");
        } catch (IllegalArgumentException | IOException e) {
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
