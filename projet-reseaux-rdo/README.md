## Protocole RDO – Transfert de fichiers en réseau (Java)

Projet académique réalisé dans le cadre du cours *Réseaux d’ordinateurs* au
baccalauréat en génie informatique (UQAC).

### Description
Ce projet consiste à la conception et à l’implémentation d’un protocole simplifié
de transfert de fichiers nommé **RDO (Remote Data Operations)**, basé sur une
architecture client–serveur utilisant le protocole TCP.

Le protocole permet à un client de s’enregistrer auprès d’un serveur, de consulter
la liste des fichiers disponibles, de téléverser et de télécharger des fichiers.
Un mécanisme de redirection (**READ-REDIRECT**) permet également l’accès à des
fichiers hébergés sur des serveurs pairs distants.

### Fonctionnalités principales
- Architecture client–serveur en Java
- Communication réseau via sockets TCP
- Gestion de jetons d’authentification
- Transfert de fichiers par fragments de taille fixe
- Redirection vers des serveurs pairs
- Gestion de connexions multiples par multi-threading

### Technologies et concepts
- Java
- Sockets TCP
- Programmation multi-thread
- Protocoles applicatifs
- Modélisation UML (diagrammes de classes et de séquence)

### Contexte
Projet réalisé en équipe dans un cadre académique.
