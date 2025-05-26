package fiskeboden.rest;

import fiskeboden.facade.ics.StatisticsFacadeLocal;
	
import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet("/api/statistics")
public class StatisticsRESTServlet extends HttpServlet {

    @EJB
    private StatisticsFacadeLocal statisticsFacade;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        try (PrintWriter out = resp.getWriter()) {

            Long totalOrders = Optional.ofNullable(statisticsFacade.getTotalOrders()).orElse(0L);
            Long totalUnits = Optional.ofNullable(statisticsFacade.getTotalUnitsSold()).orElse(0L);
            Double totalKg = Optional.ofNullable(statisticsFacade.getTotalKgSold()).orElse(0.0);
            Long totalSuppliers = Optional.ofNullable(statisticsFacade.getTotalSuppliers()).orElse(0L);
            Long totalProducts = Optional.ofNullable(statisticsFacade.getTotalProducts()).orElse(0L);
            Long totalCategories = Optional.ofNullable(statisticsFacade.getTotalCategories()).orElse(0L);
            Long totalPickupPoints = Optional.ofNullable(statisticsFacade.getTotalPickupPoints()).orElse(0L);
            String mostSoldProduct = Optional.ofNullable(statisticsFacade.getMostSoldProduct()).orElse("Okänd");

            // OBS: Locale.US för att få punkt som decimalseparator i JSON
            out.printf(java.util.Locale.US, "{\n" +
                    "  \"totalOrders\": %d,\n" +
                    "  \"totalUnitsSold\": %d,\n" +
                    "  \"totalKgSold\": %.2f,\n" +
                    "  \"totalSuppliers\": %d,\n" +
                    "  \"totalProducts\": %d,\n" +
                    "  \"totalCategories\": %d,\n" +
                    "  \"totalPickupPoints\": %d,\n" +
                    "  \"mostSoldProduct\": \"%s\"\n" +
                    "}", totalOrders, totalUnits, totalKg, totalSuppliers, totalProducts, totalCategories, totalPickupPoints, escapeJson(mostSoldProduct));

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(500, "Fel i statistikservlet: " + e.getMessage());
        }
    }


    private String escapeJson(String s) {
        return s == null ? "" : s.replace("\"", "\\\"");
    }
}

