package fiskeboden.rest;

import fiskeboden.ejb.ics.PickupPoint;
import fiskeboden.facade.ics.PickupPointFacadeLocal;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/pickup-points")
public class PickupPointRESTServlet extends HttpServlet {

    @EJB
    private PickupPointFacadeLocal pickupPointFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<PickupPoint> points = pickupPointFacade.getAllPickupPoints();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print("[");
        for (int i = 0; i < points.size(); i++) {
            PickupPoint p = points.get(i);
            out.print("{");
            out.print("\"pickupPointId\":" + p.getPickupPointId() + ",");
            out.print("\"pickupPointNo\":\"" + escapeJson(p.getPickupPointNo()) + "\",");
            out.print("\"pickupPointName\":\"" + escapeJson(p.getPickupPointName()) + "\",");
            out.print("\"pickupPointAddress\":\"" + escapeJson(p.getPickupPointAddress()) + "\"");
            out.print("}");
            if (i < points.size() - 1) out.print(",");
        }
        out.print("]");
        out.flush();
    }

    private String escapeJson(String input) {
        return input == null ? "" : input.replace("\"", "\\\"").replace("\n", "").replace("\r", "");
    }
}