package com.example.ui.auth.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.ui.R
import com.example.ui.theme.spacing

@Composable
fun SocialMediaItem(
    modifier: Modifier,
    icon: Int,
    text: String,
    shape: Shape,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.wrapContentSize(),
        onClick = { onClick() },
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.spacing.semiLarge,
            vertical = MaterialTheme.spacing.medium
        ),
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "Favorite",
            modifier = Modifier
                .size(MaterialTheme.spacing.medium),
            tint = Color.Unspecified
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.85f),
            fontSize = 15.sp
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun SocialMediaItemPreview() {
    SocialMediaItem(modifier = Modifier, R.drawable.google_ic, "Google", RectangleShape) {}
}