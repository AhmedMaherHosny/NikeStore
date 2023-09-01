package com.example.ui.saved_items

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.base.StateErrorType
import com.example.presenter.saved_items.SavedItemsEvent
import com.example.presenter.saved_items.viewmodels.SavedItemsViewModel
import com.example.ui.common.components.BackButton
import com.example.ui.destinations.ProductScreenDestination
import com.example.ui.home.components.ProductItem
import com.example.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.EmptyCoroutineContext

@Destination
@Composable
fun SavedItemsScreen(
    viewModel: SavedItemsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator? = null
) {
    val savedItemsViewState = viewModel.savedItemsViewState
    val context = LocalContext.current
    val eventError = viewModel.eventError
    var savedProductErrorDialog by remember { mutableStateOf(false) }
    var lostConnectionErrorDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = eventError) {
        eventError.collect { error ->
            when (error) {
                StateErrorType.AUTHENTICATION_FAILED -> {
                    savedProductErrorDialog = true
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
        viewModel.navigator.onEach { savedItemsNavigator ->
            viewModel.setNavigator { null }
            savedItemsNavigator.let {
                when (it) {

                    else -> {}
                }
            }
        }.flowOn(EmptyCoroutineContext).launchIn(this)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary.copy(0.05f))
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
            text = "Saved Items",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 27.sp,
            modifier = Modifier
                .padding(start = MaterialTheme.spacing.medium, bottom = MaterialTheme.spacing.large)
        )
        Divider(
            color = MaterialTheme.colorScheme.tertiary.copy(0.2f),
            modifier = Modifier.padding(bottom = MaterialTheme.spacing.medium)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(count = 2),
            modifier = Modifier
                .padding(horizontal = MaterialTheme.spacing.medium)
        ) {
            itemsIndexed(savedItemsViewState.productsItemUiModel) { index, productItem ->
                val endPadding = if ((index + 1) % 2 != 0) {
                    MaterialTheme.spacing.medium
                } else {
                    0.dp
                }
                ProductItem(
                    modifier = Modifier
                        .padding(end = endPadding, bottom = MaterialTheme.spacing.medium),
                    productItem = productItem,
                    isInSavedScreen = true,
                    onSavedIconClick = {
                        Toast.makeText(
                            context,
                            "Item removed from watch later list successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.setEventClicks(
                            SavedItemsEvent.OnSavedIconClicked(it)
                        )
                    },
                    onShoppingIconClick = {},
                    onItemClick = {
                        navigator?.navigate(ProductScreenDestination(it))
                    }
                )
            }
        }
    }
}