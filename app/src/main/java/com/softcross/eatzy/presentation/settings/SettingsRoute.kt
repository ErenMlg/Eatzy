package com.softcross.eatzy.presentation.settings


import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.softcross.eatzy.R
import com.softcross.eatzy.common.EatzySingleton
import com.softcross.eatzy.common.extension.changeAppLanguage
import com.softcross.eatzy.domain.model.Location
import com.softcross.eatzy.domain.model.Payment
import com.softcross.eatzy.presentation.components.CustomLottieAnimation
import com.softcross.eatzy.presentation.components.NonIgnorableTwoButtonDialog
import com.softcross.eatzy.presentation.components.ShowCustomLocationSheet
import com.softcross.eatzy.presentation.components.ShowCustomPaymentSheet
import com.softcross.eatzy.presentation.main.MainContract.UiAction
import com.softcross.eatzy.presentation.theme.BackgroundColor
import com.softcross.eatzy.presentation.theme.EatzyTheme
import com.softcross.eatzy.presentation.theme.PoppinsMedium
import com.softcross.eatzy.presentation.theme.PrimaryContainerColor
import com.softcross.eatzy.presentation.theme.PrimaryOrange
import com.softcross.eatzy.presentation.theme.TextColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.util.Locale

enum class Language(val code: String) {
    ENGLISH("en"),
    TURKISH("tr")
}

@Composable
fun SettingsRoute(
    uiState: SettingsContract.UiState,
    uiEffect: Flow<SettingsContract.UiEffect>,
    onAction: (SettingsContract.UiAction) -> Unit,
    navigateToMain: () -> Unit,
    navigateToIntroduce: () -> Unit,
    navigateToLocationUpdate: (Location) -> Unit,
    navigateToNewLocation: () -> Unit,
    navigateToPaymentUpdate: (Payment) -> Unit,
    navigateToNewPayment: () -> Unit
) {
    BackHandler {
        navigateToMain()
    }
    val context = LocalContext.current
    val language = context.theme.resources.configuration.locales[0].language
    val lifecycleOwner = LocalLifecycleOwner.current
    var showLocationSheet by remember { mutableStateOf(false) }
    var showPaymentSheet by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showQuitDialog by remember { mutableStateOf(false) }
    LaunchedEffect(uiEffect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEffect.collect { effect ->
                when (effect) {
                    SettingsContract.UiEffect.ShowPaymentBottomSheet -> showPaymentSheet = true

                    SettingsContract.UiEffect.ShowLocationBottomSheet -> showLocationSheet = true

                    SettingsContract.UiEffect.NavigateToIntroduceScreen -> navigateToIntroduce()

                    SettingsContract.UiEffect.ShowLanguageDialog -> showDialog = true

                    SettingsContract.UiEffect.ShowQuitDialog -> showQuitDialog = true
                }
            }
        }
    }
    val otherLanguage = if (language == Language.ENGLISH.code) {
        Language.TURKISH
    } else {
        Language.ENGLISH
    }
    if (showDialog) {
        NonIgnorableTwoButtonDialog(
            title = stringResource(R.string.change_language),
            message = stringResource(
                R.string.are_you_sure_you_want_to_change_language_to,
                otherLanguage.name.lowercase()
            ),
            buttonText = stringResource(id = R.string.yes),
            onSubmitClick = { changeAppLanguage(context, otherLanguage.code) },
            onCancelClick = { showDialog = false }
        )
    }
    if (showQuitDialog) {
        NonIgnorableTwoButtonDialog(
            title = stringResource(id = R.string.quit),
            message = stringResource(id = R.string.are_you_sure_you_want_to_quit),
            buttonText = stringResource(id = R.string.yes),
            onSubmitClick = { onAction(SettingsContract.UiAction.OnQuitSubmitClick) },
            onCancelClick = { showQuitDialog = false }
        )
    }
    if (showPaymentSheet) {
        ShowCustomPaymentSheet(
            onDismiss = { showPaymentSheet = false },
            paymentList = uiState.payments,
            onPaymentClick = { navigateToPaymentUpdate(it) },
            onNewPaymentClick = { navigateToNewPayment() }
        )
    }
    if (showLocationSheet) {
        ShowCustomLocationSheet(
            onDismiss = { showLocationSheet = false },
            locationList = uiState.locations,
            onLocationClick = { navigateToLocationUpdate(it) },
            onNewLocationClick = { navigateToNewLocation() }
        )
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
                    .clickable { navigateToMain() }
            )
            Text(
                text = stringResource(R.string.settings),
                color = TextColor,
                fontSize = 16.sp,
                fontFamily = PoppinsMedium
            )
            val icon = if (language == Language.ENGLISH.code) {
                R.drawable.en
            } else {
                R.drawable.tr
            }
            Image(
                painter = painterResource(id = icon),
                contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
                    .padding(top = 4.dp, end = 8.dp)
                    .size(32.dp)
                    .shadow(4.dp, shape = RoundedCornerShape(32.dp), spotColor = PrimaryOrange)
                    .clip(shape = RoundedCornerShape(32.dp))
                    .background(PrimaryContainerColor)
                    .padding(6.dp)
                    .clickable {
                        onAction(SettingsContract.UiAction.OnChangeLanguageClick)
                    }
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
        } else {
            SettingsScreen(
                onClickAddresses = { onAction(SettingsContract.UiAction.OnClickLocations) },
                onClickPayments = { onAction(SettingsContract.UiAction.OnClickPayments) },
                onClickQuit = { onAction(SettingsContract.UiAction.OnQuitClick) }
            )
        }
    }
}

