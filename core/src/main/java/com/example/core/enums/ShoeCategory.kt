package com.example.core.enums

enum class ShoeCategory {
    JORDAN {
        override fun toString() = "Jordan"
    },
    RUNNING {
        override fun toString() = "Running"
    },
    FOOTBALL {
        override fun toString() = "Football"
    },
    TRAINING_AND_GYM {
        override fun toString() = "Training and gym"
    }
}