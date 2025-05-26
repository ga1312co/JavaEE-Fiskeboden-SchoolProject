package fiskeboden.rest;

import fiskeboden.ejb.ics.OrderBatch;
import fiskeboden.ejb.ics.OrderBatchId;
import fiskeboden.ejb.ics.OrderStatus;
import fiskeboden.facade.ics.OrderBatchFacadeLocal;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/api/orderbatches")
public class OrderBatchRESTServlet extends HttpServlet {

    @EJB
    private OrderBatchFacadeLocal orderBatchFacade;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        List<OrderBatch> batches = orderBatchFacade.getAllOrderBatches();

        StringBuilder json = new StringBuilder();
        json.append("[");

        for (int i = 0; i < batches.size(); i++) {
            OrderBatch ob = batches.get(i);
            json.append("{")
                .append("\"orderId\":").append(ob.getId().getOrderId()).append(",")
                .append("\"batchId\":").append(ob.getId().getBatchId()).append(",")
                .append("\"orderStatus\":\"").append(ob.getOrderStatus()).append("\",")
                .append("\"quantity\":").append(ob.getOrderQuantity())
                .append("}");

            if (i < batches.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");
        out.print(json.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String action = request.getParameter("action");

            if ("create".equalsIgnoreCase(action)) {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                int batchId = Integer.parseInt(request.getParameter("batchId"));
                String quantityStr = request.getParameter("orderQuantity");

                if (quantityStr == null || quantityStr.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\":\"Saknar orderQuantity\"}");
                    return;
                }

                BigDecimal quantity = new BigDecimal(quantityStr);

                // Kontrollera om OrderBatch redan finns
                OrderBatch existing = orderBatchFacade.getOrderBatchById(orderId, batchId);

                if (existing != null) {
                    BigDecimal newQty = existing.getOrderQuantity().add(quantity);
                    existing.setOrderQuantity(newQty);
                    orderBatchFacade.updateOrderBatch(existing);
                    out.print("{\"message\":\"OrderBatch uppdaterad (existerade redan).\"}");
                } else {
                    OrderBatch ob = new OrderBatch();
                    ob.setId(new OrderBatchId(orderId, batchId));
                    ob.setOrderQuantity(quantity);
                    ob.setOrderStatus(OrderStatus.PENDING);
                    orderBatchFacade.createOrderBatch(ob);
                    out.print("{\"message\":\"OrderBatch skapad.\"}");
                }

            } else if ("deny".equalsIgnoreCase(action)) {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                int batchId = Integer.parseInt(request.getParameter("batchId"));

                orderBatchFacade.denyOrderBatch(orderId, batchId);
                out.print("{\"message\":\"OrderBatch nekad.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Ogiltig åtgärd.\"}");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"orderId och batchId måste vara siffror.\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"Fel vid skapande av OrderBatch.\"}");
            e.printStackTrace();
        }
    }
}
