package com.example.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.example.core.enums.ProductCategory
import com.example.core.enums.Sex
import com.example.core.enums.ShoeCategory
import com.example.presenter.models.ProductColorSizeInformationUiModel
import com.example.presenter.models.ProductItemUiModel
import com.example.presenter.models.ProductSizeUiModel
import com.example.ui.R
import com.example.ui.theme.spacing
import com.example.ui.utils.noRippleClickable

@Composable
fun ProductItem(
    modifier: Modifier,
    productItem: ProductItemUiModel,
    isInSavedScreen: Boolean,
    onSavedIconClick: (ProductItemUiModel) -> Unit,
    onShoppingIconClick: (ProductItemUiModel) -> Unit,
    onItemClick: (id: String) -> Unit
) {
    var isSavedIconClicked by remember { mutableStateOf(productItem.isSaved) }
    ConstraintLayout(
        modifier = modifier
            .wrapContentSize()
            .background(
                color = MaterialTheme.colorScheme.primary.copy(0.1f),
                shape = RoundedCornerShape(10.dp)
            )
            .noRippleClickable { onItemClick(productItem.id!!) }
    ) {
        val (savedIcon, productImage, productName, productPrice, shoppingBox) = createRefs()

        Icon(
            painter = if (isSavedIconClicked) painterResource(id = R.drawable.saved_items_filled) else painterResource(
                id = R.drawable.saved_items_not_filled
            ),
            contentDescription = "saved icon",
            modifier = Modifier
                .padding(MaterialTheme.spacing.medium)
                .constrainAs(savedIcon) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .noRippleClickable {
                    onSavedIconClick(productItem)
                    isSavedIconClicked = !isSavedIconClicked
                    productItem.isSaved = !productItem.isSaved
                }
        )
        AsyncImage(
            model = productItem.colorSizeInformation.first().photoUrl,
            contentDescription = "product image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .rotate(20f)
                .size(height = 120.dp, width = 210.dp)
                .padding(MaterialTheme.spacing.medium)
                .constrainAs(productImage) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
        Text(
            text = productItem.name!!,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.tertiary.copy(0.8f),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier
                .padding(MaterialTheme.spacing.medium)
                .constrainAs(productName) {
                    top.linkTo(productImage.bottom)
                    start.linkTo(parent.start)
                }
        )
        Text(
            text = "$${productItem.price}",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .padding(
                    start = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium,
                    bottom = MaterialTheme.spacing.medium
                )
                .constrainAs(productPrice) {
                    top.linkTo(productName.bottom)
                    start.linkTo(parent.start)
                }
        )
        if (!isInSavedScreen) {
            Box(
                modifier = Modifier
                    .constrainAs(shoppingBox) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .noRippleClickable {
                        onShoppingIconClick(productItem)
                        productItem.isInShoppingBag = !productItem.isInShoppingBag
                    }
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(topStart = 10.dp, bottomEnd = 10.dp)
                    )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.shopping_bag),
                    contentDescription = "shopping icon",
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(MaterialTheme.spacing.medium)
                )
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun ProductItemPreview() {
    ProductItem(
        modifier = Modifier.padding(top = 15.dp),
        productItem = ProductItemUiModel(
            id = "testid",
            name = "Nike Runner",
            description = "",
            price = 34f,
            isSaved = false,
            peopleRatedCounter = 0,
            rate = 0,
            sex = Sex.MEN,
            productCategory = ProductCategory.SHOE,
            subCategory = ShoeCategory.RUNNING,
            colorSizeInformation = listOf(
                ProductColorSizeInformationUiModel(
                    colorName = "Red",
                    colorCode = "0xFF0000",
                    photoUrl = "https://firebasestorage.googleapis.com/v0/b/nike-store-84533.appspot.com/o/red_shoe.png?alt=media&token=85b11dc8-c169-446b-878d-2ab19769de1a",
                    sizeUiModel = listOf(
                        ProductSizeUiModel(
                            size = 40f,
                            availablePieces = 1
                        )
                    )
                )
            )
        ),
        isInSavedScreen = false,
        onSavedIconClick = {},
        onShoppingIconClick = {},
        onItemClick = {}
    )
}