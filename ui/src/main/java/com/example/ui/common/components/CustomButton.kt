package com.example.ui.common.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.ui.theme.spacing

@Composable
fun CustomButton(
    modifier: Modifier,
    text: String,
    shape: Shape,
    isButtonEnabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = { onClick() },
        shape = shape,
        contentPadding = PaddingValues(
            vertical = MaterialTheme.spacing.medium
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        enabled = isButtonEnabled
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.background,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun CustomButtonPreview() {
    CustomButton(Modifier, "Pay 65$", RectangleShape) {}
}