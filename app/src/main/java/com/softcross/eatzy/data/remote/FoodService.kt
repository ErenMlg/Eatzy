package com.softcross.eatzy.data.remote

import com.softcross.eatzy.common.ResponseState
import com.softcross.eatzy.data.dto.CartDto
import com.softcross.eatzy.data.dto.CartResponse
import com.softcross.eatzy.data.dto.FoodResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface FoodService {

    @GET("tumYemekleriGetir.php")
    suspend fun getFoods(): FoodResponse

    @POST("sepeteYemekEkle.php")
    @FormUrlEncoded
    suspend fun addFoodToCart(
        @Field("yemek_adi") foodName: String,
        @Field("yemek_resim_adi") foodPic: String,
        @Field("yemek_fiyat") foodPrice: Int,
        @Field("yemek_siparis_adet") foodAmount: Int,
        @Field("kullanici_adi") username: String
    )

    @POST("sepettekiYemekleriGetir.php")
    @FormUrlEncoded
    suspend fun getCartFoods(
        @Field("kullanici_adi") username: String
    ): CartResponse

    @POST("sepettenYemekSil.php")
    @FormUrlEncoded
    suspend fun deleteFoodFromCart(
        @Field("sepet_yemek_id") foodCartID: Int,
        @Field("kullanici_adi") username: String
    )
}