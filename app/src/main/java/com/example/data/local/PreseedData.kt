package com.example.data.local

import com.example.data.local.entity.LocationEntity
import com.example.data.local.entity.ProcedureEntity
import com.example.data.local.entity.ProcedureStepEntity
import com.example.data.local.entity.ProcedureVariantEntity
import com.example.data.local.entity.RequirementPieceEntity
import com.example.data.local.entity.TestimonialEntity

object PreseedData {
    val procedures = listOf(
        ProcedureEntity(
            id = "cni",
            slug = "carte-nationale-identite",
            titleFr = "Carte Nationale d'Identité (CNI Biométrique 48h)",
            titleEn = "National Identity Card (Biometric NIC 48h)",
            category = "IDENTITE",
            officialCostXaf = 10000,
            estimatedExtraCostXaf = 1000,
            officialDelayDays = 2, // 48 heures officielles (DGSN / idcam.cm)
            observedDelayDays = 4,
            verifiedDate = "03/11/2026",
            officialSourceUrl = "https://www.idcam.cm",
            officialSourceName = "DGSN (Système biométrique express www.idcam.cm)",
            isOfficial = true,
            whoIsConcernedFr = "Tout citoyen camerounais âgé d'au moins 18 ans révolus. Nouvelle carte sécurisée en polycarbonate avec puce et QR Code VDS. Valable 10 ans.",
            whoIsConcernedEn = "Any Cameroonian citizen aged 18+. New secure polycarbonate biometric card with chip and VDS QR Code. Valid 10 years.",
            commonMistakesFr = "Se présenter au poste d'identification sans fiche de pré-enrôlement imprimée sur www.idcam.cm ; paiement en espèces au guichet (interdit : paiement 10 000 FCFA exclusivement en ligne par MoMo/OM/CB) ; absence de l'original de l'acte de naissance certifié.",
            commonMistakesEn = "Showing up without printed pre-enrollment form from www.idcam.cm; cash payments at desk (forbidden: 10,000 XAF fee paid exclusively online); missing original certified birth certificate.",
            popularPriority = 100
        ),
        ProcedureEntity(
            id = "passeport",
            slug = "passeport-biometrique",
            titleFr = "Passeport Biométrique Ordinaire (48h)",
            titleEn = "Ordinary Biometric Passport (48h)",
            category = "IDENTITE",
            officialCostXaf = 110000,
            estimatedExtraCostXaf = 2000,
            officialDelayDays = 2, // 48 heures officielles (DGSN / passcam.dgsn.cm)
            observedDelayDays = 4,
            verifiedDate = "03/11/2026",
            officialSourceUrl = "https://passcam.dgsn.cm",
            officialSourceName = "DGSN (Direction de la Police des Frontières - passcam.dgsn.cm)",
            isOfficial = true,
            whoIsConcernedFr = "Tout citoyen camerounais désirant voyager hors des frontières nationales. Valable 5 ans.",
            whoIsConcernedEn = "Any Cameroonian citizen wishing to travel internationally. Valid 5 years.",
            commonMistakesFr = "Se présenter au centre sans fiche de pré-enrôlement imprimée avec code-barres ; quittance de 110 000 FCFA non validée ; discordance entre nom sur la CNI et sur l'acte de naissance.",
            commonMistakesEn = "Showing up without printed pre-enrollment form barcode; unverified 110,000 XAF payment receipt; discrepancy between ID and birth certificate names.",
            popularPriority = 95
        ),
        ProcedureEntity(
            id = "acte_naissance",
            slug = "acte-de-naissance",
            titleFr = "Acte de Naissance (Déclaration & Copie)",
            titleEn = "Birth Certificate (Declaration & Copy)",
            category = "ETAT_CIVIL",
            officialCostXaf = 0,
            estimatedExtraCostXaf = 1000,
            officialDelayDays = 5,
            observedDelayDays = 10,
            verifiedDate = "25/10/2026",
            officialSourceUrl = "https://www.minat.gov.cm",
            officialSourceName = "Ministère de l'Administration Territoriale (MINAT / BUNEC)",
            isOfficial = true,
            whoIsConcernedFr = "Parents d'un nouveau-né (dans les 90 jours légaux gratuits) ou toute personne demandant une copie certifiée conforme.",
            whoIsConcernedEn = "Parents of newborn (free within legal 90 days) or any person requesting certified true copy.",
            commonMistakesFr = "Dépasser le délai légal de 90 jours (nécessite alors un jugement supplétif au tribunal coûtant 15 000 à 25 000 FCFA) ; erreurs d'orthographe dans les prénoms des parents ; omission de la signature des témoins.",
            commonMistakesEn = "Exceeding 90-day legal window (requires court judgment costing 15,000-25,000 XAF); misspelling parent names; missing witness signatures.",
            popularPriority = 90
        ),
        ProcedureEntity(
            id = "casier_judiciaire",
            slug = "casier-judiciaire-bulletin3",
            titleFr = "Casier Judiciaire (Bulletin N°3)",
            titleEn = "Police Record / Criminal Record (Bulletin No. 3)",
            category = "JUSTICE",
            officialCostXaf = 1000,
            estimatedExtraCostXaf = 500,
            officialDelayDays = 1,
            observedDelayDays = 2,
            verifiedDate = "18/10/2026",
            officialSourceUrl = "https://www.minjustice.gov.cm",
            officialSourceName = "Ministère de la Justice (Greffe du TPI)",
            isOfficial = true,
            whoIsConcernedFr = "Candidats aux concours administratifs, demandeurs d'emploi, dossiers de visa ou de naturalisation. Valable 3 mois.",
            whoIsConcernedEn = "Candidates for civil service exams, job seekers, visa applicants. Valid for 3 months.",
            commonMistakesFr = "Aller dans un tribunal hors de son ressort de naissance ou de résidence légale ; oublier le timbre fiscal à 1 000 FCFA.",
            commonMistakesEn = "Going to wrong jurisdiction; forgetting 1,000 XAF tax stamp.",
            popularPriority = 85
        ),
        ProcedureEntity(
            id = "certificat_nationalite",
            slug = "certificat-de-nationalite",
            titleFr = "Certificat de Nationalité Camerounaise",
            titleEn = "Certificate of Cameroonian Nationality",
            category = "JUSTICE",
            officialCostXaf = 2500,
            estimatedExtraCostXaf = 1000,
            officialDelayDays = 3,
            observedDelayDays = 7,
            verifiedDate = "15/10/2026",
            officialSourceUrl = "https://www.minjustice.gov.cm",
            officialSourceName = "Président du Tribunal de Première Instance",
            isOfficial = true,
            whoIsConcernedFr = "Indispensable pour l'obtention d'une première CNI, inscription à certains concours d'État et carrières militaires.",
            whoIsConcernedEn = "Mandatory for first-time ID card, civil service competitions, military careers.",
            commonMistakesFr = "Oublier la preuve de nationalité de l'un des parents (CNI ou acte de naissance du père/mère certifié) ; demande adressée au mauvais tribunal.",
            commonMistakesEn = "Missing parent proof of nationality; petition addressed to wrong court jurisdiction.",
            popularPriority = 80
        ),
        ProcedureEntity(
            id = "creation_entreprise",
            slug = "creation-entreprise-cfce",
            titleFr = "Création d'Entreprise (Guichet Unique CFCE)",
            titleEn = "Business Registration (CFCE One-Stop Shop)",
            category = "ENTREPRISE",
            officialCostXaf = 41500,
            estimatedExtraCostXaf = 3500,
            officialDelayDays = 3,
            observedDelayDays = 5,
            verifiedDate = "12/10/2026",
            officialSourceUrl = "https://www.cfce.cm",
            officialSourceName = "Centres de Formalités de Création des Entreprises (CFCE)",
            isOfficial = true,
            whoIsConcernedFr = "Tout entrepreneur créant une entreprise individuelle (Éts) ou une société commerciale (SARL, SAS).",
            whoIsConcernedEn = "Any entrepreneur registering a sole proprietorship (Ets) or commercial company (SARL, SAS).",
            commonMistakesFr = "Plan de localisation imprécis sans repère fixe ; ne pas prévoir de casier judiciaire datant de moins de 3 mois pour le gérant.",
            commonMistakesEn = "Imprecise location plan without landmarks; missing manager criminal record under 3 months old.",
            popularPriority = 75
        ),
        ProcedureEntity(
            id = "permis_conduire",
            slug = "permis-de-conduire",
            titleFr = "Permis de Conduire (Catégorie B)",
            titleEn = "Driver's License (Category B)",
            category = "TRANSPORT",
            officialCostXaf = 15000,
            estimatedExtraCostXaf = 5000,
            officialDelayDays = 30,
            observedDelayDays = 60,
            verifiedDate = "10/10/2026",
            officialSourceUrl = "https://www.mintransports.gov.cm",
            officialSourceName = "Ministère des Transports (Délégation Régionale)",
            isOfficial = true,
            whoIsConcernedFr = "Toute personne âgée de 18 ans et plus ayant réussi les épreuves théoriques et pratiques du permis.",
            whoIsConcernedEn = "Anyone 18+ who passed theoretical and practical driving examination.",
            commonMistakesFr = "Certificat médical d'aptitude délivré par un médecin non agréé ; groupe sanguin non certifié.",
            commonMistakesEn = "Medical fitness certificate from unauthorized doctor; uncertified blood group certificate.",
            popularPriority = 70
        ),
        ProcedureEntity(
            id = "legalisation_documents",
            slug = "legalisation-documents-administratifs",
            titleFr = "Légalisation / Certification Conforme",
            titleEn = "Document Certification / Legalization",
            category = "ETAT_CIVIL",
            officialCostXaf = 1000,
            estimatedExtraCostXaf = 200,
            officialDelayDays = 1,
            observedDelayDays = 1,
            verifiedDate = "22/10/2026",
            officialSourceUrl = "https://www.minat.gov.cm",
            officialSourceName = "Sous-préfectures, Commissariats, Mairies",
            isOfficial = true,
            whoIsConcernedFr = "Toute personne devant certifier conforme une copie de diplôme, acte ou contrat pour un dossier officiel.",
            whoIsConcernedEn = "Anyone needing certified true copies of diplomas, certificates, contracts for official files.",
            commonMistakesFr = "Se présenter sans l'original du document ; photocopie de mauvaise qualité ou coupée ; timbre fiscal contrefait (toujours acheter en régie officielle).",
            commonMistakesEn = "Coming without the original document; truncated photocopies; counterfeit tax stamps.",
            popularPriority = 65
        )
    )

