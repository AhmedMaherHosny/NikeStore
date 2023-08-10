package com.example.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.enums.ProductCategory
import com.example.core.enums.Sex
import com.example.core.enums.ShoeCategory
import com.example.presenter.home.viewmodels.HomeViewModel
import com.example.presenter.models.ProductColorSizeInformationUiModel
import com.example.presenter.models.ProductItemUiModel
import com.example.presenter.models.ProductSizeUiModel
import com.example.ui.R
import com.example.ui.home.components.ProductItem
import com.example.ui.theme.spacing
import com.example.ui.utils.noRippleClickable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigator: DestinationsNavigator? = null
) {
    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.tertiary.copy(0.05f))
            .fillMaxSize()
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
                active = active
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
                onSexItemClick(sex)
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
fun SearchToolBar(modifier: Modifier, text: MutableState<String>, active: MutableState<Boolean>) {
    val keyboardController = LocalSoftwareKeyboardController.current
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

fun onSexItemClick(selectedSex: Sex) {
    when (selectedSex) {
        Sex.MEN -> {

        }

        Sex.WOMEN -> {

        }

        Sex.KIDS -> {

        }
    }
}


