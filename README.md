# StudyWisely

StudyWisely est une application mobile Android conçue pour aider les étudiants à mieux organiser leurs périodes de révision en tenant compte de leurs tâches, de leurs échéances, du temps disponible et du lieu d’étude.

L’objectif du projet était d’aller au-delà d’une simple liste de tâches en proposant un outil capable d’associer chaque routine à une date, une priorité, un lieu et un rappel.

---

## Aperçu de l’application

StudyWisely permet à l’utilisateur de créer et de gérer ses routines de révision à partir d’une interface claire et accessible.

Chaque routine peut contenir :

- un titre;
- une description;
- une date et une heure de réalisation;
- une date d’examen ou de remise;
- une priorité calculée automatiquement;
- un lieu d’étude sélectionné sur une carte;
- une notification programmée à l’heure prévue.

---

## Captures d’écran

### Liste des routines

L’écran principal présente les routines enregistrées, leur niveau de priorité, la date prévue de réalisation ainsi que l’échéance associée.

![Liste des routines StudyWisely](images/liste-routines.png)

### Sélection d’un lieu

L’utilisateur peut rechercher ou sélectionner un lieu sur Google Maps afin de l’associer à une routine et d’obtenir un itinéraire vers celui-ci.

![Sélection d’un lieu avec Google Maps](images/carte-studywisely.png)

---

## Fonctionnalités principales

- Affichage de la liste des routines
- Création d’une nouvelle routine
- Modification d’une routine existante
- Suppression avec demande de confirmation
- Stockage local des données
- Sélection de la date et de l’heure d’une routine
- Ajout d’une date d’examen ou de remise
- Calcul automatique de la priorité selon la proximité de l’échéance
- Sélection d’un lieu sur Google Maps
- Recherche d’un lieu par son nom
- Génération d’un itinéraire vers le lieu choisi
- Programmation de notifications à l’heure de la routine

### Calcul de la priorité

La priorité est calculée automatiquement selon le temps restant avant l’examen ou la remise :

- **Élevée** : échéance dans trois jours ou moins
- **Moyenne** : échéance dans sept jours ou moins
- **Faible** : échéance dans plus de sept jours

---

## Technologies utilisées

- **Kotlin** — langage principal
- **Jetpack Compose** — création des interfaces utilisateur
- **Room** — stockage local des routines
- **SQLite** — base de données sous-jacente
- **MVVM** — organisation de l’architecture
- **Jetpack Navigation** — navigation entre les écrans
- **Google Maps API** — recherche et sélection de lieux
- **Geocoder Android** — conversion d’un nom de lieu en coordonnées
- **AlarmManager** — programmation des rappels
- **Notifications Android** — affichage des notifications
- **Coroutines et Flow** — opérations asynchrones et mise à jour des données
- **Figma** — conception et amélioration du prototype
- **Git et GitHub** — gestion des versions et collaboration

---

## Architecture

L’application suit une architecture de type **MVVM — Model, View, ViewModel**.

```text
app/
└── src/main/
    ├── java/com/example/studywisely/
    │   ├── model/
    │   │   ├── data/
    │   │   ├── local/
    │   │   └── notifications/
    │   ├── repository/
    │   ├── ui/
    │   │   ├── components/
    │   │   ├── navigation/
    │   │   ├── screens/
    │   │   └── theme/
    │   ├── viewmodel/
    │   └── MainActivity.kt
    └── res/
