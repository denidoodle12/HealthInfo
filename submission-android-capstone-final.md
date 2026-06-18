# Submission Akhir Capstone: CI, Performance & Security

---

## 📋 Daftar Konten

- [Pengantar](#pengantar)
- [Kriteria](#kriteria)
- [Penilaian](#penilaian)
- [Lainnya](#lainnya)

---

## Pengantar

Selamat! Anda sudah mempelajari beberapa aspek penting dalam mengembangkan sebuah aplikasi seperti **Continuous Integration**, **Performance**, **Security**, dan **Useful Library**.

Untuk lulus dari kelas ini, Anda perlu mengirimkan proyek Capstone dengan **tema bebas sesuka Anda**. Pada submission ini Anda diharuskan untuk menerapkan komponen-komponen yang sudah dipelajari.

---

## Kriteria

Berikut kriteria submission yang harus Anda penuhi.

### 1. Menerapkan Continuous Integration

- Tool yang digunakan bebas (CircleCI, GitHub Action, TravisCI, dll).
- Melakukan test dan build APK dengan **sukses (pass)** pada proses terakhirnya.
- Lampirkan link project CI pada kolom **Catatan** ketika akan mengirimkan tugas.

### 2. Memiliki Performa yang Baik

- Menerapkan **LeakCanary** dan tidak ada memory leaks saat dianalisa.
- Tidak ada issue terkait performance saat dilakukan **Inspect Code**.

### 3. Menerapkan Security

- Menerapkan obfuscation dengan **ProGuard**.
- Menerapkan **encryption** pada database.
- Menerapkan **certificate pinning** untuk koneksi ke server.
- Tuliskan teknik dan lokasi class pada kolom **Catatan** untuk mempercepat proses pengecekan.

### 4. Mempertahankan Syarat dari Submission Sebelumnya

- Memastikan seluruh fitur dan kriteria dari submission sebelumnya tetap berjalan dengan baik.

---

## Penilaian

Submission Anda akan dinilai oleh Reviewer dengan **skala bintang 1–5** berdasarkan parameter yang ada.

Anda dapat menerapkan beberapa saran di bawah ini untuk mendapatkan nilai tinggi.

### Saran untuk Nilai Tinggi

**Menerapkan tampilan aplikasi yang sangat menarik dan sesuai standar:**
- Tampilan aplikasi memiliki width, height, margin, dan padding yang sesuai.
- Pemilihan warna yang sesuai tema aplikasi.
- Tidak ada komponen yang saling bertumpuk.
- Penggunaan komponen yang sesuai dengan fungsinya. Contoh: komponen `ImageView` yang dijadikan sebagai button navigasi.

**Fitur & kode:**
- Menambahkan fitur tambahan selain 3 fitur utama.
- Tidak menggunakan kode yang sama dengan yang di modul — misalnya menggunakan Rx, menggunakan Dagger, atau tidak menggunakan `NetworkBoundResource`.
- Hampir tidak ada issue pada semua aspek saat dilakukan **Inspect Code**.
- Menambahkan **unit test** pada aplikasi.

**CI & Security:**
- Menerapkan Continuous Integration dengan analisis lainnya seperti code coverage, code style, dan vulnerability.
- Menerapkan security selain yang telah diajarkan.

---

## Lainnya

### Submission yang Tidak Sesuai Kriteria

Jika submission Anda tidak sesuai dengan kriteria, maka akan **ditolak** oleh reviewer. Berikut poin-poin yang harus diperhatikan:

- Tidak terdapat 3 fitur utama.
- Tidak menerapkan Continuous Integration untuk test dan build APK.
- Continuous Integration terakhir **gagal**.
- Masih terdapat issue terkait performance saat dilakukan Inspect Code.
- Tidak menerapkan ketiga security yang diajarkan (ProGuard, encryption database, certificate pinning).
- Proyek tidak bisa di-build.
- Aplikasi force closed.
- Mengirimkan file selain proyek Android Studio.
- Mengirimkan proyek yang bukan karya sendiri.
