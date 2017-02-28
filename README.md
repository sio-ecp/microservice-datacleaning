# microservice-datacleaning
##Installation
Un **fichier config.properties** contenant les paramètres
de connexion à la base de donnée doit être placé dans
le même dossier que le .jar ou son emplacement passé
en paramètre.

#### Contenu du fichier config.properties
Les paramètres nécessaires sont :
* dburl *exemple: jdbc:mysql://localhost:3306/database*
* dbuser
* dbpassword

## Utilisation
 `java -jar Cleaning-Service.jar [-conf <propertiesFile>] [-port <port>]`
 
 Paramètres :
 * port : port sur lequel le micro-service sera en écoute
 * conf : emplacement du fichier de configuration
 