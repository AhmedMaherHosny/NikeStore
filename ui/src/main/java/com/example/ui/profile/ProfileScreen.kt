package com.example.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.base.StateErrorType
import com.example.presenter.profile.ProfileEvent
import com.example.presenter.profile.viewmodels.ProfileViewModel
import com.example.ui.R
import com.example.ui.common.components.BackButton
import com.example.ui.common.components.CustomButton
import com.example.ui.common.components.CustomTextField
import com.example.ui.common.components.ErrorDialog
import com.example.ui.common.components.LoadingAnimation
import com.example.ui.common.components.LostConnectionErrorDialog
import com.example.ui.theme.spacing
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigator: DestinationsNavigator? = null
) {
    val profileViewState = viewModel.profileViewState
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
            text = "Profile",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 27.sp,
            modifier = Modifier
                .padding(start = MaterialTheme.spacing.medium, bottom = MaterialTheme.spacing.large)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.medium)
        ) {
            CustomTextField(
                leadingIcon = R.drawable.username_ic,
                placeholder = "Username",
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                shape = RoundedCornerShape(10.dp),
                inputWrapper = profileViewState.username,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            CustomTextField(
                leadingIcon = R.drawable.email_ic,
                placeholder = "E-mail",
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                shape = RoundedCornerShape(10.dp),
                inputWrapper = profileViewState.email
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            CustomTextField(
                leadingIcon = R.drawable.password_ic,
                placeholder = "Password",
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
                shape = RoundedCornerShape(10.dp),
                inputWrapper = profileViewState.password,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            CustomTextField(
                leadingIcon = R.drawable.call_ic,
                placeholder = "Country code and phone",
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done,
                shape = RoundedCornerShape(10.dp),
                inputWrapper = profileViewState.phoneNumber,
                onDone = {
                    println("a7a")
                    viewModel.setEventClicks(
                        ProfileEvent.OnSaveClicked
                    )
                    navigator?.popBackStack()
                }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                text = "Save",
                shape = RectangleShape
            ) {
                viewModel.setEventClicks(
                    ProfileEvent.OnSaveClicked
                )
                navigator?.popBackStack()
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
    if (profileViewState.isLoading) {
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
