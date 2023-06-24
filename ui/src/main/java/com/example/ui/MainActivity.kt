package com.example.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import com.example.ui.auth.NavGraphs
import com.example.ui.theme.NikeStoreTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NikeStoreTheme {
                DestinationsNavHost(
                    modifier = Modifier
                        .statusBarsPadding()
                        .systemBarsPadding(),
                    navGraph = NavGraphs.root
                )

            }
        }
    }
}