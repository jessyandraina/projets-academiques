## Outil de gestion de projets et de suivi du temps

Projet académique réalisé dans le cadre du cours *Méthodes de gestion de projets informatiques*
(6GEI505) au baccalauréat en génie informatique (UQAC).

### Description
Ce projet consiste à la conception et au développement d’un outil logiciel combinant
des fonctionnalités de **gestion de projets** et de **gestion du temps**, inspiré
des pratiques utilisées en milieu professionnel.

L’application permet de créer des projets structurés en tâches et sous-tâches,
d’assigner des utilisateurs, de suivre l’avancement des travaux et de gérer les
saisies de temps des employés selon leurs rôles et permissions.

Le projet a été réalisé en suivant une **méthode de gestion de projet traditionnelle**
pour les phases d’analyse et de planification, puis une **approche agile** pour le
développement, organisée en sprints.


### Fonctionnalités principales
- Création et gestion de projets hiérarchiques (tâches, sous-tâches)
- Assignation des utilisateurs aux tâches
- Gestion des rôles et des accès (administrateur, gestionnaire, employé)
- Suivi de l’état des tâches (active, en attente, terminée)
- Saisie et consultation du temps travaillé
- Visualisation de l’avancement des projets (diagramme de type Gantt)
- Validation et suivi des feuilles de temps


### Architecture et conception
L’application est développée sous forme d’une **application Java** avec une séparation
claire entre :
- la logique applicative
- l’accès aux données
- l’interface utilisateur

La persistance des données est assurée par une base de données relationnelle
(**SQLite**), permettant de gérer les projets, les utilisateurs, les tâches et les
saisies de temps.

Une attention particulière a été portée à la structuration du code afin de faciliter
l’évolution du système et la maintenance.


### Base de données
La base de données comprend notamment :
- des tables pour les projets, tâches et sous-tâches
- des tables pour les utilisateurs et leurs rôles
- des tables pour les inscriptions et le suivi du temps
- des contraintes d’intégrité assurant la cohérence des données

Un script SQL est fourni pour la création et l’initialisation de la base de données.

### Gestion de projet
Le projet a été réalisé en équipe avec :
- planification des tâches
- estimation du temps
- suivi des écarts entre temps estimé et temps réel
- rédaction de rapports de sprint

Le rapport du sprint final présente la répartition des tâches, les difficultés
rencontrées, les points forts du projet ainsi que les pistes d’amélioration.


### Apprentissages
Ce projet m’a permis de développer et de consolider plusieurs compétences clés :

- Compréhension concrète des **méthodes de gestion de projets informatiques**
- Application d’une **approche agile** (sprints, suivi, rétrospectives)
- Conception d’une application Java structurée et maintenable
- Gestion de la **persistance des données** avec SQLite
- Mise en place d’une **gestion des rôles et des permissions**
- Collaboration en équipe et répartition efficace des responsabilités
- Importance de la documentation et du suivi dans un projet logiciel

### Technologies et concepts
- Java
- SQLite
- SQL
- Gestion de projets informatiques
- Méthodes traditionnelles et agiles
- Diagramme de Gantt
- Gestion des rôles et permissions
- Travail en équipe et suivi de sprint


### Contexte
Projet réalisé en équipe dans un cadre académique. L’accent a été mis autant sur
la **gestion du projet** (planification, documentation, suivi) que sur la
réalisation technique de l’application.
