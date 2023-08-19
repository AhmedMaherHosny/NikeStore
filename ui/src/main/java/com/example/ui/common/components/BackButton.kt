package com.example.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BackButton(
    modifier: Modifier,
    iconTint: Color,
    elevation: Dp,
    onClick: () -> Unit,
) {

    Surface(
        modifier = Modifier.size(40.dp),
        shape = CircleShape,
        shadowElevation = elevation,
    ) {
        IconButton(
            onClick = onClick,
            modifier = modifier,
            interactionSource = MutableInteractionSource()
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(30.dp)
            )
        }
    }


}

@Preview(showSystemUi = true)
@Composable
fun BackButtonPreview() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.tertiary.copy(0.05f))
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        BackButton(
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = CircleShape
                ),
            iconTint = MaterialTheme.colorScheme.secondary,
            elevation = 3.dp
        ) {}
    }
}