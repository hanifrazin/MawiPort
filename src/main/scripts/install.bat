@echo off
echo =========================================
echo   Mawiport Global Installer (Windows)
echo =========================================
echo.
echo Menambahkan folder ini ke System PATH...
echo.

:: Menambahkan folder saat ini ke PATH user secara permanen
setx PATH "%PATH%;%CD%"

echo.
echo [SUKSES] Mawiport telah didaftarkan!
echo Silakan TUTUP dan BUKA KEMBALI CMD/PowerShell Anda.
echo Sekarang Anda bisa ketik 'mawi' dari folder mana pun.
echo.
pause