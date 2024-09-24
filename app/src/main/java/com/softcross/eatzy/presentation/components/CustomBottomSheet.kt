package com.softcross.eatzy.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcross.eatzy.R
import com.softcross.eatzy.domain.model.Location
import com.softcross.eatzy.domain.model.Payment
import com.softcross.eatzy.presentation.theme.BackgroundColor
import com.softcross.eatzy.presentation.theme.PoppinsMedium
import com.softcross.eatzy.presentation.theme.PrimaryContainerColor
import com.softcross.eatzy.presentation.theme.PrimaryOrange
import com.softcross.eatzy.presentation.theme.TextColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowCustomPaymentSheet(
    onDismiss: () -> Unit,
    paymentList: List<Payment>,
    onPaymentClick: (Payment) -> Unit,
    onNewPaymentClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        containerColor = BackgroundColor
    ) {
        Row(
            Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)
                .clickable { onDismiss(); onNewPaymentClick() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.select_a_payment),
                color = TextColor,
                fontFamily = PoppinsMedium
            )
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(PrimaryContainerColor)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.new_payment), color = TextColor, fontSize = 14.sp)
                Icon(
                    painter = painterResource(id = R.drawable.icon_add_location),
                    contentDescription = "",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        if (paymentList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
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
                        text = stringResource(id = R.string.no_favorites_yet),
                        color = TextColor,
                        fontFamily = PoppinsMedium,
                        modifier = Modifier
                            .padding(top = 190.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight(0.4f)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(paymentList.size) { item ->
                    val currentPayment = paymentList[item]
                    Row(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(0.9f)
                            .shadow(4.dp, shape = CircleShape, spotColor = PrimaryOrange)
                            .clip(CircleShape)
                            .align(Alignment.CenterHorizontally)
                            .background(PrimaryContainerColor)
                            .clickable { onDismiss(); onPaymentClick(currentPayment) },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_payment),
                            contentDescription = "",
                            tint = PrimaryOrange,
                            modifier = Modifier
                                .size(64.dp)
                                .padding(16.dp)
                        )
                        Column(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .fillMaxWidth(),
                        ) {
                            Text(
                                text = currentPayment.title + " / " + currentPayment.cartHolderName,
                                color = TextColor,
                                fontFamily = PoppinsMedium
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(R.string.no) + currentPayment.cartNumber.substring(
                                        currentPayment.cartNumber.length - 4,
                                        currentPayment.cartNumber.length
                                    ),
                                    color = TextColor,
                                    fontSize = 12.sp,
                                    fontFamily = PoppinsMedium
                                )
                                Text(
                                    text = "CVC: " + currentPayment.cartCVC,
                                    color = TextColor,
                                    fontSize = 12.sp,
                                    fontFamily = PoppinsMedium
                                )
                                Text(
                                    text = stringResource(R.string.date) + currentPayment.cartExpiryDate,
                                    color = TextColor,
                                    fontSize = 12.sp,
                                    fontFamily = PoppinsMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowCustomLocationSheet(
    onDismiss: () -> Unit,
    locationList: List<Location>,
    onLocationClick: (Location) -> Unit,
    onNewLocationClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        containerColor = BackgroundColor
    ) {
        Row(
            Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)
                .clickable { onDismiss(); onNewLocationClick() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.select_a_location),
                color = TextColor,
                fontFamily = PoppinsMedium
            )
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(PrimaryContainerColor)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.new_address), color = TextColor, fontSize = 14.sp)
                Icon(
                    painter = painterResource(id = R.drawable.icon_add_location),
                    contentDescription = "",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        if (locationList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight(0.4f)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(locationList.size) { item ->
                    val currentLocation = locationList[item]
                    Row(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(0.9f)
                            .shadow(4.dp, shape = CircleShape, spotColor = PrimaryOrange)
                            .clip(CircleShape)
                            .align(Alignment.CenterHorizontally)
                            .background(PrimaryContainerColor)
                            .clickable { onDismiss(); onLocationClick(currentLocation) },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_map),
                            contentDescription = "",
                            tint = PrimaryOrange,
                            modifier = Modifier
                                .size(64.dp)
                                .padding(16.dp)
                        )
                        Column(
                            modifier = Modifier
                                .padding(start = 8.dp, end = 16.dp)
                                .fillMaxWidth(),
                        ) {
                            Text(
                                text = "${currentLocation.title}, ${currentLocation.city} / ${currentLocation.country}",
                                color = TextColor,
                                fontFamily = PoppinsMedium
                            )
                            Text(
                                text = currentLocation.openAddress + " " + currentLocation.district,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = TextColor,
                                fontSize = 12.sp,
                                fontFamily = PoppinsMedium
                            )
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
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
                        text = stringResource(id = R.string.no_favorites_yet),
                        color = TextColor,
                        fontFamily = PoppinsMedium,
                        modifier = Modifier
                            .padding(top = 190.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}

