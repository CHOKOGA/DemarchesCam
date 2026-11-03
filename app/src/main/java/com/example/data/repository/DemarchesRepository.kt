package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.PreseedData
import com.example.data.local.entity.DossierExpenseEntity
import com.example.data.local.entity.DossierPieceStateEntity
import com.example.data.local.entity.FamilyProfileEntity
import com.example.data.local.entity.LocationEntity
import com.example.data.local.entity.OfflineSavedProcedureEntity
import com.example.data.local.entity.ProcedureEntity
import com.example.data.local.entity.ProcedureStepEntity
import com.example.data.local.entity.ProcedureVariantEntity
import com.example.data.local.entity.ReportEntity
import com.example.data.local.entity.RequirementPieceEntity
import com.example.data.local.entity.TestimonialEntity
import com.example.data.local.entity.UserDossierEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

data class UserSession(
    val isLoggedIn: Boolean = false,
    val phoneNumber: String = "",
    val userName: String = ""
)

class DemarchesRepository(private val database: AppDatabase) {

    private val procedureDao = database.procedureDao()
    private val locationDao = database.locationDao()
    private val dossierDao = database.dossierDao()
    private val communityDao = database.communityDao()
    private val familyDao = database.familyDao()
    private val offlineDao = database.offlineDao()

    // Session utilisateur
    private val _userSession = MutableStateFlow(UserSession())
    val userSession: Flow<UserSession> = _userSession.asStateFlow()

    fun login(phone: String, name: String) {
        _userSession.value = UserSession(isLoggedIn = true, phoneNumber = phone, userName = name)
    }

    fun logout() {
        _userSession.value = UserSession(isLoggedIn = false, phoneNumber = "", userName = "")
    }

    suspend fun checkAndSeedInitialData() = withContext(Dispatchers.IO) {
        procedureDao.insertProcedures(PreseedData.procedures)
        procedureDao.insertVariants(PreseedData.variants)
        procedureDao.insertPieces(PreseedData.requirementPieces)
        procedureDao.insertSteps(PreseedData.steps)
        locationDao.insertLocations(PreseedData.locations)
        communityDao.insertTestimonials(PreseedData.testimonials)
        familyDao.insertProfile(
            FamilyProfileEntity(
                name = "Moi",
                relationship = "Personnel",
                notes = "Mon profil principal"
            )
        )
    }

    // --- Procedures ---
    fun getAllProcedures(): Flow<List<ProcedureEntity>> = procedureDao.getAllProcedures()

    fun getProcedureById(id: String): Flow<ProcedureEntity?> = procedureDao.getProcedureById(id)

    fun getProcedureBySlug(slug: String): Flow<ProcedureEntity?> = procedureDao.getProcedureBySlug(slug)

    fun searchProcedures(query: String): Flow<List<ProcedureEntity>> = procedureDao.searchProcedures(query)

    fun getVariantsForProcedure(procedureId: String): Flow<List<ProcedureVariantEntity>> =
        procedureDao.getVariantsForProcedure(procedureId)

    fun getPiecesForProcedure(procedureId: String): Flow<List<RequirementPieceEntity>> =
        procedureDao.getPiecesForProcedure(procedureId)

    fun getStepsForProcedure(procedureId: String): Flow<List<ProcedureStepEntity>> =
        procedureDao.getStepsForProcedure(procedureId)

    // --- Locations ---
    fun getAllLocations(): Flow<List<LocationEntity>> = locationDao.getAllLocations()

    fun getLocationsByCity(city: String): Flow<List<LocationEntity>> =
        if (city.isEmpty() || city == "Toutes" || city == "All") {
            locationDao.getAllLocations()
        } else {
            locationDao.getLocationsByCity(city)
        }

    // --- Dossiers Suivis ---
    fun getAllDossiers(): Flow<List<UserDossierEntity>> = dossierDao.getAllDossiers()

    fun getDossierById(id: Long): Flow<UserDossierEntity?> = dossierDao.getDossierById(id)

    suspend fun createDossier(dossier: UserDossierEntity): Long = withContext(Dispatchers.IO) {
        dossierDao.insertDossier(dossier)
    }

    suspend fun updateDossier(dossier: UserDossierEntity) = withContext(Dispatchers.IO) {
        dossierDao.updateDossier(dossier)
    }

    suspend fun deleteDossier(id: Long) = withContext(Dispatchers.IO) {
        dossierDao.deleteDossier(id)
    }

    fun getPieceStatesForDossier(dossierId: Long): Flow<List<DossierPieceStateEntity>> =
        dossierDao.getPieceStatesForDossier(dossierId)

