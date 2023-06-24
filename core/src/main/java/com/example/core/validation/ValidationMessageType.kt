package com.example.core.validation

import androidx.core.util.PatternsCompat

sealed class ValidationMessageType(
    val validationType: ValidationType? = null,
    val invalidReason: InvalidReason? = null
) {
    data class Invalid(val type: ValidationType?, val reason: InvalidReason) :
        ValidationMessageType(type, reason)

    object Valid : ValidationMessageType()
}

object Validation {

    fun String.validateUsername(): ValidationMessageType {
        val trimmed = this.trim()
        if (trimmed.isEmpty())
            return ValidationMessageType.Invalid(ValidationType.USERNAME, InvalidReason.EMPTY)
        if (trimmed.length < 6)
            return ValidationMessageType.Invalid(ValidationType.USERNAME, InvalidReason.LENGTH)
        return ValidationMessageType.Valid
    }

    fun String.validateEmail(): ValidationMessageType {
        val trimmed = this.trim()
        if (trimmed.isEmpty())
            return ValidationMessageType.Invalid(ValidationType.EMAIL, InvalidReason.EMPTY)
        if (PatternsCompat.EMAIL_ADDRESS.matcher(trimmed).matches().not())
            return ValidationMessageType.Invalid(ValidationType.EMAIL, InvalidReason.INVALID_EMAIL)
        return ValidationMessageType.Valid
    }

    fun String.validatePassword(isLogin: Boolean): ValidationMessageType {
        val trimmed = this.trim()
        if (isLogin) {
            return if (trimmed.isEmpty())
                ValidationMessageType.Invalid(ValidationType.PASSWORD, InvalidReason.EMPTY)
            else
                ValidationMessageType.Valid
        }
        if (trimmed.isEmpty())
            return ValidationMessageType.Invalid(ValidationType.PASSWORD, InvalidReason.EMPTY)
        if (trimmed.length < 8)
            return ValidationMessageType.Invalid(ValidationType.PASSWORD, InvalidReason.LENGTH)
        if (!trimmed.any { it.isUpperCase() })
            return ValidationMessageType.Invalid(ValidationType.PASSWORD, InvalidReason.UPPERCASE)
        if (!trimmed.any { it.isLowerCase() })
            return ValidationMessageType.Invalid(ValidationType.PASSWORD, InvalidReason.LOWERCASE)
        if (!trimmed.any { it.isDigit() })
            return ValidationMessageType.Invalid(ValidationType.PASSWORD, InvalidReason.DIGIT)

        return ValidationMessageType.Valid
    }

    fun String.validatePhone(): ValidationMessageType {
        val trimmed = this.trim()
        if (trimmed.isEmpty())
            return ValidationMessageType.Invalid(ValidationType.PHONE, InvalidReason.EMPTY)
        if (trimmed.first() != '+')
            return ValidationMessageType.Invalid(ValidationType.PHONE, InvalidReason.INVALID_PHONE)
        return ValidationMessageType.Valid
    }

}