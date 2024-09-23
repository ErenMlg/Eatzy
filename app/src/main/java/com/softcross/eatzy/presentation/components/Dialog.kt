package com.softcross.eatzy.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.softcross.eatzy.R
import com.softcross.eatzy.presentation.theme.BackgroundColor
import com.softcross.eatzy.presentation.theme.PoppinsLight
import com.softcross.eatzy.presentation.theme.PoppinsMedium
import com.softcross.eatzy.presentation.theme.PrimaryGray
import com.softcross.eatzy.presentation.theme.PrimaryGreen
import com.softcross.eatzy.presentation.theme.PrimaryWhite
import com.softcross.eatzy.presentation.theme.SecondaryGray
import com.softcross.eatzy.presentation.theme.TextColor

@Composable
fun NonIgnorableDialog(
    title: String,
    message: String,
    buttonText: String,
    onButtonClick: () -> Unit
) {
    Dialog(
        onDismissRequest = { /* Prevent dismiss */ }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(BackgroundColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = TextColor,
                fontSize = 18.sp,
                fontFamily = PoppinsMedium,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .padding(horizontal = 16.dp)
                    .align(Alignment.Start)
            )
            Text(
                text = message,
                color = TextColor,
                fontSize = 14.sp,
                fontFamily = PoppinsLight,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .padding(horizontal = 16.dp)
            )
            FilledButton(text = buttonText, modifier = Modifier, onClick = onButtonClick)
        }
    }
}


@Composable
fun NonIgnorableTwoButtonDialog(
    title: String,
    message: String,
    buttonText: String,
    onSubmitClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Dialog(
        onDismissRequest = { /* Prevent dismiss */ }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(BackgroundColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = TextColor,
                fontSize = 18.sp,
                fontFamily = PoppinsMedium,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .padding(horizontal = 16.dp)
                    .align(Alignment.Start)
            )
            Text(
                text = message,
                color = TextColor,
                fontSize = 14.sp,
                fontFamily = PoppinsLight,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .padding(horizontal = 16.dp)
                    .align(Alignment.Start)
            )
            Row(
                Modifier.fillMaxWidth()
            ) {
                FilledButton(text = buttonText, modifier = Modifier.weight(0.5f), onClick = {
                    onCancelClick();
                    onSubmitClick();
                })
                FilledButton(text = stringResource(R.string.cancel), modifier = Modifier.weight(0.5f), onClick = onCancelClick, color = SecondaryGray)
            }
        }
    }
}

