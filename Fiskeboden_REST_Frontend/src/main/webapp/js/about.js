document.addEventListener("DOMContentLoaded", () => {
  fetch("/Fiskeboden_EJB_Frontend/api/statistics")
    .then(res => res.json())
    .then(s => {
      const text = `
        Fiskeboden samarbetar med <strong>${s.totalSuppliers}</strong> småskaliga fiskare, utspridda över <strong>${s.totalPickupPoints}</strong> upphämtningsplatser i Sverige.
        Våra producenter erbjuder idag <strong>${s.totalProducts}</strong> produkter inom <strong>${s.totalCategories}</strong> olika kategorier.
        Hittills har <strong>${s.totalOrders}</strong> beställningar lagts, vilket resulterat i <strong>${s.totalKgSold.toFixed(1)} kg</strong> färsk fisk och <strong>${s.totalUnitsSold}</strong> styckprodukter levererade direkt från hav till tallrik.
        Den mest efterfrågade produkten just nu är <strong>${s.mostSoldProduct}</strong>.
      `;
      document.getElementById("about-stats-text").innerHTML = text;
    })
    .catch(err => {
      console.error("Kunde inte ladda statistiktext:", err);
    });
});