    val variants = listOf(
        // CNI variants
        ProcedureVariantEntity("cni_first", "cni", "Première demande (dès 18 ans)", "First-time Application (18+)", "Pré-enrôlement sur idcam.cm pour tout citoyen atteignant la majorité ou n'ayant jamais été enrôlé.", "Pre-enrollment on idcam.cm for any citizen turning 18 or never previously registered."),
        ProcedureVariantEntity("cni_renew", "cni", "Renouvellement (expiration ou usure)", "Renewal (expired or worn-out)", "Pour les CNI arrivées à expiration de validité (10 ans) ou détériorées.", "For ID cards reaching 10-year expiration or damaged."),
        ProcedureVariantEntity("cni_loss", "cni", "Perte ou Vol de CNI", "Lost or Stolen ID Card", "Nécessite le Certificat de Déclaration de Perte délivré par la police et le pré-enrôlement idcam.cm.", "Requires official police Loss Declaration Certificate and idcam.cm pre-enrollment."),

        // Acte de naissance variants
        ProcedureVariantEntity("acte_normal", "acte_naissance", "Déclaration normale (< 90 jours)", "Standard declaration (< 90 days)", "Gratuite auprès de la commune ou centre d'état civil.", "Free of charge at municipality registry."),
        ProcedureVariantEntity("acte_jugement", "acte_naissance", "Jugement supplétif (> 90 jours)", "Court Supplementary Judgment (> 90 days)", "Requis si l'enfant ou l'adulte n'a pas été déclaré dans les délais légaux.", "Required if not registered within legal 90 days."),
        ProcedureVariantEntity("acte_copie", "acte_naissance", "Copie certifiée conforme", "Certified true copy", "Délivrance d'extraits d'acte de naissance légalisés.", "Delivery of certified birth certificate transcripts."),

        // Passeport variants
        ProcedureVariantEntity("pass_first", "passeport", "Première demande (48h)", "First-time applicant (48h)", "Pré-enrôlement sur passcam.dgsn.cm pour tout citoyen.", "Online pre-enrollment on passcam.dgsn.cm."),
        ProcedureVariantEntity("pass_renew", "passeport", "Renouvellement (48h)", "Passport renewal (48h)", "Pour passeport expiré ou pages de visa épuisées.", "For expired passports or exhausted visa pages."),

        // Creation entreprise variants
        ProcedureVariantEntity("cie_ets", "creation_entreprise", "Établissement (Personne physique)", "Sole Proprietorship (Ets)", "Démarche simplifiée, capital libre, responsabilité personnelle.", "Simplified setup, flexible capital, sole liability."),
        ProcedureVariantEntity("cie_sarl", "creation_entreprise", "SARL (Société à Responsabilité Limitée)", "Limited Liability Company (LLC/SARL)", "Statuts notariés ou sous seing privé, capital minimum 100 000 FCFA.", "Notarized or private statutes, min capital 100,000 XAF.")
    )

