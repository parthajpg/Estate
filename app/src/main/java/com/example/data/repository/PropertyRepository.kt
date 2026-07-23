package com.example.data.repository

import com.example.data.local.FavoriteEntity
import com.example.data.local.InquiryEntity
import com.example.data.local.PostedPropertyEntity
import com.example.data.local.RealEstateDao
import com.example.data.local.RentalAgreementEntity
import com.example.data.local.WalletTransactionEntity
import com.example.domain.model.Agent
import com.example.domain.model.FilterState
import com.example.domain.model.Inquiry
import com.example.domain.model.MortgageCalculation
import com.example.domain.model.PostedProperty
import com.example.domain.model.Property
import com.example.domain.model.PropertyType
import com.example.domain.model.RentalAgreement
import com.example.domain.model.SortOption
import com.example.domain.model.WalletTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlin.math.pow

class PropertyRepository(private val dao: RealEstateDao) {

    private val sampleAgents = listOf(
        Agent(
            name = "Victoria Sterling",
            role = "Senior Luxury Director",
            agency = "Sterling & Crest Real Estate",
            phone = "+1 (310) 892-4100",
            whatsappNumber = "+13108924100",
            email = "victoria@sterlingcrest.com",
            rating = 4.9f,
            verifiedTransactions = 142,
            avatarUrl = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=400&q=80"
        ),
        Agent(
            name = "Julian Vance",
            role = "Penthouse & Estate Specialist",
            agency = "Vance Private Office",
            phone = "+1 (212) 655-9080",
            whatsappNumber = "+12126559080",
            email = "julian@vanceprivate.com",
            rating = 5.0f,
            verifiedTransactions = 98,
            avatarUrl = "https://images.unsplash.com/photo-1560250097-0b93528c311a?w=400&q=80"
        ),
        Agent(
            name = "Elena Rostova",
            role = "Waterfront Properties Advisor",
            agency = "Aura Luxury Properties",
            phone = "+1 (305) 441-2030",
            whatsappNumber = "+13054412030",
            email = "elena@auraluxury.com",
            rating = 4.8f,
            verifiedTransactions = 115,
            avatarUrl = "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=400&q=80"
        )
    )

