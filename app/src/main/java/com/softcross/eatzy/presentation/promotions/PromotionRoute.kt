package com.softcross.eatzy.presentation.promotions


import android.content.ClipboardManager
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcross.eatzy.R
import com.softcross.eatzy.domain.model.Promotion
import com.softcross.eatzy.presentation.components.CustomAsyncImageWithoutAPI
import com.softcross.eatzy.presentation.components.CustomLottieAnimation
import com.softcross.eatzy.presentation.components.FilledButton
import com.softcross.eatzy.presentation.theme.BackgroundColor
import com.softcross.eatzy.presentation.theme.EatzyTheme
import com.softcross.eatzy.presentation.theme.PoppinsLight
import com.softcross.eatzy.presentation.theme.PoppinsMedium
import com.softcross.eatzy.presentation.theme.PrimaryButtonColor
import com.softcross.eatzy.presentation.theme.PrimaryContainerColor
import com.softcross.eatzy.presentation.theme.PrimaryOrange
import com.softcross.eatzy.presentation.theme.TextColor

const val presentURL =
    "https://static.vecteezy.com/system/resources/previews/020/995/215/original/3d-minimal-orange-gift-box-gift-box-for-special-event-3d-rendering-illustration-png.png"

@Composable
fun PromotionRoute(
    uiState: PromotionContract.UiState,
    onAction: (PromotionContract.UiAction) -> Unit,
    navigateToMain: () -> Unit
) {
    BackHandler {
        navigateToMain()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryContainerColor),
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
                    .clickable(onClick = { navigateToMain() })
            )
            Text(
                text = "Promotions",
                color = TextColor,
                fontSize = 16.sp,
                fontFamily = PoppinsMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        if (uiState.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundColor),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CustomLottieAnimation(modifier = Modifier.size(220.dp))
            }
        } else if (uiState.errorMessage.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundColor),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(Modifier.fillMaxWidth(0.9f)) {
                    CustomLottieAnimation(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(220.dp),
                        rawRes = R.raw.error,
                        iterations = 1
                    )
                    Text(
                        text = "An error on fetching promotion list",
                        color = TextColor,
                        fontFamily = PoppinsMedium,
                        modifier = Modifier
                            .padding(top = 190.dp)
                            .align(Alignment.Center)
                    )
                }
                FilledButton(
                    text = "Retry",
                    color = PrimaryButtonColor,
                    onClick = { onAction(PromotionContract.UiAction.OnRetryClicked) },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(top = 16.dp)
                )
            }
        } else {
            PromotionScreen(
                promotionList = uiState.promotions
            )
        }
    }
}


@Composable
fun PromotionScreen(
    promotionList: List<Promotion>
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    if (promotionList.isEmpty()) {
        Box(
            Modifier.fillMaxSize()
        ) {
            CustomLottieAnimation(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(220.dp),
                rawRes = R.raw.empty,
                iterations = 1
            )
            Text(
                text = stringResource(R.string.no_promotions_yet),
                color = TextColor,
                fontFamily = PoppinsMedium,
                modifier = Modifier
                    .padding(top = 190.dp)
                    .align(Alignment.Center)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(0.9f)
        ) {
            items(promotionList.size, key = { promotionList[it].code }) {
                val currentPromotion = promotionList[it]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .padding(bottom = 16.dp, top = if (it == 0) 16.dp else 0.dp)
                        .shadow(
                            2.dp,
                            shape = RoundedCornerShape(16.dp),
                            spotColor = PrimaryOrange
                        )
                        .clip(shape = RoundedCornerShape(16.dp))
                        .background(PrimaryContainerColor)
                        .clickable {
                            clipboardManager.setText(AnnotatedString(currentPromotion.code))
                            Toast
                                .makeText(
                                    context,
                                    context.getString(R.string.promotion_code_copied),
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(0.6f)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .padding(start = 16.dp)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = currentPromotion.title,
                                color = TextColor,
                                fontSize = 16.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                fontFamily = PoppinsMedium
                            )
                            Text(
                                text = currentPromotion.code,
                                fontSize = 12.sp,
                                color = TextColor,
                                fontFamily = PoppinsLight,
                            )
                            Text(
                                text = "%${currentPromotion.discount} " + stringResource(id = R.string.off),
                                color = PrimaryOrange,
                                fontSize = 14.sp,
                            )

                        }
                    }
                    CustomAsyncImageWithoutAPI(
                        model = presentURL,
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewPromotionRouteDark() {
    EatzyTheme {
        PromotionRoute(
            uiState = PromotionContract.UiState(isLoading = false, errorMessage = "SAFADGS"),
            onAction = {},
            navigateToMain = {}
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun PreviewPromotionRouteLight() {
    EatzyTheme {
        PromotionRoute(
            uiState = PromotionContract.UiState(
                isLoading = false,
                promotions = listOf(
                    Promotion(
                        code = "PROMO1",
                        title = "Promotion 1",
                        discount = 20
                    ),
                    Promotion(
                        code = "PROMO4",
                        title = "Promotion 1",
                        discount = 20
                    ),
                    Promotion(
                        code = "PROMO3",
                        title = "Promotion 1",
                        discount = 20
                    ),
                    Promotion(
                        code = "PROMO2",
                        title = "Promotion 1",
                        discount = 20
                    ),
                )
            ),
            onAction = {},
            navigateToMain = {}
        )
    }
}