package com.example.data.remote

import android.util.Log
import com.example.domain.model.Inquiry
import com.example.domain.model.PostedProperty
import com.example.domain.model.RentalAgreement
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class CloudSyncManager {

    private val firestore: FirebaseFirestore? by lazy {
        try {
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            Log.w("CloudSyncManager", "Firebase Firestore initialization skipped or missing google-services.json: ${e.message}")
            null
        }
    }

    val isCloudConnected: Boolean
        get() = firestore != null

    suspend fun syncPostedPropertyToCloud(property: PostedProperty): Boolean {
        val db = firestore ?: return false
        return try {
            val data = hashMapOf(
                "id" to property.id,
                "title" to property.title,
                "tagline" to property.tagline,
                "category" to property.category,
                "city" to property.city,
                "locality" to property.locality,
                "towerHouseNo" to property.towerHouseNo,
                "bhk" to property.bhk,
                "carpetAreaSqFt" to property.carpetAreaSqFt,
                "bathrooms" to property.bathrooms,
                "furnishing" to property.furnishing,
                "priceOrRent" to property.priceOrRent,
                "priceFormatted" to property.priceFormatted,
                "description" to property.description,
                "imageUrl" to property.imageUrl,
                "ownerType" to property.ownerType,
                "ownerName" to property.ownerName,
                "ownerPhone" to property.ownerPhone,
                "ownerEmail" to property.ownerEmail,
                "isVerified" to property.isVerified,
                "constructionStatus" to property.constructionStatus,
                "timestamp" to property.timestamp
            )
            db.collection("properties").document(property.id).set(data).await()
            Log.d("CloudSyncManager", "Successfully synced property ${property.id} to Firestore")
            true
        } catch (e: Exception) {
            Log.e("CloudSyncManager", "Failed to sync property to cloud: ${e.message}")
            false
        }
    }

    suspend fun syncInquiryToCloud(inquiry: Inquiry): Boolean {
        val db = firestore ?: return false
        return try {
            val data = hashMapOf(
                "id" to inquiry.id,
                "propertyId" to inquiry.propertyId,
                "propertyTitle" to inquiry.propertyTitle,
                "userName" to inquiry.userName,
                "userPhone" to inquiry.userPhone,
                "userEmail" to inquiry.userEmail,
                "message" to inquiry.message,
                "preferredTime" to inquiry.preferredTime,
                "timestamp" to inquiry.timestamp,
                "status" to inquiry.status
            )
            val docId = if (inquiry.id != 0L) inquiry.id.toString() else System.currentTimeMillis().toString()
            db.collection("inquiries").document(docId).set(data).await()
            Log.d("CloudSyncManager", "Successfully synced inquiry to Firestore")
            true
        } catch (e: Exception) {
            Log.e("CloudSyncManager", "Failed to sync inquiry to cloud: ${e.message}")
            false
        }
    }

    suspend fun syncRentalAgreementToCloud(agreement: RentalAgreement): Boolean {
        val db = firestore ?: return false
        return try {
            val data = hashMapOf(
                "id" to agreement.id,
                "agreementNumber" to agreement.agreementNumber,
                "tenantName" to agreement.tenantName,
                "landlordName" to agreement.landlordName,
                "propertyAddress" to agreement.propertyAddress,
                "city" to agreement.city,
                "monthlyRent" to agreement.monthlyRent,
                "securityDeposit" to agreement.securityDeposit,
                "agreementTermMonths" to agreement.agreementTermMonths,
                "startDate" to agreement.startDate,
                "lockInPeriodMonths" to agreement.lockInPeriodMonths,
                "stampDutyPaidAmount" to agreement.stampDutyPaidAmount,
                "status" to agreement.status,
                "timestamp" to agreement.timestamp
            )
            val agreementDocId = if (agreement.agreementNumber.isNotEmpty()) agreement.agreementNumber else agreement.id.toString()
            db.collection("rental_agreements").document(agreementDocId).set(data).await()
            Log.d("CloudSyncManager", "Successfully synced rental agreement ${agreement.agreementNumber} to Firestore")
            true
        } catch (e: Exception) {
            Log.e("CloudSyncManager", "Failed to sync rental agreement to cloud: ${e.message}")
            false
        }
    }

    fun observeCloudProperties(): Flow<List<PostedProperty>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val listenerRegistration = db.collection("properties")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("CloudSyncManager", "Error observing Firestore properties: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val properties = snapshot.documents.mapNotNull { doc ->
                        try {
                            PostedProperty(
                                id = doc.getString("id") ?: doc.id,
                                title = doc.getString("title") ?: "",
                                tagline = doc.getString("tagline") ?: "",
                                category = doc.getString("category") ?: "Buy",
                                city = doc.getString("city") ?: "",
                                locality = doc.getString("locality") ?: "",
                                towerHouseNo = doc.getString("towerHouseNo") ?: "",
                                bhk = (doc.getLong("bhk") ?: 3L).toInt(),
                                carpetAreaSqFt = (doc.getLong("carpetAreaSqFt") ?: 1000L).toInt(),
                                bathrooms = (doc.getLong("bathrooms") ?: 2L).toInt(),
                                furnishing = doc.getString("furnishing") ?: "Fully Furnished",
                                priceOrRent = doc.getDouble("priceOrRent") ?: 0.0,
                                priceFormatted = doc.getString("priceFormatted") ?: "$0",
                                description = doc.getString("description") ?: "",
                                imageUrl = doc.getString("imageUrl") ?: "",
                                ownerType = doc.getString("ownerType") ?: "Owner",
                                ownerName = doc.getString("ownerName") ?: "Agent",
                                ownerPhone = doc.getString("ownerPhone") ?: "",
                                ownerEmail = doc.getString("ownerEmail") ?: "",
                                isVerified = doc.getBoolean("isVerified") ?: true,
                                constructionStatus = doc.getString("constructionStatus") ?: "Ready to Move",
                                timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                    trySend(properties)
                }
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }
}
