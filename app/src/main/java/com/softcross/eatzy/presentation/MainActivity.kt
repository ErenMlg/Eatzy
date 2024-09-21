package com.softcross.eatzy.presentation

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.softcross.eatzy.common.EatzySingleton
import com.softcross.eatzy.navigation.Address
import com.softcross.eatzy.navigation.Cart
import com.softcross.eatzy.navigation.EatzyNavigation
import com.softcross.eatzy.navigation.Favorites
import com.softcross.eatzy.navigation.Introduce
import com.softcross.eatzy.navigation.Login
import com.softcross.eatzy.navigation.Main
import com.softcross.eatzy.navigation.ProductDetail
import com.softcross.eatzy.navigation.Promotion
import com.softcross.eatzy.navigation.Register
import com.softcross.eatzy.navigation.Settings
import com.softcross.eatzy.navigation.Splash
import com.softcross.eatzy.navigation.bottomNav.EatzyBottomNavigationBar
import com.softcross.eatzy.presentation.theme.BackgroundColor
import com.softcross.eatzy.presentation.theme.EatzyTheme
import com.softcross.eatzy.presentation.theme.PrimaryContainerColor
import com.softcross.eatzy.presentation.theme.PrimaryGreen
import com.softcross.eatzy.presentation.theme.PrimaryOrange
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setAppLanguage()
        setContent {
            val navController = rememberNavController()
            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = currentBackStackEntry?.destination?.route
            val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
            LaunchedEffect(currentRoute) {
                updateStatusBarAndNavBarColors(currentRoute)
                when (currentRoute) {
                    Main.route -> bottomBarState.value = true
                    Favorites.route -> bottomBarState.value = true
                    Promotion.route -> bottomBarState.value = true
                    Settings.route -> bottomBarState.value = true
                    else -> bottomBarState.value = false
                }
            }
            EatzyTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        LaunchedEffect(EatzySingleton.cartAmount.intValue) {
                            Log.e("BottomNav", "Cart Amount: ${EatzySingleton.cartAmount.value}")
                        }
                        EatzyBottomNavigationBar(
                            navController = navController,
                            bottomBarState = bottomBarState
                        )
                    }
                ) { innerPadding ->
                    EatzyNavigation(
                        navController = navController,
                        modifier = Modifier
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                    )
                }
            }
        }
    }

    private fun setAppLanguage() {
        val prefs = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val languageCode = prefs.getString("app_language", "en") // Varsayılan dil 'en'

        val locale = Locale(languageCode ?: "en")
        Locale.setDefault(locale)

        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun updateStatusBarAndNavBarColors(currentRoute: String?) {
        when (currentRoute) {
            Introduce.route -> {
                window.statusBarColor = PrimaryGreen.toArgb()
                window.navigationBarColor = PrimaryGreen.toArgb()
            }

            Login.route, Register.route, Splash.route -> {
                window.statusBarColor = BackgroundColor.toArgb()
                window.navigationBarColor = BackgroundColor.toArgb()
            }

            Main.route -> {
                window.statusBarColor = PrimaryOrange.toArgb()
                window.navigationBarColor = PrimaryOrange.toArgb()
            }

            Favorites.route, Promotion.route, Settings.route -> {
                window.statusBarColor = PrimaryContainerColor.toArgb()
                window.navigationBarColor = PrimaryOrange.toArgb()
            }

            ProductDetail.routeWithArgs, Cart.route -> {
                window.statusBarColor = PrimaryContainerColor.toArgb()
                window.navigationBarColor = PrimaryContainerColor.toArgb()
            }

            Address.route, Address.routeWithArgs -> {
                window.statusBarColor = PrimaryContainerColor.toArgb()
                window.navigationBarColor = BackgroundColor.toArgb()
            }
        }
    }
}
