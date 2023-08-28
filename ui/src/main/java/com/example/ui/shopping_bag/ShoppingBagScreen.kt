package com.example.ui.shopping_bag

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.navOptions
import com.example.core.base.StateErrorType
import com.example.core.enums.OrderStatus
import com.example.presenter.models.AddressUiModel
import com.example.presenter.models.OrderUiModel
import com.example.presenter.shopping_bag.ShoppingEvent
import com.example.presenter.shopping_bag.ShoppingNavigator
import com.example.presenter.shopping_bag.viewmodels.ShoppingBagViewModel
import com.example.ui.R
import com.example.ui.common.components.BackButton
import com.example.ui.common.components.CustomButton
import com.example.ui.common.components.LoadingAnimation
import com.example.ui.destinations.AddressScreenDestination
import com.example.ui.destinations.OrderPlacedScreenDestination
import com.example.ui.destinations.ProductScreenDestination
import com.example.ui.destinations.ShoppingBagScreenDestination
import com.example.ui.shopping_bag.components.ShoppingProductItem
import com.example.ui.theme.spacing
import com.example.ui.utils.noRippleClickable
import com.example.ui.utils.paddingWithPercentage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.EmptyCoroutineContext

@Destination
@Composable
fun ShoppingBagScreen(
    viewModel: ShoppingBagViewModel = hiltViewModel(),
    navigator: DestinationsNavigator? = null,
    orderId: String? = null,
    resultRecipient: ResultRecipient<AddressScreenDestination, AddressUiModel>
) {
    var shoppingViewState = viewModel.shoppingBagViewState
    val eventError = viewModel.eventError
    var productErrorDialog by remember { mutableStateOf(false) }
    var lostConnectionErrorDialog by remember { mutableStateOf(false) }
    var totalAmount by remember { mutableFloatStateOf(0.0f) }

    resultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {

            }

            is NavResult.Value -> {
                viewModel.setNewAddress(result.value)
            }
        }
    }

    LaunchedEffect(key1 = shoppingViewState.orderItemsUiModel) {
        totalAmount = shoppingViewState.orderItemsUiModel.map { it.price!! }.sum()
    }

    LaunchedEffect(key1 = eventError) {
        eventError.collect { error ->
            when (error) {
                StateErrorType.AUTHENTICATION_FAILED -> {
                    productErrorDialog = true
                }

                StateErrorType.NETWORK_ERROR -> {
                    lostConnectionErrorDialog = true
                }

                else -> {
                    // Handle other error types if needed
                }
            }
        }
    }

    LaunchedEffect(true) {
        viewModel.navigator.onEach { shoppingNavigator ->
            viewModel.setNavigator { null }
            shoppingNavigator.let {
                when (it) {
                    ShoppingNavigator.NavigateToOrderPlacedScreen -> {
                        navigator?.navigate(OrderPlacedScreenDestination, navOptions = navOptions {
                            popUpTo(ShoppingBagScreenDestination.route) {
                                inclusive = true
                            }
                        })
                    }
                }
            }
        }.flowOn(EmptyCoroutineContext).launchIn(this)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary.copy(0.05f))
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .padding(MaterialTheme.spacing.medium)
        ) {
            BackButton(
                modifier = Modifier
                    .background(
                        color = Color.White,
                        shape = CircleShape
                    ),
                iconTint = MaterialTheme.colorScheme.secondary,
                elevation = 10.dp
            ) {
                navigator?.popBackStack()
            }
        }
        Text(
            text = if (orderId == null) "Shopping Bag" else "Order Summary",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 27.sp,
            modifier = Modifier
                .padding(start = MaterialTheme.spacing.medium, bottom = MaterialTheme.spacing.large)
        )
        if (orderId == null) {
            shoppingViewState.orderItemsUiModel.forEach { orderItem ->
                ShoppingProductItem(
                    modifier = Modifier
                        .padding(bottom = MaterialTheme.spacing.medium)
                        .fillMaxWidth()
                        .height(paddingWithPercentage(percentage = 35))
                        .background(MaterialTheme.colorScheme.primary.copy(0.05f))
                        .noRippleClickable {
                            navigator?.navigate(ProductScreenDestination(orderItem.productId!!))
                        },
                    orderItemUiModel = orderItem,
                    onPlusButtonClicked = {
                        totalAmount += it
                    },
                    onMiensButtonClicked = {
                        totalAmount -= it
                    },
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    top = MaterialTheme.spacing.medium,
                    start = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium,
                    bottom = MaterialTheme.spacing.medium
                ),
        ) {
            Text(
                text = "Shipping Address",
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(bottom = MaterialTheme.spacing.medium)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = CenterVertically
            ) {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.delivery_icon),
                        contentDescription = null
                    )
                    Column {
                        Text(
                            text = shoppingViewState.address?.addressLine1 ?: "",
                            color = MaterialTheme.colorScheme.tertiary.copy(0.4f),
                            fontSize = 15.sp,
                            modifier = Modifier
                                .padding(bottom = MaterialTheme.spacing.extraSmall)
                        )
                        Text(
                            text = shoppingViewState.address?.addressLine2 ?: "",
                            color = MaterialTheme.colorScheme.tertiary.copy(0.4f),
                            fontSize = 15.sp,
                            modifier = Modifier
                                .padding(bottom = MaterialTheme.spacing.small)
                        )
                    }
                }
                Button(
                    modifier = Modifier,
                    onClick = {
                        navigator?.navigate(AddressScreenDestination)
                    },
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.tertiary.copy(0.11f)
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Text(
                        text = if (shoppingViewState.address?.id == null) "ADD" else "CHANGE",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
        Divider(
            color = MaterialTheme.colorScheme.tertiary.copy(0.1f),
            modifier = Modifier.padding(bottom = MaterialTheme.spacing.medium)
        )
        if (orderId == null && shoppingViewState.orderItemsUiModel.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium,
                        bottom = MaterialTheme.spacing.medium
                    ),
            ) {
                Text(
                    text = "Price Details",
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = MaterialTheme.spacing.medium)
                )
                shoppingViewState.orderItemsUiModel.forEach { item ->
                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = MaterialTheme.spacing.small)
                    ) {
                        val (name, price, div) = createRefs()

                        Text(
                            text = "${item.name}(${item.quantity}X)",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.tertiary.copy(0.4f),
                            modifier = Modifier.constrainAs(name) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            }
                        )

                        Text(
                            text = "$${item.price}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.tertiary.copy(0.4f),
                            modifier = Modifier.constrainAs(price) {
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            }
                        )

                        Divider(
                            color = MaterialTheme.colorScheme.tertiary.copy(0.1f),
                            modifier = Modifier.constrainAs(div) {
                                start.linkTo(name.end, margin = 10.dp)
                                end.linkTo(price.start, margin = 10.dp)
                                centerVerticallyTo(name)
                                width = Dimension.fillToConstraints
                                height = Dimension.wrapContent
                            }
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .align(End),
                    verticalAlignment = CenterVertically
                ) {
                    val total = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.tertiary.copy(0.4f),
                                fontSize = 12.sp
                            ),
                        ) {
                            append("Total : ")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            ),
                        ) {
                            append("$$totalAmount")
                        }
                    }
                    Text(total)
                }
            }
        }

        if (orderId == null && shoppingViewState.orderItemsUiModel.isNotEmpty() && shoppingViewState.address?.id != null) {
            CustomButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "ORDER NOW",
                shape = RectangleShape
            ) {
                val order = OrderUiModel(
                    orders = shoppingViewState.orderItemsUiModel,
                    total = totalAmount,
                    addressLine1 = shoppingViewState.address?.addressLine1,
                    addressLine2 = shoppingViewState.address?.addressLine2,
                    phoneNumber = shoppingViewState.address?.phoneNumber,
                    zipCode = shoppingViewState.address?.zipCode,
                    country = shoppingViewState.address?.country,
                    city = shoppingViewState.address?.city,
                    orderStatus = OrderStatus.PENDING
                )
                viewModel.setEventClicks(
                    ShoppingEvent.OnOrderButtonClicked(order)
                )
            }
        }

    }

    if (shoppingViewState.isLoading) {
        val systemUiController = rememberSystemUiController()
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = Color.White,
                )
            }
            Box(modifier = Modifier.fillMaxSize()) {
                LoadingAnimation(
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}