-- 1. Création de la base
CREATE DATABASE IF NOT EXISTS GestionEvenement;
USE GestionEvenement;

-- 2. Création des tables avec contraintes, auto-incrémentation, légendes et normalisation

-- Table PARTICIPANT : Informations sur les participants aux événements
CREATE TABLE PARTICIPANT (
    id_participant INT PRIMARY KEY AUTO_INCREMENT,
    nom_participant VARCHAR(50) NOT NULL,
    prenom_participant VARCHAR(50) NOT NULL,
    courriel_participant VARCHAR(100) NOT NULL UNIQUE,
    telephone_participant VARCHAR(20) NOT NULL UNIQUE,
    profession VARCHAR(50),
    nombre_billet INT DEFAULT 0 CHECK (nombre_billet >= 0)
);

-- Table ORGANISATEUR : Informations sur les organisateurs d'événements
CREATE TABLE ORGANISATEUR (
    id_organisateur INT PRIMARY KEY AUTO_INCREMENT,
    nom_organisateur VARCHAR(50) NOT NULL,
    prenom_organisateur VARCHAR(50) NOT NULL,
    courriel_organisateur VARCHAR(100) NOT NULL UNIQUE,
    telephone_organisateur VARCHAR(20) NOT NULL UNIQUE,
    organisme VARCHAR(100)
);

-- Table LIEU : Données sur les lieux d'événements
CREATE TABLE LIEU (
    id_lieu INT PRIMARY KEY AUTO_INCREMENT,
    nom_lieu VARCHAR(100) NOT NULL,
    adresse VARCHAR(100) NOT NULL,
    ville VARCHAR(50) NOT NULL,
    capacite INT NOT NULL CHECK (capacite > 0),
    type_lieu VARCHAR(50)
);

-- Table CATEGORIE : Catégories d'événements
CREATE TABLE CATEGORIE (
    id_categorie INT PRIMARY KEY AUTO_INCREMENT,
    nom_categorie VARCHAR(50) NOT NULL,
    description TEXT,
    public_cible VARCHAR(100),
    couleur_affichage VARCHAR(30)
);

-- Table EVENEMENT : Informations principales sur les événements
CREATE TABLE EVENEMENT (
    id_evenement INT PRIMARY KEY AUTO_INCREMENT,
    nom_evenement VARCHAR(100) NOT NULL,
    date_evenement DATE NOT NULL,
    heure TIME NOT NULL,
    type_evenement VARCHAR(50),
    tarif DECIMAL(10,2) DEFAULT 0.00 CHECK (tarif >= 0),
    id_lieu INT,
    id_organisateur INT,
    id_categorie INT,
    FOREIGN KEY (id_lieu) REFERENCES LIEU(id_lieu) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_organisateur) REFERENCES ORGANISATEUR(id_organisateur) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_categorie) REFERENCES CATEGORIE(id_categorie) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Table INSCRIPTION : Association entre participants et événements
CREATE TABLE INSCRIPTION (
    id_participant INT,
    id_evenement INT,
    date_inscription DATE NOT NULL,
    mode_paiement VARCHAR(30),
    montant_paye DECIMAL(10,2) CHECK (montant_paye >= 0),
    statut VARCHAR(30),
    PRIMARY KEY (id_participant, id_evenement),
    FOREIGN KEY (id_participant) REFERENCES PARTICIPANT(id_participant) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_evenement) REFERENCES EVENEMENT(id_evenement) ON DELETE CASCADE ON UPDATE CASCADE
);

-- 3. Index pour optimisation des recherches
CREATE INDEX idx_nom_evenement ON EVENEMENT(nom_evenement);
CREATE INDEX idx_date_evenement ON EVENEMENT(date_evenement);
CREATE INDEX idx_nom_participant ON PARTICIPANT(nom_participant);
CREATE INDEX idx_evenement_inscription ON INSCRIPTION(id_evenement, date_inscription);

-- Affichage des index créés
SHOW INDEX FROM EVENEMENT;
SHOW INDEX FROM PARTICIPANT;
SHOW INDEX FROM INSCRIPTION;

-- 4. Données d'exemple + Affichage

