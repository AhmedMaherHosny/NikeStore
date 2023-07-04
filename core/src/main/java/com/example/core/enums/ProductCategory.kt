package com.example.core.enums

enum class ProductCategory {
    SHOE {
        override fun toString() = "Shoe"
    },
    SLIPPER {
        override fun toString() = "Slipper"
    }
}