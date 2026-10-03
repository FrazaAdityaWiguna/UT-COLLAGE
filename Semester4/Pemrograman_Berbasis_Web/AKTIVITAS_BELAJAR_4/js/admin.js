/* ==========================================================
   admin.js — halaman admin: statistik, tabel produk (CRUD
   lewat modal), dan tabel pesanan dengan ubah status.
   ========================================================== */

const ORDER_STATUSES = {
  Diproses: "#d97706",
  Dikirim: "#2563eb",
  Selesai: "#16a34a",
  Dibatalkan: "#dc2626",
};

const productTableBody = document.getElementById("product-table-body");
const orderTableBody = document.getElementById("order-table-body");
const productSearch = document.getElementById("product-search");
const productModal = document.getElementById("product-modal");
const productModalTitle = document.getElementById("product-modal-title");
const productForm = document.getElementById("product-form");

const productSchema = {
  name: [Rules.required("Nama produk wajib diisi."), Rules.minLength(3)],
  category: [Rules.required("Pilih kategori produk.")],
  price: [Rules.required("Harga wajib diisi."), Rules.minNumber(1000, "Harga minimal Rp1.000.")],
  stock: [Rules.required("Stok wajib diisi."), Rules.integer(), Rules.minNumber(0, "Stok tidak boleh negatif.")],
  description: [Rules.required("Deskripsi wajib diisi."), Rules.minLength(10)],
};

// ---------- Statistik ----------
function renderStats() {
  const products = getProducts();
  const orders = getOrders();
  const revenue = orders
    .filter((order) => order.status === "Selesai")
    .reduce((sum, order) => sum + order.total, 0);

  document.getElementById("stat-products").textContent = products.length;
  document.getElementById("stat-orders").textContent = orders.length;
  document.getElementById("stat-revenue").textContent = formatRupiah(revenue);
  document.getElementById("stat-low-stock").textContent = products.filter((p) => p.stock <= 5).length;
}

// ---------- Tabel produk ----------
function renderProductTable() {
  const keyword = productSearch.value.trim().toLowerCase();
  const products = getProducts().filter(
    (p) => p.name.toLowerCase().includes(keyword) || p.category.toLowerCase().includes(keyword)
  );

  productTableBody.innerHTML = "";

  if (products.length === 0) {
    productTableBody.innerHTML = `<tr><td colspan="6" class="empty-state">Tidak ada produk.</td></tr>`;
    return;
  }

  products.forEach((product, index) => {
    const row = document.createElement("tr");
    const lowStock = product.stock <= 5;
    row.innerHTML = `
      <td>${index + 1}</td>
      <td>${escapeHtml(product.icon)} ${escapeHtml(product.name)}</td>
      <td>${escapeHtml(product.category)}</td>
      <td>${formatRupiah(product.price)}</td>
      <td style="color: ${lowStock ? "var(--color-danger)" : "inherit"}; font-weight: ${lowStock ? 700 : 400};">${product.stock}</td>
      <td>
        <div class="row-actions">
          <button type="button" class="btn btn-outline btn-sm" data-action="edit" data-id="${product.id}">✏️ Edit</button>
          <button type="button" class="btn btn-danger btn-sm" data-action="delete" data-id="${product.id}">🗑️ Hapus</button>
        </div>
      </td>`;
    productTableBody.appendChild(row);
  });
}

function openProductModal(product = null) {
  productForm.reset();
  clearFormErrors(productForm, productSchema);

  productModalTitle.textContent = product ? "Edit Produk" : "Tambah Produk";
  if (product) {
    const fields = productForm.elements;
    fields.id.value = product.id;
    fields.name.value = product.name;
    fields.category.value = product.category;
    fields.icon.value = product.icon;
    fields.price.value = product.price;
    fields.stock.value = product.stock;
    fields.description.value = product.description;
  } else {
    productForm.elements.id.value = "";
  }

  productModal.showModal();
}

