/* ==========================================================
   validation.js — validasi form yang bisa dipakai ulang.

   Cara pakai:
     const schema = {
       nama:  [Rules.required(), Rules.minLength(3)],
       email: [Rules.required(), Rules.email()],
     };
     if (validateForm(form, schema)) { ...simpan... }

   Setiap field butuh elemen <small id="{nama-field}-error">
   untuk menampilkan pesan error.
   ========================================================== */

// Setiap aturan mengembalikan fungsi: (nilai) => pesan error, atau "" jika valid
const Rules = {
  required: (message = "Kolom ini wajib diisi.") => (value) =>
    value.trim() !== "" ? "" : message,

  minLength: (min) => (value) =>
    value.trim().length >= min ? "" : `Minimal ${min} karakter.`,

  email: () => (value) =>
    /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/.test(value.trim())
      ? ""
      : "Format email tidak valid (contoh: nama@email.com).",

  phone: () => (value) =>
    /^(08|\+628)\d{8,11}$/.test(value.replace(/[\s-]/g, ""))
      ? ""
      : "Nomor HP diawali 08 atau +628 dan berisi 10–13 digit.",

  minNumber: (min, message) => (value) =>
    value !== "" && Number(value) >= min ? "" : message || `Nilai minimal ${min}.`,

  integer: () => (value) =>
    Number.isInteger(Number(value)) ? "" : "Harus berupa bilangan bulat.",
};

// Elemen yang diberi tanda merah: input biasa, atau <fieldset> untuk grup radio
function getFieldElement(form, name) {
  const field = form.elements[name];
  if (field instanceof RadioNodeList) {
    return field[0].closest("fieldset");
  }
  return field;
}

function setFieldError(form, name, message) {
  const element = getFieldElement(form, name);
  const errorEl = document.getElementById(`${name}-error`);

  element.classList.toggle("invalid", Boolean(message));
  element.setAttribute("aria-invalid", message ? "true" : "false");
  if (errorEl) errorEl.textContent = message;
}

// Menjalankan aturan satu per satu, berhenti di error pertama
function validateField(form, name, rules) {
  const value = form.elements[name].value;
  for (const rule of rules) {
    const message = rule(value);
    if (message) {
      setFieldError(form, name, message);
      return false;
    }
  }
  setFieldError(form, name, "");
  return true;
}

function validateForm(form, schema) {
  let firstInvalid = null;

  Object.entries(schema).forEach(([name, rules]) => {
    const isValid = validateField(form, name, rules);
    if (!isValid && !firstInvalid) firstInvalid = name;
  });

  if (firstInvalid) {
    const field = form.elements[firstInvalid];
    (field instanceof RadioNodeList ? field[0] : field).focus();
    showToast("Periksa kembali isian yang ditandai merah.", "error");
    return false;
  }
  return true;
}

// Validasi langsung saat pengguna keluar dari field / mengetik ulang
function attachLiveValidation(form, schema) {
  Object.entries(schema).forEach(([name, rules]) => {
    const field = form.elements[name];
    const inputs = field instanceof RadioNodeList ? [...field] : [field];

    inputs.forEach((input) => {
      if (input.type === "radio") {
        input.addEventListener("change", () => validateField(form, name, rules));
        return;
      }
      input.addEventListener("blur", () => validateField(form, name, rules));
      input.addEventListener("input", () => {
        if (getFieldElement(form, name).classList.contains("invalid")) {
          validateField(form, name, rules);
        }
      });
    });
  });
}

function clearFormErrors(form, schema) {
  Object.keys(schema).forEach((name) => setFieldError(form, name, ""));
}
