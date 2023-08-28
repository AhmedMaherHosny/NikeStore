package com.example.ui.shopping_bag.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.core.enums.Sex
import com.example.presenter.models.OrderItemUiModel
import com.example.ui.common.components.DrawHalfOfCircle
import com.example.ui.theme.spacing
import com.example.ui.utils.noRippleClickable
import com.example.ui.utils.paddingWithPercentage
import com.example.ui.utils.parseColor

@Composable
fun ShoppingProductItem(
    modifier: Modifier,
    orderItemUiModel: OrderItemUiModel,
    isButtonsClickable: Boolean = true,
    onPlusButtonClicked : (price : Float) -> Unit,
    onMiensButtonClicked : (price : Float) -> Unit,
) {
    var quantity by remember { mutableIntStateOf(orderItemUiModel.quantity) }
    Box(
        modifier = modifier
    ) {
        DrawHalfOfCircle(
            modifier = Modifier
                .fillMaxSize(0.9f)
                .align(Alignment.CenterStart),
            color = orderItemUiModel.colorCode?.parseColor()?.copy(0.3f) ?: Color.Red
        )
        AsyncImage(
            model = orderItemUiModel.photoUrl,
            contentDescription = "productPhoto",
            modifier = Modifier
                .rotate(25f)
                .size(110.dp)
                .align(Alignment.BottomStart)
                .offset(x = (10).dp, y = (-10).dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = orderItemUiModel.name!! + " (${orderItemUiModel.sex?.name}) (${orderItemUiModel.size})",
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = MaterialTheme.colorScheme.tertiary.copy(0.5f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = MaterialTheme.spacing.medium)
        )
        Text(
            text = '$' + orderItemUiModel.price!!.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            color = MaterialTheme.colorScheme.secondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 50.dp)
        )
        Box(
            modifier = Modifier
                .padding(end = MaterialTheme.spacing.medium, bottom = MaterialTheme.spacing.medium)
                .background(Color.White, RoundedCornerShape(MaterialTheme.spacing.extraSmall))
                .wrapContentSize()
                .align(Alignment.BottomEnd)
                .padding(MaterialTheme.spacing.extraSmall)
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            if (isButtonsClickable && quantity > 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(
                                0.2f
                            ),
                            RoundedCornerShape(MaterialTheme.spacing.extraSmall)
                        )
                        .padding(vertical = 5.dp, horizontal = 11.dp)
                        .withClickable(isClickable = isButtonsClickable && quantity > 1) {
                            quantity -= 1
                            orderItemUiModel.quantity -= 1
                            onMiensButtonClicked(orderItemUiModel.price!!)
                        }
                ) {
                    Text(
                        text = "-",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.background
                    )
                }
                Text(
                    text = quantity.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Box(
                    modifier = Modifier
                        .background(
                            if (isButtonsClickable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(
                                0.2f
                            ),
                            RoundedCornerShape(MaterialTheme.spacing.extraSmall)
                        )
                        .padding(vertical = 5.dp, horizontal = 10.dp)
                        .withClickable(isClickable = isButtonsClickable) {
                            quantity += 1
                            orderItemUiModel.quantity += 1
                            onPlusButtonClicked(orderItemUiModel.price!!)
                        }
                ) {
                    Text(
                        text = "+",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.background
                    )
                }
//                Button(
//                    modifier = Modifier,
//                    enabled = true,
//                    onClick = {
//
//                    },
//                    shape = RoundedCornerShape(MaterialTheme.spacing.extraSmall),
//                    colors = ButtonDefaults.buttonColors(
//                        contentColor = MaterialTheme.colorScheme.background,
//                        containerColor = MaterialTheme.colorScheme.primary,
//                    )
//                ) {
//                    Text(
//                        text = "+",
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 17.sp,
//                    )
//                }
            }
        }
    }
}

fun Modifier.withClickable(isClickable: Boolean, onClick: () -> Unit): Modifier {
    return if (isClickable) {
        this.noRippleClickable { onClick() }
    } else {
        this
    }
}

@Preview(showSystemUi = true)
@Composable
fun ShoppingProductItemPreview() {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        ShoppingProductItem(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(paddingWithPercentage(percentage = 50))
                .background(Color.Black),
            orderItemUiModel = OrderItemUiModel(
                name = "Nike Air",
                price = 45f,
                photoUrl = "https://firebasestorage.googleapis.com/v0/b/nike-store-84533.appspot.com/o/image_2023-08-11_203915259-removebg-preview.png?alt=media&token=722b56e0-abb0-4be6-be08-6a2720d5601c",
                colorCode = "0xFF003E74",
                colorName = "Blue",
                size = 43,
                sex = Sex.MEN
            ),
            onPlusButtonClicked = {},
            onMiensButtonClicked = {},
        )
    }
}