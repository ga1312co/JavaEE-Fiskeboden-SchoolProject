package fiskeboden.rest;

import fiskeboden.ejb.ics.Supplier;
import fiskeboden.facade.ics.SupplierFacadeLocal;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/suppliers")
public class SupplierRESTServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB
    private SupplierFacadeLocal supplierFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Supplier> suppliers = supplierFacade.getAllSuppliers();

        StringBuilder json = new StringBuilder();
        json.append("[");

        for (int i = 0; i < suppliers.size(); i++) {
            Supplier s = suppliers.get(i);
            json.append("{")
                .append("\"id\":").append(s.getSupplierId()).append(",")
                .append("\"no\":\"").append(escapeJson(s.getSupplierNo())).append("\",")
                .append("\"name\":\"").append(escapeJson(s.getSupplierName())).append("\",")
                .append("\"email\":\"").append(escapeJson(s.getSupplierEmail())).append("\",")
                .append("\"phone\":\"").append(escapeJson(s.getSupplierPhoneNumber())).append("\",")
                .append("\"location\":\"").append(escapeJson(s.getSupplierLocation())).append("\",")
                .append("\"swish\":\"").append(escapeJson(s.getSupplierSwishNumber())).append("\"")
                .append("}");

            if (i < suppliers.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }

    private String escapeJson(String s) {
        return s == null ? "" : s.replace("\"", "\\\"");
    }
}