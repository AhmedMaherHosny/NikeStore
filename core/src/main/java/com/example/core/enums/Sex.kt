package com.example.core.enums

enum class Sex {
    MEN {
        override fun toString() = "Men"
    },
    WOMEN {
        override fun toString() = "Women"
    },
    KIDS {
        override fun toString() = "Kids"
    };

    companion object {
        fun fromSerializedForm(value: String): Sex {
            return when (value) {
                "MEN" -> MEN
                "WOMEN" -> WOMEN
                "KIDS" -> KIDS
                else -> throw IllegalArgumentException("Unknown sex: $value")
            }
        }
    }
}
