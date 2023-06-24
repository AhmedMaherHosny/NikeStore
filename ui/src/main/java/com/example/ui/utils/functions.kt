package com.example.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun paddingWithPercentage(percentage: Int): Dp {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val paddingAmount = with(LocalDensity.current) {
        (screenWidth * percentage / 100)
    }
    return paddingAmount
}

fun extractCountryCode(fullNumber: String, phoneNumber: String): String {
    return if (fullNumber.startsWith(phoneNumber)) {
        fullNumber.substring(0, fullNumber.length - phoneNumber.length)
    } else {
        fullNumber.substring(0, fullNumber.indexOf(phoneNumber))
    }
}







