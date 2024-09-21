package com.softcross.eatzy.data.source.remote

import com.softcross.eatzy.common.EatzySingleton
import com.softcross.eatzy.data.dto.CartDto
import com.softcross.eatzy.data.dto.FoodDto
import com.softcross.eatzy.data.remote.FoodService
import com.softcross.eatzy.common.ResponseState
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val foodService: FoodService
) : RemoteDataSource {

    override suspend fun getAllFoods(): ResponseState<List<FoodDto>> {
        return try {
            val response = foodService.getFoods().yemekler
            println(response)
            ResponseState.Success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseState.Error(e)
        }
    }

    override suspend fun addFoodToCart(cartDto: CartDto): ResponseState<Unit> {
        return try {
            val response = foodService.addFoodToCart(
                foodName = cartDto.name,
                foodPic = cartDto.imageUrl,
                foodPrice = cartDto.price,
                foodAmount = cartDto.count,
                username = cartDto.userName
            )
            ResponseState.Success(response)
        } catch (e: Exception) {
            ResponseState.Error(e)
        }
    }

    override suspend fun getCartFoods(username: String): ResponseState<List<CartDto>> {
        return try {
            val response = foodService.getCartFoods(username)
            println(response)
            ResponseState.Success(response.cart)
        } catch (e: java.io.EOFException) {
            ResponseState.Success(emptyList())
        } catch (e: Exception) {
            ResponseState.Error(e)
        }
    }

    override suspend fun deleteFoodFromCart(
        foodCartID: Int,
        username: String
    ): ResponseState<Unit> {
        return try {
            foodService.deleteFoodFromCart(foodCartID, username)
            println("$foodCartID, $username")
            ResponseState.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseState.Error(e)
        }
    }

}