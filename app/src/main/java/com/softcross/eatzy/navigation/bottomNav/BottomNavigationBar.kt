package com.softcross.eatzy.navigation.bottomNav

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.softcross.eatzy.common.EatzySingleton
import com.softcross.eatzy.presentation.theme.PrimaryContainerColor
import com.softcross.eatzy.presentation.theme.PrimaryGray
import com.softcross.eatzy.presentation.theme.PrimaryOrange
import com.softcross.eatzy.presentation.theme.PrimaryTextFieldColor

@Composable
fun EatzyBottomNavigationBar(
    navController: NavController,
    bottomBarState: MutableState<Boolean>
) {
    val cartAmount = EatzySingleton.cartAmount.value
    val items = listOf(
        BottomNavItem.ExploreScreen,
        BottomNavItem.FavoritesScreen,
        BottomNavItem.CartScreen,
        BottomNavItem.CampaignScreen,
        BottomNavItem.AccountScreen
    )

    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = ExitTransition.None
    ) {
        NavigationBar(
            containerColor = PrimaryOrange,
            modifier = Modifier.height(90.dp)
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            items.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        if (item == BottomNavItem.CartScreen) {
                            Box(
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = item.icon),
                                    contentDescription = item.title,
                                    tint = PrimaryTextFieldColor,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(start = 4.dp, bottom = 4.dp)
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryTextFieldColor)
                                ) {
                                    BasicText(
                                        text = cartAmount.toString(),
                                        style = TextStyle(fontSize = 12.sp, color = PrimaryOrange),
                                        modifier = Modifier.align(Alignment.Center)
                                    )

                                }
                            }
                        } else {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = item.title,
                                tint = PrimaryTextFieldColor
                            )
                        }
                    },
                    label = {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(PrimaryTextFieldColor)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryTextFieldColor,
                        selectedTextColor = PrimaryTextFieldColor,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = PrimaryTextFieldColor,
                        unselectedTextColor = PrimaryTextFieldColor
                    ),
                    alwaysShowLabel = false,
                    modifier = Modifier
                        .background(PrimaryOrange)
                        .height(80.dp)
                )
            }
        }
    }
}

@Preview(backgroundColor = 0xFFFFFF)
@Composable
fun BottomNavigationBarPreview() {
    val x = remember {
        mutableStateOf(false)
    }
    MaterialTheme {
        EatzyBottomNavigationBar(
            navController = rememberNavController(),
            bottomBarState = x
        )
    }
}