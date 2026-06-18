# HealthInfo — Android Expert Capstone Submission

Aplikasi berita kesehatan berbasis Android yang mengonsumsi [NewsAPI](https://newsapi.org) dengan arsitektur **Clean Architecture** dan **Multi-Module** (app, core, favorite dynamic feature).

---

## 🔑 API Key Setup (Penting untuk Reviewer)

Project ini menggunakan API key dari [NewsAPI.org](https://newsapi.org) yang disimpan di file `local.properties` pada root project.

File `local.properties` **sudah disertakan** dalam repository ini dan berisi API key yang aktif.

> Jika terjadi error `apiKeyInvalid`, pastikan file `local.properties` terbaca dan memiliki baris `API_KEY=...` di root project.

Jika Anda perlu menggunakan API key sendiri, daftar gratis di [https://newsapi.org/register](https://newsapi.org/register), lalu ganti nilai `API_KEY` di file `local.properties`.

---

## 🏗️ Arsitektur

- **Clean Architecture** — Data → Domain → Presentation layer
- **Multi-Module**: `app` (presentation), `core` (data & domain), `favorite` (dynamic feature)
- **Kotlin Coroutines + Flow** — Reactive data streaming
- **Room** — Local database untuk menyimpan artikel favorit (terenkripsi dengan SQLCipher)
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
| **Favorit** | Simpan & hapus artikel favorit ke database lokal (Room + SQLCipher) |
| **Sinkronisasi Favorit** | Icon bookmark di daftar utama otomatis update saat status favorit berubah |
| **About Page** | Halaman informasi developer dengan profil dan tech stack |
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
   API_KEY=<your_api_key>
   ```
3. Sync Gradle (`File > Sync Project with Gradle Files`)
4. Jalankan app di emulator atau device (min SDK 24)

---

## 📦 Modul

```
├── app/          → Main module (MainActivity, DetailActivity, AboutActivity)
├── core/         → Library module (data, domain, UI components, security)
└── favorite/     → Dynamic Feature Module (FavoriteActivity)
```

---

## 🛡️ Keamanan (Security)

| Teknik | Lokasi Class / File | Keterangan |
|---|---|---|
| **ProGuard / R8** | `app/proguard-rules.pro` | Obfuscation & shrinking aktif di release build (`isMinifyEnabled = true`) |
| **Database Encryption** | `core/src/main/.../di/CoreModule.kt` — `databaseModule` | SQLCipher (`net.sqlcipher`) + passphrase disimpan di `EncryptedSharedPreferences` (AES-256-GCM) |
| **Certificate Pinning** | `core/src/main/.../di/CoreModule.kt` — `networkModule` | OkHttp `CertificatePinner` dengan SHA-256 SPKI pin untuk `newsapi.org` (leaf + intermediate) |
| **Encrypted Preferences** | `core/src/main/.../di/CoreModule.kt` — `getOrCreatePassphrase()` | AndroidX Security Crypto `EncryptedSharedPreferences` untuk menyimpan DB passphrase |
| **API Key Protection** | `app/build.gradle.kts` | API key dibaca dari `local.properties` dan diinjeksi via `BuildConfig` — tidak hardcoded di source code |
| **No Logging in Release** | `core/.../di/CoreModule.kt` — `networkModule` | `HttpLoggingInterceptor.Level.NONE` di release build |

---

## 🔄 Continuous Integration

Pipeline CI menggunakan **CircleCI** dengan tahapan:
1. **Run unit tests** — `:app:testDebugUnitTest :core:testDebugUnitTest`
2. **Run lint analysis** — `:app:lint :core:lint`
3. **Build debug APK** — `:app:assembleDebug`

> Link CircleCI project: [Lihat di kolom Catatan saat submit]

---

## 🧪 Unit Tests

Unit tests tersedia di `core/src/test/` mencakup:

- **`HeadlinesInteractorTest`** — 10 test cases untuk semua method di domain use case:
  - `getAllHeadlines` (success, error, query)
  - `getFavoriteHeadlines` (with data, empty)
  - `isHeadlineFavorite` (true/false)
  - `insertFavoriteHeadlines`
  - `deleteFavoriteHeadlines`

Menjalankan tests:
```bash
./gradlew :core:testDebugUnitTest
```
