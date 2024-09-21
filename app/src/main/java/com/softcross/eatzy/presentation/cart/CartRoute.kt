package com.softcross.eatzy.presentation.cart


import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.softcross.eatzy.R
import com.softcross.eatzy.domain.model.CartItem
import com.softcross.eatzy.presentation.components.CustomAsyncImage
import com.softcross.eatzy.presentation.components.CustomLottieAnimation
import com.softcross.eatzy.presentation.components.CustomSnackbar
import com.softcross.eatzy.presentation.components.FilledButton
import com.softcross.eatzy.presentation.components.IconTextField
import com.softcross.eatzy.presentation.components.NonIgnorableTwoButtonDialog
import com.softcross.eatzy.presentation.components.ShowCustomPaymentSheet
import com.softcross.eatzy.presentation.components.SnackbarType
import com.softcross.eatzy.presentation.theme.BackgroundColor
import com.softcross.eatzy.presentation.theme.EatzyTheme
import com.softcross.eatzy.presentation.theme.PoppinsLight
import com.softcross.eatzy.presentation.theme.PoppinsMedium
import com.softcross.eatzy.presentation.theme.PrimaryBlack
import com.softcross.eatzy.presentation.theme.PrimaryContainerColor
import com.softcross.eatzy.presentation.theme.PrimaryOrange
import com.softcross.eatzy.presentation.theme.PrimaryRed
import com.softcross.eatzy.presentation.theme.PrimaryTextFieldColor
import com.softcross.eatzy.presentation.theme.PrimaryWhite
import com.softcross.eatzy.presentation.theme.TextColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun CartRoute(
    uiState: CartContract.UiState,
    uiEffect: Flow<CartContract.UiEffect>,
    onAction: (CartContract.UiAction) -> Unit,
    navigateToBack: () -> Unit,
    navigateToNewPayment: () -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var showDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    LaunchedEffect(uiEffect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEffect.collect { effect ->
                when (effect) {
                    is CartContract.UiEffect.ShowSureDeleteDialog -> {
                        showDialog = true
                    }

                    is CartContract.UiEffect.ShowPaymentsDialog -> {
                        showPaymentDialog = true
                    }
                }
            }
        }
    }
    if (showPaymentDialog) {
        ShowCustomPaymentSheet(
            onDismiss = { showPaymentDialog = false },
            paymentList = uiState.payments,
            onPaymentClick = {
                onAction(CartContract.UiAction.ClearCart); showSuccessDialog = true
            },
            onNewPaymentClick = { navigateToNewPayment() }
        )
    }
    if (showSuccessDialog) {
        NonIgnorableTwoButtonDialog(
            title = stringResource(R.string.success),
            message = stringResource(R.string.your_order_has_taken_successfully),
            buttonText = stringResource(R.string.ok),
            onSubmitClick = { navigateToBack() },
            onCancelClick = { showSuccessDialog = false }
        )
    }
    if (showDialog) {
        NonIgnorableTwoButtonDialog(
            title = stringResource(R.string.delete_all_foods),
            message = stringResource(R.string.are_you_sure_you_want_to_delete_all_cart),
            buttonText = stringResource(R.string.yes),
            onSubmitClick = { onAction(CartContract.UiAction.ClearCart) },
            onCancelClick = { showDialog = false }
        )
    }
    BackHandler {
        navigateToBack()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
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
                    .clickable(onClick = { navigateToBack() })
            )
            Text(
                text = stringResource(R.string.cart),
                color = TextColor,
                fontSize = 16.sp,
                fontFamily = PoppinsMedium
            )
            Icon(
                painter = painterResource(id = R.drawable.icon_trash),
                contentDescription = "",
                tint = PrimaryOrange,
                modifier = Modifier
                    .padding(8.dp)
                    .padding(top = 4.dp, end = 8.dp)
                    .size(32.dp)
                    .shadow(4.dp, shape = RoundedCornerShape(32.dp), spotColor = PrimaryOrange)
                    .clip(shape = RoundedCornerShape(32.dp))
                    .background(PrimaryContainerColor)
                    .padding(6.dp)
                    .clickable(onClick = { onAction(CartContract.UiAction.OnClearClick) })
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
            } else {
                CartRouteScreen(
                    promotionCode = uiState.promotionCode,
                    cartList = uiState.cartFoods,
                    totalPrice = uiState.cartPrice,
                    isCartLoading = uiState.isCartLoading,
                    onPromotionCodeChanged = {
                        onAction(
                            CartContract.UiAction.OnPromotionCodeChanged(
                                it
                            )
                        )
                    },
                    onIncrease = { onAction(CartContract.UiAction.AddFood(it)) },
                    onDecrease = { onAction(CartContract.UiAction.RemoveFood(it)) },
                    onCheckOut = { onAction(CartContract.UiAction.OnClickPayments) },
                    onPromotionSubmit = { onAction(CartContract.UiAction.OnPromotionCodeSubmit) }
                )
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
}

