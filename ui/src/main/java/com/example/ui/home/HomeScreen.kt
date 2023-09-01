package com.example.ui.home

import android.widget.Toast
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.navOptions
import coil.compose.AsyncImage
import com.example.core.base.StateErrorType
import com.example.core.enums.Sex
import com.example.presenter.home.HomeEvent
import com.example.presenter.home.HomeNavigator
import com.example.presenter.home.viewmodels.HomeViewModel
import com.example.presenter.models.OrderItemUiModel
import com.example.presenter.utils.appUserUiModel
import com.example.ui.R
import com.example.ui.common.components.ErrorDialog
import com.example.ui.common.components.LoadingAnimation
import com.example.ui.common.components.LostConnectionErrorDialog
import com.example.ui.destinations.AuthScreenDestination
import com.example.ui.destinations.HomeScreenDestination
import com.example.ui.destinations.OrderPlacedScreenDestination
import com.example.ui.destinations.ProductScreenDestination
import com.example.ui.destinations.ProfileScreenDestination
import com.example.ui.destinations.SavedItemsScreenDestination
import com.example.ui.destinations.ShoppingBagScreenDestination
import com.example.ui.home.components.ProductItem
import com.example.ui.theme.spacing
import com.example.ui.utils.noRippleClickable
import com.example.ui.utils.paddingWithPercentage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.math.roundToInt

