package com.example.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.ui.R
import com.example.ui.theme.spacing
import com.example.ui.utils.noRippleClickable

@Composable
fun LostConnectionErrorDialog(
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Box(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.background,
                    RoundedCornerShape(10.dp)
                )
                .verticalScroll(rememberScrollState())
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "dismiss dialog",
                modifier = Modifier
                    .padding(10.dp)
                    .noRippleClickable { onDismissRequest() }
                    .align(Alignment.TopEnd),
                tint = MaterialTheme.colorScheme.secondary
            )
            Column(
                modifier = Modifier
                    .padding(
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium,
                        bottom = MaterialTheme.spacing.medium
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                LostConnectionAnimationImage(
                    modifier = Modifier
                        .size(200.dp)
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                Text(
                    text = "Network error!",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.semiLarge))
                Text(
                    text = "No internet connection, you're offline.",
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                CustomButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "I'll check it",
                    shape = RoundedCornerShape(10.dp)
                ) { onDismissRequest() }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.semiLarge))
            }
        }
    }
}

@Composable
fun LostConnectionAnimationImage(modifier: Modifier) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.lost_connection_animation))
    val progress by animateLottieCompositionAsState(composition = composition)

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}