-- PARTICIPANT
INSERT INTO PARTICIPANT (nom_participant, prenom_participant, courriel_participant, telephone_participant, profession, nombre_billet) VALUES
('Alex', 'Dipama', 'alex.dipama@email.com', '5811234567', 'Étudiant', 1),
('Diallo', 'Fatima', 'fatima.diallo@email.com', '5819876543', 'Développeuse', 2),
('Nguyen', 'Thierry', 'thierry.nguyen@email.com', '5812345678', 'Enseignant', 1);

SELECT * FROM PARTICIPANT;

-- ORGANISATEUR
INSERT INTO ORGANISATEUR (nom_organisateur, prenom_organisateur, courriel_organisateur, telephone_organisateur, organisme) VALUES
('Lavoie', 'Marianne', 'marianne.lavoie@email.com', '5811122334', 'UQAC'),
('Gagné', 'Marc', 'marc.gagne@email.com', '5814433221', 'Ville de Saguenay');
SELECT * FROM ORGANISATEUR;

-- LIEU
INSERT INTO LIEU (nom_lieu, adresse, ville, capacite, type_lieu) VALUES
('Salle Multifonctionnelle UQAC', '555 Boulevard de l’Université', 'Chicoutimi', 200, 'Amphithéâtre'),
('Centre Culturel', '1200 Rue Principale', 'Jonquière', 150, 'Salle de conférence');
SELECT * FROM LIEU;

-- CATEGORIE
INSERT INTO CATEGORIE (nom_categorie, description, public_cible, couleur_affichage) VALUES
('Technologie', 'Événements liés à l’innovation technologique', 'Étudiants en informatique', 'Bleu'),
('Culture', 'Événements culturels locaux', 'Grand public', 'Vert');
SELECT * FROM CATEGORIE;

-- EVENEMENT
INSERT INTO EVENEMENT (nom_evenement, date_evenement, heure, type_evenement, tarif, id_lieu, id_organisateur, id_categorie) VALUES
('Hackathon UQAC', '2025-05-15', '09:00:00', 'Compétition', 20.00, 1, 1, 1),
('Expo Culturelle', '2025-06-10', '18:00:00', 'Exposition', 15.00, 2, 2, 2);
SELECT * FROM EVENEMENT;

-- INSCRIPTION
INSERT INTO INSCRIPTION (id_participant, id_evenement, date_inscription, mode_paiement, montant_paye, statut) VALUES
(1, 1, '2025-05-10', 'Carte', 20.00, 'Payé'),
(2, 1, '2025-05-01', 'Espèces', 20.00, 'Payé'),
(3, 2, '2025-04-20', 'Carte', 15.00, 'Réservé');
SELECT * FROM INSCRIPTION;

-- 5. Champs calculés
SELECT id_evenement, COUNT(*) AS nb_inscriptions 
FROM INSCRIPTION
GROUP BY id_evenement;

SELECT id_participant, SUM(montant_paye) AS total_paye 
FROM INSCRIPTION 
GROUP BY id_participant;

SELECT 
    id_participant,
    id_evenement,
    DATEDIFF(CURDATE(), date_inscription) AS anciennete_jours
FROM INSCRIPTION;

-- Jointure enrichie des inscriptions
SELECT 
    I.id_participant,
    P.nom_participant,
    P.prenom_participant,
    E.nom_evenement,
    E.date_evenement,
    I.mode_paiement,
    I.montant_paye,
    I.statut
FROM INSCRIPTION I
JOIN PARTICIPANT P ON I.id_participant = P.id_participant
JOIN EVENEMENT E ON I.id_evenement = E.id_evenement;


-- 7. Transaction et rollback
START TRANSACTION;
INSERT INTO PARTICIPANT (nom_participant, prenom_participant, courriel_participant, telephone_participant, profession, nombre_billet)
VALUES ('Brassard', 'Lucas', 'lucas.brassard@email.com', '1234567890', 'Étudiant', 1);

SELECT * FROM PARTICIPANT;

INSERT INTO INSCRIPTION (id_participant, id_evenement, date_inscription, mode_paiement, montant_paye, statut)
VALUES (LAST_INSERT_ID(), 1, CURDATE(), 'Carte', 45.00, 'Payé');
COMMIT;

SELECT * FROM INSCRIPTION;

START TRANSACTION;
INSERT INTO PARTICIPANT (nom_participant, prenom_participant, courriel_participant, telephone_participant, profession, nombre_billet)
VALUES ('Test', 'Rollback', 'test.rollback@email.com', '555-5555', 'Testeur', 0);

ROLLBACK;






