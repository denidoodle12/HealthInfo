# HealthInfo — Android Expert Submission 1

Aplikasi berita kesehatan berbasis Android yang mengonsumsi [NewsAPI](https://newsapi.org) dengan arsitektur **Clean Architecture** dan **Multi-Module** (app, core, favorite dynamic feature).

---

## 🔑 API Key Setup (Penting untuk Reviewer)

Project ini menggunakan API key dari [NewsAPI.org](https://newsapi.org) yang disimpan di file `local.properties` pada root project.

File `local.properties` **sudah disertakan** dalam repository ini dan berisi API key yang aktif:

```properties
API_KEY=2c22331536494c1cb9833674128f9bb3
```

> Jika terjadi error `apiKeyInvalid`, pastikan file `local.properties` terbaca dan memiliki baris `API_KEY=...` di atas.

Jika Anda perlu menggunakan API key sendiri, daftar gratis di [https://newsapi.org/register](https://newsapi.org/register), lalu ganti nilai `API_KEY` di file `local.properties`.

---

## 🏗️ Arsitektur

- **Clean Architecture** — Data → Domain → Presentation layer
- **Multi-Module**: `app` (presentation), `core` (data & domain), `favorite` (dynamic feature)
- **Kotlin Coroutines + Flow** — Reactive data streaming
- **Room** — Local database untuk menyimpan artikel favorit
- **Retrofit** — Network layer untuk NewsAPI
- **Koin** — Dependency Injection
- **Navigation**: Single-activity per module, back-stack dikelola di `MainActivity`

---

## ✨ Fitur

| Fitur | Keterangan |
|---|---|
| **Daftar Artikel** | Menampilkan top health headlines dari NewsAPI |
| **Search** | Pencarian real-time via API dengan debounce 400ms |
| **Detail Artikel** | Tampilkan detail lengkap dengan gambar, judul, author, deskripsi |
| **Favorit** | Simpan & hapus artikel favorit ke database lokal (Room) |
| **Sinkronisasi Favorit** | Icon bookmark di daftar utama otomatis update saat status favorit berubah |
| **Loading State** | Indikator loading di setiap halaman |
| **Empty State** | Placeholder informatif saat data kosong atau pencarian tidak ditemukan |
| **Error State** | Tampilan error dengan tombol "Try Again" |
| **Swipe to Refresh** | Pull-down untuk memuat ulang data terbaru |
| **Animasi Transisi** | Slide & fade animation antar halaman |

---

## ⚙️ Cara Menjalankan

1. Clone repository ini
2. Pastikan file `local.properties` ada di root project dengan isi:
   ```
   sdk.dir=/path/to/your/Android/Sdk
   API_KEY=2c22331536494c1cb9833674128f9bb3
   ```
3. Sync Gradle (`File > Sync Project with Gradle Files`)
4. Jalankan app di emulator atau device (min SDK 24)

---

## 📦 Modul

```
├── app/          → Main module (MainActivity, DetailActivity)
├── core/         → Library module (data, domain, UI components)
└── favorite/     → Dynamic Feature Module (FavoriteActivity)
```

---

## 🛡️ Keamanan

- API key disimpan di `local.properties` dan dibaca via `BuildConfig` saat build time
- `HttpLoggingInterceptor` hanya aktif di build **debug** (BODY level), **release** tidak ada logging
- ProGuard/R8 aktif di build release
