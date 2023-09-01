package com.example.ui.product

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.core.base.StateErrorType
import com.example.presenter.models.OrderItemUiModel
import com.example.presenter.product.ProductEvent
import com.example.presenter.product.viewmodels.ProductViewModel
import com.example.ui.R
import com.example.ui.common.components.BackButton
import com.example.ui.common.components.CustomButton
import com.example.ui.common.components.DrawHalfOfCircle
import com.example.ui.common.components.ErrorDialog
import com.example.ui.common.components.LoadingAnimation
import com.example.ui.common.components.LostConnectionErrorDialog
import com.example.ui.theme.spacing
import com.example.ui.utils.formatNumberWithKNotation
import com.example.ui.utils.noRippleClickable
import com.example.ui.utils.paddingWithPercentage
import com.example.ui.utils.parseColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.EmptyCoroutineContext

@Destination
@Composable
fun ProductScreen(
    id: String,
    viewModel: ProductViewModel = hiltViewModel(),
    navigator: DestinationsNavigator? = null
) {
    var isSavedIconClicked by remember { mutableStateOf(false) }
    var productErrorDialog by remember { mutableStateOf(false) }
    var lostConnectionErrorDialog by remember { mutableStateOf(false) }
    var selectedButtonIndex by remember { mutableIntStateOf(-1) }
    var selectedColorIndex by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val productViewSate = viewModel.productViewState
    val eventError = viewModel.eventError
    var productUrl by remember { mutableStateOf("") }
    var circleColor by remember { mutableStateOf(Color.Transparent) }
    var updateSelectedButton by remember { mutableStateOf(false) }
    var isInShoppingBag by remember { mutableStateOf(false) }


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
        viewModel.navigator.onEach { productNavigator ->
            viewModel.setNavigator { null }
            productNavigator.let {
                when (it) {

                    else -> {}
                }
            }
        }.flowOn(EmptyCoroutineContext).launchIn(this)
    }

    LaunchedEffect(key1 = productViewSate) {
        val product = productViewSate.productItemUiModel
        product?.let {
            isSavedIconClicked = product.isSaved
            productUrl = it.colorSizeInformation.first().photoUrl!!
            circleColor = it.colorSizeInformation.first().colorCode?.parseColor() ?: Color.Black
            selectedButtonIndex =
                it.colorSizeInformation.first().sizeUiModel.indexOfFirst { sizeInfo ->
                    sizeInfo.availablePieces > 0
                }
            isInShoppingBag = it.isInShoppingBag
        }
    }

    LaunchedEffect(key1 = updateSelectedButton) {
        val product = productViewSate.productItemUiModel
        product?.let {
            selectedButtonIndex =
                it.colorSizeInformation[selectedColorIndex].sizeUiModel.indexOfFirst { sizeInfo ->
                    sizeInfo.availablePieces > 0
                }
        }
        updateSelectedButton = false
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary.copy(0.05f))
    ) {
        val (upperContent, stickyButton) = createRefs()
        Column(
            modifier = Modifier
                .constrainAs(upperContent) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
                .fillMaxSize()
                .padding(bottom = 57.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            productViewSate.productItemUiModel?.let { product ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.medium),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
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
                    Icon(
                        painter = if (isSavedIconClicked) painterResource(id = R.drawable.saved_items_filled) else painterResource(
                            id = R.drawable.saved_items_not_filled
                        ),
                        contentDescription = "saved icon",
                        modifier = Modifier
                            .noRippleClickable {
                                if (isSavedIconClicked) {
                                    Toast.makeText(
                                        context,
                                        "Item removed from watch later list successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    viewModel.setEventClicks(
                                        ProductEvent.OnSavedIconClicked(true)
                                    )
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Item added to watch later list successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    viewModel.setEventClicks(
                                        ProductEvent.OnSavedIconClicked(false)
                                    )
                                }
                                isSavedIconClicked = !isSavedIconClicked
                            }
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(paddingWithPercentage(percentage = 100))
                ) {
                    DrawHalfOfCircle(
                        modifier = Modifier
                            .fillMaxSize(0.9f)
                            .align(Alignment.CenterStart),
                        color = circleColor
                    )
                    AsyncImage(
                        model = productUrl,
                        contentDescription = "productPhoto",
                        modifier = Modifier
                            .rotate(25f)
                            .size(270.dp)
                            .align(Alignment.BottomCenter)
                            .offset(x = (-20).dp, y = (20).dp),
                        contentScale = ContentScale.Fit
                    )
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .offset(x = (-67).dp, y = (50).dp)
                            .background(
                                color = Color.White,
                                shape = CircleShape
                            )
                            .align(Alignment.TopCenter)
                    ) {
                        Text(
                            text = "$${formatNumberWithKNotation(product.price!!.toFloat())}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(
                                topStart = MaterialTheme.spacing.large,
                                topEnd = MaterialTheme.spacing.large
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.spacing.medium)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = product.name!!,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Button(
                                modifier = Modifier,
                                onClick = {
                                    val orderItem = OrderItemUiModel(
                                        productId = productViewSate.productItemUiModel!!.id,
                                        name = productViewSate.productItemUiModel!!.name,
                                        price = productViewSate.productItemUiModel!!.price,
                                        photoUrl = productUrl,
                                        colorCode = productViewSate.productItemUiModel!!.colorSizeInformation[selectedColorIndex].colorCode,
                                        quantity = 1,
                                        colorName = productViewSate.productItemUiModel!!.colorSizeInformation[selectedColorIndex].colorName,
                                        size = productViewSate.productItemUiModel!!.colorSizeInformation[selectedColorIndex].sizeUiModel[selectedButtonIndex].size!!.toInt(),
                                        sex = productViewSate.productItemUiModel!!.sex
                                    )
                                    if (!productViewSate.productItemUiModel!!.isInShoppingBag) {
                                        viewModel.setEventClicks(
                                            ProductEvent.OnShoppingButtonClicked(
                                                false,
                                                orderItem
                                            )
                                        )
                                        isInShoppingBag = !isInShoppingBag
                                        productViewSate.productItemUiModel!!.isInShoppingBag =
                                            !productViewSate.productItemUiModel!!.isInShoppingBag
                                    } else {
                                        viewModel.setEventClicks(
                                            ProductEvent.OnShoppingButtonClicked(
                                                true,
                                                orderItem
                                            )
                                        )
                                        isInShoppingBag = !isInShoppingBag
                                        productViewSate.productItemUiModel!!.isInShoppingBag =
                                            !productViewSate.productItemUiModel!!.isInShoppingBag
                                    }
                                },
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.tertiary.copy(0.11f)
                                ),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent
                                )
                            ) {
                                Row(
                                    modifier = Modifier,
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = if (!isInShoppingBag) "ADD TO BAG" else "REMOVE FROM BAG",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Icon(
                                        painter = painterResource(id = R.drawable.shopping_bag),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier
                                            .size(ButtonDefaults.IconSize)
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Rating view here but i will implement it later...",
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.tertiary.copy(0.5f),
                            modifier = Modifier
                                .padding(vertical = MaterialTheme.spacing.medium)
                        )
                        Text(
                            text = product.description!!,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.tertiary.copy(0.5f),
                            modifier = Modifier
                                .padding(bottom = MaterialTheme.spacing.medium)
                        )
                        Text(
                            text = "Size",
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(bottom = MaterialTheme.spacing.medium)
                        )
                        LazyRow(
                            modifier = Modifier
                                .padding(bottom = MaterialTheme.spacing.medium)
                        ) {
                            itemsIndexed(product.colorSizeInformation[selectedColorIndex].sizeUiModel) { index, productSizeModel ->
                                val isButtonSelected = index == selectedButtonIndex
                                val diagonalModifier = if (productSizeModel.availablePieces <= 0) {
                                    Modifier.drawWithContent {
                                        drawContent()
                                        drawLine(
                                            color = Color.Red,
                                            start = Offset(-20f, 0f),
                                            end = Offset(size.width, size.height - 12),
                                            strokeWidth = 1.dp.toPx()
                                        )
                                    }
                                } else {
                                    Modifier
                                }
                                Button(
                                    modifier = Modifier
                                        .padding(end = MaterialTheme.spacing.small)
                                        .then(diagonalModifier),
                                    onClick = {
                                        selectedButtonIndex = index
                                    },
                                    enabled = productSizeModel.availablePieces > 0,
                                    shape = RoundedCornerShape(4.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isButtonSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        disabledContainerColor = Color.Transparent,
                                        contentColor = if (isButtonSelected) Color.White else MaterialTheme.colorScheme.tertiary.copy(
                                            0.3f
                                        ),
                                        disabledContentColor = MaterialTheme.colorScheme.tertiary.copy(
                                            0.3f
                                        )
                                    ),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.tertiary.copy(0.11f)
                                    )
                                ) {
                                    Text(text = productSizeModel.size?.toInt().toString())
                                }
                            }
                        }
                        Text(
                            text = "Color",
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(bottom = MaterialTheme.spacing.medium)
                        )
                        LazyRow(
                            modifier = Modifier
                                .padding(bottom = MaterialTheme.spacing.medium)
                        ) {
                            itemsIndexed(product.colorSizeInformation) { index, colorInformation ->
                                val color = colorInformation.colorCode?.parseColor()
                                Button(
                                    modifier = Modifier
                                        .padding(end = MaterialTheme.spacing.small),
                                    onClick = {
                                        selectedColorIndex = index
                                        productUrl = colorInformation.photoUrl!!
                                        circleColor = color!!
                                        updateSelectedButton = true
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = color!!
                                    ),
                                    shape = RoundedCornerShape(4.dp),
                                    border = BorderStroke(
                                        width = if (index == selectedColorIndex) 0.5.dp else 0.dp,
                                        color = if (index == selectedColorIndex) Color.Black else Color.Transparent
                                    ),
                                ) {
                                    Text(
                                        text = "40",
                                        color = Color.Transparent
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        CustomButton(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(stickyButton) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                },
            text = "Buy now",
            shape = RectangleShape
        ) {}
    }

    if (productErrorDialog) {
        ErrorDialog {
            productErrorDialog = !productErrorDialog
        }
    }
    if (lostConnectionErrorDialog) {
        LostConnectionErrorDialog {
            lostConnectionErrorDialog = !lostConnectionErrorDialog
        }
    }
    if (productViewSate.isLoading) {
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
