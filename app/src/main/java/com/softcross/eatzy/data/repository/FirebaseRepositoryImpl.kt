package com.softcross.eatzy.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.softcross.eatzy.R
import com.softcross.eatzy.common.ContextProvider
import com.softcross.eatzy.common.EatzySingleton
import com.softcross.eatzy.common.ResponseState
import com.softcross.eatzy.domain.model.Location
import com.softcross.eatzy.domain.model.Payment
import com.softcross.eatzy.domain.model.Promotion
import com.softcross.eatzy.domain.model.User
import com.softcross.eatzy.domain.repository.FirebaseRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val provider: ContextProvider
) : FirebaseRepository {

    override suspend fun loginUser(email: String, password: String): ResponseState<User> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val loggedUser = getUserDetailFromFirestore()
            ResponseState.Success(loggedUser)
        } catch (e: Exception) {
            ResponseState.Error(e)
        }
    }

    override fun checkLoggedUser(): Boolean = firebaseAuth.currentUser != null

    override suspend fun registerUser(
        email: String,
        password: String,
        fullName: String,
        userName: String,
        phoneNumber: String
    ): ResponseState<User> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = User(
                id = firebaseAuth.currentUser?.uid ?: "",
                username = userName,
                fullName = fullName,
                phone = phoneNumber
            )
            addUserDetailsToFirestore(user)
            ResponseState.Success(user)
        } catch (e: Exception) {
            ResponseState.Error(e)
        }
    }

    override suspend fun addUserDetailsToFirestore(userModel: User) {
        val firestoreUsers = firebaseFirestore.collection("Users").document(userModel.id)
        firestoreUsers.set(
            hashMapOf(
                "fullName" to userModel.fullName,
                "userName" to userModel.username,
                "phone" to userModel.phone
            )
        ).addOnFailureListener {
            throw Exception(it.message)
        }.await()
    }


    override suspend fun getUserDetailFromFirestore(): User {
        val userID = firebaseAuth.currentUser?.uid ?: ""
        val firestoreDoc = firebaseFirestore.collection("Users").document(userID).get().await()
        if (firestoreDoc.data != null) {
            val fullName = firestoreDoc.data?.get("fullName").toString()
            val userName = firestoreDoc.data?.get("userName").toString()
            val phone = firestoreDoc.data?.get("phone").toString()
            val userModel = User(
                userID,
                userName,
                fullName,
                phone
            )
            return userModel
        } else {
            firebaseAuth.signOut()
            throw Exception(provider.context.getString(R.string.something_went_wrong_while_getting_user_details_from_firestore))
        }
    }

    override suspend fun sendResetPasswordEmail(email: String): ResponseState<Boolean> {
        TODO("Not yet implemented")
    }

    override fun signOutUser() = firebaseAuth.signOut()

    override suspend fun getPromotions(): ResponseState<List<Promotion>> {
        return try {
            val promotionSnapshot = firebaseFirestore.collection("Promotions")
                .whereEqualTo("userName", EatzySingleton.currentUsername)
                .get()
                .await()
            if (!promotionSnapshot.isEmpty) {
                val promotions = mutableListOf<Promotion>()
                promotionSnapshot.forEach { snapshot ->
                    val code = snapshot.data["code"].toString()
                    val title = snapshot.data["title"].toString()
                    val discount = snapshot.data["discount"].toString().toInt()
                    promotions.add(Promotion(code, title, discount))
                }
                ResponseState.Success(promotions)
            } else {
                ResponseState.Success(emptyList())
            }
        } catch (e: Exception) {
            ResponseState.Error(e)
        }
    }

    override suspend fun getLocations(): ResponseState<List<Location>> {
        return try {
            val locationSnapshot = firebaseFirestore.collection("Locations")
                .whereEqualTo("userName", EatzySingleton.currentUsername)
                .get()
                .await()
            if (!locationSnapshot.isEmpty) {
                val locations = mutableListOf<Location>()
                locationSnapshot.forEach { snapshot ->
                    val city = snapshot.data["city"].toString()
                    val country = snapshot.data["country"].toString()
                    val district = snapshot.data["district"].toString()
                    val openAdress = snapshot.data["openAdress"].toString()
                    val title = snapshot.data["title"].toString()
                    locations.add(
                        Location(
                            snapshot.id,
                            city,
                            district,
                            country,
                            openAdress,
                            title
                        )
                    )
                }
                ResponseState.Success(locations)
            } else {
                ResponseState.Success(emptyList())
            }
        } catch (e: Exception) {
            ResponseState.Error(e)
        }
    }

    override suspend fun getPayments(): ResponseState<List<Payment>> {
        return try {
            val paymentSnapshot = firebaseFirestore.collection("Payments")
                .whereEqualTo("userName", EatzySingleton.currentUsername)
                .get()
                .await()
            if (!paymentSnapshot.isEmpty) {
                val payments = mutableListOf<Payment>()
                paymentSnapshot.forEach { snapshot ->
                    val cartCVC = snapshot.data["cartCVC"].toString()
                    val cartName = snapshot.data["cartName"].toString()
                    val cartNo = snapshot.data["cartNo"].toString()
                    val cartDate = snapshot.data["cartDate"].toString()
                    val title = snapshot.data["title"].toString()
                    payments.add(
                        Payment(
                            id = snapshot.id,
                            title = title,
                            cartNumber = cartNo,
                            cartHolderName = cartName,
                            cartExpiryDate = cartDate,
                            cartCVC = cartCVC

                        )
                    )
                }
                ResponseState.Success(payments)
            } else {
                ResponseState.Success(emptyList())
            }
        } catch (e: Exception) {
            ResponseState.Error(e)
        }
    }

    override suspend fun addPayment(payment: Payment): ResponseState<Boolean> {
        return try {
            val firestoreUsers = firebaseFirestore.collection("Payments")
            firestoreUsers.add(
                hashMapOf(
                    "cartCVC" to payment.cartCVC,
                    "cartDate" to payment.cartExpiryDate,
                    "cartName" to payment.cartHolderName,
                    "cartNo" to payment.cartNumber,
                    "title" to payment.title,
                    "userName" to EatzySingleton.currentUsername
                )
            ).addOnFailureListener {
                throw Exception(it.message)
            }.await()
            ResponseState.Success(true)
        } catch (e: Exception) {
            ResponseState.Error(e)
        }
    }

    override suspend fun addLocation(location: Location): ResponseState<Boolean> {
        return try {
            val firestoreUsers = firebaseFirestore.collection("Locations")
            firestoreUsers.add(
                hashMapOf(
                    "city" to location.city,
                    "country" to location.country,
                    "district" to location.district,
                    "openAdress" to location.openAddress,
                    "title" to location.title,
                    "userName" to EatzySingleton.currentUsername
                )
            ).addOnFailureListener {
                throw Exception(it.message)
            }.await()
            ResponseState.Success(true)
        } catch (e: Exception) {
            ResponseState.Error(e)
        }
    }

    override suspend fun updateLocation(
        locationId: String,
        updatedLocation: Location
    ): ResponseState<Boolean> {
        return try {
            val locationDoc = firebaseFirestore.collection("Locations").document(locationId)
            locationDoc.set(
                hashMapOf(
                    "city" to updatedLocation.city,
                    "district" to updatedLocation.district,
                    "country" to updatedLocation.country,
                    "openAdress" to updatedLocation.openAddress,
                    "title" to updatedLocation.title
                ),
                SetOptions.merge()
            ).await()
            ResponseState.Success(true)
        } catch (e: Exception) {
            ResponseState.Error(e)
        }
    }


    override suspend fun deleteLocation(location: Location): ResponseState<Boolean> {
        return try {
            val locationDoc = firebaseFirestore.collection("Locations").document(location.id)

            locationDoc.delete().await()

            ResponseState.Success(true)
        } catch (e: Exception) {
            ResponseState.Error(e)
        }
    }

    override suspend fun updatePayment(
        paymentID: String,
        payment: Payment
    ): ResponseState<Boolean> {
        return try {
            val locationDoc = firebaseFirestore.collection("Payments").document(paymentID)
            locationDoc.set(
                hashMapOf(
                    "title" to payment.title,
                    "cartNo" to payment.cartNumber,
                    "cartName" to payment.cartHolderName,
                    "cartDate" to payment.cartExpiryDate,
                    "cartCVC" to payment.cartCVC
                ),
                SetOptions.merge()
            ).await()
            ResponseState.Success(true)
        } catch (e: Exception) {
            ResponseState.Error(e)
        }
    }


    override suspend fun deletePayment(payment: Payment): ResponseState<Boolean> {
        return try {
            val paymentDoc = firebaseFirestore.collection("Payments").document(payment.id)

            paymentDoc.delete().await()

            ResponseState.Success(true)
        } catch (e: Exception) {
            ResponseState.Error(e)
        }
    }

    override suspend fun findPromotionWithCode(promotionCode: String): ResponseState<Promotion> {
        return try {
            val promotionSnapshot = firebaseFirestore.collection("Promotions")
                .whereEqualTo("code", promotionCode)
                .get()
                .await()
            if (!promotionSnapshot.isEmpty) {
                val promotion = promotionSnapshot.documents[0]
                if (promotion.data?.get("userName").toString() == EatzySingleton.currentUsername) {
                    val code = promotion.data?.get("code").toString()
                    val title = promotion.data?.get("title").toString()
                    val discount = promotion.data?.get("discount").toString().toInt()
                    println("code: $code, title: $title, discount: $discount")
                    ResponseState.Success(Promotion(code, title, discount))
                } else {
                    ResponseState.Error(Exception(provider.context.getString(R.string.promotion_find_but_not_belongs_to_user)))
                }
            } else {
                ResponseState.Error(Exception(provider.context.getString(R.string.promotion_not_found)))
            }
        } catch (e: Exception) {
            ResponseState.Error(e)
        }
    }

}