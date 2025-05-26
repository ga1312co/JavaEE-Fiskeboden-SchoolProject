package fiskeboden.rest;

import fiskeboden.ejb.ics.OrderBatch;
import fiskeboden.ejb.ics.OrderRequest;
import fiskeboden.ejb.ics.OrderStatus;
import fiskeboden.ejb.ics.Supplier;
import fiskeboden.ejb.ics.SupplierPickupPoint;
import fiskeboden.facade.ics.OrderRequestFacadeLocal;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet("/api/orderrequests")
public class OrderRequestRESTServlet extends HttpServlet {

    @EJB
    private OrderRequestFacadeLocal orderRequestFacade;
    

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

    	resp.setContentType("application/json; charset=UTF-8");;
        PrintWriter out = resp.getWriter();

        String orderIdParam = req.getParameter("orderId");

        if (orderIdParam != null) {
            try {
                int orderId = Integer.parseInt(orderIdParam);
                OrderRequest orderRequest = orderRequestFacade.getOrderRequestById(orderId);

                if (orderRequest == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"Order saknas.\"}");
                    return;
                }

                Map<Supplier, List<OrderBatch>> grouped = orderRequestFacade.getOrderBatchesGroupedBySupplier(orderId);

                StringBuilder json = new StringBuilder();
                json.append("{");
                json.append("\"orderId\":").append(orderRequest.getOrderId()).append(",");
                json.append("\"orderStatus\":\"").append(orderRequest.getOrderStatus().name()).append("\",");
                json.append("\"supplierGroups\":[");

                int supplierIndex = 0;
                for (Map.Entry<Supplier, List<OrderBatch>> entry : grouped.entrySet()) {
                    Supplier supplier = entry.getKey();
                    List<OrderBatch> batches = entry.getValue();

                    SupplierPickupPoint spp = supplier.getSupplierPickupPoints().isEmpty()
                        ? null
                        : supplier.getSupplierPickupPoints().get(0);

                    json.append("{")
                        .append("\"supplierName\":\"").append(escapeJson(supplier.getSupplierName())).append("\",");

                    if (spp != null && spp.getPickupPoint() != null) {
                        json.append("\"pickupDay\":\"").append(mapDay(spp.getPickupDay())).append("\",")
                            .append("\"pickupTime\":\"").append(spp.getPickupTime()).append("\",")
                            .append("\"pickupPointName\":\"").append(escapeJson(spp.getPickupPoint().getPickupPointName())).append("\",")
                            .append("\"pickupPointAddress\":\"").append(escapeJson(spp.getPickupPoint().getPickupPointAddress())).append("\",");
                    }

                    json.append("\"items\":[");
                    for (int i = 0; i < batches.size(); i++) {
                        OrderBatch ob = batches.get(i);
                        var batch = ob.getBatch();
                        var product = batch.getProduct();

                        json.append("{")
                            .append("\"productName\":\"").append(escapeJson(product.getProductName())).append("\",")
                            .append("\"isMeasuredInUnits\":").append(product.isMeasuredInUnits()).append(",")
                            .append("\"price\":").append(batch.getBatchPrice()).append(",")
                            .append("\"quantity\":").append(ob.getOrderQuantity())
                            .append("}");

                        if (i < batches.size() - 1) json.append(",");
                    }
                    json.append("]}");

                    if (++supplierIndex < grouped.size()) json.append(",");
                }

                json.append("]}");
                out.print(json.toString());

            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"orderId måste vara en siffra.\"}");
            }
            return;
        }

        // Om inget orderId ges: hämta alla ordrar
        List<OrderRequest> requests = orderRequestFacade.getAllOrderRequests();

        StringBuilder json = new StringBuilder();
        json.append("[");

        for (int i = 0; i < requests.size(); i++) {
            OrderRequest or = requests.get(i);
            json.append("{")
                .append("\"orderId\":").append(or.getOrderId()).append(",")
                .append("\"orderNo\":\"").append(escapeJson(or.getOrderNo())).append("\",")
                .append("\"customerName\":\"").append(escapeJson(or.getOrderCustomerName())).append("\",")
                .append("\"orderDate\":\"").append(or.getOrderDate()).append("\",")
                .append("\"orderStatus\":\"").append(or.getOrderStatus()).append("\"")
                .append("}");

            if (i < requests.size() - 1) json.append(",");
        }

        json.append("]");
        out.print(json.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String customerName = request.getParameter("customerName");
            int batchId = Integer.parseInt(request.getParameter("batchId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            OrderRequest newOrder = orderRequestFacade.createOrder(batchId, quantity, customerName, OrderStatus.PENDING);

            out.print("{");
            out.print("\"orderId\":" + newOrder.getOrderId() + ",");
            out.print("\"orderNo\":\"" + escapeJson(newOrder.getOrderNo()) + "\",");
            out.print("\"orderStatus\":\"" + newOrder.getOrderStatus().name() + "\"");
            out.print("}");

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("{\"error\":\"Serverfel vid orderregistrering.\"}");
            e.printStackTrace();
        }
    }

    private String escapeJson(String input) {
        return input == null ? "" : input.replace("\"", "\\\"");
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
