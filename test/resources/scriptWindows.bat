@echo off

:debut
echo Test script Windows
echo.
echo 1 - Choix 1 : Réponse immédiate
echo 2 - Choix 2 : Réponse sous 1 seconde
echo 3 - Choix 3 : Réponse sous 3 secondes
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
goto debut  

:choix2 
REM le ping permet de patienter N secondes
ping 127.0.0.1 -n 1 > NUL
echo choix2
goto debut  

:choix3 
REM le ping permet de patienter N secondes
ping 127.0.0.1 -n 3 > NUL
echo choix3
goto debut  

:fin 
echo Au revoir !
exit 0