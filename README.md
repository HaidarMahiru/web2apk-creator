# Web2APK Creator CLI

Alat baris perintah (CLI) berbasis Node.js untuk mengubah situs web menjadi aplikasi Android Web Viewer secara instan. Hasil *reverse engineering* dari aplikasi *Bikin Aplikasi Web Viewer*.

## Persyaratan Sistem

Pastikan perangkat Anda (Termux / Linux) sudah terpasang:
1. **Node.js**
2. **Java Development Kit (JDK 21+)** (dibutuhkan untuk `javac`, `jar`)
3. **unzip**
4. **apksigner** (dari Android SDK / Build Tools)

## Instalasi & Persiapan

1. Clone repositori ini.
2. Compile alat patcher manifes biner:
   ```bash
   mkdir -p build_patcher
   javac -d build_patcher patcher_src/ManifestPatcher.java patcher_src/com/muhfau/bikinaplikasi/helper/*.java
   ```

## Cara Penggunaan

Jalankan perintah berikut:

```bash
node build_apk.js <AppName> <URL> <PackageName> <IconPath> [OutputApkPath]
```

### Parameter:
* `<AppName>`: Nama aplikasi yang akan tampil di HP (contoh: `"HaidarOTP"`).
* `<URL>`: URL website tujuan (contoh: `"https://haidarshop.my.id"`).
* `<PackageName>`: Nama paket aplikasi unik (contoh: `"com.haidar.otp"`).
* `<IconPath>`: Jalur ke file gambar ikon, disarankan ukuran <= 512x512 piksel (contoh: `"foto.jpg"`).
* `[OutputApkPath]` (Opsional): Nama file APK hasil build. Jika dikosongkan, otomatis menggunakan `<AppName>.apk`.

### Contoh:
```bash
node build_apk.js "HaidarOTP" "https://haidarshop.my.id" "com.haidar.otp" "foto.jpg"
```

## Fitur Template
* **Swipe to Refresh**: Aktif secara bawaan.
* **Tanda Tangan Digital**: Otomatis ditandatangani menggunakan Keystore bawaan (`mmdfauzan.key`), sehingga APK langsung siap diinstal di perangkat Android.
* **Ringan**: Repositori ini sangat ringan (~3.2 MB) karena tidak menyertakan APK pembuat yang berat.
