package com.softcross.eatzy.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcross.eatzy.presentation.theme.PrimaryGreen
import com.softcross.eatzy.presentation.theme.PrimaryOrange
import com.softcross.eatzy.presentation.theme.PrimaryRed
import com.softcross.eatzy.presentation.theme.PrimaryWhite

enum class SnackbarType {
    ERROR,
    SUCCESS
}

@Composable
fun CustomSnackbar(
    type: SnackbarType,
    message: String,
    modifier: Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message)
        }
    }

    val backgroundColor = when (type) {
        SnackbarType.ERROR -> PrimaryRed
        SnackbarType.SUCCESS -> PrimaryGreen
    }

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier
            .background(backgroundColor)
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = message,
            color = PrimaryWhite,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

    }
}

@Preview
@Composable
fun SnackbarPreviewError() {
    MaterialTheme {
        CustomSnackbar(
            type = SnackbarType.ERROR,
            message = "Sel",
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun SnackbarPreviewSuccess() {
    MaterialTheme {
        CustomSnackbar(
            type = SnackbarType.SUCCESS,
            message = "Sel",
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}