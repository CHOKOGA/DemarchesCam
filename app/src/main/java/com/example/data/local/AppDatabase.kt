package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.CommunityDao
import com.example.data.local.dao.DossierDao
import com.example.data.local.dao.FamilyDao
import com.example.data.local.dao.LocationDao
import com.example.data.local.dao.OfflineDao
import com.example.data.local.dao.ProcedureDao
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ProcedureEntity::class,
        ProcedureVariantEntity::class,
        RequirementPieceEntity::class,
        ProcedureStepEntity::class,
        LocationEntity::class,
        UserDossierEntity::class,
        DossierPieceStateEntity::class,
        DossierExpenseEntity::class,
        TestimonialEntity::class,
        ReportEntity::class,
        FamilyProfileEntity::class,
        OfflineSavedProcedureEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun procedureDao(): ProcedureDao
    abstract fun locationDao(): LocationDao
    abstract fun dossierDao(): DossierDao
    abstract fun communityDao(): CommunityDao
    abstract fun familyDao(): FamilyDao
    abstract fun offlineDao(): OfflineDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "demarchescam.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                val database = getInstance(context)
                                database.procedureDao().insertProcedures(PreseedData.procedures)
                                database.procedureDao().insertVariants(PreseedData.variants)
                                database.procedureDao().insertPieces(PreseedData.requirementPieces)
                                database.procedureDao().insertSteps(PreseedData.steps)
                                database.locationDao().insertLocations(PreseedData.locations)
                                database.communityDao().insertTestimonials(PreseedData.testimonials)
                                database.familyDao().insertProfile(
                                    FamilyProfileEntity(
                                        name = "Moi",
                                        relationship = "Personnel",
                                        notes = "Profil principal"
                                    )
                                )
                            }
                        }
                    }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