    private val sampleProperties = listOf(
        Property(
            id = "prop_1",
            title = "The Sovereign Horizon Villa",
            tagline = "Infinity Pool Waterfront Villa with Panoramic Sunset Ocean Views",
            priceFormatted = "$8,950,000",
            priceRaw = 8950000.0,
            location = "Pacific Coast Hwy, Malibu",
            city = "Malibu",
            neighborhood = "Carbon Beach",
            bhk = 5,
            bathrooms = 6,
            areaSqFt = 8200,
            isVerified = true,
            isFeatured = true,
            type = PropertyType.VILLA,
            images = listOf(
                "https://images.unsplash.com/photo-1613977257363-707ba9348227?w=1000&q=80",
                "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?w=1000&q=80",
                "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=1000&q=80",
                "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?w=1000&q=80"
            ),
            latitude = 34.0259,
            longitude = -118.7798,
            description = "An architectural masterpiece perched along the prestigious Carbon Beach coastline. Features frameless floor-to-ceiling glass panels, private heated infinity pool, smart home automation, private beach access, chef's kitchen, and climate-controlled wine lounge.",
            amenities = listOf("Infinity Pool", "Private Beach Access", "Wine Cellar", "Smart Home", "Helipad Access", "Sub-Zero Chef Kitchen"),
            agent = sampleAgents[0]
        ),
        Property(
            id = "prop_2",
            title = "Skyline Crown Penthouse",
            tagline = "Duplex Penthouse with Private Rooftop Terrace & Skyline Views",
            priceFormatted = "$12,400,000",
            priceRaw = 12400000.0,
            location = "Franklin St, TriBeCa",
            city = "New York",
            neighborhood = "TriBeCa",
            bhk = 4,
            bathrooms = 5,
            areaSqFt = 6100,
            isVerified = true,
            isFeatured = true,
            type = PropertyType.PENTHOUSE,
            images = listOf(
                "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=1000&q=80",
                "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=1000&q=80",
                "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=1000&q=80"
            ),
            latitude = 40.7180,
            longitude = -74.0078,
            description = "Crown jewel duplex penthouse boasting 360-degree Manhattan skyline vistas. Features double-height ceilings, private internal elevator, 2,000 sq ft landscaped rooftop terrace with plunge pool, custom Italian marble finishes, and 24/7 white-glove concierge.",
            amenities = listOf("Rooftop Plunge Pool", "Private Elevator", "360° City View", "Concierge 24/7", "Valet Parking", "Fitness Center"),
            agent = sampleAgents[1]
        ),
        Property(
            id = "prop_3",
            title = "Aura Coastal Residence",
            tagline = "Modernist Waterfront Mansion with Deepwater Dockage",
            priceFormatted = "$14,900,000",
            priceRaw = 14900000.0,
            location = "Star Island Dr, Miami Beach",
            city = "Miami",
            neighborhood = "Star Island",
            bhk = 6,
            bathrooms = 8,
            areaSqFt = 12000,
            isVerified = true,
            isFeatured = false,
            type = PropertyType.WATERFRONT,
            images = listOf(
                "https://images.unsplash.com/photo-1512915922686-57c11dde9b6b?w=1000&q=80",
                "https://images.unsplash.com/photo-1600565193348-f74bd3c7ccdf?w=1000&q=80",
                "https://images.unsplash.com/photo-1600585154526-990dced4db0d?w=1000&q=80"
            ),
            latitude = 25.7781,
            longitude = -80.1519,
            description = "Resort-style trophy asset on ultra-exclusive Star Island. Includes 100ft of deepwater yacht dockage, tropical courtyard garden, outdoor summer kitchen, private spa & sauna, and home cinema theater.",
            amenities = listOf("Yacht Dock", "Home Theater", "Private Spa & Sauna", "Summer Kitchen", "Gated Security", "4-Car Garage"),
            agent = sampleAgents[2]
        ),
        Property(
            id = "prop_4",
            title = "The Glass Pavilion",
            tagline = "Contemporary Mountain Estate surrounded by Aspen Pines",
            priceFormatted = "$4,750,000",
            priceRaw = 4750000.0,
            location = "Red Mountain Rd, Aspen",
            city = "Aspen",
            neighborhood = "Red Mountain",
            bhk = 4,
            bathrooms = 4,
            areaSqFt = 5400,
            isVerified = true,
            isFeatured = false,
            type = PropertyType.VILLA,
            images = listOf(
                "https://images.unsplash.com/photo-1518780664697-55e3ad937233?w=1000&q=80",
                "https://images.unsplash.com/photo-1600607687920-4e2a09cf159d?w=1000&q=80",
                "https://images.unsplash.com/photo-1600566753190-17f0baa2a6c3?w=1000&q=80"
            ),
            latitude = 39.1911,
            longitude = -106.8175,
            description = "Striking architectural mountain retreat combining sustainable cedar wood and structural glass. Direct ski-in ski-out access, radiant floor heating, outdoor hot tub, and wood-burning open fireplace.",
            amenities = listOf("Ski-In Ski-Out", "Outdoor Hot Tub", "Radiant Heating", "Fireplace", "Mountain Views", "Wine Storage"),
            agent = sampleAgents[0]
        ),
        Property(
            id = "prop_5",
            title = "Bellevue Heritage Townhouse",
            tagline = "Restored Victorian Luxury Residence in Prime Knightsbridge",
            priceFormatted = "$9,200,000",
            priceRaw = 9200000.0,
            location = "Knightsbridge Green",
            city = "London",
            neighborhood = "Knightsbridge",
            bhk = 3,
            bathrooms = 4,
            areaSqFt = 4200,
            isVerified = true,
            isFeatured = false,
            type = PropertyType.TOWNHOUSE,
            images = listOf(
                "https://images.unsplash.com/photo-1600585152220-90363fe7e115?w=1000&q=80",
                "https://images.unsplash.com/photo-1600607687644-c7171b42498b?w=1000&q=80"
            ),
            latitude = 51.5015,
            longitude = -0.1603,
            description = "Elegantly restored 5-story Victorian residence steps from Harrods. Features private rear walled garden, subterranean wellness suite with lap pool, bespoke wood paneling, and private garage.",
            amenities = listOf("Subterranean Pool", "Walled Garden", "Underfloor Heating", "Wine Cellar", "Private Garage"),
            agent = sampleAgents[1]
        ),
        Property(
            id = "prop_6",
            title = "The Palm Serenity Villa",
            tagline = "Ultra Luxury Palm Jumeirah Beachfront Villa",
            priceFormatted = "$6,800,000",
            priceRaw = 6800000.0,
            location = "Frond M, Palm Jumeirah",
            city = "Dubai",
            neighborhood = "Palm Jumeirah",
            bhk = 5,
            bathrooms = 6,
            areaSqFt = 7800,
            isVerified = true,
            isFeatured = true,
            type = PropertyType.VILLA,
            images = listOf(
                "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?w=1000&q=80",
                "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?w=1000&q=80"
            ),
            latitude = 25.1124,
            longitude = 55.1390,
            description = "Signature beachfront villa on Dubai's famous Palm Jumeirah. Includes private white sand beach frontage, temperature-controlled infinity pool, marble flooring throughout, and floor-to-ceiling glass.",
            amenities = listOf("Private Beach", "Infinity Pool", "Sky Garden", "Marble Floors", "24/7 Security", "Maid Quarter"),
            agent = sampleAgents[2]
        )
    )

