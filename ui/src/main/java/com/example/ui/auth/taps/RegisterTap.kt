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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.core.validation.InputWrapper
import com.example.ui.R
import com.example.ui.common.components.CustomButton
import com.example.ui.common.components.CustomTextField
import com.example.ui.theme.spacing
import com.example.ui.utils.paddingWithPercentage
import com.togitech.ccp.component.TogiCountryCodePicker

@Composable
fun RegisterTap(
    username: InputWrapper,
    email: InputWrapper,
    password: InputWrapper,
    phoneNumber : InputWrapper,
    isButtonEnabled: Boolean,
    onRegisterClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = paddingWithPercentage(percentage = 10))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.3f)),
    ) {
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        CustomTextField(
            leadingIcon = R.drawable.username_ic,
            placeholder = "Username",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            shape = RoundedCornerShape(10.dp),
            inputWrapper = username
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        CustomTextField(
            leadingIcon = R.drawable.email_ic,
            placeholder = "E-mail",
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            shape = RoundedCornerShape(10.dp),
            inputWrapper = email
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        CustomTextField(
            leadingIcon = R.drawable.password_ic,
            placeholder = "Password",
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next,
            shape = RoundedCornerShape(10.dp),
            inputWrapper = password,
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        CustomTextField(
            leadingIcon = R.drawable.call_ic,
            placeholder = "Country code and phone",
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Done,
            shape = RoundedCornerShape(10.dp),
            inputWrapper = phoneNumber,
            onDone = onRegisterClicked
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        CustomButton(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            text = "Register",
            shape = RoundedCornerShape(10.dp),
            isButtonEnabled = isButtonEnabled
        ) { onRegisterClicked() }
    }

}