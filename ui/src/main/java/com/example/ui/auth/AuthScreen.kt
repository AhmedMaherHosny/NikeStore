package com.example.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.core.StateErrorType
import com.example.presenter.auth.AuthEvent
import com.example.presenter.auth.viewmodels.AuthViewModel
import com.example.ui.R
import com.example.ui.auth.components.OrSeparate
import com.example.ui.auth.components.SocialMediaItem
import com.example.ui.auth.enums.AuthTaps
import com.example.ui.auth.taps.LoginTap
import com.example.ui.auth.taps.RegisterTap
import com.example.ui.common.components.ErrorDialog
import com.example.ui.common.components.LoadingAnimation
import com.example.ui.common.components.LostConnectionErrorDialog
import com.example.ui.theme.spacing
import com.example.ui.utils.modifyTap
import com.example.ui.utils.paddingWithPercentage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalComposeUiApi::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navigator: DestinationsNavigator? = null
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val loginViewState = viewModel.loginViewState
    val registerViewState = viewModel.registerViewState
    val eventError = viewModel.eventError
    var authErrorDialog by remember { mutableStateOf(false) }
    var lostConnectionErrorDialog by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = eventError) {
        eventError.collect { error ->
            when (error) {
                StateErrorType.AUTHENTICATION_FAILED -> {
                    authErrorDialog = true
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
    val isButtonEnabledWhileLogin by remember(
        loginViewState.email.text,
        loginViewState.password.text
    ) {
        derivedStateOf {
            loginViewState.email.isValid.value && loginViewState.password.isValid.value
        }
    }
    val isButtonEnabledWhileRegister by remember(
        registerViewState.email.text,
        registerViewState.password.text,
        registerViewState.phoneNumber.text
    ) {
        derivedStateOf {
            registerViewState.email.isValid.value
                    && registerViewState.password.isValid.value
                    && registerViewState.phoneNumber.isValid.value
        }
    }
    val onLoginClicked = remember {
        {
            keyboardController?.hide()
            viewModel.setEventClicks(
                AuthEvent.OnLoginClicked
            )
        }
    }
    val onRegisterClicked = remember {
        {
            keyboardController?.hide()
            viewModel.setEventClicks(
                AuthEvent.OnRegisterClicked
            )
        }
    }
    val onGoogleClicked = remember {
        {
            keyboardController?.hide()
            viewModel.setEventClicks(
                AuthEvent.OnGoogleClicked
            )
        }
    }
    val onFacebookClicked = remember {
        {
            keyboardController?.hide()
            viewModel.setEventClicks(
                AuthEvent.OnFacebookClicked
            )
        }
    }
    var currentTab by remember { mutableStateOf(AuthTaps.Login) }
    val nikeLogoPainter = rememberAsyncImagePainter(R.drawable.nike_logo)
    val authImagePainter = rememberAsyncImagePainter(R.drawable.auth_image)

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.3f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.32f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.04f))
            ) {
                Image(
                    modifier = Modifier
                        .padding(start = MaterialTheme.spacing.small)
                        .size(65.dp)
                        .align(TopStart),
                    painter = nikeLogoPainter,
                    contentDescription = "nike logo"
                )
                Image(
                    modifier = Modifier
                        .size(200.dp)
                        .align(BottomCenter),
                    painter = authImagePainter,
                    contentDescription = "auth image"
                )
            }
            TabRow(
                modifier = Modifier,
                selectedTabIndex = currentTab.ordinal,
                containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.04f),
                divider = {},
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[currentTab.ordinal])
                            .padding(horizontal = paddingWithPercentage(percentage = 10)),
                        height = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                AuthTaps.values().forEach { tab ->
                    Tab(
                        text = {
                            Text(
                                text = tab.name,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        selected = currentTab == tab,
                        onClick = { currentTab = tab },
                        selectedContentColor = MaterialTheme.colorScheme.secondary,
                        unselectedContentColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
                        modifier = Modifier
                            .padding(horizontal = paddingWithPercentage(percentage = 10))
                            .modifyTap(
                                isSelected = currentTab == tab,
                                color = MaterialTheme.colorScheme.background.copy(alpha = 0.3f)
                            )
                    )
                }
            }
            when (currentTab) {
                AuthTaps.Login -> LoginTap(
                    email = loginViewState.email,
                    password = loginViewState.password,
                    isButtonEnabled = isButtonEnabledWhileLogin,
                    onLoginClicked = onLoginClicked,
                )

                AuthTaps.Register -> RegisterTap(
                    username = registerViewState.username,
                    email = registerViewState.email,
                    password = registerViewState.password,
                    phoneNumber = registerViewState.phoneNumber,
                    isButtonEnabledWhileRegister,
                    onRegisterClicked = onRegisterClicked
                )
            }
            OrSeparate(
                modifier = Modifier
                    .padding(
                        horizontal = paddingWithPercentage(percentage = 10),
                        vertical = MaterialTheme.spacing.medium
                    )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = paddingWithPercentage(percentage = 10)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SocialMediaItem(
                    modifier = Modifier,
                    icon = R.drawable.google_ic,
                    text = "Google",
                    shape = RoundedCornerShape(10.dp)
                ) { onGoogleClicked() }
                SocialMediaItem(
                    modifier = Modifier,
                    icon = R.drawable.facebook_ic,
                    text = "Facebook",
                    shape = RoundedCornerShape(10.dp)
                ) { onFacebookClicked() }
            }
        }

    }
    if (authErrorDialog) {
        ErrorDialog {
            authErrorDialog = !authErrorDialog
        }
    }
    if (lostConnectionErrorDialog) {
        LostConnectionErrorDialog {
            lostConnectionErrorDialog = !lostConnectionErrorDialog
        }
    }

    if (loginViewState.isLoading || registerViewState.isLoading) {
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
                LoadingAnimation(modifier = Modifier
                    .size(150.dp)
                    .align(Center))
            }
        }
    }
}
