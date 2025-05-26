package fiskeboden.rest;

import fiskeboden.ejb.ics.SupplierPickupPoint;
import fiskeboden.facade.ics.SupplierPickupPointFacadeLocal;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/supplierpickuppoints")
public class SupplierPickupPointRESTServlet extends HttpServlet {

    @EJB
    private SupplierPickupPointFacadeLocal sppFacade;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<SupplierPickupPoint> sppList = sppFacade.getAllSupplierPickupPoints();

        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("[");
        for (int i = 0; i < sppList.size(); i++) {
            SupplierPickupPoint spp = sppList.get(i);
            out.print("  {");
            out.print("\"id\":" + spp.getSupplierPickupPointId() + ",");
            out.print("\"supplierId\":" + spp.getSupplierId() + ",");
            out.print("\"pickupPointId\":" + spp.getPickupPointId() + ",");
            out.print("\"pickupDay\":" + spp.getPickupDay() + ",");
            out.print("\"pickupTime\":\"" + spp.getPickupTime() + "\"");
            out.print("}");
            if (i < sppList.size() - 1) out.println(",");
        }
        out.println("]");
    }
}