package com.softcross.eatzy.presentation.main


import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.softcross.eatzy.R
import com.softcross.eatzy.domain.model.Food
import com.softcross.eatzy.domain.model.Location
import com.softcross.eatzy.presentation.components.CustomAsyncImage
import com.softcross.eatzy.presentation.components.CustomSnackbar
import com.softcross.eatzy.presentation.components.FilledButton
import com.softcross.eatzy.presentation.components.CustomLottieAnimation
import com.softcross.eatzy.presentation.components.NonIgnorableDialog
import com.softcross.eatzy.presentation.components.NonIgnorableTwoButtonDialog
import com.softcross.eatzy.presentation.components.SearchTextField
import com.softcross.eatzy.presentation.components.ShowCustomLocationSheet
import com.softcross.eatzy.presentation.components.SnackbarType
import com.softcross.eatzy.presentation.main.MainContract.UiAction
import com.softcross.eatzy.presentation.main.MainContract.UiEffect
import com.softcross.eatzy.presentation.main.MainContract.UiState
import com.softcross.eatzy.presentation.theme.BackgroundColor
import com.softcross.eatzy.presentation.theme.EatzyTheme
import com.softcross.eatzy.presentation.theme.PoppinsLight
import com.softcross.eatzy.presentation.theme.PoppinsMedium
import com.softcross.eatzy.presentation.theme.PrimaryBlack
import com.softcross.eatzy.presentation.theme.PrimaryButtonColor
import com.softcross.eatzy.presentation.theme.PrimaryContainerColor
import com.softcross.eatzy.presentation.theme.PrimaryOrange
import com.softcross.eatzy.presentation.theme.PrimaryTextFieldColor
import com.softcross.eatzy.presentation.theme.PrimaryWhite
import com.softcross.eatzy.presentation.theme.PrimaryYellow
import com.softcross.eatzy.presentation.theme.TextColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun MainRoute(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    navigateToIntroduce: () -> Unit,
    navigateToDetail: (Food) -> Unit,
    navigateToCart: () -> Unit,
    navigateToAddLocation: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var showBottomSheet by remember { mutableStateOf(false) }
    var showQuitDialog by remember { mutableStateOf(false) }
    LaunchedEffect(uiEffect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEffect.collect { effect ->
                when (effect) {
                    is UiEffect.NavigateToIntroduceScreen -> {
                        navigateToIntroduce()
                    }

                    is UiEffect.ShowLocationBottomSheet -> {
                        showBottomSheet = true
                    }

                    is UiEffect.ShowQuitDialog -> {
                        showQuitDialog = true
                    }
                }
            }
        }
    }
    if (showQuitDialog) {
        NonIgnorableTwoButtonDialog(
            title = stringResource(R.string.quit),
            message = stringResource(R.string.are_you_sure_you_want_to_quit),
            buttonText = stringResource(id = R.string.yes),
            onSubmitClick = { onAction(UiAction.OnSignOutClick) },
            onCancelClick = { showQuitDialog = false }
        )
    }
    if (uiState.locationList.isEmpty() && uiState.isLoading.not() && uiState.currentLocation == null) {
        NonIgnorableDialog(
            title = stringResource(R.string.please_add_an_address),
            message = stringResource(R.string.you_don_t_have_any_address_please_add_an_address_to_continue),
            buttonText = stringResource(R.string.add),
            onButtonClick = { navigateToAddLocation() }
        )
    } else {
        if (showBottomSheet) {
            ShowCustomLocationSheet(
                onDismiss = { showBottomSheet = false },
                locationList = uiState.locationList,
                onLocationClick = { onAction(UiAction.OnLocationItemClick(it)) },
                onNewLocationClick = { navigateToAddLocation() }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .background(BackgroundColor)
        ) {
            if (uiState.isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackgroundColor),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CustomLottieAnimation(modifier = Modifier.size(220.dp))
                }
            } else if (uiState.allFoodList.isEmpty() && uiState.errorMessage.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackgroundColor),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(Modifier.fillMaxWidth(0.9f)) {
                        CustomLottieAnimation(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(220.dp),
                            rawRes = R.raw.error,
                            iterations = 1
                        )
                        Text(
                            text = stringResource(R.string.an_error_on_fetching_food_list),
                            color = TextColor,
                            fontFamily = PoppinsMedium,
                            modifier = Modifier
                                .padding(top = 190.dp)
                                .align(Alignment.Center)
                        )
                    }
                    FilledButton(
                        text = stringResource(id = R.string.retry),
                        color = PrimaryButtonColor,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(top = 16.dp),
                        onClick = { onAction(UiAction.OnRetryClick) })
                }
            } else {
                uiState.currentLocation?.let { location ->
                    MainScreen(
                        foodList = uiState.foodList,
                        searchKey = uiState.searchKey,
                        cartPrice = uiState.cartPrice,
                        currentLocation = location,
                        onQuitClick = { onAction(UiAction.OnQuitClick) },
                        onSearchChanged = { onAction(UiAction.SearchChanged(it)) },
                        onFoodClick = { navigateToDetail(it) },
                        onFavoriteClick = { onAction(UiAction.OnFavoriteClick(it)) },
                        onNavigateToCart = { navigateToCart() },
                        onLocationClick = { onAction(UiAction.OnLocationClick) },
                        onAddItemClick = { onAction(UiAction.OnFoodAddClick(it)) }
                    )
                }
            }
            if (uiState.errorMessage.isNotEmpty()) {
                CustomSnackbar(
                    type = SnackbarType.ERROR,
                    message = uiState.errorMessage,
                    modifier = Modifier
                        .align(alignment = Alignment.BottomCenter)
                )
            }
            if (uiState.successMessage.isNotEmpty()) {
                CustomSnackbar(
                    type = SnackbarType.SUCCESS,
                    message = uiState.successMessage,
                    modifier = Modifier
                        .align(alignment = Alignment.BottomCenter)
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    foodList: List<Food>,
    cartPrice: Int,
    searchKey: String,
    currentLocation: Location,
    onQuitClick: () -> Unit,
    onSearchChanged: (String) -> Unit,
    onFoodClick: (Food) -> Unit,
    onFavoriteClick: (Int) -> Unit,
    onNavigateToCart: () -> Unit,
    onLocationClick: () -> Unit,
    onAddItemClick: (Food) -> Unit
) {
    val state = rememberLazyListState()

    LaunchedEffect(searchKey) {
        state.animateScrollToItem(0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .background(PrimaryOrange)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_quit),
                    tint = PrimaryWhite,
                    contentDescription = "",
                    modifier = Modifier
                        .clickable(onClick = { onQuitClick() })
                        .align(Alignment.CenterStart)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clickable { onLocationClick() }
                ) {
                    Text(
                        text = currentLocation.title,
                        color = PrimaryWhite,
                        fontFamily = PoppinsMedium
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.icon_down_arrow),
                        tint = PrimaryWhite,
                        contentDescription = "",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    if (cartPrice > 0) {
                        Text(
                            text = stringResource(R.string.price, cartPrice),
                            color = PrimaryBlack,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .shadow(4.dp, shape = RoundedCornerShape(16.dp))
                                .clip(shape = RoundedCornerShape(16.dp))
                                .background(PrimaryTextFieldColor)
                                .padding(horizontal = 6.dp)
                        )
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.icon_cart),
                        tint = PrimaryWhite,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable(onClick = onNavigateToCart)
                    )
                }
            }
            SearchTextField(
                givenValue = searchKey,
                placeHolder = stringResource(R.string.search),
                onValueChange = onSearchChanged,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(0.9f),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_search),
                    contentDescription = "",
                    tint = PrimaryBlack,
                    modifier = Modifier
                        .padding(start = 12.dp, top = 4.dp, bottom = 4.dp)
                        .size(18.dp)
                )
            }
        }
        if (foodList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight()
                    .padding(top = 16.dp)
            ) {
                CustomLottieAnimation(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(220.dp),
                    rawRes = R.raw.empty,
                    iterations = 1
                )
                Text(
                    text = stringResource(R.string.no_food_found),
                    color = TextColor,
                    fontFamily = PoppinsMedium,
                    modifier = Modifier
                        .padding(top = 190.dp)
                        .align(Alignment.Center)
                )
            }
        } else {
            Text(
                text = stringResource(R.string.best_products_product, foodList.size),
                color = TextColor,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 16.dp, start = 24.dp)
            )
            LazyColumn(
                state = state,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 16.dp)
            ) {
                items(foodList.size, key = { foodList[it].id }) {
                    val currentFood = foodList[it]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .padding(bottom = 16.dp)
                            .shadow(
                                2.dp,
                                shape = RoundedCornerShape(16.dp),
                                spotColor = PrimaryOrange
                            )
                            .clip(shape = RoundedCornerShape(16.dp))
                            .background(PrimaryContainerColor)
                            .clickable(onClick = { onFoodClick(currentFood) }),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(0.6f)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.icon_star),
                                        contentDescription = "",
                                        tint = PrimaryYellow,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = currentFood.rate.toString(),
                                        color = TextColor,
                                        fontFamily = PoppinsLight,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )
                                }
                                Text(
                                    text = currentFood.name,
                                    color = TextColor,
                                    fontSize = 18.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontFamily = PoppinsMedium
                                )
                                Text(
                                    text = stringResource(R.string.free_delivery),
                                    fontSize = 12.sp,
                                    color = TextColor,
                                    fontFamily = PoppinsLight,
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.icon_add),
                                        contentDescription = "",
                                        tint = PrimaryOrange,
                                        modifier = Modifier
                                            .padding(end = 8.dp)
                                            .size(18.dp)
                                            .clickable { onAddItemClick(currentFood) }
                                    )
                                    Text(
                                        text = stringResource(R.string.price, currentFood.price),
                                        color = PrimaryOrange,
                                        fontSize = 14.sp,
                                    )
                                }

                            }

                            val icon = if (currentFood.isFavorite) {
                                R.drawable.icon_fav_filled
                            } else {
                                R.drawable.icon_fav_outlined
                            }
                            Icon(
                                painter = painterResource(id = icon),
                                contentDescription = "",
                                tint = PrimaryOrange,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .padding(top = 4.dp)
                                    .size(24.dp)
                                    .align(Alignment.TopEnd)
                                    .clip(shape = RoundedCornerShape(16.dp))
                                    .background(PrimaryContainerColor)
                                    .clickable(onClick = { onFavoriteClick(currentFood.id) })
                            )
                        }
                        CustomAsyncImage(
                            model = currentFood.image,
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(horizontal = 8.dp, vertical = 8.dp)
                        )
                    }

                }

            }
        }
    }
}


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewMainRouteDark() {
    EatzyTheme {
        var foodList = listOf(
            Food(1, "Izgara Tavuk", "izgaratavuk.png", 20.0, false, 4.2),
            Food(2, "Burger", "20.0", 20.0, false, 4.8),
            Food(3, "Ayran", "20.0", 20.0, true, 4.9),
            Food(4, "Kola", "20.0", 20.0, false, 3.5)
        )
        MainRoute(
            uiState = UiState(
                isLoading = false,
                foodList = foodList,
                allFoodList = foodList,
            ),
            uiEffect = emptyFlow(),
            onAction = {}, navigateToIntroduce = {}, navigateToDetail = {}, navigateToCart = {}, {})
    }
}


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun PreviewMainRouteLight() {
    EatzyTheme {
        var foodList = listOf(
            Food(1, "Izgara Tavuk", "izgaratavuk.png", 20.0, false, 4.2),
            Food(2, "Burger", "20.0", 20.0, false, 4.8),
            Food(3, "Ayran", "20.0", 20.0, true, 4.9),
            Food(4, "Kola", "20.0", 20.0, false, 3.5)
        )
        MainRoute(
            uiState = UiState(
                isLoading = false,
                foodList = foodList,
                allFoodList = foodList,
                currentLocation = Location(
                    id = "voluptatum",
                    city = "Agatha",
                    district = "phasellus",
                    country = "Kenya",
                    openAddress = "vehicula",
                    title = "dicunt"
                )
            ),
            uiEffect = emptyFlow(),
            onAction = {}, {}, {}, navigateToCart = {}, {})
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun PreviewMainRouteError() {
    EatzyTheme {
        var foodList = listOf(
            Food(1, "Izgara Tavuk", "izgaratavuk.png", 20.0, false, 4.2),
            Food(2, "Burger", "20.0", 20.0, false, 4.8),
            Food(3, "Ayran", "20.0", 20.0, true, 4.9),
            Food(4, "Kola", "20.0", 20.0, false, 3.5)
        )
        MainRoute(
            uiState = UiState(isLoading = false, foodList = foodList, errorMessage = "Error"),
            uiEffect = emptyFlow(),
            onAction = {}, {}, {}, {}, {})
    }
}