    val requirementPieces = listOf(
        // CNI Pieces (DGSN Express 48h - idcam.cm)
        RequirementPieceEntity("p_cni_0", "cni", "", "Fiche de pré-enrôlement www.idcam.cm imprimée avec QR code", "Printed www.idcam.cm pre-enrollment form with QR code", isOriginalRequired = true, copiesCount = 1, certifiedCopyRequired = false, whereToGetFr = "Générée en ligne sur www.idcam.cm", whereToGetEn = "Online portal www.idcam.cm", tipsFr = "Obligatoire. Contient la quittance de paiement numérique de 10 000 FCFA.", tipsEn = "Mandatory. Includes digital 10,000 XAF payment receipt."),
        RequirementPieceEntity("p_cni_1", "cni", "", "Original de l'acte de naissance", "Original Birth Certificate", isOriginalRequired = true, copiesCount = 1, certifiedCopyRequired = false, whereToGetFr = "Mairie du lieu de naissance", whereToGetEn = "Birth municipality", tipsFr = "Indispensable pour vérification du registre.", tipsEn = "Mandatory for registry check."),
        RequirementPieceEntity("p_cni_2", "cni", "", "Photocopie certifiée conforme de l'acte de naissance", "Certified copy of birth certificate", isOriginalRequired = false, copiesCount = 1, certifiedCopyRequired = true, whereToGetFr = "Sous-préfecture, Mairie ou Commissariat", whereToGetEn = "Sub-prefecture or Police station", tipsFr = "Moins de 3 mois de délivrance.", tipsEn = "Under 3 months old."),
        RequirementPieceEntity("p_cni_3", "cni", "cni_first", "Certificat de nationalité camerounaise", "Nationality certificate", isOriginalRequired = true, copiesCount = 1, certifiedCopyRequired = false, whereToGetFr = "Tribunal de Première Instance", whereToGetEn = "Court of First Instance", tipsFr = "Délivré par le président du TPI.", tipsEn = "Issued by court president."),
        RequirementPieceEntity("p_cni_4", "cni", "cni_renew", "Ancienne carte nationale d'identité", "Previous National ID card", isOriginalRequired = true, copiesCount = 1, certifiedCopyRequired = false, whereToGetFr = "Votre possession", whereToGetEn = "Your possession", tipsFr = "Sera restituée ou poinçonnée.", tipsEn = "Will be cancelled/returned."),
        RequirementPieceEntity("p_cni_5", "cni", "cni_loss", "Certificat de déclaration de perte", "Loss Declaration Certificate", isOriginalRequired = true, copiesCount = 1, certifiedCopyRequired = false, whereToGetFr = "Commissariat de police ou Brigade de gendarmerie", whereToGetEn = "Police station or Gendarmerie", tipsFr = "Délivré suite à la déclaration de perte.", tipsEn = "Issued following police loss report."),
        RequirementPieceEntity("p_cni_6", "cni", "", "Quittance numérique de paiement (10 000 FCFA)", "Digital payment receipt (10,000 XAF)", isOriginalRequired = true, copiesCount = 1, certifiedCopyRequired = false, whereToGetFr = "Validée en ligne sur idcam.cm (MoMo, OM, CB)", whereToGetEn = "Validated online on idcam.cm (MoMo, OM, Card)", tipsFr = "Ne jamais payer en espèces au poste de police : le tarif officiel de 10 000 FCFA est exclusivement numérique.", tipsEn = "Never pay cash at counter: official 10,000 XAF fee is paid online only."),

        // Acte de naissance Pieces
        RequirementPieceEntity("p_act_1", "acte_naissance", "acte_normal", "Déclaration de naissance de la maternité / hôpital", "Maternity / Hospital birth certificate slip", isOriginalRequired = true, copiesCount = 1, certifiedCopyRequired = false, whereToGetFr = "Hôpital ou clinique d'accouchement", whereToGetEn = "Hospital of delivery", tipsFr = "Signée par le médecin ou la sage-femme.", tipsEn = "Signed by doctor or midwife."),
        RequirementPieceEntity("p_act_2", "acte_naissance", "acte_normal", "Photocopies des CNI des deux parents", "Photocopies of both parents' IDs", isOriginalRequired = false, copiesCount = 2, certifiedCopyRequired = true, whereToGetFr = "Commissariat ou Mairie", whereToGetEn = "Police or Municipality", tipsFr = "Avec originaux pour présentation.", tipsEn = "Bring originals for verification."),
        RequirementPieceEntity("p_act_3", "acte_naissance", "acte_normal", "Acte de mariage des parents (si mariés légalement)", "Parents' Marriage Certificate (if married)", isOriginalRequired = false, copiesCount = 1, certifiedCopyRequired = true, whereToGetFr = "Centre d'état civil de célébration", whereToGetEn = "Registry of celebration"),
        RequirementPieceEntity("p_act_4", "acte_naissance", "acte_jugement", "Requête timbrée au Président du Tribunal", "Stamped petition to Court President", isOriginalRequired = true, copiesCount = 1, certifiedCopyRequired = false, whereToGetFr = "Greffe du TPI", whereToGetEn = "Court clerk office", tipsFr = "Prévoir 2 témoins majeurs pour l'audience.", tipsEn = "Arrange 2 adult witnesses for hearing."),

        // Passeport Pieces (DGSN Express 48h - passcam.dgsn.cm)
        RequirementPieceEntity("p_pass_1", "passeport", "", "Fiche de pré-enrôlement passcam.dgsn.cm imprimée avec QR code", "Printed passcam.dgsn.cm pre-enrollment form with QR code", isOriginalRequired = true, copiesCount = 1, certifiedCopyRequired = false, whereToGetFr = "Générée en ligne sur passcam.dgsn.cm", whereToGetEn = "Online portal passcam.dgsn.cm", tipsFr = "Imprimer en haute résolution avec la quittance de 110 000 FCFA.", tipsEn = "Print with clear barcode/QR and 110,000 XAF receipt."),
        RequirementPieceEntity("p_pass_2", "passeport", "", "Original de la CNI en cours de validité", "Original valid National ID card", isOriginalRequired = true, copiesCount = 1, certifiedCopyRequired = false, whereToGetFr = "Votre possession", whereToGetEn = "Your possession", tipsFr = "Non expirée et parfaitement lisible.", tipsEn = "Must not be expired."),
        RequirementPieceEntity("p_pass_3", "passeport", "", "Photocopie certifiée conforme de l'acte de naissance", "Certified copy of birth certificate", isOriginalRequired = true, copiesCount = 1, certifiedCopyRequired = true, whereToGetFr = "Mairie d'enregistrement", whereToGetEn = "Issuing town hall", tipsFr = "Délivrée il y a moins de 3 mois.", tipsEn = "Issued within last 3 months."),
        RequirementPieceEntity("p_pass_4", "passeport", "", "Justificatif de profession (diplôme, attestation de travail)", "Proof of profession (degree, employment letter)", isOriginalRequired = false, copiesCount = 1, certifiedCopyRequired = true, whereToGetFr = "Employeur ou Établissement académique", whereToGetEn = "Employer or university", tipsFr = "Mentionné sur la page biographique.", tipsEn = "Printed on biographical page."),

        // Casier judiciaire Pieces
        RequirementPieceEntity("p_cas_1", "casier_judiciaire", "", "Photocopie de la CNI ou de l'Acte de naissance", "Copy of ID card or birth certificate", isOriginalRequired = false, copiesCount = 1, certifiedCopyRequired = false, whereToGetFr = "Votre possession", whereToGetEn = "Your possession"),
        RequirementPieceEntity("p_cas_2", "casier_judiciaire", "", "Timbre fiscal de 1 000 FCFA", "1,000 XAF fiscal stamp", isOriginalRequired = true, copiesCount = 1, certifiedCopyRequired = false, whereToGetFr = "Régie des recettes du tribunal", whereToGetEn = "Court stamp counter"),

        // Certificat nationalite Pieces
        RequirementPieceEntity("p_nat_1", "certificat_nationalite", "", "Demande timbrée adressée au Président du TPI", "Stamped application to Court President", isOriginalRequired = true, copiesCount = 1, certifiedCopyRequired = false, whereToGetFr = "Greffe du tribunal", whereToGetEn = "Court registry"),
        RequirementPieceEntity("p_nat_2", "certificat_nationalite", "", "Copie certifiée conforme d'acte de naissance du demandeur", "Certified copy of applicant's birth certificate", isOriginalRequired = true, copiesCount = 1, certifiedCopyRequired = true, whereToGetFr = "Mairie d'enregistrement", whereToGetEn = "Town hall"),
        RequirementPieceEntity("p_nat_3", "certificat_nationalite", "", "Copie certifiée conforme de la CNI ou acte d'un parent", "Certified copy of parent's ID or birth certificate", isOriginalRequired = true, copiesCount = 1, certifiedCopyRequired = true, whereToGetFr = "Parent ou mairie", whereToGetEn = "Parent or registry"),

        // Entreprise Pieces
        RequirementPieceEntity("p_ent_1", "creation_entreprise", "", "Formulaire unique CFCE rempli et signé", "Completed and signed CFCE single form", isOriginalRequired = true, copiesCount = 1, certifiedCopyRequired = false, whereToGetFr = "Au guichet CFCE ou téléchargé", whereToGetEn = "CFCE desk or download"),
        RequirementPieceEntity("p_ent_2", "creation_entreprise", "", "Casier judiciaire du dirigeant / gérant (< 3 mois)", "Manager criminal record (< 3 months)", isOriginalRequired = true, copiesCount = 1, certifiedCopyRequired = false, whereToGetFr = "Tribunal de Première Instance", whereToGetEn = "Court of First Instance"),
        RequirementPieceEntity("p_ent_3", "creation_entreprise", "", "Plan de localisation du siège de l'entreprise", "Business location map with landmarks", isOriginalRequired = true, copiesCount = 1, certifiedCopyRequired = false, whereToGetFr = "Rédigé sur papier libre ou plan officiel", whereToGetEn = "Drawn on paper or map"),
        RequirementPieceEntity("p_ent_4", "creation_entreprise", "", "Photocopie certifiée conforme de la CNI du promoteur", "Certified copy of promoter's ID card", isOriginalRequired = false, copiesCount = 2, certifiedCopyRequired = true, whereToGetFr = "Commissariat ou Mairie", whereToGetEn = "Police or Municipality")
    )

