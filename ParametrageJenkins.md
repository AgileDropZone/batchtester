Le lancement d'un traitement batch à travers Jenkins _<sup>ou Hudson</sup>_ est des plus simple.

Si nous avons l'arborescence de workspace suivante pour notre job :
  * bin
    * batchtester.jar
  * resources
    * scriptUNIX.sh
    * Test\_script\_Unix\_OK.scenario

<br>
La configuration du job Jenkins se limite à son appel :<br>
<br>
<img src='http://batchtester.googlecode.com/svn/wiki/images/Jenkins-configuration.png' />

Son lancement nous remonte alors le résultat du test du traitement (<i>en fait du déroulement de l'ensemble du job</i>) :<br>
<br>
<img src='http://batchtester.googlecode.com/svn/wiki/images/Jenkins-res-OK.png' />

et la sortie console reflète le déroulement du traitement :<br>
<br>
<img src='http://batchtester.googlecode.com/svn/wiki/images/Jenkins-console-OK.png' />

<b>Note :</b> La seule précaution à prendre est de s'assurer que la commande de lancement du traitement (dans le fichier scénario) soit renseignée de façon relative par rapport au workspace.<br>
<br>
Ici :<br>
<br>
<img src='http://batchtester.googlecode.com/svn/wiki/images/Jenkins-conf-script.png' />


<br>
<br>
<br>
En cas de problème, l'erreur sera automatiquement remontée dans Jenkins :<br>
<br>
<img src='http://batchtester.googlecode.com/svn/wiki/images/Jenkins-res-KO.png' />

et dans la sortie console rapportera le déroulement du traitement :<br>
<br>
<img src='http://batchtester.googlecode.com/svn/wiki/images/Jenkins-console-KO.png' />