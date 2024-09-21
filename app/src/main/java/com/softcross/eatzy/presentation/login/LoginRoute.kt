package com.softcross.eatzy.presentation.login

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.softcross.eatzy.R
import com.softcross.eatzy.common.extension.emailRegex
import com.softcross.eatzy.common.extension.passwordRegex
import com.softcross.eatzy.presentation.components.CustomSnackbar
import com.softcross.eatzy.presentation.components.FilledButton
import com.softcross.eatzy.presentation.components.IconTextField
import com.softcross.eatzy.presentation.components.LoadingFilledButton
import com.softcross.eatzy.presentation.components.SnackbarType
import com.softcross.eatzy.presentation.theme.BackgroundColor
import com.softcross.eatzy.presentation.theme.EatzyTheme
import com.softcross.eatzy.presentation.theme.PoppinsLight
import com.softcross.eatzy.presentation.theme.PoppinsMedium
import com.softcross.eatzy.presentation.theme.PrimaryBlack
import com.softcross.eatzy.presentation.theme.PrimaryOrange
import com.softcross.eatzy.presentation.theme.PrimaryRed
import com.softcross.eatzy.presentation.theme.PrimaryWhite
import com.softcross.eatzy.presentation.theme.TextColor
import kotlinx.coroutines.flow.Flow

@Composable
fun LoginRoute(
    uiState: LoginContract.UiState,
    uiEffect: Flow<LoginContract.UiEffect>,
    onAction: (LoginContract.UiAction) -> Unit,
    navigateToSignUp: () -> Unit,
    navigateToMain: () -> Unit
) {
    var errorMessage by remember { mutableStateOf("") }
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(uiEffect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEffect.collect { effect ->
                when (effect) {
                    is LoginContract.UiEffect.ShowSnackbar -> {
                        errorMessage = effect.message
                    }

                    is LoginContract.UiEffect.NavigateToMainScreen -> {
                        navigateToMain()
                    }
                }
            }
        }
    }
    Box(
        Modifier
            .fillMaxSize()
            .imePadding()) {
        LoginScreen(
            email = uiState.email,
            password = uiState.password,
            loading = uiState.isLoading,
            onEmailChange = { onAction(LoginContract.UiAction.EmailChanged(it)) },
            onPasswordChange = { onAction(LoginContract.UiAction.PasswordChanged(it)) },
            onLoginClick = {
                errorMessage = ""
                onAction(LoginContract.UiAction.SignInClick)
            },
            onRegisterClick = navigateToSignUp
        )
        if (errorMessage.isNotEmpty()) {
            CustomSnackbar(
                type = SnackbarType.ERROR,
                message = errorMessage,
                modifier = Modifier
                    .align(alignment = Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun LoginScreen(
    email: String,
    password: String,
    loading: Boolean = true,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
) {
    var passwordVisibility by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(BackgroundColor),
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
                colorFilter = ColorFilter.tint(PrimaryOrange),
                modifier = Modifier.size(160.dp),
            )
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 24.sp,
                fontFamily = PoppinsMedium,
                color = PrimaryOrange,
            )
            Text(
                text = stringResource(R.string.str_welcome_login),
                fontFamily = PoppinsLight,
                color = TextColor
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 32.dp)
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
                        text = stringResource(R.string.str_validation, stringResource(id = R.string.str_email)),
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
            LoadingFilledButton(
                text = stringResource(R.string.str_log_in),
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                isEnabled = email.isNotEmpty() && password.isNotEmpty() && !loading,
                onClick = onLoginClick,
                isLoading = loading
            )
            Text(
                text = stringResource(R.string.str_forgot_password),
                color = TextColor,
                fontFamily = PoppinsLight,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(alignment = Alignment.End)
                    .clickable(onClick = onRegisterClick)
            )
        }
    }
}

@Composable
fun PasswordChecker(
    password: String
) {
    Column(
        Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.str_password_validation_title),
            fontSize = 12.sp,
            color = TextColor
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (password.length >= 8) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "",
                    modifier = Modifier.size(12.dp),
                    tint = PrimaryRed
                )
            } else {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "",
                    modifier = Modifier.size(12.dp),
                    tint = PrimaryRed
                )
            }
            Text(
                text = stringResource(R.string.str_password_validation_one),
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 4.dp),
                color = TextColor
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if ("([0-9])".toRegex().containsMatchIn(password)) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "",
                    modifier = Modifier.size(12.dp),
                    tint = PrimaryRed
                )
            } else {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "",
                    modifier = Modifier.size(12.dp),
                    tint = PrimaryRed
                )
            }
            Text(
                text = stringResource(R.string.str_password_validation_two),
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 4.dp),
                color = TextColor
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if ("([a-z])".toRegex().containsMatchIn(password)) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "",
                    modifier = Modifier.size(12.dp),
                    tint = PrimaryRed
                )
            } else {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "",
                    modifier = Modifier.size(12.dp),
                    tint = PrimaryRed
                )
            }
            Text(
                text = stringResource(R.string.str_password_validation_three),
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 4.dp),
                color = TextColor
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if ("([A-Z])".toRegex().containsMatchIn(password)) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "",
                    modifier = Modifier.size(12.dp),
                    tint = PrimaryRed
                )
            } else {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "",
                    modifier = Modifier.size(12.dp),
                    tint = PrimaryRed
                )
            }
            Text(
                text = stringResource(R.string.str_password_validation_four),
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 4.dp),
                color = TextColor
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun LoginRoutePreviewDark() {
    EatzyTheme {
        LoginScreen(
            email = "",
            password = "",
            loading = false,
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun LoginRoutePreviewLight() {
    EatzyTheme {
        LoginScreen(
            email = "",
            password = "",
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}