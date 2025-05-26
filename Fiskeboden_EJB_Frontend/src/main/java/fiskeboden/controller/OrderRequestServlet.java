package fiskeboden.controller;

import fiskeboden.ejb.ics.Batch;
import fiskeboden.ejb.ics.OrderBatch;
import fiskeboden.ejb.ics.OrderRequest;
import fiskeboden.ejb.ics.OrderStatus;
import fiskeboden.facade.ics.BatchFacadeLocal;
import fiskeboden.facade.ics.OrderBatchFacadeLocal;
import fiskeboden.facade.ics.OrderRequestFacadeLocal;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@WebServlet("/manageOrders")
public class OrderRequestServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB
    private OrderRequestFacadeLocal orderRequestFacade;

    @EJB
    private OrderBatchFacadeLocal orderBatchFacade;

    @EJB
    private BatchFacadeLocal batchFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
        List<OrderRequest> orderRequests = orderRequestFacade.getAllOrderRequests();

        Map<OrderRequest, List<OrderBatch>> orderBatchMap = new HashMap<>();
        for (OrderRequest orderRequest : orderRequests) {
            List<OrderBatch> orderBatches = new ArrayList<>(orderRequest.getOrderBatches());
            orderBatchMap.put(orderRequest, orderBatches);
        }
        
        // Sort the orders of the orderBatchMap by orderNo
        List<Map.Entry<OrderRequest, List<OrderBatch>>> sortedEntries = new ArrayList<>(orderBatchMap.entrySet());
        sortedEntries.sort(Comparator.comparing(entry -> entry.getKey().getOrderNo()));
        
        // Construct a new map with sorted entries
        Map<OrderRequest, List<OrderBatch>> sortedOrderBatchMap = new LinkedHashMap<>();
        for (Map.Entry<OrderRequest, List<OrderBatch>> entry : sortedEntries) {
				sortedOrderBatchMap.put(entry.getKey(), entry.getValue());
		}

        request.setAttribute("orderBatchMap", sortedOrderBatchMap);


        } catch (Exception e) {

            request.setAttribute("errorMessage", "Kunde inte hämta orderförfrågningar eller partiinformation: " + e.getMessage());
        }

        request.getRequestDispatcher("/WEB-INF/jsp/orderManagement.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String actionType = request.getParameter("actionType");
        String orderIdStr = request.getParameter("orderId");
        String batchIdStr = request.getParameter("batchId");

        try {
            // Validate Order ID
            if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Order ID saknas.");
            }
            int orderId = Integer.parseInt(orderIdStr);

            // Handle Accept Action
            if ("accept".equals(action)) {
                if ("order".equals(actionType)) {
                    orderRequestFacade.approveOrderRequest(orderId);
                    request.getSession().setAttribute("successMessage", "Order har godkänts.");

                } else if ("batch".equals(actionType)) {
                    if (batchIdStr == null || batchIdStr.trim().isEmpty()) {
                        throw new IllegalArgumentException("Parti ID saknas.");
                    }
                    int batchId = Integer.parseInt(batchIdStr);
                    OrderBatch orderBatch = orderBatchFacade.getOrderBatchById(orderId, batchId);
                    if (orderBatch == null) {
                        throw new IllegalArgumentException("Parti ID " + batchId + " hittades inte för order ID " + orderId);
                    }

                    orderBatch.setOrderStatus(OrderStatus.APPROVED);
                    orderBatchFacade.updateOrderBatch(orderBatch);
                    Batch batch = batchFacade.getBatchById(batchId);
                    OrderRequest order = orderRequestFacade.findOrderById(orderId);
                    request.getSession().setAttribute("successMessage", "Partinummer " + batch.getBatchNo() + " har godkänts för order " + order.getOrderNo());
                } else {
                    throw new IllegalArgumentException("Ogiltig åtgärd: " + actionType);
                }

            } else if ("deny".equals(action)) {
                if ("batch".equals(actionType)) {
                    if (batchIdStr == null || batchIdStr.trim().isEmpty()) {
                        throw new IllegalArgumentException("Parti ID saknas.");
                    }

                    int batchId = Integer.parseInt(batchIdStr);
                    orderBatchFacade.denyOrderBatch(orderId, batchId);

                    Batch batch = batchFacade.getBatchById(batchId);
                    OrderRequest order = orderRequestFacade.findOrderById(orderId);
                    request.getSession().setAttribute("successMessage", "Partinummer " + batch.getBatchNo() + " har nekats i order " + order.getOrderNo());

                } else if ("order".equals(actionType)) {
                    orderRequestFacade.denyOrderRequest(orderId);
                    request.getSession().setAttribute("successMessage", "Order har nekats.");
                } else {
                    throw new IllegalArgumentException("Ogiltig åtgärd: " + actionType);
                }
            } else {
                throw new IllegalArgumentException("Ogiltig åtgärd: " + action);
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Ogiltigt Order ID format.");
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
        } catch (IllegalStateException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Ett fel inträffade: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/manageOrders");
    }
}


