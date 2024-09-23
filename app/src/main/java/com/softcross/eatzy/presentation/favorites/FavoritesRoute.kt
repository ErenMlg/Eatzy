package com.softcross.eatzy.presentation.favorites


import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.BackHandler
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
import com.softcross.eatzy.presentation.components.CustomAsyncImage
import com.softcross.eatzy.presentation.components.CustomLottieAnimation
import com.softcross.eatzy.presentation.components.FilledButton
import com.softcross.eatzy.presentation.components.NonIgnorableTwoButtonDialog
import com.softcross.eatzy.presentation.main.MainContract.UiAction
import com.softcross.eatzy.presentation.theme.BackgroundColor
import com.softcross.eatzy.presentation.theme.EatzyTheme
import com.softcross.eatzy.presentation.theme.PoppinsLight
import com.softcross.eatzy.presentation.theme.PoppinsMedium
import com.softcross.eatzy.presentation.theme.PrimaryButtonColor
import com.softcross.eatzy.presentation.theme.PrimaryContainerColor
import com.softcross.eatzy.presentation.theme.PrimaryOrange
import com.softcross.eatzy.presentation.theme.PrimaryYellow
import com.softcross.eatzy.presentation.theme.TextColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun FavoritesRoute(
    uiState: FavoritesContract.UiState,
    uiEffect: Flow<FavoritesContract.UiEffect>,
    onAction: (FavoritesContract.UiAction) -> Unit,
    navigateToDetail: (Food) -> Unit,
    navigateToMain: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var showDialog by remember { mutableStateOf(false) }
    LaunchedEffect(uiEffect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEffect.collect { effect ->
                when (effect) {
                    is FavoritesContract.UiEffect.ShowClearFavoritesDialog -> {
                        showDialog = true
                    }
                }
            }
        }
    }

    if (showDialog) {
        NonIgnorableTwoButtonDialog(
            title = stringResource(R.string.clear_favorites),
            message = stringResource(R.string.are_you_sure_you_want_to_clear_all_favorites),
            buttonText = stringResource(id = R.string.yes),
            onSubmitClick = { onAction(FavoritesContract.UiAction.OnClearFavorites) },
            onCancelClick = { showDialog = false }
        )
    }

    BackHandler {
        navigateToMain()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(BackgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryContainerColor),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_arrow_left),
                contentDescription = "",
                tint = PrimaryOrange,
                modifier = Modifier
                    .padding(8.dp)
                    .padding(top = 4.dp, start = 8.dp)
                    .size(32.dp)
                    .shadow(4.dp, shape = RoundedCornerShape(32.dp), spotColor = PrimaryOrange)
                    .clip(shape = RoundedCornerShape(32.dp))
                    .background(PrimaryContainerColor)
                    .padding(6.dp)
                    .clickable(onClick = { navigateToMain() })
            )
            Text(
                text = stringResource(R.string.favorites),
                color = TextColor,
                fontSize = 16.sp,
                fontFamily = PoppinsMedium
            )
            Icon(
                painter = painterResource(id = R.drawable.icon_fav_filled),
                contentDescription = "",
                tint = if (uiState.favorites.isNotEmpty()) PrimaryOrange else BackgroundColor,
                modifier = Modifier
                    .padding(8.dp)
                    .padding(top = 4.dp, end = 8.dp)
                    .size(32.dp)
                    .shadow(4.dp, shape = RoundedCornerShape(32.dp), spotColor = PrimaryOrange)
                    .clip(shape = RoundedCornerShape(32.dp))
                    .background(PrimaryContainerColor)
                    .padding(6.dp)
                    .clickable(onClick = {
                        if (uiState.favorites.isNotEmpty()) onAction(FavoritesContract.UiAction.OnClearClicked)
                    })
            )
        }
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
        } else if (uiState.errorMessage.isNotEmpty()) {
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
                        text = stringResource(R.string.an_error_on_fetching_favorite_list),
                        color = TextColor,
                        fontFamily = PoppinsMedium,
                        modifier = Modifier
                            .padding(top = 190.dp)
                            .align(Alignment.Center)
                    )
                }
                FilledButton(
                    text = stringResource(R.string.retry),
                    color = PrimaryButtonColor,
                    onClick = { onAction(FavoritesContract.UiAction.OnRetryClicked) },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(top = 16.dp)
                )
            }
        } else {
            FavoritesScreen(
                favoritesList = uiState.favorites,
                onFavoriteClick = { onAction(FavoritesContract.UiAction.OnFavoriteClicked(it)) },
                onFavoriteItemClick = { navigateToDetail(it) },
            )
        }
    }
}

@Composable
fun FavoritesScreen(
    favoritesList: List<Food>,
    onFavoriteClick: (Food) -> Unit,
    onFavoriteItemClick: (Food) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (favoritesList.isEmpty()) {
            Box(
                Modifier.fillMaxSize()
            ) {
                CustomLottieAnimation(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(220.dp),
                    rawRes = R.raw.empty,
                    iterations = 1
                )
                Text(
                    text = stringResource(R.string.no_favorites_yet),
                    color = TextColor,
                    fontFamily = PoppinsMedium,
                    modifier = Modifier
                        .padding(top = 190.dp)
                        .align(Alignment.Center)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
            ) {
                items(favoritesList.size, key = { favoritesList[it].id }) { index ->
                    val currentFood = favoritesList[index]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .padding(bottom = 16.dp, top = if (index == 0) 16.dp else 0.dp)
                            .shadow(
                                2.dp,
                                shape = RoundedCornerShape(16.dp),
                                spotColor = PrimaryOrange
                            )
                            .clip(shape = RoundedCornerShape(16.dp))
                            .background(PrimaryContainerColor)
                            .clickable(onClick = { onFavoriteItemClick(currentFood) }),
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
                                    text = stringResource(id = R.string.free_delivery),
                                    fontSize = 12.sp,
                                    color = TextColor,
                                    fontFamily = PoppinsLight,
                                )
                                Text(
                                    text = stringResource(id = R.string.price, currentFood.price),
                                    color = PrimaryOrange,
                                    fontSize = 14.sp,
                                )

                            }
                            Icon(
                                painter = painterResource(id = R.drawable.icon_fav_filled),
                                contentDescription = "",
                                tint = PrimaryOrange,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .padding(top = 4.dp)
                                    .size(24.dp)
                                    .align(Alignment.TopEnd)
                                    .clip(shape = RoundedCornerShape(16.dp))
                                    .background(PrimaryContainerColor)
                                    .clickable(onClick = { onFavoriteClick(currentFood) })
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
fun PreviewFavoritesRouteDark() {
    EatzyTheme {
        FavoritesRoute(uiState = FavoritesContract.UiState(
            isLoading = false,
            favorites = listOf(
                Food(
                    id = 1,
                    name = "Hamburger",
                    rate = 4.5,
                    price = 15.0,
                    image = ""
                ),
                Food(
                    id = 2,
                    name = "Hamburger",
                    rate = 4.5,
                    price = 15.0,
                    image = ""
                ),
                Food(
                    id = 3,
                    name = "Hamburger",
                    rate = 4.5,
                    price = 15.0,
                    image = ""
                )
            ),
        ), emptyFlow(), onAction = {}, {}, {})
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun PreviewFavoritesRouteLight() {
    EatzyTheme {
        FavoritesRoute(uiState = FavoritesContract.UiState(
            isLoading = false,
            errorMessage = "EE",
            favorites = listOf(

            ),
        ), emptyFlow(), onAction = {}, {}, {})
    }
}