    fun getPropertiesStream(filterState: FilterState): Flow<List<Property>> {
        return combine(
            dao.getAllFavoriteIds(),
            dao.getAllPostedProperties()
        ) { favoriteIds, postedEntities ->
            val favSet = favoriteIds.toSet()
            
            val convertedPostedProps = postedEntities.map { entity ->
                Property(
                    id = entity.id,
                    title = entity.title,
                    tagline = entity.tagline,
                    priceFormatted = entity.priceFormatted,
                    priceRaw = entity.priceOrRent,
                    location = "${entity.locality}, ${entity.city}",
                    city = entity.city,
                    neighborhood = entity.locality,
                    bhk = entity.bhk,
                    bathrooms = entity.bathrooms,
                    areaSqFt = entity.carpetAreaSqFt,
                    isVerified = entity.isVerified,
                    isFeatured = true,
                    type = if (entity.category.contains("Rent", ignoreCase = true)) PropertyType.APARTMENT else PropertyType.VILLA,
                    images = listOf(entity.imageUrl.ifEmpty { "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=1000&q=80" }),
                    latitude = 34.0259,
                    longitude = -118.7798,
                    description = entity.description,
                    amenities = listOf("Security 24/7", "Power Backup", "Parking", "Water Supply", "Gym"),
                    agent = Agent(
                        name = entity.ownerName,
                        role = entity.ownerType,
                        agency = "Haven ${entity.ownerType}",
                        phone = entity.ownerPhone,
                        whatsappNumber = entity.ownerPhone.replace("+", "").replace(" ", "").replace("-", ""),
                        email = entity.ownerEmail,
                        rating = 4.9f,
                        verifiedTransactions = 12,
                        avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&q=80"
                    ),
                    isFavorite = favSet.contains(entity.id)
                )
            }

            val allProps = convertedPostedProps + sampleProperties.map { prop ->
                prop.copy(isFavorite = favSet.contains(prop.id))
            }

            allProps.filter { prop ->
                // Category Filter ("Buy", "Rent", "Commercial", "New Projects")
                val matchesCategory = when (filterState.categoryTab) {
                    "Rent" -> prop.type == PropertyType.APARTMENT || prop.priceFormatted.contains("mo", ignoreCase = true) || prop.id.startsWith("post_")
                    "Commercial" -> prop.type == PropertyType.PENTHOUSE || prop.type == PropertyType.TOWNHOUSE
                    "New Projects" -> prop.isVerified && prop.isFeatured
                    else -> true // "Buy"
                }

                // Query Filter
                val matchesQuery = filterState.query.isEmpty() ||
                        prop.title.contains(filterState.query, ignoreCase = true) ||
                        prop.location.contains(filterState.query, ignoreCase = true) ||
                        prop.city.contains(filterState.query, ignoreCase = true)

                // City Filter
                val matchesCity = filterState.selectedCity == "All Cities" ||
                        prop.city.equals(filterState.selectedCity, ignoreCase = true)

                // Price Filter
                val matchesPrice = prop.priceRaw.toFloat() in filterState.minPrice..filterState.maxPrice

                // BHK Filter
                val matchesBhk = filterState.selectedBhk.isEmpty() || filterState.selectedBhk.contains(prop.bhk)

                // Verified Only
                val matchesVerified = !filterState.verifiedOnly || prop.isVerified

                // Property Type
                val matchesType = filterState.selectedPropertyType == PropertyType.ALL ||
                        prop.type == filterState.selectedPropertyType

                matchesCategory && matchesQuery && matchesCity && matchesPrice && matchesBhk && matchesVerified && matchesType
            }.sortedWith { a, b ->
                when (filterState.sortBy) {
                    SortOption.FEATURED -> b.isFeatured.compareTo(a.isFeatured)
                    SortOption.PRICE_LOW_HIGH -> a.priceRaw.compareTo(b.priceRaw)
                    SortOption.PRICE_HIGH_LOW -> b.priceRaw.compareTo(a.priceRaw)
                    SortOption.AREA_DESC -> b.areaSqFt.compareTo(a.areaSqFt)
                }
            }
        }
    }

