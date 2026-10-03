/* ==========================================================
   keranjang.js — halaman keranjang: tabel belanja, ubah
   jumlah, hapus item, ringkasan harga, dan checkout.
   ========================================================== */

const FREE_SHIPPING_MIN = 1000000;
const SHIPPING_COST = 25000;

const cartBody = document.getElementById("cart-body");
const checkoutForm = document.getElementById("checkout-form");
const checkoutBtn = document.getElementById("checkout-btn");
const successModal = document.getElementById("success-modal");
const successBody = document.getElementById("success-body");

const checkoutSchema = {
  nama: [Rules.required("Nama wajib diisi."), Rules.minLength(3)],
  email: [Rules.required("Email wajib diisi."), Rules.email()],
  telepon: [Rules.required("Nomor HP wajib diisi."), Rules.phone()],
  alamat: [Rules.required("Alamat wajib diisi."), Rules.minLength(10)],
  pembayaran: [Rules.required("Pilih salah satu metode pembayaran.")],
};

// Menggabungkan isi keranjang dengan data produk terbaru
function getCartDetails() {
  return getCart()
    .map((item) => ({ ...item, product: getProductById(item.productId) }))
    .filter((item) => item.product);
}

function calculateTotals(items) {
  const subtotal = items.reduce((sum, item) => sum + item.product.price * item.qty, 0);
  const shipping = subtotal === 0 || subtotal >= FREE_SHIPPING_MIN ? 0 : SHIPPING_COST;
  return { subtotal, shipping, total: subtotal + shipping };
}

function renderSummary(items) {
  const { subtotal, shipping, total } = calculateTotals(items);
  document.getElementById("summary-subtotal").textContent = formatRupiah(subtotal);
  document.getElementById("summary-shipping").textContent = shipping === 0 && subtotal > 0 ? "Gratis 🎁" : formatRupiah(shipping);
  document.getElementById("summary-total").textContent = formatRupiah(total);
}

function renderCart() {
  const items = getCartDetails();
  cartBody.innerHTML = "";

  if (items.length === 0) {
    cartBody.innerHTML = `
      <tr>
        <td colspan="5">
          <div class="empty-state">
            <span class="emoji" aria-hidden="true">🛒</span>
            <p>Keranjang masih kosong. <a href="index.html">Mulai belanja</a></p>
          </div>
        </td>
      </tr>`;
  }

  items.forEach(({ product, qty }) => {
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${escapeHtml(product.icon)} ${escapeHtml(product.name)}</td>
      <td>${formatRupiah(product.price)}</td>
      <td>
        <div class="qty-control">
          <button type="button" data-action="decrease" data-id="${product.id}" aria-label="Kurangi jumlah">−</button>
          <span>${qty}</span>
          <button type="button" data-action="increase" data-id="${product.id}" aria-label="Tambah jumlah">+</button>
        </div>
      </td>
      <td>${formatRupiah(product.price * qty)}</td>
      <td><button type="button" class="btn btn-outline btn-sm" data-action="remove" data-id="${product.id}" aria-label="Hapus ${escapeHtml(product.name)}">🗑️</button></td>`;
    cartBody.appendChild(row);
  });

  checkoutBtn.disabled = items.length === 0;
  renderSummary(items);
  updateCartBadge();
}

function changeQty(productId, delta) {
  const cart = getCart();
  const item = cart.find((entry) => entry.productId === productId);
  const product = getProductById(productId);
  if (!item || !product) return;

  const newQty = item.qty + delta;
  if (newQty > product.stock) {
    showToast(`Stok ${product.name} hanya ${product.stock}.`, "error");
    return;
  }
  if (newQty < 1) {
    removeItem(productId);
    return;
  }

  item.qty = newQty;
  saveCart(cart);
  renderCart();
}

async function removeItem(productId) {
  const product = getProductById(productId);
  const confirmed = await confirmDialog(`Hapus ${product.name} dari keranjang?`, {
    title: "Hapus Item",
    okText: "Hapus",
    danger: true,
  });
  if (!confirmed) return;

  saveCart(getCart().filter((entry) => entry.productId !== productId));
  renderCart();
  showToast(`${product.name} dihapus dari keranjang.`, "info");
}

function placeOrder() {
  const items = getCartDetails();

  // Cek ulang stok, siapa tahu admin mengubahnya
  const shortage = items.find((item) => item.qty > item.product.stock);
  if (shortage) {
    showToast(`Stok ${shortage.product.name} tidak mencukupi. Kurangi jumlahnya.`, "error");
    return;
  }

  const { subtotal, shipping, total } = calculateTotals(items);
  const data = new FormData(checkoutForm);
  const order = {
    code: generateOrderCode(),
    date: new Date().toISOString(),
    customer: {
      name: data.get("nama").trim(),
      email: data.get("email").trim(),
      phone: data.get("telepon").trim(),
      address: data.get("alamat").trim(),
    },
    payment: data.get("pembayaran"),
    items: items.map(({ product, qty }) => ({ name: product.name, price: product.price, qty })),
    subtotal,
    shipping,
    total,
    status: "Diproses",
  };

  // Kurangi stok produk sesuai jumlah yang dibeli
  const products = getProducts().map((product) => {
    const bought = items.find((item) => item.productId === product.id);
    return bought ? { ...product, stock: product.stock - bought.qty } : product;
  });

  saveProducts(products);
  saveOrders([order, ...getOrders()]);
  saveCart([]);

  checkoutForm.reset();
  renderCart();

  successBody.innerHTML = `
    <p>Terima kasih, <strong>${escapeHtml(order.customer.name)}</strong>! Pesananmu sedang diproses.</p>
    <p>Kode pesanan:</p>
    <p class="order-code">${order.code}</p>
    <p>Total pembayaran <strong>${formatRupiah(order.total)}</strong> via ${escapeHtml(order.payment)}.</p>`;
  successModal.showModal();
}

// ---------- Event listener ----------
cartBody.addEventListener("click", (event) => {
  const button = event.target.closest("button[data-action]");
  if (!button) return;

  const id = Number(button.dataset.id);
  if (button.dataset.action === "increase") changeQty(id, 1);
  if (button.dataset.action === "decrease") changeQty(id, -1);
  if (button.dataset.action === "remove") removeItem(id);
});

checkoutForm.addEventListener("submit", (event) => {
  event.preventDefault();
  if (getCart().length === 0) {
    showToast("Keranjang masih kosong.", "error");
    return;
  }
  if (validateForm(checkoutForm, checkoutSchema)) placeOrder();
});

attachLiveValidation(checkoutForm, checkoutSchema);
renderCart();
