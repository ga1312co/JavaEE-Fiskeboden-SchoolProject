<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<header style="text-align: center; margin-bottom: 20px;"></header>

<main class="container">
    <img src="${pageContext.request.contextPath}/images/fiskebod_logo.png" alt="Fiskeboden Logo" style="max-width: 200px; height: auto; filter: invert(1)">
    <h2>Orderförfrågningar</h2>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">

    <c:if test="${not empty sessionScope.successMessage}">
        <div class="message success-message">
            <p><c:out value="${sessionScope.successMessage}" /></p>
        </div>
        <c:remove var="successMessage" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="message error-message">
            <p><c:out value="${sessionScope.errorMessage}" /></p>
        </div>
        <c:remove var="errorMessage" scope="session"/>
    </c:if>

    <c:if test="${not empty requestScope.errorMessage}">
        <div class="message error-message">
            <p><c:out value="${requestScope.errorMessage}" /></p>
        </div>
    </c:if>
    
    
    <div class="tab-container">
    	<button class="tab-button active" onclick="showTab('pending')">Ohanterade Orderförfrågningar</button>
    	<button class="tab-button" onclick="showTab('approved')">Godkända Orderförfrågningar</button>
    	<button class="tab-button" onclick="showTab('denied')">Nekade Orderförfrågningar</button>
    </div>

    <!-- Pending Orders tab -->
	<div id="pending" class="order-cards">
	    <c:choose>
	        <c:when test="${not empty orderBatchMap}">
	            <!-- Pending Orders tab - UI Elements shown above batchCards --> 
	            <c:forEach var="entry" items="${orderBatchMap}">
	                <c:if test="${entry.key.orderStatus == 'PENDING'}">
	                    <div class="order-card collapsed" data-order-id="${entry.key.orderId}">
	                        <h3>Ordernummer: <c:out value="${entry.key.orderNo}"/></h3>
	                        <p>Kundnamn: <c:out value="${entry.key.orderCustomerName}"/></p>
	                        <p>
	                            Orderdatum:
	                            <fmt:parseDate value="${entry.key.orderDate}" pattern="yyyy-MM-dd" var="parsedDate" />
	                            <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd" />
	                        </p>
	                        <p>Orderstatus:
	                            <span class="status ${entry.key.orderStatus == 'APPROVED' ? 'approved' : entry.key.orderStatus == 'PENDING' ? 'pending' : 'denied'}">
	                                <c:out value="${entry.key.orderStatus}"/>
	                            </span>
	                        </p>
	                        <c:set var="totalPrice" value="0" />
	                        <c:forEach var="batch" items="${entry.value}">
	                            <c:set var="totalPrice" value="${totalPrice + (batch.orderQuantity * batch.batch.batchPrice)}" />
	                        </c:forEach>
	                        <p><strong>Totalt pris:</strong> <fmt:formatNumber value="${totalPrice}" type="currency" currencySymbol="kr" /></p>
	                        
	                        <button type="button" class="toggle-button">Visa Partier</button>
	                        
	                        <!-- Pending Batches -->
	                        <div class="batch-cards pending-batch-cards">
	                            <c:forEach var="batch" items="${entry.value}">
	                                <div class="batch-card">
	                                    <p>Produktnamn: <c:out value="${batch.batch.product.productName}" /></p>
	                                    <p>Partinummer: <c:out value="${batch.batch.batchNo}"/></p>
	                                    <p>Parti Ursprung: <c:out value="${batch.batch.batchOrigin}"/></p>
	                                    <p>
	                                        Parti Lagersaldo:
	                                        <c:choose>
	                                            <c:when test="${batch.batch.product.measuredInUnits}">
	                                                <c:out value="${batch.batch.batchQuantity}" /> st
	                                            </c:when>
	                                            <c:otherwise>
	                                                <c:out value="${batch.batch.batchQuantity}" /> kg
	                                            </c:otherwise>
	                                        </c:choose>
	                                    </p>
	                                    <p>
	                                        Beställd Kvantitet:
	                                        <c:choose>
	                                            <c:when test="${batch.batch.product.measuredInUnits}">
	                                                <c:out value="${batch.orderQuantity}" /> st
	                                            </c:when>
	                                            <c:otherwise>
	                                                <c:out value="${batch.orderQuantity}" /> kg
	                                            </c:otherwise>
	                                        </c:choose>
	                                    </p>
	                                    <p>Parti Styckpris: <fmt:formatNumber value="${batch.batch.batchPrice}" type="currency" currencySymbol="kr" /></p>
	                                    <p>Totalt Pris: <fmt:formatNumber value="${batch.orderQuantity * batch.batch.batchPrice}" type="currency" currencySymbol="kr" /></p>
	                                    <p>Status: 
	                                        <span class="status ${batch.orderStatus == 'APPROVED' ? 'approved' : batch.orderStatus == 'PENDING' ? 'pending' : 'denied'}">
	                                            <c:out value="${batch.orderStatus}"/>
	                                        </span>
	                                    </p>

	                                    <form action="${pageContext.request.contextPath}/manageOrders" method="POST" style="display: inline;">	                                        
											<input type="hidden" name="orderId" value="${entry.key.orderId}">
											<input type="hidden" name="batchId" value="${batch.batch.batchId}">
											<input type="hidden" name="actionType" value="batch">
											
	                                        <button type="submit" name="action" value="accept" class="button accept-button">OK</button>
	                                        <button type="submit" name="action" value="deny" class="button deny-button"
	                                                onclick="return confirm('Är du säker på att du vill neka detta parti (${batch.batch.batchNo})?');">Neka</button>
	                                    </form>
	                                </div>
	                            </c:forEach>
	                        </div>
	                        
	                        <!-- Pending Orders - UI shown below batchCards -->
							
							<!-- Calculate nbr of pending and approved batches -->
	                        <c:set var="pendingCount" value="0"/> 
	                        <c:set var="approvedBatchCount" value="0" />
	                        <c:forEach var="batch" items="${entry.value}">
	                            <c:if test="${batch.orderStatus != 'APPROVED' && batch.orderStatus != 'DENIED'}">
	                                <c:set var="pendingCount" value="${pendingCount + 1}"/>
	                            </c:if>
	                            
	                            <c:if test="${batch.orderStatus == 'APPROVED'}">
	                            	<c:set var="approvedBatchCount" value="${approvedBatchCount + 1}" />
	                            </c:if>
	                        </c:forEach>
	                        
	                        
	                        <p class="info message">
	                            <c:if test="${pendingCount > 0}">
	                                <span style="color: red;">Antal partier kvar att hantera: <c:out value="${pendingCount}"/></span>
	                            </c:if>
	                            <c:if test="${pendingCount == 0}">
	                                <span style="color: green;">Alla partier är hanterade.</span>
	                            </c:if>
	                        </p>
	                        
	                        
	                        <!-- Will display different confirm messages depending on action accept or deny of orderRequest and if batches are approved/denied -->
	                        <form action="${pageContext.request.contextPath}/manageOrders"
	                            method="POST"
	                            style="display: inline;"
	                            onsubmit="return checkPendingConfirmation(this);"
	                            data-pending="${pendingCount}"
	                            data-approved="${approvedBatchCount}">
	                            
	                            <input type="hidden" name="orderId" value="${entry.key.orderId}">
	                            <input type="hidden" name="actionType" value="order">
	                            
	                            <button type="submit" name="action" value="accept" class="button accept-button">
	                                Godkänn Orderförfrågan
	                            </button>
	                            
	                            <button type="submit" name="action" value="deny" class="button deny-button">
	                                Neka Orderförfrågan
	                            </button>
	                        </form>
	                    </div>
	                </c:if>
	            </c:forEach>
	        </c:when>
	    </c:choose>
	    <p class="empty-message">Det finns för närvarande inga ohanterade orderförfrågningar.</p>
	</div>
            
	<!-- Approved orders tab -->
	<div id="approved" class="order-cards">
	    <c:choose>
	        <c:when test="${not empty orderBatchMap}">
	            <c:forEach var="entry" items="${orderBatchMap}">
	                <c:if test="${entry.key.orderStatus == 'APPROVED'}">
	                    <div class="order-card approved-card">
	                        <h3>Ordernummer: <c:out value="${entry.key.orderNo}"/></h3>
	                        <p>Kundnamn: <c:out value="${entry.key.orderCustomerName}"/></p>
	                        <p>
	                          Orderdatum:
	                          <fmt:parseDate value="${entry.key.orderDate}" pattern="yyyy-MM-dd" var="parsedDate" />
	                          <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd" />
	                        </p>
	                        <p>
	                          Orderstatus:
	                          <span class="status ${entry.key.orderStatus == 'APPROVED' ? 'approved' : entry.key.orderStatus == 'PENDING' ? 'pending' : 'denied'}">
	                            <c:out value="${entry.key.orderStatus}"/>
	                          </span>
	                        </p>
	                        
	                        <!-- Calculations for approved batches and total price of approved batches -->
	                        <c:set var="approvedBatchCount" value="0" />
	                        <c:set var="totalApprovedPrice" value="0" />
	                        <c:set var="totalBatchCount" value="0" />
	                        
	                        <c:forEach var="batch" items="${entry.value}">
	                            <c:set var="totalBatchCount" value="${totalBatchCount + 1}" />
	                            <c:if test="${batch.orderStatus == 'APPROVED'}">
	                                <c:set var="approvedBatchCount" value="${approvedBatchCount + 1}" />
	                                <c:set var="totalApprovedPrice" value="${totalApprovedPrice + (batch.orderQuantity * batch.batch.batchPrice)}" />
	                            </c:if>
	                        </c:forEach>
	                        
	                        <!-- UI total price for APPROVED batches -->
	                        <p><strong>Totalt pris (endast godkända partier): </strong>
	                            <fmt:formatNumber value="${totalApprovedPrice}" type="currency" currencySymbol="kr" />
	                        </p>
	                        
	                        <!-- UI for showing nbr of approved batches -->
	                        <p><strong>Godkända Partier: </strong> ${approvedBatchCount} / ${totalBatchCount}</p>
	                        
	                        <button type="button" class="toggle-button">Visa Partier</button>
	                        
	                        <!-- Batch Cards for Approved Orders -->
	                        <div class="batch-cards approved-batch-cards">
	                            <c:forEach var="batch" items="${entry.value}">
	                                <div class="batch-card approved-batch">
	                                    <p>Produktnamn: <c:out value="${batch.batch.product.productName}" /></p>
	                                    <p>Partinummer: <c:out value="${batch.batch.batchNo}"/></p>
	                                    <p>Parti Ursprung: <c:out value="${batch.batch.batchOrigin}"/></p>
	                                    
	                                    <p>Beställd Kvantitet:
	                                        <c:choose>
	                                            <c:when test="${batch.batch.product.measuredInUnits}">
	                                                <c:out value="${batch.orderQuantity}" /> st
	                                            </c:when>
	                                            <c:otherwise>
	                                                <c:out value="${batch.orderQuantity}" /> kg
	                                            </c:otherwise>
	                                        </c:choose>
	                                    </p>
	                                    <p>Parti Styckpris: <fmt:formatNumber value="${batch.batch.batchPrice}" type="currency" currencySymbol="kr" /></p>
	                                    <p>Totalt Pris: <fmt:formatNumber value="${batch.orderQuantity * batch.batch.batchPrice}" type="currency" currencySymbol="kr" /></p>
	                                    <p>Status:
	                                        <span class="status ${batch.orderStatus == 'APPROVED' ? 'approved' : batch.orderStatus == 'PENDING' ? 'pending' : 'denied'}">
	                                            <c:out value="${batch.orderStatus}"/>
	                                        </span>
	                                    </p>
	                                </div>
	                            </c:forEach>
	                        </div>
	                    </div>
	                </c:if>
	            </c:forEach>
	        </c:when>
	    </c:choose>
	    <p class="empty-message">Det finns för närvarande inga godkända orderförfrågningar.</p>
	</div>
		          
             
   	<!-- Denied orders tab - UI shown above batchCards -->
   	<div id="denied" class="order-cards">
   		<c:choose>
       		<c:when test="${not empty orderBatchMap}">
       			<c:forEach var="entry" items="${orderBatchMap}">
       				<c:if test="${entry.key.orderStatus == 'DENIED'}">	
       				<div class="order-card denied-card">
       					<h3>Ordernummer: <c:out value="${entry.key.orderNo}"/></h3>
       					<p>Kundnamn: <c:out value="${entry.key.orderCustomerName}"/></p>
       					<p>Orderdatum:
       				 		<fmt:parseDate value="${entry.key.orderDate}" pattern="yyyy-MM-dd" var="parsedDate" />
                          	<fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd" />
                       	</p>
                       	<p>Orderstatus:
                       		<span class="status denied">
                       			<c:out value="${entry.key.orderStatus}"/>
                       		</span>
                      		</p>
                      				            					
          					<!-- Calculate total batches -->	
          					<c:set var="totalBatchCount" value="0"/>
          					<c:forEach var="batch" items="${entry.value}">
          						<c:set var="totalBatchCount" value="${totalBatchCount + 1}" />
          					</c:forEach>
          					
          					<!-- UI for showing total (DENIED) batches -->
          					<p><strong>Antal nekade partier: </strong>${totalBatchCount}</p>
          					
          					<button type="button" class="toggle-button">Visa Partier</button>
          					
          					<!-- Batch Cards for DENIED orders -->
                    		<div class="batch-cards denied-batch-cards">
                    			<c:forEach var="batch" items="${entry.value}">
                    				<div class="batch-card denied-batch">
                    					<p>Produktnamn: <c:out value="${batch.batch.product.productName}"/></p>
                    					<p>Partinummer: <c:out value="${batch.batch.batchNo}"/></p>
                    					<p>Parti Ursprung: <c:out value="${batch.batch.batchOrigin}"/></p>
                    					<p>Beställd Kvantitet: 
                    						<c:choose>
                    							<c:when test="${batch.batch.product.measuredInUnits}">
                    								<c:out value="${batch.orderQuantity}" /> st
                    							</c:when>
                    							<c:otherwise>
                    								<c:out value="${batch.orderQuantity}" /> kg
                    							</c:otherwise>
                    						</c:choose>
                    					</p>
                    					<p>Parti Styckpris: <fmt:formatNumber value="${batch.batch.batchPrice}" type="currency" currencySymbol="kr" /></p>
                    					<p>Totalt Pris: <fmt:formatNumber value="${batch.orderQuantity * batch.batch.batchPrice}" type="currency" currencySymbol="kr" /></p>
                    					<p>Status:
                    						<span class="status ${batch.orderStatus == 'APPROVED' ? 'approved' : batch.orderStatus == 'PENDING' ? 'pending' : 'denied'}">
	            							<c:out value="${batch.orderStatus}"/>
	            							</span>
            							</p>
	            					</div>
	            				</c:forEach>
                   			</div>
                    	</div>
               		</c:if>
              	</c:forEach>
             </c:when>
           </c:choose>
           <p class="empty-message">Det finns för närvarande inga nekade orderförfrågningar.</p>
        </div>
