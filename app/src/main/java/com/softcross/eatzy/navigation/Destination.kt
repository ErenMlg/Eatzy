package com.softcross.eatzy.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.softcross.eatzy.domain.model.Food
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

interface Destination {
    val route: String
}

object Splash : Destination {
    override val route = "splash"
}

object Introduce : Destination {
    override val route = "introduce"
}

object Login : Destination {
    override val route = "login"
}

object Register : Destination {
    override val route = "register"
}

object Main : Destination {
    override val route = "main"
}

object ProductDetail : Destination {
    override val route = "productDetail"
    val routeWithArgs = "${route}/{food}"
    val arguments = listOf(
        navArgument("food") {
            type = NavType.StringType
        }
    )
}

object Cart : Destination {
    override val route = "cart"
}

object Favorites : Destination {
    override val route = "favorites"
}

object Promotion : Destination {
    override val route = "promotion"
}

object Settings : Destination {
    override val route = "settings"
}

object Address : Destination {
    override val route = "address"
    val routeWithArgs = "${route}/{data}"
    val arguments = listOf(
        navArgument("data") {
            type = NavType.StringType
        }
    )
}

object Payment : Destination {
    override val route = "payment"
    val routeWithArgs = "${route}/{data}"
    val arguments = listOf(
        navArgument("data") {
            type = NavType.StringType
        }
    )
}