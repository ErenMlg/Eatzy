package com.softcross.eatzy.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcross.eatzy.presentation.theme.PoppinsLight
import com.softcross.eatzy.presentation.theme.PrimaryBlack
import com.softcross.eatzy.presentation.theme.PrimaryButtonColor
import com.softcross.eatzy.presentation.theme.PrimaryGray
import com.softcross.eatzy.presentation.theme.PrimaryOrange
import com.softcross.eatzy.presentation.theme.PrimaryWhite

@Composable
fun OutlinedButton(
    text: String,
    modifier: Modifier,
    color: Color = PrimaryWhite,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        border = BorderStroke(1.dp, color),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        )
    ) {
        Text(
            text = text, fontSize = 14.sp,
            color = PrimaryWhite,
            fontFamily = PoppinsLight,
            modifier = Modifier.padding(vertical = 6.dp)
        )
    }
}

@Composable
fun FilledButton(
    text: String,
    modifier: Modifier,
    color: Color = PrimaryButtonColor,
    isEnabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        enabled = isEnabled,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        )
    ) {
        Text(
            text = text, fontSize = 14.sp,
            color = PrimaryWhite,
            fontFamily = PoppinsLight,
            modifier = Modifier.padding(vertical = 6.dp)
        )
    }
}

@Composable
fun LoadingFilledButton(
    text: String,
    modifier: Modifier,
    color: Color = PrimaryButtonColor,
    isEnabled: Boolean = true,
    onClick: () -> Unit = {},
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp),
        enabled = isEnabled,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                trackColor = if(isSystemInDarkTheme()) PrimaryWhite else PrimaryOrange,
                color = if(isSystemInDarkTheme()) PrimaryGray else PrimaryOrange.copy(alpha = 0.5f),
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text(
                text = text, fontSize = 14.sp,
                color = PrimaryWhite,
                fontFamily = PoppinsLight,
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }
    }
}