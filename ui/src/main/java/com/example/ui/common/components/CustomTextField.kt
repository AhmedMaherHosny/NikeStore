package com.example.ui.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.validation.InputWrapper
import com.example.core.validation.ValidationType
import com.example.ui.R

@Composable
fun CustomTextField(
    leadingIcon: Int? = null,
    placeholder: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    shape: Shape,
    inputWrapper: InputWrapper,
    onDone: (() -> Unit)? = null,
    onSearch: (() -> Unit)? = null,
    onSend: (() -> Unit)? = null,
    onGo: (() -> Unit)? = null,
) {
    val focusManager = LocalFocusManager.current
    val text by remember { inputWrapper.text }
    var passwordVisible by remember { mutableStateOf(inputWrapper.validationType != ValidationType.PASSWORD) }

    val visualTransformation by remember {
        derivedStateOf {
            if (inputWrapper.validationType == ValidationType.PASSWORD && !passwordVisible)
                PasswordVisualTransformation()
            else VisualTransformation.None
        }
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(shape),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = text,
                onValueChange = { inputWrapper.onValueChange(it) },
                shape = shape,
                leadingIcon = {
                    if (inputWrapper.validationType != ValidationType.NONE) {
                        Icon(
                            painter = painterResource(id = leadingIcon!!),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)
                        )
                    }
                },
                placeholder = {
                    Text(
                        text = placeholder,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    cursorColor = MaterialTheme.colorScheme.secondary,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedIndicatorColor = Color.Unspecified,
                    unfocusedIndicatorColor = Color.Unspecified,
                    errorContainerColor = MaterialTheme.colorScheme.error,
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = imeAction
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    },
                    onDone = {
                        onDone?.invoke()
                        focusManager.clearFocus()
                    },
                    onSearch = {
                        onSearch?.invoke()
                        focusManager.clearFocus()
                    },
                    onSend = {
                        onSend?.invoke()
                        KeyboardActions.Default.onSend?.let { it() }
                    },
                    onGo = {
                        onGo?.invoke()
                        focusManager.clearFocus()
                    }
                ),
                trailingIcon = {
                    if (inputWrapper.validationType == ValidationType.PASSWORD) {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                painter = if (passwordVisible) painterResource(id = R.drawable.hide_password_ic) else painterResource(
                                    id = R.drawable.show_password_ic
                                ),
                                contentDescription = if (passwordVisible) "Show Password" else "Hide Password",
                                tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)
                            )
                        }
                    }
                },
                visualTransformation = visualTransformation
            )
        }
        AnimatedVisibility(
            visible = inputWrapper.isValid.value.not(),
            enter = scaleIn() + expandVertically(expandFrom = Alignment.CenterVertically),
            exit = scaleOut() + shrinkVertically(shrinkTowards = Alignment.CenterVertically)
        ) {
            Text(
                text = stringResource(id = inputWrapper.validationMessageResId),
                color = Color.Red,
                fontSize = 11.sp,
                modifier = Modifier
                    .padding(top = 2.dp)
            )
        }
    }
}


