@echo off

:debut
echo TST 1
echo.
echo 1 - Choix 1
echo 2 - Choix 2
echo 3 - Choix 3
echo.
echo 0 - Sortie
echo.
set choix=""
set /p choix= 
if "%choix%"=="1" goto choix1 
if "%choix%"=="2" goto choix2 
if "%choix%"=="3" goto choix3 
if "%choix%"=="0" goto fin 
echo choix inconnu : "%choix%"
goto debut  

:choix1 
echo choix1
REM le ping permet de patienter N secondes
ping 127.0.0.1 -n 5 > NUL
goto debut  

:choix2 
echo choix2
goto debut  

:choix3 
echo choix3
goto debut  

:fin 
echo Au revoir !
exit 0