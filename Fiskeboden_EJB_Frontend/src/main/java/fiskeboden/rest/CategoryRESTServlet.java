package fiskeboden.rest;

import fiskeboden.ejb.ics.Category;
import fiskeboden.facade.ics.CategoryFacadeLocal;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/categories")
public class CategoryRESTServlet extends HttpServlet {

    @EJB
    private CategoryFacadeLocal categoryFacade;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        List<Category> categories = categoryFacade.getAllCategories();

        StringBuilder json = new StringBuilder();
        json.append("[");

        for (int i = 0; i < categories.size(); i++) {
            Category c = categories.get(i);
            json.append("{")
                .append("\"id\":").append(c.getCategoryId()).append(",")
                .append("\"no\":\"").append(escapeJson(c.getCategoryNo())).append("\",")
                .append("\"name\":\"").append(escapeJson(c.getCategoryName())).append("\"")
                .append("}");

            if (i < categories.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");
        out.print(json.toString());
    }

    private String escapeJson(String input) {
        return input == null ? "" : input.replace("\"", "\\\"");
    }
}