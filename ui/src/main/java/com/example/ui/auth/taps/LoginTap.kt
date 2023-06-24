package com.example.ui.auth.taps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.core.validation.InputWrapper
import com.example.ui.R
import com.example.ui.common.components.CustomButton
import com.example.ui.common.components.CustomTextField
import com.example.ui.theme.spacing
import com.example.ui.utils.paddingWithPercentage

@Composable
fun LoginTap(
    email: InputWrapper,
    password: InputWrapper,
    isButtonEnabled: Boolean,
    onLoginClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = paddingWithPercentage(percentage = 10))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.3f)),
    ) {
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        CustomTextField(
            leadingIcon = R.drawable.email_ic,
            placeholder = "E-mail",
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            shape = RoundedCornerShape(10.dp),
            inputWrapper = email,
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        CustomTextField(
            leadingIcon = R.drawable.password_ic,
            placeholder = "Password",
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            shape = RoundedCornerShape(10.dp),
            inputWrapper = password,
            onDone = {
                if (isButtonEnabled)
                    onLoginClicked()
            }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        CustomButton(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            text = "Login",
            shape = RoundedCornerShape(10.dp),
            isButtonEnabled = isButtonEnabled
        ) {
            onLoginClicked()
        }
    }
}