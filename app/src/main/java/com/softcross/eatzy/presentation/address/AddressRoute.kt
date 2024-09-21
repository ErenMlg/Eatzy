package com.softcross.eatzy.presentation.address


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
fun AddressRoute(
    uiState: AddressContract.UiState,
    uiEffect: Flow<AddressContract.UiEffect>,
    onAction: (AddressContract.UiAction) -> Unit,
    onNavigateProfile: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(uiEffect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEffect.collect { effect ->
                when (effect) {

                    is AddressContract.UiEffect.OnNavigateProfile -> onNavigateProfile()

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
                text = stringResource(R.string.new_address),
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
                    .clickable(onClick = { onAction(AddressContract.UiAction.OnClearFields) })
            )
        }
        AddressScreen(
            title = uiState.title,
            country = uiState.country,
            city = uiState.city,
            district = uiState.district,
            openAddress = uiState.openAddress,
            onTitleChanged = { onAction(AddressContract.UiAction.OnTitleChanged(it)) },
            onCountryChanged = { onAction(AddressContract.UiAction.OnCountryChanged(it)) },
            onCityChanged = { onAction(AddressContract.UiAction.OnCityChanged(it)) },
            onDistrictChanged = { onAction(AddressContract.UiAction.OnDistrictChanged(it)) },
            onOpenAddressChanged = { onAction(AddressContract.UiAction.OnOpenAddressChanged(it)) },
            onAddClicked = { onAction(AddressContract.UiAction.OnSubmitClicked) }
        )
    }
}

@Composable
fun AddressScreen(
    title: String,
    country: String,
    city: String,
    district: String,
    openAddress: String,
    onTitleChanged: (String) -> Unit,
    onCountryChanged: (String) -> Unit,
    onCityChanged: (String) -> Unit,
    onDistrictChanged: (String) -> Unit,
    onOpenAddressChanged: (String) -> Unit,
    onAddClicked: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
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
            placeHolder = stringResource(R.string.title),
            onValueChange = { onTitleChanged(it) },
            modifier = Modifier.padding(top = 16.dp),
            keyboardType = KeyboardType.Text,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_add_location),
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
                    text = stringResource(R.string.str_validation, stringResource(R.string.title).lowercase()),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    color = TextColor
                )
            }
        }
        IconTextField(
            givenValue = country,
            placeHolder = stringResource(R.string.country),
            onValueChange = { onCountryChanged(it) },
            modifier = Modifier.padding(top = 16.dp),
            keyboardType = KeyboardType.Text,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_text),
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
                    text = stringResource(R.string.str_validation, stringResource(R.string.country).lowercase()),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    color = TextColor
                )
            }
        }
        IconTextField(
            givenValue = city,
            placeHolder = stringResource(R.string.city),
            onValueChange = { onCityChanged(it) },
            modifier = Modifier.padding(top = 16.dp),
            keyboardType = KeyboardType.Text,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_text),
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
                    text = stringResource(R.string.str_validation, stringResource(R.string.city).lowercase()),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    color = TextColor
                )
            }
        }
        IconTextField(
            givenValue = district,
            placeHolder = stringResource(R.string.district),
            onValueChange = { onDistrictChanged(it) },
            modifier = Modifier.padding(top = 16.dp),
            keyboardType = KeyboardType.Text,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_text),
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
                    text = stringResource(R.string.str_validation, stringResource(R.string.district).lowercase()),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    color = TextColor
                )
            }
        }
        IconTextField(
            givenValue = openAddress,
            placeHolder = stringResource(R.string.open_address),
            onValueChange = { onOpenAddressChanged(it) },
            modifier = Modifier.padding(top = 16.dp),
            keyboardType = KeyboardType.Text,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_text),
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
                    text = stringResource(R.string.str_validation, stringResource(R.string.open_address).lowercase()),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    color = TextColor
                )
            }
        }
        FilledButton(
            text = stringResource(R.string.add_update),
            isEnabled = title.isNotEmpty() && country.isNotEmpty() && city.isNotEmpty() && district.isNotEmpty() && openAddress.isNotEmpty(),
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                keyboardController?.hide()
                onAddClicked()
            }
        )
    }

}


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewAddressRouteDark() {
    EatzyTheme {
        AddressRoute(
            uiState = AddressContract.UiState(isLoading = false),
            uiEffect = emptyFlow(),
            onAction = {},
            {}
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun PreviewAddressRouteLight() {
    EatzyTheme {
        AddressRoute(
            uiState = AddressContract.UiState(isLoading = false),
            uiEffect = emptyFlow(),
            onAction = {},
            {}
        )
    }
}