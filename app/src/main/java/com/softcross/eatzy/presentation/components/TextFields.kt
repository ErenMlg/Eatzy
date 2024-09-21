package com.softcross.eatzy.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcross.eatzy.R
import com.softcross.eatzy.presentation.theme.PoppinsLight
import com.softcross.eatzy.presentation.theme.PrimaryBlack
import com.softcross.eatzy.presentation.theme.PrimaryContainerColor
import com.softcross.eatzy.presentation.theme.PrimaryOrange
import com.softcross.eatzy.presentation.theme.PrimaryRed
import com.softcross.eatzy.presentation.theme.PrimaryTextFieldColor
import com.softcross.eatzy.presentation.theme.PrimaryWhite

@Composable
fun IconTextField(
    modifier: Modifier = Modifier,
    givenValue: String,
    placeHolder: String,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    regex: String.() -> Boolean = String::isNotEmpty,
    keyboardType: KeyboardType = KeyboardType.Text,
    errorField: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        TextField(
            value = givenValue,
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            onValueChange = onValueChange,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = PrimaryOrange,
                focusedIndicatorColor = PrimaryOrange,
                errorIndicatorColor = PrimaryOrange,
                unfocusedContainerColor = PrimaryTextFieldColor,
                focusedContainerColor = PrimaryTextFieldColor,
                errorContainerColor = PrimaryTextFieldColor,
                cursorColor = PrimaryOrange,
                errorCursorColor = PrimaryOrange,
                selectionColors = TextSelectionColors(
                    handleColor = PrimaryOrange,
                    backgroundColor = PrimaryOrange.copy(alpha = 0.3f),
                ),
            ),
            isError = !givenValue.regex(),
            shape = RoundedCornerShape(8.dp),
            placeholder = {
                Text(
                    text = placeHolder,
                    fontFamily = PoppinsLight,
                    fontSize = 16.sp,
                )
            },
            modifier = modifier
                .fillMaxWidth()
                .shadow(3.dp, shape = RoundedCornerShape(8.dp))
        )
        AnimatedVisibility(
            visible = !givenValue.regex() && givenValue.isNotEmpty(),
            enter = slideInVertically() + expandVertically() + fadeIn(),
            exit = slideOutVertically() + shrinkVertically() + fadeOut(),
        ) {
            errorField()
        }
    }
}


@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    givenValue: String,
    placeHolder: String,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit) = {},
) {
    val customTextSelectionColors = TextSelectionColors(
        handleColor = PrimaryOrange,
        backgroundColor = PrimaryOrange.copy(alpha = 0.3f)
    )

    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        BasicTextField(
            value = givenValue, onValueChange = onValueChange,
            modifier = modifier
                .padding(top = 16.dp)
                .wrapContentHeight()
                .fillMaxWidth(0.9f)
                .shadow(3.dp, shape = RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(PrimaryTextFieldColor),
            cursorBrush = SolidColor(PrimaryOrange),
            textStyle = TextStyle(
                color = PrimaryBlack,
                fontSize = 16.sp,
                fontFamily = PoppinsLight,
                textAlign = TextAlign.Start,
            )
        ) { innerTextField ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                leadingIcon()
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .fillMaxWidth()
                ) {
                    if (givenValue.isEmpty()) {
                        Text(
                            text = placeHolder,
                            style = TextStyle(
                                color = Color.Gray,
                                fontSize = 16.sp,
                                fontFamily = PoppinsLight,
                                textAlign = TextAlign.Start
                            )
                        )
                    }
                    innerTextField()
                }
            }
        }
    }
}

@Preview
@Composable
fun SearchTextFieldPreview() {
    IconTextField(givenValue = "", placeHolder = "adsfdsg", onValueChange = {}) {

    }
}