package com.example.stolker.ui.utils

fun Float.percentageDifferenceWith(other: Float): Pair<String, String> {
    return when {
        this < other -> {
            val diff = String.format("%.2f%%", (other-this)/this*100)
            ("-" to diff)
        }
        else -> {
            val diff = String.format("%.2f%%", (this-other)/this*100)
            ("+" to diff)
        }
    }
}

