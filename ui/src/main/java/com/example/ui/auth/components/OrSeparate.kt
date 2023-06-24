package com.example.ui.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ui.theme.spacing

@Composable
fun OrSeparate(modifier: Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f))
        )
        Text(
            text = "Or",
            modifier = Modifier
                .padding(horizontal = MaterialTheme.spacing.large),
            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)
        )
        Divider(
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f))
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun OrSeparatePreview() {
    OrSeparate(Modifier.fillMaxWidth())
}