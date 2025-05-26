
document.addEventListener("DOMContentLoaded", () => {
  const cartContainer = document.getElementById("cart-items");
  const clearBtn = document.getElementById("clear-cart");
  const checkoutBtn = document.getElementById("checkout-btn");
  const backToProductsBtn = document.getElementById("back-to-products");
  const checkoutSection = document.getElementById("checkout-section");
  const checkoutForm = document.getElementById("checkout-form");
  const receipt = document.getElementById("receipt");

  function translateStatus(status) {
    switch (status) {
      case "PENDING": return "Väntar på bekräftelse";
      case "APPROVED": return "Godkänd";
      case "DENIED": return "Nekad";
      default: return status;
    }
  }

  function updateCartCount() {
    const cart = JSON.parse(localStorage.getItem("cart")) || [];
    const count = cart.reduce((sum, item) => sum + parseInt(item.quantity || 0), 0);
    const counter = document.getElementById("cart-count");
    if (counter) {
      counter.textContent = count;
      counter.style.display = count > 0 ? "inline-block" : "none";
    }
  }

  function updateCartUI(cart) {
    if (cart.length === 0) {
      checkoutBtn.style.display = "none";
      clearBtn.style.display = "none";
      backToProductsBtn.style.display = "inline-block";
    } else {
      checkoutBtn.style.display = "inline-block";
      clearBtn.style.display = "inline-block";
      backToProductsBtn.style.display = "none";
    }
  }

  function renderCart() {
    const cart = JSON.parse(localStorage.getItem("cart")) || [];

    if (cart.length === 0) {
      cartContainer.innerHTML = "<p>Din varukorg är tom.</p>";
      updateCartCount();
      updateCartUI(cart);
      return;
    }

    let total = 0;
    const rows = cart.map((item, index) => {
      const lineTotal = item.price * item.quantity;
      total += lineTotal;

      return `
        <tr data-index="${index}">
          <td>${item.name}</td>
          <td>
            <input type="number" class="cart-qty" min="1" value="${item.quantity}" style="width: 60px;" />
            ${item.unit || "st"}
          </td>
          <td>${item.price} kr/${item.unit || "st"}</td>
          <td>${lineTotal.toFixed(2)} kr</td>
          <td><button class="remove-item" title="Ta bort">X</button></td>
        </tr>
      `;
    });

    cartContainer.innerHTML = `
      <table class="cart-table">
        <thead>
          <tr>
            <th>Produkt</th>
            <th>Antal</th>
            <th>Pris</th>
            <th>Totalt</th>
            <th></th>
          </tr>
        </thead>
        <tbody>${rows.join("")}</tbody>
        <tfoot>
          <tr>
            <td colspan="4"><strong>Totalt:</strong></td>
            <td><strong>${total.toFixed(2)} kr</strong></td>
          </tr>
        </tfoot>
      </table>
    `;

    updateCartCount();
    updateCartUI(cart);

    document.querySelectorAll(".cart-qty").forEach(input => {
      input.addEventListener("change", () => {
        const row = input.closest("tr");
        const index = parseInt(row.dataset.index, 10);
        let newQty = parseInt(input.value, 10);
        if (isNaN(newQty) || newQty < 1) {
          newQty = 1;
          input.value = 1;
        }
        cart[index].quantity = newQty;
        localStorage.setItem("cart", JSON.stringify(cart));
        renderCart();
      });
    });

    document.querySelectorAll(".remove-item").forEach(btn => {
      btn.addEventListener("click", () => {
        const row = btn.closest("tr");
        const index = parseInt(row.dataset.index, 10);
        cart.splice(index, 1);
        localStorage.setItem("cart", JSON.stringify(cart));
        renderCart();
      });
    });
  }

  function setupReceiptListeners(order) {
    const statusEl = document.getElementById("order-status");

    document.getElementById("close-receipt").addEventListener("click", () => {
      if (confirm("Vill du ta bort kvittot?")) {
        localStorage.removeItem("lastOrder");
        receipt.style.display = "none";
      }
    });

    document.getElementById("refresh-status").addEventListener("click", () => {
      fetch(`/Fiskeboden_EJB_Frontend/api/orderrequests?orderId=${order.orderId}`)
        .then(res => res.json())
        .then(data => {
          if (!data || !data.orderStatus) {
            alert("Kunde inte hitta matchande order i svaret.");
            return;
          }

          order.orderStatus = data.orderStatus;
          localStorage.setItem("lastOrder", JSON.stringify(order));

          statusEl.textContent = translateStatus(data.orderStatus);
          if (data.orderStatus === "APPROVED") {
            statusEl.style.color = "green";
          }
        })
        .catch(err => {
          console.error("Fel vid statusuppdatering:", err);
          alert("Kunde inte hämta ny orderstatus.");
        });
    });

    if (order.orderStatus === "APPROVED") {
      statusEl.style.color = "green";
    }
	if (order.orderStatus === "DENIED") {
	  statusEl.style.color = "red";
	}
  }

  function renderReceipt(order) {
    const { customerName, orderNo, orderId, orderStatus, date, items } = order;
    if (!orderId) {
      localStorage.removeItem("lastOrder");
      return;
    }

    let total = 0;
    const itemsHtml = items.map(item => {
      const lineTotal = item.quantity * item.price;
      total += lineTotal;
      return `<li>${item.quantity} ${item.unit || "st"} ${item.name} – ${lineTotal.toFixed(2)} kr</li>`;
    }).join("");

    let receiptHTML = `
      <button id="close-receipt" style="float:right;">✖</button>
      <h4>Tack för din order, ${customerName}!</h4>
      <p><strong>Ordernummer:</strong> ${orderNo}</p>
      <p><strong>Status:</strong> <span id="order-status">${translateStatus(orderStatus)}</span></p>
      <p><strong>Datum:</strong> ${date}</p>
      <ul>${itemsHtml}</ul>
      <p><strong>Totalt:</strong> ${total.toFixed(2)} kr</p>
      <button id="refresh-status">Uppdatera orderstatus</button>
    `;

    fetch(`/Fiskeboden_EJB_Frontend/api/orderrequests?orderId=${orderId}`)
      .then(res => res.json())
      .then(data => {
        const groups = data.supplierGroups || [];
        const pickupHtml = groups.map(group => `
          <li><strong>${group.supplierName}</strong> – ${group.pickupDay} kl. ${group.pickupTime}<br>
          ${group.pickupPointName}, ${group.pickupPointAddress}</li>
        `).join("");

        if (pickupHtml) {
          receiptHTML += `
            <h4>Upphämtningsinformation</h4>
            <ul>${pickupHtml}</ul>
          `;
        }

        receipt.innerHTML = receiptHTML;
        receipt.style.display = "block";
        setupReceiptListeners(order);
      })
      .catch(err => {
        console.error("Kunde inte hämta upphämtningsinfo:", err);
        receipt.innerHTML = receiptHTML;
        receipt.style.display = "block";
        setupReceiptListeners(order);
      });
  }

  clearBtn.addEventListener("click", () => {
    if (confirm("Är du säker på att du vill tömma hela varukorgen?")) {
      localStorage.removeItem("cart");
      renderCart();
    }
  });

  checkoutBtn.addEventListener("click", () => {
    checkoutSection.style.display = "block";
    checkoutSection.scrollIntoView({ behavior: "smooth" });
  });

  checkoutForm.addEventListener("submit", (e) => {
    e.preventDefault();
    const name = document.getElementById("customer-name").value.trim();
    const cart = JSON.parse(localStorage.getItem("cart")) || [];

    if (!name) {
      alert("Ange ditt namn.");
      return;
    }

    if (cart.length === 0) {
      alert("Varukorgen är tom.");
      return;
    }

    const submitBtn = checkoutForm.querySelector("button[type='submit']");
    submitBtn.disabled = true;

    const firstItem = cart[0];
    const formData = new URLSearchParams();
    formData.append("customerName", name);
    formData.append("batchId", firstItem.batchId);
    formData.append("quantity", firstItem.quantity);

    fetch("/Fiskeboden_EJB_Frontend/api/orderrequests", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: formData
    })
      .then(res => res.json())
      .then(orderData => {
        const orderId = orderData.orderId;
        const orderNo = orderData.orderNo;
        const orderStatus = orderData.orderStatus;

        const promises = cart.filter(item => item !== firstItem).map(item => {
          const batchData = new URLSearchParams();
          batchData.append("action", "create");
          batchData.append("orderId", orderId);
          batchData.append("batchId", item.batchId);
          batchData.append("orderQuantity", item.quantity);

          return fetch("/Fiskeboden_EJB_Frontend/api/orderbatches", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: batchData
          });
        });

        return Promise.all(promises).then(() => {
          const date = new Date().toLocaleDateString("sv-SE");

          const order = {
            customerName: name,
            orderNo: orderNo,
            orderId: orderId,
            orderStatus: orderStatus,
            date: date,
            items: cart
          };

          localStorage.setItem("lastOrder", JSON.stringify(order));
          localStorage.removeItem("cart");
          renderCart();
          checkoutForm.reset();
          renderReceipt(order);
        });
      })
      .catch(err => {
        console.error("Fel vid orderregistrering:", err);
        alert("Något gick fel vid orderregistrering.");
      })
      .finally(() => {
        submitBtn.disabled = false;
      });
  });

  renderCart();

  const lastOrder = JSON.parse(localStorage.getItem("lastOrder"));
  if (lastOrder && lastOrder.orderId) {
    renderReceipt(lastOrder);
  } else {
    localStorage.removeItem("lastOrder");
  }
});