    val steps = listOf(
        // CNI Steps (Nouveau processus 48h DGSN / idcam.cm)
        ProcedureStepEntity("s_cni_1", "cni", 1, "Pré-enrôlement en ligne et paiement sur idcam.cm", "Online pre-enrollment and digital payment", "Connectez-vous sur www.idcam.cm, remplissez le formulaire avec vos informations d'état civil et payez les 10 000 FCFA de timbre par Orange Money, MTN MoMo ou carte bancaire. Imprimez la fiche avec QR Code.", "Register on www.idcam.cm and pay 10,000 XAF via MoMo/OM/Card.", "EN_LIGNE", 10000, "Ne payez jamais en espèces au commissariat : le paiement est exclusivement numérique sur idcam.cm.", "Never pay cash at police station: digital payment only on idcam.cm."),
        ProcedureStepEntity("s_cni_2", "cni", 2, "Enrôlement biométrique physique au poste d'identification", "Physical biometric enrollment", "Présentez-vous au poste d'identification choisi avec la fiche imprimée et vos justificatifs originaux (acte de naissance) pour la capture des 10 empreintes, photo et signature.", "Show up at chosen center with printed form and original birth certificate for biometric capture.", "COMMISSARIAT", 0, "Soyez ponctuel à l'heure du créneau choisi sur idcam.cm. Tenue décente exigée.", "Arrive on time per chosen slot. Proper attire required."),
        ProcedureStepEntity("s_cni_3", "cni", 3, "Délivrance express sous 48 heures (2 jours)", "Express 48-hour delivery", "La carte biométrique sécurisée en polycarbonate est produite sous 48 heures. Vous recevez une alerte SMS dès qu'elle est prête à être retirée avec votre récépissé.", "Polycarbonate card produced within 48h. Receive SMS when ready for pickup.", "COMMISSARIAT", 0, "Retrait impératif par le demandeur en personne après contrôle d'empreinte digitale.", "In-person pickup by applicant after fingerprint verification."),

        // Passeport Steps (DGSN passcam 48h)
        ProcedureStepEntity("s_pass_1", "passeport", 1, "Pré-enrôlement en ligne et paiement sur passcam.dgsn.cm", "Online pre-enrollment and payment", "Remplissez le formulaire sur passcam.dgsn.cm et payez les 110 000 FCFA par Mobile Money (Orange/MTN) ou carte. Choisissez votre centre et date de rendez-vous.", "Complete online form on passcam.dgsn.cm and pay 110,000 XAF.", "EN_LIGNE", 110000, "Téléchargez et imprimez immédiatement la confirmation avec QR code.", "Download and print confirmation with QR code."),
        ProcedureStepEntity("s_pass_2", "passeport", 2, "Rendez-vous et enrôlement biométrique physique", "Physical appointment and enrollment", "Présentez-vous au centre de production des passeports choisi avec l'ensemble des originaux (CNI, acte de naissance certifié) et la fiche imprimée.", "Show up at enrollment center with originals.", "COMMISSARIAT", 0, "Soyez ponctuel à l'heure du créneau choisi (15 min à l'avance).", "Arrive 15 minutes before your time slot."),
        ProcedureStepEntity("s_pass_3", "passeport", 3, "Notification SMS et retrait sous 48 heures (2 jours)", "SMS notification and pickup (48h)", "Vous recevez un SMS confirmant la disponibilité du passeport sous 48 heures. Retrait immédiat avec récépissé.", "Receive SMS alert when ready within 48 hours.", "COMMISSARIAT", 0, "Seul le titulaire peut retirer son passeport.", "Only cardholder can physically pick up passport."),

        // CFCE Steps
        ProcedureStepEntity("s_ent_1", "creation_entreprise", 1, "Dépôt du dossier au Guichet Unique CFCE", "File submission at CFCE desk", "Déposez les pièces requises, le formulaire unique et le plan de localisation.", "Submit required files and location plan.", "GUICHET_UNIQUE", 41500, "Le guichet unique regroupe le Greffe, la DGI et la CNPS en un seul lieu.", "Consolidates court, tax and social security."),
        ProcedureStepEntity("s_ent_2", "creation_entreprise", 2, "Traitement express sous 72 heures", "72-hour fast-track processing", "Les agents vérifient la non-similarité du nom commercial et enregistrent au RCCM.", "Agents process RCCM and business registration.", "GUICHET_UNIQUE", 0, "Suivez l'état de traitement avec votre numéro de récépissé.", "Track status with receipt number."),
        ProcedureStepEntity("s_ent_3", "creation_entreprise", 3, "Retrait du kit de création d'entreprise", "Pick up company registration kit", "Retirez votre RCCM, votre Attestation d'Immatriculation Fiscale et votre récépissé CNPS.", "Collect RCCM, tax card, and CNPS.", "GUICHET_UNIQUE", 0, "Faites immédiatement plastifier votre attestation fiscale.", "Laminate your tax certificate.")
    )

