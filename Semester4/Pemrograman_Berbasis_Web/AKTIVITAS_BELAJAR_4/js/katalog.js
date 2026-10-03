/* ==========================================================
   katalog.js — halaman katalog: menampilkan produk, cari,
   filter kategori, urutkan, detail produk, dan tambah ke
   keranjang.
   ========================================================== */

const grid = document.getElementById("product-grid");
const searchInput = document.getElementById("search-input");
const categoryFilter = document.getElementById("category-filter");
const sortSelect = document.getElementById("sort-select");
const resultInfo = document.getElementById("result-info");
const detailModal = document.getElementById("detail-modal");
const detailBody = document.getElementById("detail-body");
const detailAddBtn = document.getElementById("detail-add-btn");

function populateCategories() {
  const categories = [...new Set(getProducts().map((p) => p.category))].sort();
  categories.forEach((category) => {
    const option = document.createElement("option");
    option.value = category;
    option.textContent = category;
    categoryFilter.appendChild(option);
  });
}

// Mengambil produk sesuai kata kunci, kategori, dan urutan yang dipilih
function getFilteredProducts() {
  const keyword = searchInput.value.trim().toLowerCase();
  const category = categoryFilter.value;

  const result = getProducts().filter((product) => {
    const matchName = product.name.toLowerCase().includes(keyword);
    const matchCategory = !category || product.category === category;
    return matchName && matchCategory;
  });

  switch (sortSelect.value) {
    case "price-asc":
      result.sort((a, b) => a.price - b.price);
      break;
    case "price-desc":
      result.sort((a, b) => b.price - a.price);
      break;
    case "name-asc":
      result.sort((a, b) => a.name.localeCompare(b.name));
      break;
    default:
      result.sort((a, b) => b.id - a.id);
  }
  return result;
}

function createProductCard(product) {
  const outOfStock = product.stock <= 0;
  const article = document.createElement("article");
  article.className = "product-card";
  article.innerHTML = `
    <div class="product-thumb" aria-hidden="true">${escapeHtml(product.icon)}</div>
    <div class="product-body">
      <span class="product-category">${escapeHtml(product.category)}</span>
      <h3>${escapeHtml(product.name)}</h3>
      <span class="product-price">${formatRupiah(product.price)}</span>
      <span class="product-stock">${outOfStock ? "❌ Stok habis" : `Stok: ${product.stock}`}</span>
      <div class="product-actions">
        <button type="button" class="btn btn-outline btn-sm" data-action="detail" data-id="${product.id}">Detail</button>
        <button type="button" class="btn btn-primary btn-sm" data-action="add" data-id="${product.id}" ${outOfStock ? "disabled" : ""}>+ Keranjang</button>
      </div>
    </div>`;
  return article;
}

function renderProducts() {
  const products = getFilteredProducts();
  grid.innerHTML = "";

  if (products.length === 0) {
    grid.innerHTML = `
      <div class="empty-state">
        <span class="emoji" aria-hidden="true">🔎</span>
        <p>Produk tidak ditemukan. Coba kata kunci lain.</p>
      </div>`;
  } else {
    products.forEach((product) => grid.appendChild(createProductCard(product)));
  }

  resultInfo.textContent = `Menampilkan ${products.length} produk`;
}

function addToCart(productId) {
  const product = getProductById(productId);
  if (!product) return;

  const cart = getCart();
  const item = cart.find((entry) => entry.productId === productId);
  const currentQty = item ? item.qty : 0;

  if (currentQty + 1 > product.stock) {
    showToast(`Stok ${product.name} tidak mencukupi.`, "error");
    return;
  }

  if (item) {
    item.qty += 1;
  } else {
    cart.push({ productId, qty: 1 });
  }
  saveCart(cart);
  updateCartBadge();
  showToast(`${product.name} ditambahkan ke keranjang.`, "success");
}

function openDetail(productId) {
  const product = getProductById(productId);
  if (!product) return;

  detailBody.innerHTML = `
    <div class="detail-icon" aria-hidden="true">${escapeHtml(product.icon)}</div>
    <h3>${escapeHtml(product.name)}</h3>
    <p class="product-category">${escapeHtml(product.category)}</p>
    <p>${escapeHtml(product.description || "Belum ada deskripsi.")}</p>
    <p class="product-price">${formatRupiah(product.price)}</p>
    <p class="product-stock">Stok tersedia: ${product.stock}</p>`;

  detailAddBtn.dataset.id = product.id;
  detailAddBtn.disabled = product.stock <= 0;
  detailModal.showModal();
}

// ---------- Event listener ----------
grid.addEventListener("click", (event) => {
  const button = event.target.closest("button[data-action]");
  if (!button) return;

  const id = Number(button.dataset.id);
  if (button.dataset.action === "add") addToCart(id);
  if (button.dataset.action === "detail") openDetail(id);
});

detailAddBtn.addEventListener("click", () => {
  addToCart(Number(detailAddBtn.dataset.id));
  detailModal.close();
});

detailModal.querySelectorAll("[data-close]").forEach((button) =>
  button.addEventListener("click", () => detailModal.close())
);
enableBackdropClose(detailModal);

searchInput.addEventListener("input", renderProducts);
categoryFilter.addEventListener("change", renderProducts);
sortSelect.addEventListener("change", renderProducts);

populateCategories();
renderProducts();