@OptIn(ExperimentalComposeUiApi::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigator: DestinationsNavigator? = null,
    resultRecipient: ResultRecipient<OrderPlacedScreenDestination, Boolean>

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

    var currentState = remember { mutableStateOf(MenuState.COLLAPSED) }
    val updateAnim = updateTransition(currentState.value, label = "MenuState")
    val scale = updateAnim.animateFloat(
        transitionSpec = {
            when {
                MenuState.EXPANDED isTransitioningTo MenuState.COLLAPSED -> {
                    tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                }

                MenuState.COLLAPSED isTransitioningTo MenuState.EXPANDED -> {
                    tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                }

                else -> {
                    snap()
                }
            }
        }, label = ""
    ) {
        when (it) {
            MenuState.EXPANDED -> 0.7f
            MenuState.COLLAPSED -> 1f
        }
    }
    val transitionOffset = updateAnim.animateOffset({
        when {
            MenuState.EXPANDED isTransitioningTo MenuState.COLLAPSED -> {
                tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            }

            MenuState.COLLAPSED isTransitioningTo MenuState.EXPANDED -> {
                tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            }

            else -> {
                snap()
            }
        }
    }, label = "") {
        when (it) {
            MenuState.EXPANDED -> Offset(750f, 60f)
            MenuState.COLLAPSED -> Offset(0f, 0f)
        }
    }
    val alphaMenu = updateAnim.animateFloat({
        when {
            MenuState.EXPANDED isTransitioningTo MenuState.COLLAPSED -> {
                tween(durationMillis = 300)
            }

            MenuState.COLLAPSED isTransitioningTo MenuState.EXPANDED -> {
                tween(durationMillis = 300)
            }

            else -> {
                snap()
            }
        }
    }, label = "") {
        when (it) {
            MenuState.EXPANDED -> 1f
            MenuState.COLLAPSED -> 0.5f
        }
    }
    val roundness = updateAnim.animateDp({
        when {
            MenuState.EXPANDED isTransitioningTo MenuState.COLLAPSED -> {
                tween(durationMillis = 300)
            }

            MenuState.COLLAPSED isTransitioningTo MenuState.EXPANDED -> {
                tween(durationMillis = 300)
            }

            else -> {
                snap()
            }
        }
    }, label = "") {
        when (it) {
            MenuState.EXPANDED -> 20.dp
            MenuState.COLLAPSED -> 0.dp
        }
    }
    val menuOffset = updateAnim.animateOffset({
        when {
            MenuState.EXPANDED isTransitioningTo MenuState.COLLAPSED -> {
                tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            }

            MenuState.COLLAPSED isTransitioningTo MenuState.EXPANDED -> {
                tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            }

            else -> {
                snap()
            }
        }
    }, label = "") {
        when (it) {
            MenuState.EXPANDED -> Offset(0f, 0f)
            MenuState.COLLAPSED -> Offset(-100f, 0f)
        }
    }

    resultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {

            }

            is NavResult.Value -> {
                onSexFilterClicked(Sex.MEN)
            }
        }
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

                    HomeNavigator.NavigateToAuthScreen -> {
                        navigator?.navigate(AuthScreenDestination, navOptions = navOptions {
                            popUpTo(HomeScreenDestination) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        })
                    }
                }
            }
        }.flowOn(EmptyCoroutineContext).launchIn(this)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        //side menu
        MenuComponent(
            Modifier
                .offset {
                    IntOffset(
                        menuOffset.value.x.roundToInt(),
                        menuOffset.value.y.roundToInt()
                    )
                }
                .alpha(alphaMenu.value),
        ) {
            when (it) {
                is HomeMenuAction.MenuSelected -> {
                    when (it.menu) {
                        HomeMenu.PROFILE -> {
                            navigator?.navigate(ProfileScreenDestination)
                        }

                        HomeMenu.LOGOUT -> {
                            viewModel.setEventClicks(
                                HomeEvent.OnLogoutClicked
                            )
                        }
                    }
                }

                else -> {
                    currentState.value = MenuState.COLLAPSED
                }
            }
            currentState.value = MenuState.COLLAPSED
        }
        // stack layer 0
        Box(
            modifier = Modifier
                .fillMaxSize()
                .scale(scale.value - 0.05f)
                .offset {
                    IntOffset(
                        transitionOffset.value.x.toInt() - 50,
                        transitionOffset.value.y.toInt()
                    )
                }
                .background(
                    Color(0xFFF3F6FA).copy(alpha = .90f),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(8.dp)
                .alpha(alphaMenu.value)
        )
        //stack layer 1
        Box(
            modifier = Modifier
                .fillMaxSize()
                .scale(scale.value - 0.08f)
                .offset {
                    IntOffset(
                        transitionOffset.value.x.toInt() - 100,
                        transitionOffset.value.y.toInt()
                    )
                }
                .background(Color(0xFFF3F6FA).copy(.5f), shape = RoundedCornerShape(20.dp))
                .padding(8.dp)
                .alpha(alphaMenu.value)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .scale(scale.value)
                .offset {
                    IntOffset(
                        transitionOffset.value.x.toInt(),
                        transitionOffset.value.y.toInt()
                    )
                }
                .clip(shape = RoundedCornerShape(roundness.value))
                .background(Color.White)
                .background(MaterialTheme.colorScheme.tertiary.copy(0.05f)),
        ) {
            item {
                var text = remember { mutableStateOf("") }
                var active = remember { mutableStateOf(false) }
                ToolBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = MaterialTheme.spacing.medium,
                            vertical = MaterialTheme.spacing.default
                        ),
                    currentState,
                    onSavedIconClicked = {
                        navigator?.navigate(SavedItemsScreenDestination)
                    },
                    onShoppingBagClicked = {
                        navigator?.navigate(ShoppingBagScreenDestination())
                    }
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
                            .padding(vertical = paddingWithPercentage(percentage = 50))
                            .alpha(0.5f)
                            .fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(R.drawable.nike_logo),
                            contentDescription = "Logo",
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
                                        isInSavedScreen = false,
                                        onSavedIconClick = {
                                            if (!it.isSaved) {
                                                Toast.makeText(
                                                    context,
                                                    "Item added to watch later list successfully",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                viewModel.setEventClicks(
                                                    HomeEvent.OnSavedIconClicked(false, it.id!!)
                                                )
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Item removed from watch later list successfully",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                viewModel.setEventClicks(
                                                    HomeEvent.OnSavedIconClicked(true, it.id!!)
                                                )
                                            }
                                        },
                                        onShoppingIconClick = {
                                            val orderItem = OrderItemUiModel(
                                                productId = it.id,
                                                name = it.name,
                                                price = it.price,
                                                photoUrl = it.colorSizeInformation.first().photoUrl,
                                                colorCode = it.colorSizeInformation.first().colorCode,
                                                quantity = 1,
                                                colorName = it.colorSizeInformation.first().colorName,
                                                size = it.colorSizeInformation.first().sizeUiModel.first().size!!.toInt(),
                                                sex = it.sex
                                            )
                                            if (!it.isInShoppingBag) {
                                                Toast.makeText(
                                                    context,
                                                    "Item added to shopping list successfully",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                viewModel.setEventClicks(
                                                    HomeEvent.OnShoppingIconClicked(
                                                        false,
                                                        orderItem
                                                    )
                                                )
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Item removed from shopping list successfully",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                viewModel.setEventClicks(
                                                    HomeEvent.OnShoppingIconClicked(true, orderItem)
                                                )
                                            }
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
fun ToolBar(
    modifier: Modifier,
    currentState: MutableState<MenuState>,
    onSavedIconClicked: () -> Unit,
    onShoppingBagClicked: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.menu_icon),
            contentDescription = "menu",
            modifier = Modifier
                .noRippleClickable {
                    currentState.value = when (currentState.value) {
                        MenuState.EXPANDED -> MenuState.COLLAPSED
                        MenuState.COLLAPSED -> MenuState.EXPANDED
                    }
                }
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
                modifier = Modifier
                    .size(20.dp)
                    .noRippleClickable {
                        onSavedIconClicked()
                    }
            )
            Icon(
                painter = painterResource(id = R.drawable.shopping_bag),
                contentDescription = "shopping bag icon",
                modifier = Modifier
                    .size(MaterialTheme.spacing.semiLarge)
                    .noRippleClickable {
                        onShoppingBagClicked()
                    }
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

@Composable
fun MenuComponent(modifier: Modifier, menuAction: (HomeMenuAction) -> Unit) {

    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.Center) {

        Spacer(modifier = Modifier.height(40.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = appUserUiModel?.avatar,
                contentDescription = "profile pic",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(shape = CircleShape)
            )

            Text(
                text = appUserUiModel?.name ?: "",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(40.dp))


        LazyColumn {

            items(HomeMenu.values()) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 10.dp, end = 16.dp, top = 26.dp, bottom = 16.dp)
                        .clickable {
                            menuAction(HomeMenuAction.MenuSelected(it))
                        }
                ) {
                    Icon(
                        painter = painterResource(id = it.icon),
                        contentDescription = it.title,
                        tint = MaterialTheme.colorScheme.background,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = it.title,
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 16.dp),
                        fontWeight = FontWeight.Medium
                    )
                }

            }
        }

    }

}