@Composable
fun SettingsScreen(
    onClickAddresses: () -> Unit,
    onClickPayments: () -> Unit,
    onClickQuit: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(0.9f)
            .shadow(4.dp, shape = CircleShape, spotColor = PrimaryOrange)
            .clip(CircleShape)
            .background(PrimaryContainerColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_avatar),
            contentDescription = "",
            tint = PrimaryOrange,
            modifier = Modifier
                .size(64.dp)
                .padding(16.dp)
        )
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = EatzySingleton.currentUser?.fullName ?: "User",
                color = TextColor,
                fontFamily = PoppinsMedium
            )
            Text(
                text = EatzySingleton.currentUser?.phone ?: "Phone Number",
                color = TextColor,
                fontSize = 12.sp,
                fontFamily = PoppinsMedium
            )
        }
    }
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(0.9f)
            .shadow(4.dp, shape = CircleShape, spotColor = PrimaryOrange)
            .clip(CircleShape)
            .background(PrimaryContainerColor)
            .clickable {
                Toast
                    .makeText(
                        context,
                        context.getString(R.string.you_dont_have_notification_yet),
                        Toast.LENGTH_SHORT
                    )
                    .show()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_notification),
            contentDescription = "",
            tint = PrimaryOrange,
            modifier = Modifier
                .size(64.dp)
                .padding(16.dp)
        )
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.notifications),
                color = TextColor,
                fontFamily = PoppinsMedium
            )
            Text(
                text = stringResource(R.string.manage_notification_options),
                color = TextColor,
                fontSize = 12.sp,
                fontFamily = PoppinsMedium
            )
        }
    }
    Row(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(0.9f)
            .shadow(4.dp, shape = CircleShape, spotColor = PrimaryOrange)
            .clip(CircleShape)
            .clickable { onClickPayments() }
            .background(PrimaryContainerColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_payment),
            contentDescription = "",
            tint = PrimaryOrange,
            modifier = Modifier
                .size(64.dp)
                .padding(16.dp)
        )
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.payments_methods),
                color = TextColor,
                fontFamily = PoppinsMedium
            )
            Text(
                text = stringResource(R.string.manage_your_payment_methods),
                color = TextColor,
                fontSize = 12.sp,
                fontFamily = PoppinsMedium
            )
        }
    }
    Row(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(0.9f)
            .shadow(4.dp, shape = CircleShape, spotColor = PrimaryOrange)
            .clip(CircleShape)
            .background(PrimaryContainerColor)
            .clickable { onClickAddresses() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_map),
            contentDescription = "",
            tint = PrimaryOrange,
            modifier = Modifier
                .size(64.dp)
                .padding(16.dp)
        )
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.addresses),
                color = TextColor,
                fontFamily = PoppinsMedium
            )
            Text(
                text = stringResource(R.string.manage_your_addresses),
                color = TextColor,
                fontSize = 12.sp,
                fontFamily = PoppinsMedium
            )
        }
    }
    Row(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(0.9f)
            .shadow(4.dp, shape = CircleShape, spotColor = PrimaryOrange)
            .clip(CircleShape)
            .background(PrimaryContainerColor)
            .clickable { onClickQuit() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_quit),
            contentDescription = "",
            tint = PrimaryOrange,
            modifier = Modifier
                .size(64.dp)
                .padding(16.dp)
        )
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.sign_out),
                color = TextColor,
                fontFamily = PoppinsMedium
            )
            Text(
                text = stringResource(R.string.sign_out_from_your_account),
                color = TextColor,
                fontSize = 12.sp,
                fontFamily = PoppinsMedium
            )
        }
    }
}


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewSettingsRouteDark() {
    EatzyTheme {
        SettingsRoute(
            uiState = SettingsContract.UiState(isLoading = false),
            emptyFlow(),
            onAction = {},
            {},
            navigateToIntroduce = {}, {}, {}, {}, {})
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun PreviewSettingsRouteLight() {
    EatzyTheme {
        SettingsRoute(
            uiState = SettingsContract.UiState(isLoading = false),
            emptyFlow(),
            onAction = {},
            {},
            navigateToIntroduce = {}, {}, {}, {}, {})
    }
}