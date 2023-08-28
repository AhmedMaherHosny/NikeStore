package com.example.core.enums

enum class OrderStatus {
    ACCEPTED, REJECTED, SHIPPED, CANCELED, DONE, PENDING;

    companion object {
        fun fromSerializedForm(value: String): OrderStatus {
            return when (value) {
                "ACCEPTED" -> ACCEPTED
                "REJECTED" -> REJECTED
                "SHIPPED" -> SHIPPED
                "CANCELED" -> CANCELED
                "DONE" -> DONE
                "PENDING" -> PENDING
                else -> throw IllegalArgumentException("Unknown sex: $value")
            }
        }
    }
}