    suspend fun setPieceState(dossierId: Long, pieceId: String, isChecked: Boolean, note: String = "") =
        withContext(Dispatchers.IO) {
            dossierDao.insertOrUpdatePieceState(
                DossierPieceStateEntity(
                    dossierId = dossierId,
                    pieceId = pieceId,
                    isChecked = isChecked,
                    userNote = note
                )
            )
        }

    fun getExpensesForDossier(dossierId: Long): Flow<List<DossierExpenseEntity>> =
        dossierDao.getExpensesForDossier(dossierId)

    suspend fun addExpense(expense: DossierExpenseEntity): Long = withContext(Dispatchers.IO) {
        dossierDao.insertExpense(expense)
    }

    suspend fun deleteExpense(id: Long) = withContext(Dispatchers.IO) {
        dossierDao.deleteExpense(id)
    }

    // --- Community & Transparency ---
    fun getAllTestimonials(): Flow<List<TestimonialEntity>> = communityDao.getAllTestimonials()

    fun getTestimonialsForProcedure(procedureId: String): Flow<List<TestimonialEntity>> =
        communityDao.getTestimonialsForProcedure(procedureId)

    suspend fun submitTestimonial(
        procedureId: String,
        procedureTitle: String,
        city: String,
        serviceName: String,
        delayDays: Int,
        costPaidXaf: Int,
        rating: Int,
        comment: String,
        authorDisplay: String,
        isAnonymous: Boolean
    ): Long = withContext(Dispatchers.IO) {
        val sanitizedComment = sanitizeCommunityText(comment)
        val testimonial = TestimonialEntity(
            procedureId = procedureId,
            procedureTitle = procedureTitle,
            city = city,
            serviceName = serviceName,
            delayDays = delayDays,
            costPaidXaf = costPaidXaf,
            rating = rating,
            comment = sanitizedComment,
            authorDisplay = if (isAnonymous) "Citoyen anonyme" else authorDisplay,
            isAnonymous = isAnonymous,
            createdAt = "Aujourd'hui"
        )
        communityDao.insertTestimonial(testimonial)
    }

    suspend fun incrementHelpful(id: Long) = withContext(Dispatchers.IO) {
        communityDao.incrementHelpful(id)
    }

    fun getAllReports(): Flow<List<ReportEntity>> = communityDao.getAllReports()

    suspend fun submitReport(
        procedureId: String,
        procedureTitle: String,
        fieldReported: String,
        userCostReportedXaf: Int,
        userComment: String
    ): Long = withContext(Dispatchers.IO) {
        val sanitizedComment = sanitizeCommunityText(userComment)
        val report = ReportEntity(
            procedureId = procedureId,
            procedureTitle = procedureTitle,
            fieldReported = fieldReported,
            userCostReportedXaf = userCostReportedXaf,
            userComment = sanitizedComment,
            status = "EN_REVUE",
            createdAt = "Aujourd'hui"
        )
        communityDao.insertReport(report)
    }

    // --- Family Profiles ---
    fun getAllFamilyProfiles(): Flow<List<FamilyProfileEntity>> = familyDao.getAllProfiles()

    suspend fun addFamilyProfile(name: String, relationship: String, notes: String = ""): Long = withContext(Dispatchers.IO) {
        familyDao.insertProfile(
            FamilyProfileEntity(
                name = name,
                relationship = relationship,
                notes = notes
            )
        )
    }

    suspend fun deleteFamilyProfile(id: Long) = withContext(Dispatchers.IO) {
        familyDao.deleteProfile(id)
    }

    // --- Offline Saved Procedures ---
    fun isProcedureSavedOffline(procedureId: String): Flow<Boolean> = offlineDao.isSavedOffline(procedureId)

    fun getAllOfflineSavedProcedures(): Flow<List<OfflineSavedProcedureEntity>> = offlineDao.getAllSavedProcedures()

    suspend fun toggleOfflineSave(procedureId: String, currentlySaved: Boolean) = withContext(Dispatchers.IO) {
        if (currentlySaved) {
            offlineDao.removeOffline(procedureId)
        } else {
            offlineDao.saveOffline(OfflineSavedProcedureEntity(procedureId = procedureId))
        }
    }

    private fun sanitizeCommunityText(input: String): String {
        var cleaned = input.replace(Regex("""(\+?237\s?)?[62]\d{8}"""), "[Numéro masqué]")
        cleaned = cleaned.replace(
            Regex("""(?i)\b(agent|commissaire|inspecteur|officier|greffier|monsieur|mme)\s+[A-Z][a-z]+"""),
            "[Agent masqué par modération]"
        )
        return cleaned
    }
}
