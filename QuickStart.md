Ce guide de démarrage rapide va vous permettre de mettre en place un scénario de test/de lancement automatique d'un traitement batch à l'aide de batchtester.

### Préparation du scénario : ###
  1. Lancer le traitement dont le lancement doit être automatisé.
  1. Créer un fichier scénario en s'appuyant sur la liste des commandes gérées http://code.google.com/p/batchtester/wiki/CommandesGerees, et sur un exemple de fichier scénario http://code.google.com/p/batchtester/source/browse/trunk/test/resources/Test_script_Windows_OK.scenario (qui teste le batch http://code.google.com/p/batchtester/source/browse/trunk/test/resources/scriptWindows.bat).
Pour commencer, il est nécessaire de renseigner :
  * la commande d'appel du traitement/script à lancer.
  * les informations remontées par le traitement qui permettent de synchroniser le déroulement du scénario avec celui du traitement. En général, la dernière ligne d'un menu ou une demande de choix. Cela fera l'objet de commandes '`WAITFOR`'.
  * les réponses à saisir pour poursuivre les différentes phases du traitement. Cela fera l'objet de commandes '`INJECT`'.
A ce niveau, si une documentation a été rédigée sur ledit traitement, elle peut s'avérer d'une aide précieuse (cela permettra aussi de la valider ;-)).

Avec ces informations, vous pouvez déjà automatiser le déroulement d'un traitement batch interactif et valider le déroulement nominal.

### Lancement : ###
  1. Télécharger le module batchtester.jar ici : http://code.google.com/p/batchtester/downloads/list.
  1. Pour ne pas avoir à modifier vos commandes de lancement suite à une mise à jour de la version, je vous recommande de renommer le fichier obtenu en `batchtester.jar`.
  1. Lancer `java -jar batchtester.jar <nomFichierScenario.scenario>`.
Voilà. C'est tout ... vous souhaitiez quelque chose de plus compliqué ?