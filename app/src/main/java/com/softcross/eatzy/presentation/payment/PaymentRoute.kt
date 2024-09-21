package com.softcross.eatzy.presentation.payment


import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.softcross.eatzy.R
import com.softcross.eatzy.common.CreditCardDateVisualTransformation
import com.softcross.eatzy.common.extension.creditCardDateRegex
import com.softcross.eatzy.common.extension.creditCardNumberRegex
import com.softcross.eatzy.common.extension.cvcRegex
import com.softcross.eatzy.common.extension.nameSurnameRegexWithSpace
import com.softcross.eatzy.presentation.address.AddressContract
import com.softcross.eatzy.presentation.components.FilledButton
import com.softcross.eatzy.presentation.components.IconTextField
import com.softcross.eatzy.presentation.theme.BackgroundColor
import com.softcross.eatzy.presentation.theme.EatzyTheme
import com.softcross.eatzy.presentation.theme.PoppinsMedium
import com.softcross.eatzy.presentation.theme.PrimaryBlack
import com.softcross.eatzy.presentation.theme.PrimaryContainerColor
import com.softcross.eatzy.presentation.theme.PrimaryOrange
import com.softcross.eatzy.presentation.theme.PrimaryRed
import com.softcross.eatzy.presentation.theme.TextColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun PaymentRoute(
    uiState: PaymentContract.UiState,
    uiEffect: Flow<PaymentContract.UiEffect>,
    onAction: (PaymentContract.UiAction) -> Unit,
    onNavigateProfile: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(uiEffect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEffect.collect { effect ->
                when (effect) {
                    is PaymentContract.UiEffect.OnNavigateProfile -> onNavigateProfile()
                }
            }
        }
    }
    BackHandler {
        onNavigateProfile()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
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
                    .clickable { onNavigateProfile() }
            )
            Text(
                text = stringResource(R.string.new_payment),
                color = TextColor,
                fontSize = 16.sp,
                fontFamily = PoppinsMedium,
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
                    .clickable(onClick = { onAction(PaymentContract.UiAction.OnClearFields) })
            )
        }
        PaymentScreen(
            title = uiState.title,
            cartNo = uiState.cartNo,
            cardHolder = uiState.cardHolder,
            expiryDate = uiState.expiryDate,
            cvv = uiState.cvv,
            onTitleChanged = { onAction(PaymentContract.UiAction.OnTitleChanged(it)) },
            onCartNoChanged = { onAction(PaymentContract.UiAction.OnCartNoChanged(it)) },
            onCardHolderChanged = { onAction(PaymentContract.UiAction.OnCardHolderChanged(it)) },
            onExpiryDateChanged = { onAction(PaymentContract.UiAction.OnExpiryDateChanged(it)) },
            onCvvChanged = { onAction(PaymentContract.UiAction.OnCvvChanged(it)) },
            onSubmitClicked = { onAction(PaymentContract.UiAction.OnSubmitClicked) }
        )
    }
}

@Composable
fun PaymentScreen(
    title: String = "",
    cartNo: String = "",
    cardHolder: String = "",
    expiryDate: String = "",
    cvv: String = "",
    onTitleChanged: (String) -> Unit,
    onCartNoChanged: (String) -> Unit,
    onCardHolderChanged: (String) -> Unit,
    onExpiryDateChanged: (String) -> Unit,
    onCvvChanged: (String) -> Unit,
    onSubmitClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight()
            .background(BackgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconTextField(
            givenValue = title,
            placeHolder = stringResource(id = R.string.title),
            onValueChange = { onTitleChanged(it) },
            modifier = Modifier.padding(top = 16.dp),
            keyboardType = KeyboardType.Text,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_payment),
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
                    text = stringResource(R.string.str_validation, stringResource(id = R.string.title).lowercase()),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    color = TextColor
                )
            }
        }
        IconTextField(
            givenValue = cartNo,
            placeHolder = stringResource(R.string.cart_number),
            onValueChange = { if (it.length <= 16) onCartNoChanged(it) },
            modifier = Modifier.padding(top = 16.dp),
            keyboardType = KeyboardType.Number,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_text),
                    contentDescription = "",
                    tint = PrimaryBlack
                )
            },
            regex = String::creditCardNumberRegex
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
                    text = stringResource(R.string.str_validation, stringResource(R.string.cart_number).lowercase()),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    color = TextColor
                )
            }
        }
        IconTextField(
            givenValue = cardHolder,
            placeHolder = stringResource(R.string.cart_holder),
            onValueChange = { onCardHolderChanged(it) },
            modifier = Modifier.padding(top = 16.dp),
            keyboardType = KeyboardType.Text,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_text),
                    contentDescription = "",
                    tint = PrimaryBlack
                )
            },
            regex = String::nameSurnameRegexWithSpace
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
                    text = stringResource(R.string.str_validation, stringResource(R.string.cart_holder).lowercase()),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    color = TextColor
                )
            }
        }
        IconTextField(
            givenValue = expiryDate,
            placeHolder = stringResource(R.string.cart_date),
            visualTransformation = CreditCardDateVisualTransformation(),
            onValueChange = { if (it.length <= 4) onExpiryDateChanged(it) },
            modifier = Modifier.padding(top = 16.dp),
            keyboardType = KeyboardType.Number,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_text),
                    contentDescription = "",
                    tint = PrimaryBlack
                )
            },
            regex = String::creditCardDateRegex
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
                    text = stringResource(R.string.str_validation, stringResource(R.string.cart_date).lowercase()),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    color = TextColor
                )
            }
        }
        IconTextField(
            givenValue = cvv,
            placeHolder = stringResource(R.string.cvv),
            onValueChange = { onCvvChanged(it) },
            modifier = Modifier.padding(top = 16.dp),
            keyboardType = KeyboardType.Number,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_text),
                    contentDescription = "",
                    tint = PrimaryBlack
                )
            },
            regex = String::cvcRegex
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
                    text = stringResource(R.string.str_validation, stringResource(R.string.cvv).lowercase()),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    color = TextColor
                )
            }
        }
        val keyboardController = LocalSoftwareKeyboardController.current
        FilledButton(
            text = stringResource(id = R.string.add_update),
            isEnabled = title.isNotEmpty() && cartNo.creditCardNumberRegex() && cardHolder.nameSurnameRegexWithSpace() && expiryDate.creditCardDateRegex() && cvv.cvcRegex(),
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                keyboardController?.hide()
                onSubmitClicked()
            }
        )
    }
}


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewPaymentRouteDark() {
    EatzyTheme {
        PaymentRoute(uiState = PaymentContract.UiState(
            isLoading = false,
            selectedPayment = null,
            title = "omnesque",
            cartNo = "idque",
            cardHolder = "lobortis",
            expiryDate = "decore",
            cvv = "tacimates"
        ), uiEffect = emptyFlow(), onAction = {},{}
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun PreviewPaymentRouteLight() {
    EatzyTheme {
        PaymentRoute(uiState = PaymentContract.UiState(
            isLoading = false,
            selectedPayment = null,
            title = "omnesque",
            cartNo = "idque",
            cardHolder = "lobortis",
            expiryDate = "decore",
            cvv = "tacimates"
        ), uiEffect = emptyFlow(), onAction = {},{}
        )
    }
}