    val locations = listOf(
        // Yaoundé
        LocationEntity(
            id = "loc_yde_1",
            name = "Centre Principal de Production des Passeports",
            city = "Yaoundé",
            district = "Centre Administratif",
            address = "Face Immeuble Ministériel N°1, Quartier du Lac",
            latitude = 3.8667,
            longitude = 11.5167,
            openingHours = "Lundi - Vendredi : 07h30 - 15h30",
            phone = "+237 222 21 28 84",
            category = "POLICE",
            bestTimeToVisit = "Mardi ou Jeudi dès 07h30 pour éviter l'affluence de mi-journée.",
            servicesOffered = "Enrôlement passeports ordinaires express 48h, biométrie, retrait."
        ),
        LocationEntity(
            id = "loc_yde_2",
            name = "Poste d'Identification Commissariat Central N°1 (idcam.cm)",
            city = "Yaoundé",
            district = "Centre-Ville",
            address = "Avenue Kennedy, près de la Poste Centrale",
            latitude = 3.8680,
            longitude = 11.5200,
            openingHours = "Lundi - Vendredi : 07h30 - 16h00, Samedi : 08h00 - 12h00",
            phone = "+237 222 22 01 22",
            category = "POLICE",
            bestTimeToVisit = "Mercredi matin à 08h00. Validation enrôlement CNI 48h sur idcam.cm.",
            servicesOffered = "Enrôlement CNI biométrique express 48h (idcam.cm), certificats de perte."
        ),
        LocationEntity(
            id = "loc_yde_3",
            name = "Mairie de Yaoundé 1er (Centre d'État Civil)",
            city = "Yaoundé",
            district = "Nlongkak",
            address = "Rond-point Nlongkak, montée Bastos",
            latitude = 3.8850,
            longitude = 11.5180,
            openingHours = "Lundi - Vendredi : 08h00 - 15h30",
            phone = "+237 222 20 18 45",
            category = "MAIRIE",
            bestTimeToVisit = "Tous les matins entre 08h30 et 10h30.",
            servicesOffered = "Déclaration de naissance, délivrance copies conformes, légalisation."
        ),
        LocationEntity(
            id = "loc_yde_4",
            name = "Tribunal de Première Instance (TPI) de Yaoundé",
            city = "Yaoundé",
            district = "Centre Administratif",
            address = "Boulevard du 20 Mai, Palais de Justice",
            latitude = 3.8640,
            longitude = 11.5150,
            openingHours = "Lundi - Vendredi : 07h30 - 15h30",
            phone = "+237 222 23 10 90",
            category = "TRIBUNAL",
            bestTimeToVisit = "Vendredi matin pour le casier judiciaire.",
            servicesOffered = "Casier judiciaire (Bulletin N°3), certificat de nationalité, jugements supplétifs."
        ),
        LocationEntity(
            id = "loc_yde_5",
            name = "CFCE Yaoundé - Guichet Unique de Création d'Entreprise",
            city = "Yaoundé",
            district = "Centre Administratif",
            address = "Immeuble Rose, Face Ministère du Commerce",
            latitude = 3.8655,
            longitude = 11.5175,
            openingHours = "Lundi - Vendredi : 08h00 - 15h30",
            phone = "+237 222 20 63 94",
            category = "CFCE",
            bestTimeToVisit = "Lundi ou Mardi dès l'ouverture.",
            servicesOffered = "Création d'entreprise Éts et SARL, RCCM, carte de contribuable, CNPS."
        ),

        // Douala
        LocationEntity(
            id = "loc_dla_1",
            name = "Centre d'Enrôlement des Passeports de Douala",
            city = "Douala",
            district = "Bonanjo",
            address = "Quartier Administratif Bonanjo, près de la Trésorerie Générale",
            latitude = 4.0450,
            longitude = 9.6900,
            openingHours = "Lundi - Vendredi : 07h30 - 16h00",
            phone = "+237 233 42 15 20",
            category = "POLICE",
            bestTimeToVisit = "Jeudi matin. Climatisation et système de ticket à l'entrée.",
            servicesOffered = "Enrôlement passeports express 48h, biométrie, retrait."
        ),
        LocationEntity(
            id = "loc_dla_2",
            name = "Centre d'Identification CNI Douala (idcam.cm)",
            city = "Douala",
            district = "Akwa",
            address = "Boulevard de la Liberté, Akwa",
            latitude = 4.0520,
            longitude = 9.7020,
            openingHours = "Lundi - Vendredi : 08h00 - 16h00",
            phone = "+237 233 42 22 10",
            category = "POLICE",
            bestTimeToVisit = "Mardi ou Jeudi dès 08h00 avec reçu idcam.cm imprimé.",
            servicesOffered = "Établissement et renouvellement CNI 48h, déclarations de perte."
        ),
        LocationEntity(
            id = "loc_dla_3",
            name = "Mairie de Douala 1er (Hôtel de Ville)",
            city = "Douala",
            district = "Bonanjo",
            address = "Place du Gouvernement, Bonanjo",
            latitude = 4.0430,
            longitude = 9.6880,
            openingHours = "Lundi - Vendredi : 08h00 - 15h30",
            phone = "+237 233 42 05 11",
            category = "MAIRIE",
            bestTimeToVisit = "Matinée entre 08h30 et 11h00.",
            servicesOffered = "Actes d'état civil, légalisation certifiée conforme, célébration mariages."
        ),
        LocationEntity(
            id = "loc_dla_4",
            name = "CFCE Douala - Guichet Unique Entreprises",
            city = "Douala",
            district = "Akwa",
            address = "Immeuble Chambre de Commerce, Rue Soppo Priso",
            latitude = 4.0505,
            longitude = 9.6980,
            openingHours = "Lundi - Vendredi : 08h00 - 15h30",
            phone = "+237 233 42 58 77",
            category = "CFCE",
            bestTimeToVisit = "Mardi matin.",
            servicesOffered = "Création SARL et Éts, immatriculation fiscale, RCCM express."
        ),

        // Bafoussam
        LocationEntity(
            id = "loc_bfm_1",
            name = "Délégation Régionale de la Sûreté Nationale (Ouest - idcam / passcam)",
            city = "Bafoussam",
            district = "Centre Administratif",
            address = "Avenue de l'Indépendance, Face Services du Gouverneur",
            latitude = 5.4770,
            longitude = 10.4170,
            openingHours = "Lundi - Vendredi : 07h30 - 15h30",
            phone = "+237 233 44 12 30",
            category = "POLICE",
            bestTimeToVisit = "Mercredi matin avec reçu de pré-enrôlement imprimé.",
            servicesOffered = "Enrôlement express 48h passeports et CNI biométrique (idcam.cm)."
        ),
        LocationEntity(
            id = "loc_bfm_2",
            name = "Mairie de Bafoussam 1er",
            city = "Bafoussam",
            district = "Djeleng",
            address = "Quartier Djeleng, route Foumbot",
            latitude = 5.4800,
            longitude = 10.4250,
            openingHours = "Lundi - Vendredi : 08h00 - 15h30",
            phone = "+237 233 44 20 15",
            category = "MAIRIE",
            bestTimeToVisit = "Mardi ou Jeudi 08h30.",
            servicesOffered = "Actes d'état civil, légalisations de documents."
        ),
        LocationEntity(
            id = "loc_bfm_3",
            name = "Tribunal de Première Instance de Bafoussam",
            city = "Bafoussam",
            district = "Palais de Justice",
            address = "Avenue des Tribunaux, près du Commissariat Central",
            latitude = 5.4750,
            longitude = 10.4150,
            openingHours = "Lundi - Vendredi : 07h30 - 15h30",
            phone = "+237 233 44 10 05",
            category = "TRIBUNAL",
            bestTimeToVisit = "Jeudi matin pour le casier judiciaire Bulletin n°3.",
            servicesOffered = "Casier judiciaire, certificat de nationalité, jugements supplétifs."
        ),
        LocationEntity(
            id = "loc_bfm_4",
            name = "CFCE Bafoussam - Guichet Unique",
            city = "Bafoussam",
            district = "Quartier Administratif",
            address = "Délégation Régionale du MINPMEESA, Ouest",
            latitude = 5.4760,
            longitude = 10.4160,
            openingHours = "Lundi - Vendredi : 08h00 - 15h30",
            phone = "+237 233 44 33 21",
            category = "CFCE",
            bestTimeToVisit = "Lundi ou Mercredi matin.",
            servicesOffered = "Création d'entreprise, RCCM, carte contribuable Ouest."
        )
    )

