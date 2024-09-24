package com.softcross.eatzy.presentation.producDetail


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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
import com.softcross.eatzy.presentation.components.CustomSnackbar
import com.softcross.eatzy.presentation.components.FilledButton
import com.softcross.eatzy.presentation.components.SnackbarType
import com.softcross.eatzy.presentation.components.StarRatingBar
import com.softcross.eatzy.presentation.theme.BackgroundColor
import com.softcross.eatzy.presentation.theme.EatzyTheme
import com.softcross.eatzy.presentation.theme.PoppinsLight
import com.softcross.eatzy.presentation.theme.PoppinsMedium
import com.softcross.eatzy.presentation.theme.PrimaryButtonColor
import com.softcross.eatzy.presentation.theme.PrimaryContainerColor
import com.softcross.eatzy.presentation.theme.PrimaryOrange
import com.softcross.eatzy.presentation.theme.TextColor
import kotlinx.coroutines.flow.Flow

val detailCardTexts = listOf(
    "25-30 Dk",
    "Ücretsiz Teslimat",
    "Kredi Kartı",
    "Nakit",
    "Yemek Çeki",
    "Kapıda Ödeme",
    "%10 İndirim"
)

@Composable
fun ProductDetailRoute(
    uiState: ProductDetailContract.UiState,
    onAction: (ProductDetailContract.UiAction) -> Unit,
    uiEffect: Flow<ProductDetailContract.UiEffect>,
    navigateToMain: () -> Unit,
    navigateToCart: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(uiEffect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEffect.collect { effect ->
                when (effect) {
                    is ProductDetailContract.UiEffect.NavigateToCartScreen -> {
                        navigateToCart()
                    }
                }
            }
        }
    }

    BackHandler {
        navigateToMain()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(BackgroundColor)
    ) {
        if (uiState.food != null) {
            ProductDetailRouteScreen(
                food = uiState.food,
                amount = uiState.amount,
                onIncrease = { onAction(ProductDetailContract.UiAction.IncreaseAmount) },
                onDecrease = { onAction(ProductDetailContract.UiAction.DecreaseAmount) },
                onFavoriteClick = { onAction(ProductDetailContract.UiAction.OnFavoriteClick(uiState.food.id)) },
                onBackPress = navigateToMain,
                onClickOrder = { onAction(ProductDetailContract.UiAction.AddToCart) }
            )
        } else {
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
                        text = stringResource(R.string.an_error_fetching_the_food),
                        color = TextColor,
                        fontFamily = PoppinsMedium,
                        modifier = Modifier
                            .padding(top = 190.dp)
                            .align(Alignment.Center)
                    )
                }
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
    }
}

@Composable
fun ProductDetailRouteScreen(
    food: Food,
    amount: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onFavoriteClick: () -> Unit,
    onBackPress: () -> Unit,
    onClickOrder: () -> Unit
) {
    val icon = if (food.isFavorite) {
        R.drawable.icon_fav_filled
    } else {
        R.drawable.icon_fav_outlined
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        CustomAsyncImage(
            model = food.image,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(vertical = 32.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.65f)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryContainerColor)
        ) {
            StarRatingBar(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.TopCenter),
                rating = food.rate.toFloat(),
                spaceBetween = 8.dp
            )
            Icon(
                painter = painterResource(id = R.drawable.icon_arrow_left),
                contentDescription = "",
                tint = PrimaryOrange,
                modifier = Modifier
                    .padding(8.dp)
                    .padding(top = 4.dp, start = 8.dp)
                    .size(32.dp)
                    .align(Alignment.TopStart)
                    .shadow(4.dp, shape = RoundedCornerShape(32.dp), spotColor = PrimaryOrange)
                    .clip(shape = RoundedCornerShape(32.dp))
                    .background(PrimaryContainerColor)
                    .padding(6.dp)
                    .clickable(onClick = onBackPress)
            )
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "",
                tint = PrimaryOrange,
                modifier = Modifier
                    .padding(8.dp)
                    .padding(top = 4.dp, end = 8.dp)
                    .size(32.dp)
                    .align(Alignment.TopEnd)
                    .shadow(4.dp, shape = RoundedCornerShape(32.dp), spotColor = PrimaryOrange)
                    .clip(shape = RoundedCornerShape(32.dp))
                    .background(PrimaryContainerColor)
                    .padding(6.dp)
                    .clickable(onClick = onFavoriteClick)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .shadow(
                    4.dp,
                    shape = RoundedCornerShape(topEnd = 24.dp, topStart = 24.dp),
                    spotColor = PrimaryOrange
                )
                .clip(RoundedCornerShape(topEnd = 24.dp, topStart = 24.dp))
                .background(PrimaryContainerColor)
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = food.name,
                        color = TextColor,
                        fontSize = 24.sp,
                        fontFamily = PoppinsMedium,
                    )
                    Text(
                        text = stringResource(id = R.string.price, food.price),
                        color = PrimaryOrange,
                        fontSize = 24.sp,
                        fontFamily = PoppinsMedium,
                    )
                }
                Text(
                    text = stringResource(R.string.description),
                    color = TextColor,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 8.dp)
                )
                Text(
                    text = stringResource(R.string.our_chefs_use_local_and_seasonal_produce_to_preserve_the_naturalness_and_freshness_of_the_dishes_turning_each_dish_into_a_work_of_art),
                    color = TextColor,
                    fontFamily = PoppinsLight,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(bottom = 8.dp)
                )
                LazyRow(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    items(detailCardTexts.size) {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(BackgroundColor)
                        ) {
                            Text(
                                text = detailCardTexts[it],
                                color = TextColor,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_subtraction),
                    contentDescription = "",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable(onClick = onDecrease),
                    tint = PrimaryButtonColor
                )
                Text(
                    text = amount.toString(),
                    fontFamily = PoppinsMedium,
                    color = TextColor,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .width(32.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.icon_add),
                    contentDescription = "",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable(onClick = onIncrease),
                    tint = PrimaryButtonColor
                )
            }
            FilledButton(
                text = stringResource(R.string.order),
                onClick = onClickOrder,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewProductDetailRouteDark() {
    EatzyTheme {
        ProductDetailRouteScreen(
            food = Food(
                id = 1,
                name = "Test Food",
                price = 10.0,
                rate = 4.5,
                image = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                isFavorite = true
            ),
            0,
            {},
            {},
            {},
            {},
            {}
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun PreviewProductDetailRouteLight() {
    EatzyTheme {
        ProductDetailRouteScreen(
            food = Food(
                id = 1,
                name = "Test Food",
                price = 10.0,
                rate = 4.5,
                image = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                isFavorite = true
            ),
            0,
            {},
            {},
            {},
            {},
            {}
        )
    }
}