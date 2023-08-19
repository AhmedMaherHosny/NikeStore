package com.example.ui.utils

import android.graphics.Color.parseColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
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

fun String.parseColor(): Color {
    require(length == 10 && startsWith("0x")) {
        "Invalid color code format: $this. Please use format 0xRRGGBBAA."
    }

    val alpha = substring(2, 4).toInt(16)
    val red = substring(4, 6).toInt(16)
    val green = substring(6, 8).toInt(16)
    val blue = substring(8, 10).toInt(16)

    return Color(alpha = alpha, red = red, green = green, blue = blue)
}
fun formatNumberWithKNotation(number: Float): String {
    if (number < 1000f){
        return number.toString()
    }
    val truncatedNumber = (number / 1000.0).toString()
    val decimalIndex = truncatedNumber.indexOf(".")

    return if (decimalIndex != -1) {
        truncatedNumber.substring(0, decimalIndex + 3)+"K"
    } else {
        truncatedNumber+"K"
    }
}







