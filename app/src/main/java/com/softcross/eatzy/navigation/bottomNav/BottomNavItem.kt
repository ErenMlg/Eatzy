package com.softcross.eatzy.navigation.bottomNav

import com.softcross.eatzy.R

sealed class BottomNavItem(
    val title: String,
    val icon: Int,
    val route: String
) {
    data object ExploreScreen : BottomNavItem(
        title = "Explore",
        icon = R.drawable.icon_explore,
        route = "main"
    )

    data object FavoritesScreen : BottomNavItem(
        title = "Favorites",
        icon = R.drawable.icon_fav_filled,
        route = "favorites"
    )

    data object AccountScreen : BottomNavItem(
        title = "Account",
        icon = R.drawable.icon_settings,
        route = "settings"
    )

    data object CampaignScreen : BottomNavItem(
        title = "Campaign",
        icon = R.drawable.icon_campaings,
        route = "promotion"
    )

    data object CartScreen : BottomNavItem(
        title = "Cart",
        icon = R.drawable.icon_cart,
        route = "cart"
    )
}