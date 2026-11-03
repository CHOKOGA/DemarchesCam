package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "procedures")
data class ProcedureEntity(
    @PrimaryKey val id: String,
    val slug: String,
    val titleFr: String,
    val titleEn: String,
    val category: String, // IDENTITE, ETAT_CIVIL, JUSTICE, ENTREPRISE, TRANSPORT, FONCIER
    val officialCostXaf: Int,
    val estimatedExtraCostXaf: Int,
    val officialDelayDays: Int,
    val observedDelayDays: Int,
    val verifiedDate: String, // e.g., "15/10/2026"
    val officialSourceUrl: String,
    val officialSourceName: String,
    val isOfficial: Boolean = true,
    val whoIsConcernedFr: String,
    val whoIsConcernedEn: String,
    val commonMistakesFr: String,
    val commonMistakesEn: String,
    val popularPriority: Int = 0
)

@Entity(tableName = "procedure_variants")
data class ProcedureVariantEntity(
    @PrimaryKey val id: String,
    val procedureId: String,
    val titleFr: String,
    val titleEn: String,
    val descriptionFr: String,
    val descriptionEn: String
)

@Entity(tableName = "requirement_pieces")
data class RequirementPieceEntity(
    @PrimaryKey val id: String,
    val procedureId: String,
    val variantId: String = "", // empty means common to all variants
    val labelFr: String,
    val labelEn: String,
    val isOriginalRequired: Boolean = true,
    val copiesCount: Int = 1,
    val certifiedCopyRequired: Boolean = false,
    val whereToGetFr: String,
    val whereToGetEn: String,
    val tipsFr: String = "",
    val tipsEn: String = ""
)

@Entity(tableName = "procedure_steps")
data class ProcedureStepEntity(
    @PrimaryKey val id: String,
    val procedureId: String,
    val stepOrder: Int,
    val titleFr: String,
    val titleEn: String,
    val descriptionFr: String,
    val descriptionEn: String,
    val locationType: String, // COMMISSARIAT, MAIRIE, TRIBUNAL, GUICHET_UNIQUE, EN_LIGNE
    val costXaf: Int = 0,
    val practicalAdviceFr: String = "",
    val practicalAdviceEn: String = ""
)

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey val id: String,
    val name: String,
    val city: String, // Douala, Yaoundé, Bafoussam
    val district: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val openingHours: String,
    val phone: String,
    val category: String, // POLICE, MAIRIE, TRIBUNAL, CFCE, TRANSPORT
    val bestTimeToVisit: String,
    val servicesOffered: String
)

@Entity(tableName = "user_dossiers")
data class UserDossierEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val procedureId: String,
    val procedureTitle: String,
    val variantTitle: String,
    val city: String,
    val beneficiaryName: String = "Moi", // Pour qui est le dossier (enfant, parent, soi-même)
    val targetRetireDate: String = "",
    val depositDate: String = "",
    val receiptNumber: String = "",
    val status: String = "EN_COURS", // A_FAIRE, EN_COURS, TERMINE, ABANDONNE
    val createdAtTimestamp: Long = System.currentTimeMillis(),
    val notes: String = ""
)

@Entity(tableName = "dossier_piece_states")
data class DossierPieceStateEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dossierId: Long,
    val pieceId: String,
    val isChecked: Boolean = false,
    val userNote: String = ""
)

@Entity(tableName = "dossier_expenses")
data class DossierExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dossierId: Long,
    val label: String,
    val amountXaf: Int,
    val isOfficialFee: Boolean = false,
    val receiptRef: String = "",
    val dateString: String = ""
)

@Entity(tableName = "testimonials")
data class TestimonialEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val procedureId: String,
    val procedureTitle: String,
    val city: String,
    val serviceName: String,
    val delayDays: Int,
    val costPaidXaf: Int,
    val rating: Int, // 1 to 5
    val comment: String,
    val authorDisplay: String = "Citoyen anonyme",
    val isAnonymous: Boolean = true,
    val createdAt: String,
    val helpfulCount: Int = 0
)

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val procedureId: String,
    val procedureTitle: String,
    val fieldReported: String, // PRIX, DELAI, ADRESSE, PIECE
    val userCostReportedXaf: Int = 0,
    val userComment: String,
    val status: String = "EN_REVUE", // EN_REVUE, TRAITE, REJETE
    val createdAt: String
)

@Entity(tableName = "family_profiles")
data class FamilyProfileEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val relationship: String, // "Moi", "Enfant", "Parent", "Conjoint", "Autre"
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "offline_saved_procedures")
data class OfflineSavedProcedureEntity(
    @PrimaryKey val procedureId: String,
    val savedDate: String = "03/11/2026"
)