function saveProduct() {
  const data = new FormData(productForm);
  const id = Number(data.get("id"));
  const productData = {
    name: data.get("name").trim(),
    category: data.get("category"),
    icon: data.get("icon").trim() || "📦",
    price: Number(data.get("price")),
    stock: Number(data.get("stock")),
    description: data.get("description").trim(),
  };

  const products = getProducts();
  const duplicate = products.some(
    (p) => p.name.toLowerCase() === productData.name.toLowerCase() && p.id !== id
  );
  if (duplicate) {
    setFieldError(productForm, "name", "Nama produk sudah ada.");
    showToast("Nama produk sudah digunakan.", "error");
    return;
  }

  if (id) {
    saveProducts(products.map((p) => (p.id === id ? { ...p, ...productData } : p)));
    showToast(`${productData.name} berhasil diperbarui.`, "success");
  } else {
    saveProducts([...products, { id: nextProductId(), ...productData }]);
    showToast(`${productData.name} berhasil ditambahkan.`, "success");
  }

  productModal.close();
  refreshAll();
}

async function deleteProduct(id) {
  const product = getProductById(id);
  const confirmed = await confirmDialog(`Yakin ingin menghapus "${product.name}"? Tindakan ini tidak bisa dibatalkan.`, {
    title: "Hapus Produk",
    okText: "Hapus",
    danger: true,
  });
  if (!confirmed) return;

  saveProducts(getProducts().filter((p) => p.id !== id));
  saveCart(getCart().filter((item) => item.productId !== id));
  updateCartBadge();
  refreshAll();
  showToast(`${product.name} telah dihapus.`, "info");
}

// ---------- Tabel pesanan ----------
function renderOrderTable() {
  const orders = getOrders();
  orderTableBody.innerHTML = "";

  if (orders.length === 0) {
    orderTableBody.innerHTML = `<tr><td colspan="6" class="empty-state">Belum ada pesanan.</td></tr>`;
    return;
  }

  orders.forEach((order) => {
    const row = document.createElement("tr");
    const itemsList = order.items
      .map((item) => `<li>${escapeHtml(item.name)} × ${item.qty}</li>`)
      .join("");
    const options = Object.keys(ORDER_STATUSES)
      .map((status) => `<option ${status === order.status ? "selected" : ""}>${status}</option>`)
      .join("");

    row.innerHTML = `
      <td><code>${order.code}</code></td>
      <td>${new Date(order.date).toLocaleDateString("id-ID", { day: "numeric", month: "short", year: "numeric" })}</td>
      <td>${escapeHtml(order.customer.name)}<br><small>${escapeHtml(order.customer.phone)}</small></td>
      <td><ul class="order-items">${itemsList}</ul></td>
      <td>${formatRupiah(order.total)}</td>
      <td>
        <span class="badge" style="background: ${ORDER_STATUSES[order.status]};">${order.status}</span>
        <label class="visually-hidden" for="status-${order.code}">Ubah status ${order.code}</label>
        <select id="status-${order.code}" data-code="${order.code}">${options}</select>
      </td>`;
    orderTableBody.appendChild(row);
  });
}

function updateOrderStatus(code, status) {
  const orders = getOrders().map((order) => (order.code === code ? { ...order, status } : order));
  saveOrders(orders);
  refreshAll();
  showToast(`Status ${code} diubah menjadi ${status}.`, "success");
}

function refreshAll() {
  renderStats();
  renderProductTable();
  renderOrderTable();
}

// ---------- Event listener ----------
document.getElementById("add-product-btn").addEventListener("click", () => openProductModal());

document.getElementById("reset-btn").addEventListener("click", async () => {
  const confirmed = await confirmDialog("Semua produk, keranjang, dan pesanan akan dikembalikan ke data awal. Lanjutkan?", {
    title: "Reset Data",
    okText: "Reset",
    danger: true,
  });
  if (!confirmed) return;
  resetAllData();
  updateCartBadge();
  refreshAll();
  showToast("Data berhasil direset.", "info");
});

productTableBody.addEventListener("click", (event) => {
  const button = event.target.closest("button[data-action]");
  if (!button) return;

  const id = Number(button.dataset.id);
  if (button.dataset.action === "edit") openProductModal(getProductById(id));
  if (button.dataset.action === "delete") deleteProduct(id);
});

orderTableBody.addEventListener("change", (event) => {
  if (event.target.matches("select[data-code]")) {
    updateOrderStatus(event.target.dataset.code, event.target.value);
  }
});

productForm.addEventListener("submit", (event) => {
  event.preventDefault();
  if (validateForm(productForm, productSchema)) saveProduct();
});

productModal.querySelectorAll("[data-close]").forEach((button) =>
  button.addEventListener("click", () => productModal.close())
);
enableBackdropClose(productModal);

productSearch.addEventListener("input", renderProductTable);

attachLiveValidation(productForm, productSchema);
refreshAll();