    suspend fun postProperty(posted: PostedProperty) {
        dao.insertPostedProperty(
            PostedPropertyEntity(
                id = posted.id,
                title = posted.title,
                tagline = posted.tagline,
                category = posted.category,
                city = posted.city,
                locality = posted.locality,
                towerHouseNo = posted.towerHouseNo,
                bhk = posted.bhk,
                carpetAreaSqFt = posted.carpetAreaSqFt,
                bathrooms = posted.bathrooms,
                furnishing = posted.furnishing,
                priceOrRent = posted.priceOrRent,
                priceFormatted = posted.priceFormatted,
                description = posted.description,
                imageUrl = posted.imageUrl,
                ownerType = posted.ownerType,
                ownerName = posted.ownerName,
                ownerPhone = posted.ownerPhone,
                ownerEmail = posted.ownerEmail,
                isVerified = posted.isVerified,
                constructionStatus = posted.constructionStatus,
                timestamp = posted.timestamp
            )
        )
    }

    fun getPostedPropertiesStream(): Flow<List<PostedProperty>> {
        return dao.getAllPostedProperties().map { list ->
            list.map { entity ->
                PostedProperty(
                    id = entity.id,
                    title = entity.title,
                    tagline = entity.tagline,
                    category = entity.category,
                    city = entity.city,
                    locality = entity.locality,
                    towerHouseNo = entity.towerHouseNo,
                    bhk = entity.bhk,
                    carpetAreaSqFt = entity.carpetAreaSqFt,
                    bathrooms = entity.bathrooms,
                    furnishing = entity.furnishing,
                    priceOrRent = entity.priceOrRent,
                    priceFormatted = entity.priceFormatted,
                    description = entity.description,
                    imageUrl = entity.imageUrl,
                    ownerType = entity.ownerType,
                    ownerName = entity.ownerName,
                    ownerPhone = entity.ownerPhone,
                    ownerEmail = entity.ownerEmail,
                    isVerified = entity.isVerified,
                    constructionStatus = entity.constructionStatus,
                    timestamp = entity.timestamp
                )
            }
        }
    }

    suspend fun deletePostedProperty(id: String) {
        dao.deletePostedProperty(id)
    }

    // Rental Agreement Generator
    suspend fun saveRentalAgreement(agreement: RentalAgreement) {
        dao.insertRentalAgreement(
            RentalAgreementEntity(
                agreementNumber = agreement.agreementNumber,
                tenantName = agreement.tenantName,
                landlordName = agreement.landlordName,
                propertyAddress = agreement.propertyAddress,
                city = agreement.city,
                monthlyRent = agreement.monthlyRent,
                securityDeposit = agreement.securityDeposit,
                agreementTermMonths = agreement.agreementTermMonths,
                startDate = agreement.startDate,
                lockInPeriodMonths = agreement.lockInPeriodMonths,
                stampDutyPaidAmount = agreement.stampDutyPaidAmount,
                status = agreement.status,
                timestamp = agreement.timestamp
            )
        )
    }

    fun getRentalAgreementsStream(): Flow<List<RentalAgreement>> {
        return dao.getAllRentalAgreements().map { list ->
            list.map { entity ->
                RentalAgreement(
                    id = entity.id,
                    agreementNumber = entity.agreementNumber,
                    tenantName = entity.tenantName,
                    landlordName = entity.landlordName,
                    propertyAddress = entity.propertyAddress,
                    city = entity.city,
                    monthlyRent = entity.monthlyRent,
                    securityDeposit = entity.securityDeposit,
                    agreementTermMonths = entity.agreementTermMonths,
                    startDate = entity.startDate,
                    lockInPeriodMonths = entity.lockInPeriodMonths,
                    stampDutyPaidAmount = entity.stampDutyPaidAmount,
                    status = entity.status,
                    timestamp = entity.timestamp
                )
            }
        }
    }