    val testimonials = listOf(
        TestimonialEntity(
            procedureId = "cni",
            procedureTitle = "Carte Nationale d'Identité (CNI Biométrique 48h)",
            city = "Yaoundé",
            serviceName = "Poste d'Identification Central (idcam.cm)",
            delayDays = 2,
            costPaidXaf = 10000,
            rating = 5,
            comment = "Pré-enrôlement fait un mardi soir sur www.idcam.cm avec paiement Orange Money (10 000 FCFA). Rendez-vous mercredi matin pour la photo et les empreintes. CNI en polycarbonate reçue par SMS et retirée vendredi matin (48h chrono) ! Révolutionnaire !",
            authorDisplay = "Alain M. (Yaoundé)",
            isAnonymous = false,
            createdAt = "02/11/2026",
            helpfulCount = 42
        ),
        TestimonialEntity(
            procedureId = "passeport",
            procedureTitle = "Passeport Biométrique Ordinaire (48h)",
            city = "Douala",
            serviceName = "Centre d'enrôlement Bonanjo",
            delayDays = 2,
            costPaidXaf = 110000,
            rating = 5,
            comment = "Pré-enrôlement fait sur passcam.dgsn.cm avec paiement Orange Money (110 000 FCFA). Rendez-vous pris pour mardi 9h. Reçu par SMS le jeudi que mon passeport était disponible ! Retrait sous 48h respecté au pied de la lettre.",
            authorDisplay = "Béatrice T. (Douala)",
            isAnonymous = false,
            createdAt = "24/10/2026",
            helpfulCount = 38
        ),
        TestimonialEntity(
            procedureId = "creation_entreprise",
            procedureTitle = "Création d'Entreprise (CFCE)",
            city = "Douala",
            serviceName = "CFCE Akwa",
            delayDays = 3,
            costPaidXaf = 41500,
            rating = 5,
            comment = "Pour mon Établissement commercial, j'ai tout eu en 3 jours ouvrables : RCCM + Carte contribuable. Prix respecté au centime près (41 500 FCFA). Évitez les démarcheurs qui rôdent à l'entrée !",
            authorDisplay = "Cédric E. (Douala)",
            isAnonymous = false,
            createdAt = "19/10/2026",
            helpfulCount = 27
        ),
        TestimonialEntity(
            procedureId = "casier_judiciaire",
            procedureTitle = "Casier Judiciaire (Bulletin N°3)",
            city = "Bafoussam",
            serviceName = "TPI Bafoussam",
            delayDays = 1,
            costPaidXaf = 1000,
            rating = 4,
            comment = "Déposé à 8h avec photocopie de CNI et timbre de 1 000 FCFA acheté au guichet intérieur. Retiré à 14h sans problème.",
            authorDisplay = "Sandrine K. (Bafoussam)",
            isAnonymous = false,
            createdAt = "16/10/2026",
            helpfulCount = 12
        )
    )
}
