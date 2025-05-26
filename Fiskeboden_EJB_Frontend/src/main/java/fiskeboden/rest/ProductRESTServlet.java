package fiskeboden.rest;

import fiskeboden.ejb.ics.Product;
import fiskeboden.facade.ics.ProductFacadeLocal;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/products")
public class ProductRESTServlet extends HttpServlet {

    @EJB
    private ProductFacadeLocal productFacade;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            List<Product> products = productFacade.getAllProducts();

            StringBuilder json = new StringBuilder();
            json.append("[");
            for (int i = 0; i < products.size(); i++) {
                Product p = products.get(i);
                json.append("{")
                    .append("\"productId\":").append(p.getProductId()).append(",")
                    .append("\"productNo\":\"").append(escapeJson(p.getProductNo())).append("\",")
                    .append("\"productName\":\"").append(escapeJson(p.getProductName())).append("\",")
                    .append("\"isMeasuredInUnits\":").append(p.isMeasuredInUnits()).append(",")
                    .append("\"categoryId\":").append(p.getCategory())
                    .append("}");
                if (i < products.size() - 1) {
                    json.append(",");
                }
            }
            json.append("]");

            out.print(json.toString());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Kunde inte hämta produkter.\"}");
        } finally {
            out.flush();
        }
    }

    // Enkel utility för att escapa JSON-strängar
    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}