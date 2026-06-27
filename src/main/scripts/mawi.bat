@echo off
echo =========================================
echo   Mawiport Global Installer (Windows)
echo =========================================
echo.
echo Mendaftarkan Mawiport ke PATH User secara aman...

:: Menggunakan PowerShell untuk manipulasi PATH yang aman (Mencegah bug truncation setx)
powershell -Command "$p=[Environment]::GetEnvironmentVariable('Path','User'); $d='%CD%'; if($p -notlike \"*$d*\"){[Environment]::SetEnvironmentVariable('Path',\"$p;$d\",'User'); Write-Host '[SUKSES] Path berhasil ditambahkan!' -ForegroundColor Green}else{Write-Host '[INFO] Path sudah terdaftar sebelumnya.' -ForegroundColor Yellow}"

echo.
echo =========================================
echo PENTING: Tutup CMD/PowerShell ini, lalu buka yang baru!
echo =========================================
pause