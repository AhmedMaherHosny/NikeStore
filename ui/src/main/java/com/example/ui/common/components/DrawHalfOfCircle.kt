package com.example.ui.common.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DrawHalfOfCircle(modifier: Modifier, color: Color) {
    Canvas(
        modifier = modifier
    ) {
        val radius = size.minDimension / 2
        val startAngle = 270f
        val sweepAngle = 360 * 0.5f

        drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = true,
            topLeft = Offset(-radius, 0f),
            size = Size(radius * 2, radius * 2)
        )
    }
}


@Preview(showSystemUi = true)
@Composable
fun DrawHalfOfCirclePreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .background(Color.Black)
        ) {
            DrawHalfOfCircle(
                modifier = Modifier
                    .fillMaxSize(0.9f)
                    .align(Alignment.CenterStart)
                    .background(Color.Black),
                color = Color.Red
            )
        }
    }
}