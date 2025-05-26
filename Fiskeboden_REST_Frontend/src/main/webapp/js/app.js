document.addEventListener("DOMContentLoaded", () => {
  const week = 22;
  const baseUrl = `${window.location.origin}/Fiskeboden_EJB_Frontend`;

  const categories = [
    { id: "sotvatten", name: "Sötvattenfisk" },
    { id: "saltvatten", name: "Saltvattenfisk" },
    { id: "rokt", name: "Rökt fisk" },
    { id: "konserverad", name: "Konserverad fisk" },
    { id: "skaldjur", name: "Skaldjur" },
    { id: "fiskrom", name: "Fiskrom" }
  ];

  const pickupSelect = document.getElementById("pickupSelect");

  function updateCartCount() {
    const cart = JSON.parse(localStorage.getItem("cart")) || [];
    const count = cart.reduce((sum, item) => sum + parseInt(item.quantity || 0), 0);
    const counter = document.getElementById("cart-count");
    if (counter) {
      counter.textContent = count;
      counter.style.display = count > 0 ? "inline-block" : "none";
    }
  }

  function updateHeaderText() {
    const header = document.getElementById("weekly-header");
    const selectedOption = pickupSelect.options[pickupSelect.selectedIndex];

    if (!pickupSelect || pickupSelect.value === "") {
      header.textContent = "Veckans fångster - Vecka 22";
    } else {
      header.textContent = `Veckans fångster – ${selectedOption.textContent} – Vecka 22`;
    }
  }

  function fetchAndRenderProducts(categoryName, container) {
    const pickupPointId = pickupSelect ? pickupSelect.value : "";
    const url = `${baseUrl}/api/batches?category=${encodeURIComponent(categoryName)}&week=${week}&pickupPointId=${pickupPointId}`;

    fetch(url)
      .then(res => {
        if (!res.ok) throw new Error(`Fel från server: ${res.status}`);
        return res.json();
      })
      .then(batches => {
        container.innerHTML = "";

        if (batches.length === 0) {
          container.innerHTML = "<p>Inga produkter hittades för denna kategori.</p>";
          return;
        }

        batches.forEach(batch => {
          const unitLabel = batch.isMeasuredInUnits ? "st" : "kg";

          const item = document.createElement("div");
          item.className = "product-item";
          item.innerHTML = `
            <div class="product-info">
              <h4 class="product-title">${batch.productName}</h4>
              <strong>Tillgängligt:</strong> ${batch.batchQuantity} ${unitLabel}<br>
              <strong>Pris:</strong> ${batch.batchPrice} kr/${unitLabel}<br>
              <strong>Ursprung:</strong> ${batch.batchOrigin}<br>
              <strong>Leverantör:</strong> ${batch.supplierName}<br>
              <span class="pickup-line"><strong>Utlämning:</strong> ${batch.pickupPointName}, ${batch.pickupPointAddress}</span><br>
              <span class="pickup-line"><strong>Tid:</strong> ${batch.pickupDay} kl ${batch.pickupTime}</span><br>
              <label><strong>Välj antal:</strong> 
                <input type="number" 
                       class="product-quantity" 
                       min="1" 
                       max="${batch.batchQuantity}" 
                       value="1"
                       data-max="${batch.batchQuantity}" /> ${unitLabel}
              </label>
            </div>
            <button class="add-to-cart" 
                    data-name="${batch.productName}" 
                    data-price="${batch.batchPrice}" 
                    data-max="${batch.batchQuantity}"
                    data-unit="${unitLabel}"
                    data-batchid="${batch.batchId}">
              <img src="images/shoppingCart.png" class="cart-icon-small" />
              Lägg i varukorg
            </button>
          `;

          container.appendChild(item);
        });
      })
      .catch(err => {
        console.error(`Fel vid hämtning av batcher för ${categoryName}:`, err);
        container.innerHTML = "<p>Kunde inte hämta produkter.</p>";
      });
  }

  function renderAllCategories() {
    updateHeaderText(); // uppdatera rubriken varje gång kategorier renderas
    categories.forEach(cat => {
      const container = document.querySelector(`#${cat.id} .product-list`);
      if (container) {
        fetchAndRenderProducts(cat.name, container);
      }
    });
  }

  function loadPickupPointsAndRenderProducts() {
    fetch(`${baseUrl}/api/pickup-points`)
      .then(res => {
        if (!res.ok) throw new Error("Fel från servern vid hämtning av pickup-points.");
        return res.json();
      })
      .then(points => {
        points.forEach(p => {
          const opt = document.createElement("option");
          opt.value = p.pickupPointId;
          opt.textContent = p.pickupPointName;
          pickupSelect.appendChild(opt);
        });
        updateHeaderText(); // första gången vid laddning
        renderAllCategories();
      })
      .catch(err => {
        console.error("Kunde inte hämta pickup-points:", err);
      });
  }

  if (pickupSelect) {
    pickupSelect.addEventListener("change", () => {
      updateHeaderText();
      renderAllCategories();
    });
    loadPickupPointsAndRenderProducts();
  }

  const catSelect = document.getElementById("catchSelect");
  if (catSelect) {
    catSelect.addEventListener("change", function () {
      const targetId = this.value;
      const element = document.getElementById(targetId);
      if (element) {
        element.scrollIntoView({ behavior: "smooth" });
      }
    });
  }

  document.addEventListener("click", function (event) {
    if (event.target.closest(".add-to-cart")) {
      const button = event.target.closest(".add-to-cart");

      const productName = button.dataset.name;
      const productPrice = parseFloat(button.dataset.price);
      const unitLabel = button.dataset.unit;
      const maxStock = parseInt(button.dataset.max, 10);
      const batchId = parseInt(button.dataset.batchid, 10);
      const quantityInput = button.parentElement.querySelector(".product-quantity");
      const quantity = parseInt(quantityInput.value, 10);

      if (!batchId || isNaN(batchId)) {
        alert("❌ batchId saknas – kan inte lägga till i varukorg.");
        return;
      }

      if (quantity <= 0 || isNaN(quantity)) {
        alert("❌ Felaktig kvantitet.");
        return;
      }

      let cart = JSON.parse(localStorage.getItem("cart")) || [];
      const existingProduct = cart.find(item => item.name === productName);
      const currentQty = existingProduct ? existingProduct.quantity : 0;
      const newQty = currentQty + quantity;

      if (newQty > maxStock) {
        alert(`Endast ${maxStock} ${unitLabel} ${productName} finns i lager.`);
        return;
      }

      if (existingProduct) {
        existingProduct.quantity = newQty;
      } else {
        cart.push({
          name: productName,
          price: productPrice,
          quantity: quantity,
          unit: unitLabel,
          batchId: batchId
        });
      }

      localStorage.setItem("cart", JSON.stringify(cart));
      updateCartCount();
      alert(`${quantity} ${unitLabel} ${productName} lades till i varukorgen!`);
    }
  });

  function initCarousel() {
    const track = document.querySelector('.carousel-track');
    const btnLeft = document.querySelector('.carousel-btn.left');
    const btnRight = document.querySelector('.carousel-btn.right');

    if (!track || !btnLeft || !btnRight) {
      console.log("🎡 Carousel hittades inte – hoppar över");
      return;
    }

    let currentIndex = 0;
    const cardWidth = 330;

    btnLeft.addEventListener('click', () => {
      if (currentIndex > 0) {
        currentIndex--;
        updateCarousel();
      }
    });

    btnRight.addEventListener('click', () => {
      const maxIndex = track.children.length - 3;
      if (currentIndex < maxIndex) {
        currentIndex++;
        updateCarousel();
      }
    });

    function updateCarousel() {
      track.style.transform = `translateX(-${currentIndex * cardWidth}px)`;
    }

    console.log("✅ Carousel initierad");
  }

  // Initiera alltid
  updateCartCount();
  initCarousel();
});
