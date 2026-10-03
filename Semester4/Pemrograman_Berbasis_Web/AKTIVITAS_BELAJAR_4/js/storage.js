/* ==========================================================
   storage.js — lapisan data. Semua baca/tulis localStorage
   lewat file ini supaya halaman lain tidak perlu tahu
   bagaimana data disimpan.
   ========================================================== */

const STORAGE_KEYS = {
  products: "GadgetZa_products",
  cart: "GadgetZa_cart",
  orders: "GadgetZa_orders",
  theme: "GadgetZa_theme",
};

// Data awal saat aplikasi pertama kali dibuka
const DEFAULT_PRODUCTS = [
  { id: 1, name: "Smartphone Nova X", category: "Smartphone", price: 3499000, stock: 12, icon: "📱", description: "Layar AMOLED 6,5 inci, RAM 8 GB, baterai 5000 mAh." },
  { id: 2, name: "Laptop AeroBook 14", category: "Laptop", price: 8999000, stock: 5, icon: "💻", description: "Prosesor hemat daya, SSD 512 GB, bobot hanya 1,3 kg." },
  { id: 3, name: "TWS SoundBuds", category: "Audio", price: 459000, stock: 30, icon: "🎧", description: "Earbuds nirkabel dengan noise cancelling dan baterai 24 jam." },
  { id: 4, name: "Smartwatch FitPro", category: "Wearable", price: 899000, stock: 15, icon: "⌚", description: "Pantau detak jantung, langkah, dan kualitas tidur." },
  { id: 5, name: "Speaker BoomBox Mini", category: "Audio", price: 329000, stock: 20, icon: "🔊", description: "Speaker Bluetooth tahan air dengan bass mantap." },
  { id: 6, name: "Keyboard Mekanik K68", category: "Aksesoris", price: 549000, stock: 8, icon: "⌨️", description: "Switch hot-swap, lampu RGB, koneksi kabel & wireless." },
  { id: 7, name: "Mouse Gaming Swift", category: "Aksesoris", price: 249000, stock: 25, icon: "🖱️", description: "Sensor 16000 DPI dengan 6 tombol yang bisa diatur." },
  { id: 8, name: "Kamera Action GoCam", category: "Kamera", price: 1799000, stock: 0, icon: "📷", description: "Rekam video 4K, tahan air hingga 10 meter." },
];

function readData(key, fallback) {
  try {
    const raw = localStorage.getItem(key);
    return raw ? JSON.parse(raw) : fallback;
  } catch (error) {
    console.error("Gagal membaca data:", error);
    return fallback;
  }
}

function writeData(key, value) {
  try {
    localStorage.setItem(key, JSON.stringify(value));
  } catch (error) {
    console.error("Gagal menyimpan data:", error);
  }
}

// ---------- Produk ----------
function getProducts() {
  const products = readData(STORAGE_KEYS.products, null);
  if (products === null) {
    writeData(STORAGE_KEYS.products, DEFAULT_PRODUCTS);
    return [...DEFAULT_PRODUCTS];
  }
  return products;
}

function saveProducts(products) {
  writeData(STORAGE_KEYS.products, products);
}

function getProductById(id) {
  return getProducts().find((product) => product.id === id);
}

function nextProductId() {
  const products = getProducts();
  return products.length ? Math.max(...products.map((p) => p.id)) + 1 : 1;
}

// ---------- Keranjang: [{ productId, qty }] ----------
function getCart() {
  return readData(STORAGE_KEYS.cart, []);
}

function saveCart(cart) {
  writeData(STORAGE_KEYS.cart, cart);
}

function getCartCount() {
  return getCart().reduce((total, item) => total + item.qty, 0);
}

// ---------- Pesanan ----------
function getOrders() {
  return readData(STORAGE_KEYS.orders, []);
}

function saveOrders(orders) {
  writeData(STORAGE_KEYS.orders, orders);
}

function generateOrderCode() {
  const date = new Date();
  const ymd = date.toISOString().slice(0, 10).replace(/-/g, "");
  const random = Math.floor(1000 + Math.random() * 9000);
  return `INV-${ymd}-${random}`;
}

// Mengembalikan semua data ke kondisi awal
function resetAllData() {
  writeData(STORAGE_KEYS.products, DEFAULT_PRODUCTS);
  writeData(STORAGE_KEYS.cart, []);
  writeData(STORAGE_KEYS.orders, []);
}
