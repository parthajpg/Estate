package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RealEstateDao {
    @Query("SELECT propertyId FROM favorites")
    fun getAllFavoriteIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE propertyId = :propertyId")
    suspend fun removeFavorite(propertyId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE propertyId = :propertyId)")
    suspend fun isFavorite(propertyId: String): Boolean

    @Query("SELECT * FROM inquiries ORDER BY timestamp DESC")
    fun getAllInquiries(): Flow<List<InquiryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInquiry(inquiry: InquiryEntity)

    // Posted Properties
    @Query("SELECT * FROM posted_properties ORDER BY timestamp DESC")
    fun getAllPostedProperties(): Flow<List<PostedPropertyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPostedProperty(property: PostedPropertyEntity)

    @Query("DELETE FROM posted_properties WHERE id = :id")
    suspend fun deletePostedProperty(id: String)

    // Rental Agreements
    @Query("SELECT * FROM rental_agreements ORDER BY timestamp DESC")
    fun getAllRentalAgreements(): Flow<List<RentalAgreementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRentalAgreement(agreement: RentalAgreementEntity)

    // Wallet Transactions
    @Query("SELECT * FROM wallet_transactions ORDER BY timestamp DESC")
    fun getAllWalletTransactions(): Flow<List<WalletTransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWalletTransaction(transaction: WalletTransactionEntity)
}
