package com.example.core.validation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.example.core.R
import com.example.core.validation.Validation.validateEmail
import com.example.core.validation.Validation.validateNone
import com.example.core.validation.Validation.validatePassword
import com.example.core.validation.Validation.validatePhone
import com.example.core.validation.Validation.validateUsername

data class InputWrapper(
    var text: MutableState<String> = mutableStateOf(""),
    var isValid: MutableState<Boolean> = mutableStateOf(false),
    var borderColor: Color = Color.Unspecified,
    val validationType: ValidationType? = ValidationType.NONE,
    val isLogin: Boolean = false
) {
    var validationMessageResId: Int = R.string.empty_string
    fun onValueChange(input: String) {
        text.value = input
        validationMessageResId = when (validationType) {
            ValidationType.USERNAME -> input.validateUsername().toMessageRes()
            ValidationType.EMAIL -> input.validateEmail().toMessageRes()
            ValidationType.PASSWORD -> input.validatePassword(isLogin).toMessageRes()
            ValidationType.NONE -> input.validateNone().toMessageRes()
            else -> input.validatePhone().toMessageRes()
        }
        borderColor = if (isValid.value) Color.Unspecified else Color.Red
        isValid.value = validationMessageResId == R.string.empty_string && text.value.isNotEmpty()
    }
}

private fun ValidationMessageType.toMessageRes(): Int {
    return when (this) {
        is ValidationMessageType.Invalid -> {
            when (this.reason) {
                InvalidReason.EMPTY -> {
                    when (this.validationType) {
                        ValidationType.USERNAME -> R.string.empty_username
                        ValidationType.EMAIL -> R.string.empty_email
                        ValidationType.PASSWORD -> R.string.empty_password
                        else -> R.string.empty_phone
                    }
                }

                InvalidReason.LENGTH -> {
                    when (this.validationType) {
                        ValidationType.USERNAME -> R.string.username_must_be_at_least_6
                        else -> R.string.password_must_be_at_least_8
                    }
                }

                InvalidReason.UPPERCASE -> R.string.password_upper
                InvalidReason.LOWERCASE -> R.string.password_lower
                InvalidReason.DIGIT -> R.string.password_digit
                InvalidReason.INVALID_EMAIL -> R.string.invalid_email
                InvalidReason.INVALID_PHONE ->R.string.invalid_phone
            }
        }

        ValidationMessageType.Valid -> R.string.empty_string
    }
}






