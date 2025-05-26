package fiskeboden.rest;

import fiskeboden.ejb.ics.Batch;
import fiskeboden.ejb.ics.PickupPoint;
import fiskeboden.ejb.ics.SupplierPickupPoint;
import fiskeboden.facade.ics.BatchFacadeLocal;
import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/batches")
public class BatchRESTServlet extends HttpServlet {

    @EJB
    private BatchFacadeLocal batchFacade;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String category = req.getParameter("category");
        String weekStr = req.getParameter("week");

        int week = 0;
        if (weekStr != null) {
            try {
                week = Integer.parseInt(weekStr);
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ogiltigt veckonummer");
                return;
            }
        }

        String pickupStr = req.getParameter("pickupPointId");
        Integer pickupPointId = null;
        if (pickupStr != null && !pickupStr.isBlank()) {
            try {
                pickupPointId = Integer.parseInt(pickupStr);
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ogiltigt pickupPointId");
                return;
            }
        }

        List<Object[]> rows = batchFacade.getBatchesWithPickupInfo(category, week, pickupPointId);

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("[");
        for (int i = 0; i < rows.size(); i++) {
            Object[] row = rows.get(i);
            Batch b = (Batch) row[0];
            SupplierPickupPoint spp = (SupplierPickupPoint) row[1];
            PickupPoint pickupPoint = spp.getPickupPoint();

            String pickupPointName = pickupPoint != null ? pickupPoint.getPickupPointName() : "";
            String pickupPointAddress = pickupPoint != null ? pickupPoint.getPickupPointAddress() : "";
            String pickupDay = mapDay(spp.getPickupDay());
            String pickupTime = spp.getPickupTime() != null ? spp.getPickupTime().toString() : "";

            out.print("  {");
            out.print("\"batchId\": " + b.getBatchId() + ", ");
            out.print("\"batchNo\": \"" + escapeJson(b.getBatchNo()) + "\", ");
            out.print("\"batchWeek\": " + b.getBatchWeek() + ", ");
            out.print("\"batchQuantity\": " + b.getBatchQuantity() + ", ");
            out.print("\"batchPrice\": " + b.getBatchPrice() + ", ");
            out.print("\"batchOrigin\": \"" + escapeJson(b.getBatchOrigin()) + "\", ");
            out.print("\"productName\": \"" + escapeJson(b.getProduct().getProductName()) + "\", ");
            out.print("\"supplierName\": \"" + escapeJson(b.getSupplier().getSupplierName()) + "\", ");
            out.print("\"isMeasuredInUnits\": " + b.getProduct().isMeasuredInUnits() + ", ");
            out.print("\"pickupPointName\": \"" + escapeJson(pickupPointName) + "\", ");
            out.print("\"pickupPointAddress\": \"" + escapeJson(pickupPointAddress) + "\", ");
            out.print("\"pickupDay\": \"" + escapeJson(pickupDay) + "\", ");
            out.print("\"pickupTime\": \"" + escapeJson(pickupTime) + "\"");
            out.print("}");
            if (i < rows.size() - 1) out.println(",");
        }
        out.println("\n]");
    }

    private String escapeJson(String s) {
        return s == null ? "" : s.replace("\"", "\\\"");
    }

    private String mapDay(byte day) {
        return switch (day) {
            case 1 -> "Måndag";
            case 2 -> "Tisdag";
            case 3 -> "Onsdag";
            case 4 -> "Torsdag";
            case 5 -> "Fredag";
            case 6 -> "Lördag";
            case 7 -> "Söndag";
            default -> "Okänd dag";
        };
    }
}
