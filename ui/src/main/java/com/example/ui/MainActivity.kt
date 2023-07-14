package com.example.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.presenter.auth.google_auth.GoogleAuthUiClient
import com.example.presenter.main_activity.viewmodels.MainActivityViewModel
import com.example.presenter.utils.appUserUiModel
import com.example.ui.destinations.AuthScreenDestination
import com.example.ui.destinations.HomeScreenDestination
import com.example.ui.theme.NikeStoreTheme
import com.google.android.gms.auth.api.identity.Identity
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NikeStoreTheme {
                val isInitialized by remember { viewModel.isInitialized }
                LaunchedEffect(true) {
                    viewModel.setAppUserUiModel()
                }
                if (isInitialized) {
                    val startDestination =
                        if (appUserUiModel == null) AuthScreenDestination else HomeScreenDestination
                    val navGraph = NavGraph(
                        route = NavGraphs.root.route,
                        startRoute = startDestination,
                        destinations = NavGraphs.root.destinations
                    )
                    DestinationsNavHost(
                        modifier = Modifier
                            .statusBarsPadding()
                            .systemBarsPadding(),
                        navGraph = navGraph
                    )
                }
            }
        }
    }
}
