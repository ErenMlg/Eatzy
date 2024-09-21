package com.softcross.eatzy.presentation.components

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.softcross.eatzy.R

@Composable
fun CustomLottieAnimation(
    modifier: Modifier = Modifier,
    @RawRes rawRes: Int = R.raw.loading,
    iterations: Int = LottieConstants.IterateForever
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawRes))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = iterations,
    )
    LottieAnimation(
        clipTextToBoundingBox = true,
        maintainOriginalImageBounds = true,
        modifier = modifier,
        composition = composition,
        progress = { progress },
    )
}