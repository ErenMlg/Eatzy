package com.softcross.eatzy.domain.repository

import com.softcross.eatzy.domain.model.User
import com.softcross.eatzy.common.ResponseState
import com.softcross.eatzy.domain.model.Location
import com.softcross.eatzy.domain.model.Payment
import com.softcross.eatzy.domain.model.Promotion

interface FirebaseRepository {

    suspend fun loginUser(email: String, password: String): ResponseState<User>

    fun checkLoggedUser(): Boolean

    suspend fun registerUser(
        email: String,
        password: String,
        fullName: String,
        userName:String,
        phoneNumber: String
    ): ResponseState<User>

    suspend fun addUserDetailsToFirestore(userModel: User)

    suspend fun getUserDetailFromFirestore(): User

    suspend fun sendResetPasswordEmail(email: String): ResponseState<Boolean>

    fun signOutUser()

    suspend fun getPromotions(): ResponseState<List<Promotion>>

    suspend fun getLocations(): ResponseState<List<Location>>

    suspend fun getPayments(): ResponseState<List<Payment>>

    suspend fun addPayment(payment: Payment): ResponseState<Boolean>

    suspend fun addLocation(location: Location): ResponseState<Boolean>

    suspend fun updateLocation(locationId: String, updatedLocation: Location): ResponseState<Boolean>

    suspend fun deleteLocation(location: Location): ResponseState<Boolean>

    suspend fun updatePayment(paymentID: String, payment: Payment): ResponseState<Boolean>

    suspend fun deletePayment(payment: Payment): ResponseState<Boolean>

    suspend fun findPromotionWithCode(promotionCode: String): ResponseState<Promotion>
}