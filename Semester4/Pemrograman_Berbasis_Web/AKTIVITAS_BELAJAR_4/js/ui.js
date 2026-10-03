/* ==========================================================
   ui.js — helper tampilan yang dipakai semua halaman:
   format Rupiah, toast, modal konfirmasi, badge keranjang,
   dan tema gelap/terang.
   ========================================================== */

function formatRupiah(number) {
  return new Intl.NumberFormat("id-ID", {
    style: "currency",
    currency: "IDR",
    minimumFractionDigits: 0,
  }).format(number);
}

// Mencegah input pengguna dieksekusi sebagai HTML
function escapeHtml(text) {
  const div = document.createElement("div");
  div.textContent = text;
  return div.innerHTML;
}

// ---------- Toast (notifikasi kecil di pojok layar) ----------
function showToast(message, type = "info") {
  let container = document.querySelector(".toast-container");
  if (!container) {
    container = document.createElement("div");
    container.className = "toast-container";
    container.setAttribute("role", "status");
    container.setAttribute("aria-live", "polite");
    document.body.appendChild(container);
  }

  const toast = document.createElement("div");
  toast.className = `toast ${type}`;
  toast.textContent = message;
  container.appendChild(toast);

  setTimeout(() => {
    toast.classList.add("hide");
    toast.addEventListener("animationend", () => toast.remove());
  }, 3000);
}

// ---------- Modal konfirmasi (pengganti window.confirm) ----------
// Mengembalikan Promise<boolean>: true jika pengguna menekan "Ya".
function confirmDialog(message, { title = "Konfirmasi", okText = "Ya", danger = false } = {}) {
  return new Promise((resolve) => {
    const dialog = document.createElement("dialog");
    dialog.innerHTML = `
      <div class="modal-header">
        <h2>${escapeHtml(title)}</h2>
      </div>
      <div class="modal-body"><p>${escapeHtml(message)}</p></div>
      <div class="modal-footer">
        <button type="button" class="btn btn-outline" data-answer="no">Batal</button>
        <button type="button" class="btn ${danger ? "btn-danger" : "btn-primary"}" data-answer="yes">${escapeHtml(okText)}</button>
      </div>`;
    document.body.appendChild(dialog);

    const close = (answer) => {
      dialog.close();
      dialog.remove();
      resolve(answer);
    };

    dialog.addEventListener("click", (event) => {
      const answer = event.target.dataset.answer;
      if (answer) close(answer === "yes");
    });
    dialog.addEventListener("cancel", (event) => {
      event.preventDefault();
      close(false);
    });

    dialog.showModal();
  });
}

// Menutup <dialog> ketika area gelap di luar kotak modal diklik
function enableBackdropClose(dialog) {
  dialog.addEventListener("click", (event) => {
    if (event.target === dialog) dialog.close();
  });
}

// ---------- Badge jumlah item keranjang di navigasi ----------
function updateCartBadge() {
  const badge = document.getElementById("cart-badge");
  if (badge) badge.textContent = getCartCount();
}

// ---------- Tema gelap / terang ----------
function applyTheme(theme) {
  document.documentElement.setAttribute("data-theme", theme);
  const toggle = document.getElementById("theme-toggle");
  if (toggle) {
    toggle.textContent = theme === "dark" ? "☀️" : "🌙";
    toggle.setAttribute("aria-label", theme === "dark" ? "Gunakan tema terang" : "Gunakan tema gelap");
  }
}

function initTheme() {
  applyTheme(localStorage.getItem(STORAGE_KEYS.theme) || "light");

  const toggle = document.getElementById("theme-toggle");
  if (!toggle) return;
  toggle.addEventListener("click", () => {
    const next = document.documentElement.getAttribute("data-theme") === "dark" ? "light" : "dark";
    localStorage.setItem(STORAGE_KEYS.theme, next);
    applyTheme(next);
  });
}

// Dijalankan di setiap halaman
document.addEventListener("DOMContentLoaded", () => {
  initTheme();
  updateCartBadge();
  const year = document.getElementById("year");
  if (year) year.textContent = new Date().getFullYear();
});
