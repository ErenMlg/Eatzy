package com.softcross.eatzy.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.softcross.eatzy.presentation.address.AddressRoute
import com.softcross.eatzy.presentation.address.AddressViewModel
import com.softcross.eatzy.presentation.cart.CartRoute
import com.softcross.eatzy.presentation.cart.CartViewModel
import com.softcross.eatzy.presentation.favorites.FavoritesRoute
import com.softcross.eatzy.presentation.favorites.FavoritesViewModel
import com.softcross.eatzy.presentation.introduce.IntroduceRoute
import com.softcross.eatzy.presentation.login.LoginRoute
import com.softcross.eatzy.presentation.login.LoginViewModel
import com.softcross.eatzy.presentation.main.MainRoute
import com.softcross.eatzy.presentation.main.MainViewModel
import com.softcross.eatzy.presentation.payment.PaymentRoute
import com.softcross.eatzy.presentation.payment.PaymentViewModel
import com.softcross.eatzy.presentation.producDetail.ProductDetailRoute
import com.softcross.eatzy.presentation.producDetail.ProductDetailViewModel
import com.softcross.eatzy.presentation.promotions.PromotionRoute
import com.softcross.eatzy.presentation.promotions.PromotionViewModel
import com.softcross.eatzy.presentation.register.RegisterRoute
import com.softcross.eatzy.presentation.register.RegisterViewModel
import com.softcross.eatzy.presentation.settings.SettingsRoute
import com.softcross.eatzy.presentation.settings.SettingsViewModel
import com.softcross.eatzy.presentation.splash.SplashScreen
import com.softcross.eatzy.presentation.splash.SplashViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun EatzyNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Splash.route,
        modifier = modifier
    ) {
        composable(Splash.route) {
            val viewModel: SplashViewModel = hiltViewModel()
            val uiEffect = viewModel.uiEffect
            SplashScreen(uiEffect = uiEffect,
                navigateToMain = {
                    navController.navigate(Main.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                navigateToIntroduce = {
                    navController.navigate(Introduce.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Introduce.route) {
            IntroduceRoute(
                navigateToLogin = { navController.navigate(Login.route) },
                navigateToSingUp = { navController.navigate(Register.route) }
            )
        }

        composable(Login.route) {
            val viewModel: LoginViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            LoginRoute(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                navigateToSignUp = {
                    navController.navigate(Register.route) {
                        popUpTo(Login.route) {
                            inclusive = true
                        }
                    }
                },
                navigateToMain = {
                    navController.navigate(Main.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Register.route) {
            val viewModel: RegisterViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            RegisterRoute(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                navigateToLogin = {
                    navController.navigate(Login.route) {
                        popUpTo(Register.route) { inclusive = true }
                    }
                },
                navigateToMain = {
                    navController.navigate(Main.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }

        composable(Main.route) {
            val viewModel: MainViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            MainRoute(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                navigateToIntroduce = {
                    navController.navigate(Introduce.route) {
                        popUpTo(Main.route) { inclusive = true }
                    }
                },
                navigateToDetail = {
                    navController.navigate(
                        "${ProductDetail.route}/${Uri.encode(Json.encodeToString(it))}"
                    )
                },
                navigateToCart = { navController.navigate(Cart.route) },
                navigateToAddLocation = { navController.navigate(Address.route) },
            )
        }

        composable(ProductDetail.routeWithArgs, arguments = ProductDetail.arguments) {
            val viewModel: ProductDetailViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            ProductDetailRoute(
                uiState = uiState,
                onAction = viewModel::onAction,
                uiEffect = uiEffect,
                navigateToMain = {
                    navController.navigate(Main.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                        restoreState = true
                    }
                },
                navigateToCart = {
                    navController.navigate(Cart.route) {
                        popUpTo(Main.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Cart.route) {
            val viewModel: CartViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            CartRoute(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                navigateToBack = {
                    navController.navigate(Main.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                        restoreState = true
                    }
                },
                navigateToNewPayment = {
                    navController.navigate(Payment.route)
                }
                )
        }


        composable(Favorites.route) {
            val viewModel: FavoritesViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            FavoritesRoute(
                uiState = uiState,
                onAction = viewModel::onAction,
                uiEffect = uiEffect,
                navigateToDetail = {
                    navController.navigate(
                        "${ProductDetail.route}/${Uri.encode(Json.encodeToString(it))}"
                    )
                },
                navigateToMain = {
                    navController.navigate(Main.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }

        composable(Promotion.route) {
            val viewModel: PromotionViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            PromotionRoute(
                uiState = uiState,
                onAction = viewModel::onAction,
                navigateToMain = {
                    navController.navigate(Main.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }

        composable(Settings.route) {
            val viewModel: SettingsViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            SettingsRoute(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                navigateToIntroduce = {
                    navController.navigate(Introduce.route) {
                        popUpTo(Main.route) { inclusive = true }
                    }
                },
                navigateToLocationUpdate = {
                    navController.navigate("${Address.route}/${Uri.encode(Json.encodeToString(it))}")
                },
                navigateToNewLocation = {
                    navController.navigate(Address.route)
                },
                navigateToPaymentUpdate = {
                    navController.navigate("${Payment.route}/${Uri.encode(Json.encodeToString(it))}")
                },
                navigateToNewPayment = {
                    navController.navigate(Payment.route)
                },
                navigateToMain = {
                    navController.navigate(Main.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }

        composable(Address.routeWithArgs, arguments = Address.arguments) {
            val viewModel: AddressViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            AddressRoute(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                onNavigateProfile = {
                    navController.navigate(Settings.route) {
                        restoreState = true
                    }
                }
            )
        }

        composable(Address.route) {
            val viewModel: AddressViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            AddressRoute(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                onNavigateProfile = {
                    navController.navigate(Settings.route) {
                        restoreState = true
                    }
                }
            )
        }

        composable(Payment.route) {
            val viewModel: PaymentViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            PaymentRoute(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                onNavigateProfile = {
                    navController.navigate(Settings.route) {
                        restoreState = true
                    }
                }
            )
        }

        composable(Payment.routeWithArgs, arguments = Payment.arguments) {
            val viewModel: PaymentViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            PaymentRoute(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                onNavigateProfile = {
                    navController.navigate(Settings.route) {
                        restoreState = true
                    }
                }
            )
        }
    }
}