package com.softcross.eatzy.presentation

import android.content.Context
import android.content.Intent
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.softcross.eatzy.R
import com.softcross.eatzy.common.EatzySingleton
import com.softcross.eatzy.common.NetworkStatusHelper
import com.softcross.eatzy.common.extension.observeAsState
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
import com.softcross.eatzy.presentation.components.NonIgnorableTwoButtonDialog
import com.softcross.eatzy.presentation.main.MainContract.UiAction
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
            val context = LocalContext.current
            val networkStatusHelper = NetworkStatusHelper(context)
            val networkStatus by networkStatusHelper.isNetworkAvailable.observeAsState(
                initial = true
            )
            var showNetworkError by remember {
                mutableStateOf(false)
            }

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

                    LaunchedEffect(networkStatus) {
                        showNetworkError = networkStatus.not()
                    }
                    if (showNetworkError) {
                        NonIgnorableTwoButtonDialog(
                            title = stringResource(R.string.network_connection_error),
                            message = stringResource(R.string.you_don_t_have_an_active_network_connection_do_you_want_retry),
                            buttonText = stringResource(id = R.string.retry),
                            onSubmitClick = {
                                val intent = Intent(context, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                context.startActivity(intent)
                                Runtime.getRuntime().exit(0)
                            },
                            onCancelClick = { finishAffinity() }
                        )
                    }else{
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
