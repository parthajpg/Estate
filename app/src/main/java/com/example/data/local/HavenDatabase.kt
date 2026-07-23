package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        FavoriteEntity::class,
        InquiryEntity::class,
        PostedPropertyEntity::class,
        RentalAgreementEntity::class,
        WalletTransactionEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class HavenDatabase : RoomDatabase() {
    abstract fun dao(): RealEstateDao

    companion object {
        @Volatile
        private var INSTANCE: HavenDatabase? = null

        fun getInstance(context: Context): HavenDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HavenDatabase::class.java,
                    "haven_estate_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
