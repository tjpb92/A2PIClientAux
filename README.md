# A2PIClientAux
Programme Java auxiliaire de a2pi-client permettant d?échanger des données entre Anstel et Performance Immo (lien montant)

## Utilisation:
```
java A2PIClientAux [-ifxdb prod|pre-prod] [-apiserver apiserver] [-d] [-t] 
```
où :
* ```-ifxdb prod|pre-prod``` est la référence à la base de données Informix, par défaut désigne la base de données de pré-production, voir fichier *A2PIClientAux.prop* (optionnel).
* ```-apiserver prod|pre-prod``` est la référence au serveur d'API distant, par défaut désigne le serveur de pré-production, voir fichier *A2PIClientAux.prop* (paramètre optionnel).
* ```-d``` le programme s'exécute en mode débug, il est beaucoup plus verbeux. Désactivé par défaut (paramètre optionnel).
* ```-t``` le programme s'exécute en mode test, les transactions en base de données ne sont pas faites. Désactivé par défaut (paramètre optionnel).

## Pré-requis :
- Java 8 ou supérieur.
- Driver MongoDB
- [xmlbeans-2.6.0.jar](https://xmlbeans.apache.org/)
- [commons-collections4-4.1.jar](https://commons.apache.org/proper/commons-collections/download_collections.cgi)
- [junit-4.12.jar] (https://github.com/junit-team/junit4/releases/tag/r4.12)
- [hamcrest-2.1.jar] (https://search.maven.org/search?q=g:org.hamcrest)

## Fichier des paramètres : 

Ce fichier permet de spécifier les paramètres d'accès aux différentes bases de données.

A adapter selon les implémentations locales.

Ce fichier est nommé : *A2PIClientAux.prop*.

Le fichier *A2PIClientAux_Example.prop* est fourni à titre d'exemple.

## Références:

- [Pour valider un Json] (https://jsonformatter.curiousconcept.com/)
