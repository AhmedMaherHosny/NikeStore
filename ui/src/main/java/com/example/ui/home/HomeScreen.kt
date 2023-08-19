package com.example.ui.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.base.StateErrorType
import com.example.core.enums.Sex
import com.example.presenter.home.HomeEvent
import com.example.presenter.home.HomeNavigator
import com.example.presenter.home.viewmodels.HomeViewModel
import com.example.ui.R
import com.example.ui.common.components.ErrorDialog
import com.example.ui.common.components.LoadingAnimation
import com.example.ui.common.components.LostConnectionErrorDialog
import com.example.ui.destinations.ProductScreenDestination
import com.example.ui.home.components.ProductItem
import com.example.ui.theme.spacing
import com.example.ui.utils.noRippleClickable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.EmptyCoroutineContext

@OptIn(ExperimentalComposeUiApi::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigator: DestinationsNavigator? = null
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var homeErrorDialog by remember { mutableStateOf(false) }
    var lostConnectionErrorDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val homeViewSate = viewModel.homeViewState
    val eventError = viewModel.eventError
    val onSexFilterClicked = { sex: Sex ->
        keyboardController?.hide()
        viewModel.setEventClicks(
            HomeEvent.OnSexFilterClicked(sex)
        )
    }

    LaunchedEffect(key1 = eventError) {
        eventError.collect { error ->
            when (error) {
                StateErrorType.AUTHENTICATION_FAILED -> {
                    homeErrorDialog = true
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
        viewModel.navigator.onEach { homeNavigator ->
            viewModel.setNavigator { null }
            homeNavigator.let {
                when (it) {
                    is HomeNavigator.NavigateToProductScreen -> {

                    }
                }
            }
        }.flowOn(EmptyCoroutineContext).launchIn(this)
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.tertiary.copy(0.05f))
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        var text = remember { mutableStateOf("") }
        var active = remember { mutableStateOf(false) }
        ToolBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MaterialTheme.spacing.medium,
                    vertical = MaterialTheme.spacing.default
                )
        )
        Text(
            text = "Discover your",
            color = MaterialTheme.colorScheme.tertiary.copy(0.6f),
            fontSize = 24.sp,
            modifier = Modifier.padding(
                top = MaterialTheme.spacing.medium,
                start = MaterialTheme.spacing.medium
            )
        )
        Text(
            text = "Favourite footwear",
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                top = MaterialTheme.spacing.default,
                start = MaterialTheme.spacing.medium,
                bottom = MaterialTheme.spacing.medium
            )
        )
        SearchToolBar(
            modifier = if (active.value) {
                Modifier
                    .fillMaxSize()
            } else {
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium)
            },
            text = text,
            active = active,
            keyboardController
        )
        SexSelector(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium,
                    top = MaterialTheme.spacing.large,
                    bottom = 3.dp
                )
        ) { sex ->
            onSexFilterClicked(sex)
        }
        /*
        * seeding the database
        */
//            ButtonToSeedTheDbForMen()
//            ButtonToSeedTheDbForWomen()
        /*
         * seeding the database
         */
        if (homeViewSate.productsGroupedByCategory.values.flatten().isEmpty()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .alpha(0.5f)
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(R.drawable.nike_logo),
                    contentDescription = "Logo"
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "No items yet!",
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        } else {
            homeViewSate.productsGroupedByCategory.forEach { (categorySubCategoryPair, products) ->
                val (category, subCategory) = categorySubCategoryPair
                Column(
                    modifier = Modifier
                        .padding(bottom = MaterialTheme.spacing.medium)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.spacing.medium),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = subCategory.toString(),
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "See all",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                    LazyRow(
                        modifier = Modifier
                            .padding(horizontal = MaterialTheme.spacing.medium),
                    ) {
                        itemsIndexed(products) { index, product ->
                            val paddingEnd =
                                if (index < products.size - 1) MaterialTheme.spacing.medium else 0.dp
                            ProductItem(
                                modifier = Modifier
                                    .padding(end = paddingEnd),
                                productItem = product,
                                onSavedIconClick = {
                                    Toast.makeText(
                                        context,
                                        "Item added to watch later list successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onShoppingIconClick = {
                                    Toast.makeText(
                                        context,
                                        "Item added to shopping card successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onItemClick = {
                                    navigator?.navigate(ProductScreenDestination(it))
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (homeErrorDialog) {
        ErrorDialog {
            homeErrorDialog = !homeErrorDialog
        }
    }
    if (lostConnectionErrorDialog) {
        LostConnectionErrorDialog {
            lostConnectionErrorDialog = !lostConnectionErrorDialog
        }
    }
    if (homeViewSate.isLoading) {
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

@Composable
fun ToolBar(modifier: Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.menu_icon),
            contentDescription = "menu",
            modifier = Modifier
                .noRippleClickable { /* todo : menu item here */ }
                .size(MaterialTheme.spacing.semiLarge)
        )
        Icon(
            painter = painterResource(id = R.drawable.nike_logo),
            contentDescription = "nike logo",
            modifier = Modifier.size(50.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.saved_items_not_filled),
                contentDescription = "saved item icon",
                modifier = Modifier.size(20.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.shopping_bag),
                contentDescription = "shopping bag icon",
                modifier = Modifier.size(MaterialTheme.spacing.semiLarge)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchToolBar(
    modifier: Modifier,
    text: MutableState<String>,
    active: MutableState<Boolean>,
    keyboardController: SoftwareKeyboardController?
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()
    var isSearchBarFocused by remember { mutableStateOf(false) }

    SearchBar(
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged {
                isSearchBarFocused = it.isFocused
                if (!isSearchBarFocused) {
                    focusRequester.freeFocus()
                }
            },
        query = text.value,
        onQueryChange = { text.value = it },
        onSearch = {},
        onActiveChange = {},
        active = active.value,
        placeholder = {
            Text(
                text = "Search shoes...",
                color = MaterialTheme.colorScheme.tertiary.copy(0.4f)
            )
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary.copy(0.4f)
            )
        },
        trailingIcon = {
            if (isSearchBarFocused) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary.copy(0.4f),
                    modifier = Modifier.noRippleClickable {
                        if (text.value.isNotEmpty()) {
                            text.value = ""
                        } else {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    }
                )
            }
        },
        shape = RoundedCornerShape(8.dp),
        colors = SearchBarDefaults.colors(MaterialTheme.colorScheme.background)
    ) {}
}

@Composable
fun SexSelector(modifier: Modifier, onSexItemClick: (Sex) -> Unit) {
    var selectedSex by remember { mutableStateOf(Sex.MEN) }
    Column(
        Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Sex.values().forEach { sex ->
                val isSelected = sex == selectedSex
                Text(
                    text = sex.toString(),
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary.copy(
                        0.6f
                    ),
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = if (isSelected) 17.sp else 16.sp,
                    modifier = Modifier
                        .noRippleClickable {
                            selectedSex = sex
                            onSexItemClick(selectedSex)
                        }
                )
            }
        }
        Divider(color = MaterialTheme.colorScheme.tertiary.copy(0.1f))

    }
}



