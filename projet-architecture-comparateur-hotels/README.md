## Logiciel de comparaison de prix d’hôtels – Architecture logicielle

Projet académique réalisé dans le cadre du cours *Architecture des logiciels* au
baccalauréat en génie informatique (UQAC).

### Description
Ce projet consiste à la conception et à l’évaluation de l’architecture d’un
logiciel de comparaison de prix d’hôtels. L’objectif du système est de permettre
aux utilisateurs de rechercher des hôtels, de comparer leurs prix et d’effectuer
des réservations à partir d’une interface web.

Le travail couvre l’ensemble du processus architectural, incluant l’analyse des
exigences, la proposition d’une architecture initiale, la réalisation d’un
prototype (Proof of Concept) et l’évaluation de l’architecture selon différents
attributs de qualité.

### Architecture proposée
L’application repose sur une architecture en couches avec une séparation claire
des responsabilités entre les différents composants du système :
- un frontend web développé avec **Next.js**
- un backend basé sur **Flask**
- une base de données **MongoDB**

Les communications entre le frontend et le backend sont assurées par une API
REST. L’architecture met en œuvre des contrôleurs, des services et des
repositories afin de favoriser la modularité, la maintenabilité et l’évolution
du système.

### Attributs de qualité évalués
L’architecture a été évaluée principalement selon les attributs de qualité
suivants :
- **Modificabilité** : séparation des responsabilités, usage d’abstractions et
  injection de dépendances facilitant l’ajout ou la modification de
  fonctionnalités
- **Déployabilité** : compatibilité multiplateforme et déploiement accessible via
  un navigateur web
- **Utilisabilité** : scénarios utilisateurs clairs et structure favorisant une
  interface simple à faire évoluer

Une analyse plus détaillée de ces attributs est présentée dans le document
`ARCHITECTURE_EVALUATION.md`.

### Technologies et concepts
- Architecture logicielle
- Flask (backend)
- Next.js (frontend)
- MongoDB
- API REST
- Injection de dépendances
- Modélisation UML
- Proof of Concept (PoC)

### Contexte
Projet réalisé en équipe dans un cadre académique. L’accent a été mis sur la
qualité de la conception architecturale, la justification des choix techniques et
l’évaluation du système à l’aide de scénarios concrets.