</main>


<%-- Include the footer --%>
<%@ include file="footer.jsp" %>

<script>
function toggleBatchCards(buttonElement) {
    const orderCard = buttonElement.closest('.order-card');
    orderCard.classList.toggle('collapsed');
    buttonElement.innerText = orderCard.classList.contains('collapsed') ? 'Visa Partier' : 'Dölj Partier';
    
}

function checkPendingConfirmation(formElement) {
    const pending = parseInt(formElement.getAttribute('data-pending')) || 0;
    const approved = parseInt(formElement.getAttribute('data-approved')) || 0;
    const action = formElement.querySelector('button[type="submit"][name="action"]:focus').value;
    
    
    if (action === "accept" && pending > 0) {
        const message = `Ordern innehåller ohanterade partier.\n\n` +
        				"Alla ohanterade partier kommer godkännas automatiskt. \n\n" +
                        "Är du säker på att du vill godkänna ordern ändå?";
        return confirm(message);
    }

    if (action === "deny") {
        if (pending > 0 && approved > 0) {
            const message = `Ordern innehåller ohanterade partier och godkända partier. \n\n` +
                            "Alla partier kommer att nekas automatiskt.\n\n" +
                            "Är du säker på att du vill neka ordern?";
            return confirm(message);
        }
        if (pending > 0) {
            const message = `Ordern innehåller ohanterade partier. \n` +
                            "Alla ohanterade partier kommer att nekas automatiskt.\n\n" +
                            "Är du säker på att du vill neka ordern?";
            return confirm(message);
        }
        if (approved > 0) {
            const message = `Ordern innehåller godkända partier. \n` +
                            "Alla godkända partier kommer att nekas automatiskt.\n\n" +
                            "Är du säker på att du vill neka ordern?";
            return confirm(message);
        }
    }

    return true;
}

