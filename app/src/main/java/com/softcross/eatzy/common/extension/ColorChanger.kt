package com.softcross.eatzy.common.extension

import android.content.Context
import android.content.Intent
import com.softcross.eatzy.presentation.theme.BackgroundColor
import com.softcross.eatzy.presentation.theme.PrimaryBlack
import com.softcross.eatzy.presentation.theme.PrimaryButtonColor
import com.softcross.eatzy.presentation.theme.PrimaryContainerColor
import com.softcross.eatzy.presentation.theme.PrimaryGray
import com.softcross.eatzy.presentation.theme.PrimaryOrange
import com.softcross.eatzy.presentation.theme.PrimaryTextFieldColor
import com.softcross.eatzy.presentation.theme.PrimaryWhite
import com.softcross.eatzy.presentation.theme.SecondaryGray
import com.softcross.eatzy.presentation.theme.SecondaryWhite
import com.softcross.eatzy.presentation.theme.SubWhite
import com.softcross.eatzy.presentation.theme.TextColor

fun changeColor(context: Context, darkTheme: Boolean) {
    TextColor = if (darkTheme) PrimaryWhite else PrimaryBlack
    BackgroundColor = if (darkTheme) PrimaryGray else PrimaryWhite
    PrimaryContainerColor = if (darkTheme) SecondaryGray else SubWhite
    PrimaryTextFieldColor = if (darkTheme) SecondaryWhite else PrimaryWhite
    PrimaryButtonColor = if (darkTheme) PrimaryOrange else PrimaryBlack
    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}