@Composable
fun CartRouteScreen(
    promotionCode: String,
    cartList: List<CartItem>,
    totalPrice: Int,
    isCartLoading: Boolean,
    onPromotionCodeChanged: (String) -> Unit,
    onIncrease: (CartItem) -> Unit,
    onDecrease: (CartItem) -> Unit,
    onCheckOut: () -> Unit,
    onPromotionSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (cartList.isNotEmpty()) {
            if (isCartLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.55f)
                        .padding(top = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box {
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape)
                                .background(PrimaryContainerColor)
                                .align(Alignment.Center)
                        )
                        CircularProgressIndicator(
                            color = PrimaryOrange,
                            modifier = Modifier
                                .align(Alignment.Center),
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.55f)
                        .padding(top = 8.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    items(cartList.size, key = { cartList[it].id }) { index ->
                        val currentItem = cartList[index]
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
                                .background(PrimaryContainerColor),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(0.7f),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CustomAsyncImage(
                                    model = currentItem.image,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .fillMaxWidth(0.45f)
                                        .fillMaxHeight()
                                        .padding(horizontal = 8.dp, vertical = 8.dp)
                                )
                                Box(
                                    Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(vertical = 8.dp),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = currentItem.name,
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
                                            text = stringResource(id = R.string.price, currentItem.price),
                                            color = PrimaryOrange,
                                            fontSize = 14.sp,
                                        )
                                    }
                                }
                            }
                            Box(
                                Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.Center),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.icon_subtraction_outlined),
                                        contentDescription = "",
                                        tint = PrimaryWhite,
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(PrimaryOrange)
                                            .padding(6.dp)
                                            .size(16.dp)
                                            .clickable(onClick = {
                                                onDecrease(currentItem)
                                            })
                                    )
                                    Text(
                                        text = currentItem.count.toString(),
                                        color = TextColor,
                                        fontSize = 18.sp,
                                        fontFamily = PoppinsMedium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .width(24.dp)
                                    )
                                    Icon(
                                        painter = painterResource(id = R.drawable.icon_add_outlined),
                                        contentDescription = "",
                                        tint = PrimaryWhite,
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(PrimaryOrange)
                                            .padding(6.dp)
                                            .size(16.dp)
                                            .clickable(onClick = {
                                                onIncrease(currentItem)
                                            })
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Box(
                Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.55f)
            ) {
                CustomLottieAnimation(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(220.dp),
                    rawRes = R.raw.empty,
                    iterations = 1
                )
                Text(
                    text = stringResource(R.string.no_items_in_cart),
                    color = TextColor,
                    fontFamily = PoppinsMedium,
                    modifier = Modifier
                        .padding(top = 190.dp)
                        .align(Alignment.Center)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier
                    .fillMaxWidth(0.8f)
            ) {
                IconTextField(
                    givenValue = promotionCode.uppercase(),
                    placeHolder = stringResource(R.string.promotion_code),
                    onValueChange = { onPromotionCodeChanged(it.uppercase()) },
                    modifier = Modifier
                        .padding(vertical = 16.dp),
                    keyboardType = KeyboardType.Text,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_campaings),
                            contentDescription = "",
                            tint = PrimaryBlack
                        )
                    },
                    regex = String::isNotEmpty
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "",
                            modifier = Modifier.size(12.dp),
                            tint = PrimaryRed
                        )
                        Text(
                            text = stringResource(
                                R.string.str_validation,
                                stringResource(id = R.string.promotion_code).lowercase()
                            ),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 4.dp),
                            color = TextColor
                        )
                    }
                }

            }
            Icon(
                painter = painterResource(id = R.drawable.icon_add),
                contentDescription = "",
                tint = PrimaryBlack,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(45.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PrimaryTextFieldColor)
                    .padding(8.dp)
                    .clickable { onPromotionSubmit() },
            )
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .shadow(
                    4.dp,
                    shape = RoundedCornerShape(topEnd = 24.dp, topStart = 24.dp),
                    spotColor = PrimaryOrange
                )
                .clip(RoundedCornerShape(topEnd = 24.dp, topStart = 24.dp))
                .background(PrimaryContainerColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.subtotal),
                    color = TextColor,
                )
                Text(
                    text = stringResource(id = R.string.price, totalPrice),
                    color = PrimaryOrange
                )
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(0.9f),
                color = PrimaryOrange.copy(0.7f),
                thickness = 0.2.dp
            )
            Row(
                Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.delivery),
                    color = TextColor,
                )
                Text(
                    text = stringResource(R.string.free),
                    color = PrimaryOrange
                )
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(0.9f),
                color = PrimaryOrange.copy(0.7f),
                thickness = 0.2.dp
            )
            Row(
                Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.total),
                    color = TextColor,
                    fontFamily = PoppinsMedium
                )
                Text(
                    text = stringResource(id = R.string.price, totalPrice),
                    color = PrimaryOrange,
                    fontFamily = PoppinsMedium
                )
            }
            FilledButton(
                text = stringResource(R.string.check_out),
                modifier = Modifier.padding(vertical = 8.dp),
                onClick = onCheckOut,
                isEnabled = cartList.isNotEmpty()
            )
        }
    }
}


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewCartRouteDark() {
    EatzyTheme {
        CartRoute(
            uiState = CartContract.UiState(
                isLoading = false,
                isCartLoading = true,
                cartFoods = listOf(
                    CartItem(
                        id = 1,
                        name = "Burger",
                        price = 10.0,
                        image = "https://via.placeholder.com/150",
                        count = 1
                    )
                ),
            ),
            uiEffect = emptyFlow(),
            onAction = {},
            {},
            {}
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun PreviewCartRouteLight() {
    EatzyTheme {
        CartRoute(
            uiState = CartContract.UiState(
                errorMessage = "ASDGF<hd",
                isLoading = false,
                isCartLoading = false,
                cartFoods = listOf(
                    CartItem(
                        id = 1,
                        name = "Burger",
                        price = 10.0,
                        image = "https://via.placeholder.com/150",
                        count = 1
                    ), CartItem(
                        id = 2,
                        name = "Burger",
                        price = 10.0,
                        image = "https://via.placeholder.com/150",
                        count = 1
                    ), CartItem(
                        id = 3,
                        name = "Burger",
                        price = 10.0,
                        image = "https://via.placeholder.com/150",
                        count = 1
                    ), CartItem(
                        id = 4,
                        name = "Burger",
                        price = 10.0,
                        image = "https://via.placeholder.com/150",
                        count = 1
                    )
                )
            ),
            uiEffect = emptyFlow(),
            onAction = {},
            {},
            {}
        )
    }
}