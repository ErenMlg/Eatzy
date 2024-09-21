package com.softcross.eatzy.presentation.introduce

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcross.eatzy.R
import com.softcross.eatzy.common.extension.changeAppLanguage
import com.softcross.eatzy.presentation.components.FilledButton
import com.softcross.eatzy.presentation.components.OutlinedButton
import com.softcross.eatzy.presentation.theme.EatzyTheme
import com.softcross.eatzy.presentation.theme.PoppinsLight
import com.softcross.eatzy.presentation.theme.PoppinsMedium
import com.softcross.eatzy.presentation.theme.PrimaryBlack
import com.softcross.eatzy.presentation.theme.PrimaryGreen
import com.softcross.eatzy.presentation.theme.PrimaryWhite

@Composable
fun IntroduceRoute(
    navigateToLogin: () -> Unit,
    navigateToSingUp: () -> Unit
) {
    val localContext = LocalConfiguration.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryGreen),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
        ) {
            Box(
                modifier = Modifier
                    .rotate(45f)
                    .shadow(5.dp, shape = RoundedCornerShape(64.dp))
                    .size((localContext.screenHeightDp * 0.4f).dp)
                    .background(PrimaryWhite)
                    .align(Alignment.Center)
            )
            Image(
                painter = painterResource(id = R.drawable.salad),
                contentDescription = "",
                modifier = Modifier
                    .size((localContext.screenHeightDp * 0.42f).dp)
                    .align(Alignment.Center),
            )
        }
        Text(
            text = stringResource(id = R.string.first_login_desc),
            fontSize = 32.sp,
            lineHeight = 35.sp,
            color = PrimaryWhite,
            fontFamily = PoppinsMedium,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .background(PrimaryGreen)
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            Canvas(modifier = Modifier) {
                drawLine(
                    color = PrimaryWhite,
                    start = Offset(0f, 30f),
                    end = Offset(0f, 200f),
                    strokeWidth = 0.4.dp.toPx()
                )
            }
            Text(
                text = stringResource(R.string.order_delicious_dishes_pay_online_and_live_track_your_order_state),
                fontSize = 14.sp,
                color = PrimaryWhite,
                fontFamily = PoppinsLight,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
        Row(Modifier.padding(top = 8.dp)) {
            OutlinedButton(
                text = stringResource(id = R.string.str_log_in),
                modifier = Modifier.weight(0.5f),
                onClick = navigateToLogin
            )
            FilledButton(
                text = stringResource(R.string.register),
                modifier = Modifier.weight(0.5f),
                color = PrimaryBlack,
                onClick = navigateToSingUp
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun IntroducePreviewLight() {
    EatzyTheme {
        IntroduceRoute(
            navigateToLogin = {},
            navigateToSingUp = {}
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun IntroducePreviewDark() {
    EatzyTheme {
        IntroduceRoute(
            navigateToLogin = {},
            navigateToSingUp = {}
        )
    }
}