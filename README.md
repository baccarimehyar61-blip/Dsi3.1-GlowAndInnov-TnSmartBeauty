# Smart Beauty - Android (style TP) : connexion, inscription, salons, prestations, images

Ecrans : ConnexionActivity, InscriptionActivity, AccueilActivity,
SalonsActivity (RecyclerView + recherche + filtre ville), SalonDetailActivity (salon + prestations).

## Images des salons (comme dans le TP : res/drawable)
Les 3 images sont deja dans le projet (salon_1.jpg, salon_2.jpg, salon_3.jpg). Pour en changer, remplacer les fichiers dans app/src/main/res/drawable avec ces noms (minuscules, underscore) :
  salon_1.jpg   (salon avec id = 1 dans la base)
  salon_2.jpg   (id = 2)
  salon_3.jpg   (id = 3)
Verifier les ids : SELECT id, nom FROM smart_beauty.salons;
Une seule extension par nom (ne pas avoir salon_1.jpg ET salon_1.png).
Sans image locale : l'application utilise l'URL du salon, sinon un fond rose.

## Lancer
1. Android Studio > File > Open > dossier SmartBeautyTP_Salons_v4 (celui qui contient settings.gradle).
2. MariaDB + backend "smartbeauty" (set DB_PASSWORD=... puis mvnw spring-boot:run).
3. Run.
