package com.example.core.enums

enum class ProductCategory {
    SHOE {
        override fun toString() = "Shoe"
    },
    SLIPPER {
        override fun toString() = "Slipper"
    };

    companion object {
        fun fromSerializedForm(value: String): ProductCategory {
            return when (value) {
                "SHOE" -> SHOE
                "SLIPPER" -> SLIPPER
                // ... handle other cases ...
                else -> throw IllegalArgumentException("Unknown product category: $value")
            }
        }
    }
}