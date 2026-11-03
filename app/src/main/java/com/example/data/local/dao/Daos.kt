package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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
import kotlinx.coroutines.flow.Flow

@Dao
interface ProcedureDao {
    @Query("SELECT * FROM procedures ORDER BY popularPriority DESC, titleFr ASC")
    fun getAllProcedures(): Flow<List<ProcedureEntity>>

    @Query("SELECT * FROM procedures WHERE id = :id LIMIT 1")
    fun getProcedureById(id: String): Flow<ProcedureEntity?>

    @Query("SELECT * FROM procedures WHERE slug = :slug LIMIT 1")
    fun getProcedureBySlug(slug: String): Flow<ProcedureEntity?>

    @Query("SELECT * FROM procedures WHERE titleFr LIKE '%' || :query || '%' OR titleEn LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%'")
    fun searchProcedures(query: String): Flow<List<ProcedureEntity>>

    @Query("SELECT * FROM procedure_variants WHERE procedureId = :procedureId")
    fun getVariantsForProcedure(procedureId: String): Flow<List<ProcedureVariantEntity>>

    @Query("SELECT * FROM requirement_pieces WHERE procedureId = :procedureId")
    fun getPiecesForProcedure(procedureId: String): Flow<List<RequirementPieceEntity>>

    @Query("SELECT * FROM procedure_steps WHERE procedureId = :procedureId ORDER BY stepOrder ASC")
    fun getStepsForProcedure(procedureId: String): Flow<List<ProcedureStepEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProcedures(procedures: List<ProcedureEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVariants(variants: List<ProcedureVariantEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPieces(pieces: List<RequirementPieceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(steps: List<ProcedureStepEntity>)
}

@Dao
interface LocationDao {
    @Query("SELECT * FROM locations ORDER BY city ASC, name ASC")
    fun getAllLocations(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE city = :city ORDER BY name ASC")
    fun getLocationsByCity(city: String): Flow<List<LocationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocations(locations: List<LocationEntity>)
}

@Dao
interface DossierDao {
    @Query("SELECT * FROM user_dossiers ORDER BY createdAtTimestamp DESC")
    fun getAllDossiers(): Flow<List<UserDossierEntity>>

    @Query("SELECT * FROM user_dossiers WHERE id = :id LIMIT 1")
    fun getDossierById(id: Long): Flow<UserDossierEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDossier(dossier: UserDossierEntity): Long

    @Update
    suspend fun updateDossier(dossier: UserDossierEntity)

    @Query("DELETE FROM user_dossiers WHERE id = :id")
    suspend fun deleteDossier(id: Long)

    @Query("SELECT * FROM dossier_piece_states WHERE dossierId = :dossierId")
    fun getPieceStatesForDossier(dossierId: Long): Flow<List<DossierPieceStateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePieceState(pieceState: DossierPieceStateEntity)

    @Query("SELECT * FROM dossier_expenses WHERE dossierId = :dossierId ORDER BY id ASC")
    fun getExpensesForDossier(dossierId: Long): Flow<List<DossierExpenseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: DossierExpenseEntity): Long

    @Query("DELETE FROM dossier_expenses WHERE id = :id")
    suspend fun deleteExpense(id: Long)
}

@Dao
interface CommunityDao {
    @Query("SELECT * FROM testimonials ORDER BY id DESC")
    fun getAllTestimonials(): Flow<List<TestimonialEntity>>

    @Query("SELECT * FROM testimonials WHERE procedureId = :procedureId ORDER BY id DESC")
    fun getTestimonialsForProcedure(procedureId: String): Flow<List<TestimonialEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestimonial(testimonial: TestimonialEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestimonials(testimonials: List<TestimonialEntity>)

    @Query("UPDATE testimonials SET helpfulCount = helpfulCount + 1 WHERE id = :id")
    suspend fun incrementHelpful(id: Long)

    @Query("SELECT * FROM reports ORDER BY id DESC")
    fun getAllReports(): Flow<List<ReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportEntity): Long
}

@Dao
interface FamilyDao {
    @Query("SELECT * FROM family_profiles ORDER BY id ASC")
    fun getAllProfiles(): Flow<List<FamilyProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: FamilyProfileEntity): Long

    @Query("DELETE FROM family_profiles WHERE id = :id")
    suspend fun deleteProfile(id: Long)
}

@Dao
interface OfflineDao {
    @Query("SELECT * FROM offline_saved_procedures")
    fun getAllSavedProcedures(): Flow<List<OfflineSavedProcedureEntity>>

    @Query("SELECT COUNT(*) > 0 FROM offline_saved_procedures WHERE procedureId = :procedureId")
    fun isSavedOffline(procedureId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOffline(item: OfflineSavedProcedureEntity)

    @Query("DELETE FROM offline_saved_procedures WHERE procedureId = :procedureId")
    suspend fun removeOffline(procedureId: String)
}
