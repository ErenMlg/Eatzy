package com.softcross.eatzy.presentation.register


import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.softcross.eatzy.R
import com.softcross.eatzy.common.NanpVisualTransformation
import com.softcross.eatzy.common.extension.emailRegex
import com.softcross.eatzy.common.extension.nameSurnameRegexWithSpace
import com.softcross.eatzy.common.extension.passwordRegex
import com.softcross.eatzy.common.extension.phoneRegex
import com.softcross.eatzy.common.extension.userNameRegex
import com.softcross.eatzy.presentation.components.CustomSnackbar
import com.softcross.eatzy.presentation.components.IconTextField
import com.softcross.eatzy.presentation.components.LoadingFilledButton
import com.softcross.eatzy.presentation.components.SnackbarType
import com.softcross.eatzy.presentation.login.PasswordChecker
import com.softcross.eatzy.presentation.register.RegisterContract.UiAction
import com.softcross.eatzy.presentation.register.RegisterContract.UiEffect
import com.softcross.eatzy.presentation.register.RegisterContract.UiState
import com.softcross.eatzy.presentation.theme.BackgroundColor
import com.softcross.eatzy.presentation.theme.EatzyTheme
import com.softcross.eatzy.presentation.theme.PoppinsLight
import com.softcross.eatzy.presentation.theme.PoppinsMedium
import com.softcross.eatzy.presentation.theme.PrimaryBlack
import com.softcross.eatzy.presentation.theme.PrimaryOrange
import com.softcross.eatzy.presentation.theme.PrimaryRed
import com.softcross.eatzy.presentation.theme.TextColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun RegisterRoute(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    navigateToLogin: () -> Unit,
    navigateToMain: () -> Unit
) {
    var errorMessage by remember { mutableStateOf("") }
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(uiEffect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEffect.collect { effect ->
                when (effect) {
                    is UiEffect.ShowSnackbar -> {
                        errorMessage = effect.message
                    }

                    is UiEffect.NavigateToMainScreen -> navigateToMain()
                }
            }
        }
    }
    Box(
        Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        RegisterScreen(
            email = uiState.email,
            password = uiState.password,
            fullName = uiState.fullName,
            userName = uiState.username,
            phoneNumber = uiState.phoneNumber,
            loading = uiState.isLoading,
            onEmailChange = { onAction(UiAction.EmailChanged(it)) },
            onPasswordChange = { onAction(UiAction.PasswordChanged(it)) },
            onUserNameChanged = { onAction(UiAction.UserNameChanged(it)) },
            onFullNameChanged = { onAction(UiAction.FullNameChanged(it)) },
            onPhoneChange = { onAction(UiAction.PhoneNumberChanged(it)) },
            onRegisterClick = {
                errorMessage = ""
                onAction(UiAction.RegisterClick)
            },
            onLoginClick = navigateToLogin
        )
        if (errorMessage.isNotEmpty()) {
            CustomSnackbar(
                message = errorMessage,
                type = SnackbarType.ERROR,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun RegisterScreen(
    email: String,
    password: String,
    fullName: String,
    userName: String,
    phoneNumber: String,
    loading: Boolean = false,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onUserNameChanged: (String) -> Unit,
    onFullNameChanged: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
) {
    var passwordVisibility by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "",
                modifier = Modifier.size(160.dp),
            )
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 24.sp,
                fontFamily = PoppinsMedium,
                color = PrimaryOrange,
            )
            Text(
                text = stringResource(R.string.str_welcome_register),
                textAlign = TextAlign.Center,
                fontFamily = PoppinsLight,
                color = TextColor
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            IconTextField(
                givenValue = email,
                placeHolder = stringResource(R.string.str_email),
                onValueChange = onEmailChange,
                modifier = Modifier.padding(top = 16.dp),
                keyboardType = KeyboardType.Email,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_avatar),
                        contentDescription = "",
                        tint = PrimaryBlack
                    )
                },
                regex = String::emailRegex
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
                            stringResource(R.string.str_email).lowercase()
                        ),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 4.dp),
                        color = TextColor
                    )
                }
            }
            IconTextField(
                givenValue = password,
                placeHolder = stringResource(R.string.str_password),
                onValueChange = onPasswordChange,
                modifier = Modifier.padding(top = 16.dp),
                keyboardType = KeyboardType.Password,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_password),
                        contentDescription = "",
                        tint = PrimaryBlack
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = if (passwordVisibility) R.drawable.icon_hide_password else R.drawable.icon_show_password),
                        contentDescription = "",
                        tint = PrimaryOrange,
                        modifier = Modifier.clickable {
                            passwordVisibility = !passwordVisibility
                        }
                    )
                },
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                regex = String::passwordRegex
            ) {
                PasswordChecker(password = password)
            }
            IconTextField(
                givenValue = userName,
                placeHolder = stringResource(R.string.str_username),
                onValueChange = onUserNameChanged,
                modifier = Modifier.padding(top = 16.dp),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_text),
                        contentDescription = "",
                        tint = PrimaryBlack
                    )
                },
                regex = String::userNameRegex
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
                            stringResource(R.string.str_username).lowercase()
                        ),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 4.dp),
                        color = TextColor
                    )
                }
            }
            IconTextField(
                givenValue = fullName,
                placeHolder = stringResource(R.string.str_fullname),
                onValueChange = onFullNameChanged,
                modifier = Modifier.padding(top = 16.dp),
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
                        text = stringResource(
                            R.string.str_validation,
                            stringResource(R.string.str_fullname).lowercase()
                        ),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 4.dp),
                        color = TextColor
                    )
                }
            }
            IconTextField(
                givenValue = phoneNumber,
                placeHolder = stringResource(R.string.str_phone),
                onValueChange = { if (it.length <= 10) onPhoneChange(it) },
                visualTransformation = NanpVisualTransformation(),
                modifier = Modifier.padding(top = 16.dp),
                keyboardType = KeyboardType.Phone,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_phone),
                        contentDescription = "",
                        tint = PrimaryBlack
                    )
                },
                regex = String::phoneRegex
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
                            stringResource(R.string.str_phone).lowercase()
                        ),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 4.dp),
                        color = TextColor
                    )
                }
            }
        }
        Column(
            Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 32.dp)
                .imePadding(),
            verticalArrangement = Arrangement.Top,
        ) {
            LoadingFilledButton(
                text = stringResource(id = R.string.register),
                modifier = Modifier.padding(top = 16.dp),
                isEnabled = email.isNotEmpty() && password.isNotEmpty() && userName.isNotEmpty() && fullName.isNotEmpty() && phoneNumber.isNotEmpty(),
                onClick = onRegisterClick,
                isLoading = loading
            )
            Text(
                text = stringResource(R.string.already_have_an_account_log_in),
                color = TextColor,
                fontFamily = PoppinsLight,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp, end = 16.dp)
                    .clickable(onClick = onLoginClick)
            )
        }
    }

}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewRegisterRouteDark() {
    EatzyTheme {
        RegisterRoute(
            uiState = UiState(),
            uiEffect = emptyFlow(),
            onAction = {},
            navigateToLogin = {},
            {}
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun PreviewRegisterRouteLight() {
    EatzyTheme {
        RegisterRoute(
            uiState = UiState(),
            uiEffect = emptyFlow(),
            onAction = {},
            navigateToLogin = {},
            {}
        )
    }
}