function showTab(tabId) {
    const tabs = ['pending', 'approved', 'denied'];
    
    tabs.forEach(tab => {
        const tabElement = document.getElementById(tab);
        
        if (tabElement) {
            // Show or hide the tab content
            tabElement.style.display = (tab === tabId) ? 'flex' : 'none';
            
            // Check if the current tab has any order cards
            if (tab === tabId) {
                const hasOrderCards = tabElement.querySelectorAll('.order-card').length > 0;
                const emptyMessage = tabElement.querySelector(".empty-message");
                
                if (emptyMessage) {
                    emptyMessage.style.display = hasOrderCards ? 'none' : 'block';
                }
            }
        }
    });

    // Set the active class on the correct button
    const tabButtons = document.querySelectorAll(".tab-button");
    tabButtons.forEach(button => {
        // Get the target tab ID from the onclick attribute
        const targetTab = button.getAttribute("onclick").match(/showTab\('([^']+)'\)/);
        
        // Set the active class
        if (targetTab && targetTab[1] === tabId) {
            button.classList.add("active");
        } else {
            button.classList.remove("active");
        }
    });
}

// Initializations 

document.addEventListener('DOMContentLoaded', function() {
    // Restore expanded states and initialize buttons
    const orderCards = document.querySelectorAll('.order-card');
    orderCards.forEach(orderCard => {
        const orderId = orderCard.getAttribute("data-order-id");
        const isExpanded = localStorage.getItem("expanded-" + orderId) === "true";

        // Set initial state
        if (isExpanded) {
            orderCard.classList.remove("collapsed");
        } else {
            orderCard.classList.add("collapsed");
        }

        // Set initial button text
        const button = orderCard.querySelector(".toggle-button");
        if (button) {
            button.textContent = isExpanded ? "Dölj Partier" : "Visa Partier";

            // Add the click event listener
            button.addEventListener("click", function() {
                const isCollapsed = orderCard.classList.toggle("collapsed");
                button.textContent = isCollapsed ? "Visa Partier" : "Dölj Partier";
                
                // Save the expanded state
                if (isCollapsed) {
                    localStorage.removeItem("expanded-" + orderId);
                } else {
                    localStorage.setItem("expanded-" + orderId, "true");
                }
            });
        }
    });

    // Set the initial tab to "pending"
    showTab("pending");
});

</script>
