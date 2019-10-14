# A2PIClientAux
Programme Java auxiliaire de a2pi-client permettant d'�changer des donn�es entre Anstel et Performance Immo (lien montant)

## Utilisation:
```
java A2PIClientAux [-ifxdb prod|pre-prod] [-apiserver prod|pre-prod] [-limit limit] [-d] [-t] 
```
o� :
* ```-ifxdb prod|pre-prod``` est la r�f�rence � la base de donn�es Informix, par d�faut d�signe la base de donn�es de pr�-production, voir fichier *A2PIClientAux.prop* (optionnel).
* ```-apiserver prod|pre-prod``` est la r�f�rence au serveur d'API distant, par d�faut d�signe le serveur de pr�-production, voir fichier *A2PIClientAux.prop* (param�tre optionnel).
* ```-limit limit``` est le nombre maximum d'�v�nement(s) trait�(s) par ex�cution du programme, par d�faut 10.000 (param�tre optionnel).
* ```-d``` le programme s'ex�cute en mode d�bug, il est beaucoup plus verbeux. D�sactiv� par d�faut (param�tre optionnel).
* ```-t``` le programme s'ex�cute en mode test, les transactions en base de donn�es ne sont pas faites. D�sactiv� par d�faut (param�tre optionnel).

## Pr�-requis :
- Java 8 ou sup�rieur.
- Driver MongoDB
- [xmlbeans-2.6.0.jar](https://xmlbeans.apache.org/)
- [commons-collections4-4.1.jar](https://commons.apache.org/proper/commons-collections/download_collections.cgi)
- [junit-4.12.jar](https://github.com/junit-team/junit4/releases/tag/r4.12)
- [hamcrest-2.1.jar](https://search.maven.org/search?q=g:org.hamcrest)

- unirest-java-1.4.9.jar
- httpcomponents-asyncclient-4.1.4.zip
- httpcomponents-client-4.5.10.zip
- httpcomponents-core-4.4.12.zip
- java-json.jar.zip

## Fichier des param�tres : 

Ce fichier permet de sp�cifier les param�tres d'acc�s aux diff�rentes bases de donn�es.

A adapter selon les impl�mentations locales.

Ce fichier est nomm� : *A2PIClientAux.prop*.

Le fichier *A2PIClientAux_Example.prop* est fourni � titre d'exemple.

## R�f�rences:

- [Pour valider un Json](https://jsonformatter.curiousconcept.com/)

### Unirest
[Tuto Unirest #1](https://www.baeldung.com/unirest)
[Tuto Unirest #2](https://fr.slideshare.net/rahulpatel184/unirest-java-tutorial-java-http-client)

### Conversion DateTime en TimeStamp et r�ciproquement
- http://www.javacodex.com/Date-and-Time/Convert-DateTime-into-Timestamp
- http://www.javacodex.com/Date-and-Time/Convert-Timestamp-into-DateTime
