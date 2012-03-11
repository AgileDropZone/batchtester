#!/bin/bash

pause()
{
   echo "Appuyez sur une touche pour continuer..."   
   read dummy
}

choix1()
{
   printf "choix1\n"
   pause
}

choix2()
{
   sleep 1
   printf "choix2\n"
   pause
}

choix3() {
   sleep 3
   printf "choix3\n"
   pause
}

sortie() {
   printf "Au revoir !\n"
   return 0
}

menu() {
   printf "Test script Unix/Linux\n"
   printf "\n"
   printf "1 - Choix 1 : Réponse immédiate\n"
   printf "2 - Choix 2 : Réponse sous 1 seconde\n"
   printf "3 - Choix 3 : Réponse sous 3 secondes\n"
   printf "\n"
   printf "0 - Sortie\n"
   printf "\n"

   read choix
   case $choix in
      1)
         choix1
         menu;;
      2)
         choix2
         menu;;
      3)
         choix3
         menu;;
      0)
         sortie;;
      *) menu;;
   esac
}

menu
