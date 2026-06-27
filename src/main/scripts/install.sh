#!/usr/bin/env bash

echo "========================================="
echo "  Mawiport Global Installer (Smart)      "
echo "========================================="
echo ""

# 1. Deteksi file konfigurasi yang FISIK-NYA ada di laptop user
if [ -f "$HOME/.zshrc" ]; then
    SHELL_RC="$HOME/.zshrc"
    echo "🍎 Terdeteksi pengguna Mac (Zsh). Target: $SHELL_RC"
elif [ -f "$HOME/.bashrc" ]; then
    SHELL_RC="$HOME/.bashrc"
    echo "🐧 Terdeteksi pengguna Linux (Bash). Target: $SHELL_RC"
else
    SHELL_RC="$HOME/.profile"
    echo "⚠️ Fallback ke: $SHELL_RC"
fi

# 2. Cegah duplikasi PATH jika script tidak sengaja dijalankan 2x
if grep -q "Mawiport CLI Path" "$SHELL_RC" 2>/dev/null; then
    echo "ℹ️ Path Mawiport sudah pernah didaftarkan sebelumnya."
else
    echo "" >> "$SHELL_RC"
    echo "# Mawiport CLI Path" >> "$SHELL_RC"
    echo "export PATH=\"$PWD:\$PATH\"" >> "$SHELL_RC"
    echo "✅ Path berhasil disuntikkan!"
fi

echo ""
echo "========================================="
echo " [SUKSES] Instalasi Selesai!"
echo "========================================="
echo "⚠️ PENTING: Terminal yang sedang terbuka TIDAK BISA membaca perubahan secara otomatis."
echo "Silakan TUTUP tab terminal ini, lalu buka terminal baru."
echo "Atau jalankan: source $SHELL_RC"
echo ""
echo "Setelah itu, Anda bisa panggil 'mawi' dari mana saja tanpa './'!"