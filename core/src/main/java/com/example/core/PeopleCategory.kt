package com.example.core

enum class PeopleCategory {
    MEN {
        override fun toString() = "Men"
    },
    WOMEN {
        override fun toString() = "Women"
    },
    KIDS {
        override fun toString() = "Kids"
    }
}
