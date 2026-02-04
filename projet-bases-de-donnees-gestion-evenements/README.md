## Système de gestion d’événements – Bases de données

Projet académique réalisé dans le cadre du cours *Introduction aux bases de données*
au baccalauréat en génie informatique (UQAC).

### Description
Ce projet consiste à la conception et à l’implémentation d’un système de gestion
d’événements permettant de gérer des participants, des événements, des lieux,
des organisateurs, des catégories ainsi que les inscriptions aux événements.

Le projet couvre l’ensemble du processus de conception d’une base de données
relationnelle, depuis le modèle conceptuel jusqu’à l’implémentation SQL et la
manipulation des données via des interfaces applicatives.

### Modélisation des données
La conception repose sur un **Modèle Conceptuel de Données (MCD)** comprenant
les entités principales suivantes :
- Participant
- Événement
- Lieu
- Organisateur
- Catégorie
- Inscription

Les relations entre les entités incluent notamment des relations **N:N**
(participants–événements) gérées à l’aide d’une table d’association.  
Le modèle a ensuite été traduit en **schéma relationnel** avec la définition
des clés primaires et des clés étrangères.

### Implémentation SQL
La base de données a été implémentée à l’aide de **MySQL**, incluant :
- la création de la base de données et des tables (DDL)
- la définition de contraintes d’intégrité référentielle
- la création d’index pour l’optimisation des requêtes
- l’insertion de données de test
- des requêtes SQL complexes avec jointures, agrégations et sous-requêtes

### Fonctionnalités et traitements
Le système permet notamment :
- l’inscription de participants à des événements
- la création, modification et suppression d’événements
- la gestion des catégories d’événements
- la consultation des inscriptions et des événements

Des **champs calculés** sont utilisés pour fournir des informations dynamiques,
comme :
- le nombre total d’inscriptions à un événement
- le montant total payé par un participant
- l’ancienneté d’une inscription

### Transactions
Des instructions **TCL (COMMIT, ROLLBACK)** ont été intégrées afin d’assurer la
cohérence des données lors des opérations critiques.

### Interfaces applicatives
Une interface applicative a été développée afin de démontrer les opérations
CRIM (Create, Read, Insert, Modify) sur les principales entités du système,
notamment pour la gestion des participants et des événements.

### Technologies et concepts
- Bases de données relationnelles
- MySQL
- SQL (DDL, DML, TCL)
- Modélisation conceptuelle (MCD)
- Schéma relationnel
- Contraintes d’intégrité
- Indexation
- Transactions
- Interfaces CRIM

### Contexte
Projet réalisé en équipe dans un cadre académique. L’accent a été mis sur la
rigueur de la modélisation, la cohérence des données et la qualité des requêtes
SQL, ainsi que sur la validation du système à l’aide de scénarios concrets.
