# 🇨🇲 DémarchesCam

> **« Savoir exactement quoi apporter, où aller et combien payer, avant de sortir de chez soi. »**

**DémarchesCam** est une application mobile Android citoyenne (Civic Tech) conçue pour simplifier, clarifier et accompagner les démarches administratives au Cameroun. Elle regroupe les fiches de procédures officielles certifiées, un suivi de dossier interactif, un comparateur de dépenses face aux tarifs légaux, la gestion des proches et un mode 100% hors-ligne.

---

## 🌟 Fonctionnalités Principales

- **📑 Catalogue Officiel & Standardisé** :
  - **CNI Biométrique Express (48h)** : Intégration du portail officiel DGSN (`idcam.cm`), timbre numérique à 10 000 FCFA, fiche imprimée avec QR code et pièces requises.
  - **Passeport Biométrique Express (48h)** : Pré-enrôlement sur `passcam.dgsn.cm` à 110 000 FCFA.
  - **Autres démarches clés** : Acte de naissance, Casier judiciaire (Bulletin N°3), Certificat de nationalité, Création d'entreprise (CFCE), Permis de conduire, Légalisation de documents.
- **✅ Fiche de Procédure Pratique** :
  - Checklist cochable de pièces avec spécification du type (original, copie certifiée conforme, nombre d'exemplaires).
  - Coûts officiels en FCFA et frais annexes connus (photocopies, légalisation).
  - Délais officiels et délais réels constatés par les usagers.
  - Étapes chronologiques numérotées, pièges fréquents et dates de dernière vérification.
- **📁 Mon Espace (Suivi de Dossier)** :
  - Suivi d'avancement étape par étape et gestion des pièces préparées.
  - Gestion des bénéficiaires (*"Mes Proches"* : Enfant, Conjoint, Parent) sans collecte de documents d'identité confidentiels.
  - Suivi des dépenses réelles avec comparateur en temps réel face au tarif légal et alertes en cas de surcoût ou de surfacturation.
- **📲 Partage Direct WhatsApp** :
  - Génération en un clic d'un message synthétique formaté pour informer un proche ou un collègue des pièces et étapes à préparer.
- **📍 Lieux & Guichets Administratifs** :
  - Annuaire géolocalisé avec adresses, horaires d'ouverture et conseils pratiques (commissariats, centres d'état civil, tribunaux, centres d'enrôlement à Yaoundé, Douala et Bafoussam).
- **💬 Avis & Transparence Citoyenne** :
  - Partage de retours d'expérience et signalement d'anomalies tarifaires avec modération automatique protégeant la vie privée (masquage systématique des noms d'agents et des numéros de téléphone).
- **📴 Mode 100% Hors-Ligne** :
  - Toutes les fiches, étapes et lieux sont stockés localement grâce à Room Database pour une consultation fluide même sans connexion Internet.

---

## 🛠️ Stack Technique

- **Plateforme** : Android (Kotlin)
- **UI Toolkit** : Jetpack Compose & Material Design 3 (M3)
- **Architecture** : MVVM (Model-View-ViewModel) + Clean Architecture
- **Persistance Locale** : Room Database + Coroutines & Flow (Offline-First)
- **Navigation** : Jetpack Navigation Compose
- **Qualité & Tests** : JUnit 4, Robolectric

---

## 🚀 Installation & Développement

### Prérequis
- Android Studio Ladybug ou version ultérieure
- JDK 17+
- Android SDK 36 (Minimum SDK 24)

### Compilation
```bash
# Cloner le dépôt
git clone https://github.com/votre-compte/demarchescam.git

# Compiler le projet
gradle assembleDebug

# Lancer les tests unitaires et Robolectric
gradle :app:testDebugUnitTest
```

---

## 📜 Mentions Légales & Neutralité

*DémarchesCam est une initiative indépendante citoyenne et pédagogique. L'application constitue un guide pratique d'orientation citoyenne et ne remplace en aucun cas les services officiels des administrations publiques camerounaises (DGSN, MINAT, MINJUSTICE, MINFOPRA, CFCE).*