    // Wallet & Credits
    fun getWalletTransactionsStream(): Flow<List<WalletTransaction>> {
        return dao.getAllWalletTransactions().map { list ->
            list.map { entity ->
                WalletTransaction(
                    id = entity.id,
                    description = entity.description,
                    creditsDelta = entity.creditsDelta,
                    type = entity.type,
                    timestamp = entity.timestamp
                )
            }
        }
    }

    fun getWalletBalanceStream(): Flow<Int> {
        return dao.getAllWalletTransactions().map { list ->
            val base = 250 // Initial free credits for Dealer
            base + list.sumOf { it.creditsDelta }
        }
    }

    suspend fun addWalletCredits(description: String, credits: Int, type: String) {
        dao.insertWalletTransaction(
            WalletTransactionEntity(
                description = description,
                creditsDelta = credits,
                type = type
            )
        )
    }

    fun getFavoritePropertiesStream(): Flow<List<Property>> {
        return dao.getAllFavoriteIds().map { favoriteIds ->
            val set = favoriteIds.toSet()
            sampleProperties.filter { set.contains(it.id) }.map { it.copy(isFavorite = true) }
        }
    }

    suspend fun toggleFavorite(propertyId: String) {
        val isFav = dao.isFavorite(propertyId)
        if (isFav) {
            dao.removeFavorite(propertyId)
        } else {
            dao.insertFavorite(FavoriteEntity(propertyId))
        }
    }

    suspend fun submitInquiry(inquiry: Inquiry) {
        dao.insertInquiry(
            InquiryEntity(
                propertyId = inquiry.propertyId,
                propertyTitle = inquiry.propertyTitle,
                userName = inquiry.userName,
                userPhone = inquiry.userPhone,
                userEmail = inquiry.userEmail,
                message = inquiry.message,
                preferredTime = inquiry.preferredTime
            )
        )
    }

    fun getAllInquiriesStream(): Flow<List<Inquiry>> {
        return dao.getAllInquiries().map { list ->
            list.map { entity ->
                Inquiry(
                    id = entity.id,
                    propertyId = entity.propertyId,
                    propertyTitle = entity.propertyTitle,
                    userName = entity.userName,
                    userPhone = entity.userPhone,
                    userEmail = entity.userEmail,
                    message = entity.message,
                    preferredTime = entity.preferredTime,
                    timestamp = entity.timestamp,
                    status = entity.status
                )
            }
        }
    }

    fun calculateMortgage(
        homePrice: Double,
        downPaymentPercent: Float,
        interestRateAnnual: Float,
        tenureYears: Int
    ): MortgageCalculation {
        val downPaymentAmount = homePrice * (downPaymentPercent / 100f)
        val loanAmount = (homePrice - downPaymentAmount).coerceAtLeast(0.0)
        
        val monthlyInterestRate = (interestRateAnnual / 100f / 12f).toDouble()
        val totalMonths = tenureYears * 12

        val emi = if (monthlyInterestRate > 0 && totalMonths > 0) {
            val numerator = loanAmount * monthlyInterestRate * (1 + monthlyInterestRate).pow(totalMonths.toDouble())
            val denominator = (1 + monthlyInterestRate).pow(totalMonths.toDouble()) - 1
            if (denominator != 0.0) numerator / denominator else 0.0
        } else if (totalMonths > 0) {
            loanAmount / totalMonths
        } else {
            0.0
        }

        val totalPayment = emi * totalMonths
        val totalInterestPaid = (totalPayment - loanAmount).coerceAtLeast(0.0)

        return MortgageCalculation(
            homePrice = homePrice,
            downPaymentPercent = downPaymentPercent,
            downPaymentAmount = downPaymentAmount,
            interestRateAnnual = interestRateAnnual,
            tenureYears = tenureYears,
            loanAmount = loanAmount,
            monthlyEMI = emi,
            totalInterestPaid = totalInterestPaid,
            totalPayment = totalPayment
        )
    }
}
