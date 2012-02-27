@echo off
REM Ce script n'est là que pour transmettre l'appel au lanceur ANT à travers les tests unitaires
REM quelque soit l'emplacement auquel il est installé (pour peu qu'il soit accessible via le PATH).
ant %1 %2 %3 %4 %5 %6 %7 %8 %9
