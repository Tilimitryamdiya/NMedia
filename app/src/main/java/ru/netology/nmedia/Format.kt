package ru.netology.nmedia

import java.math.RoundingMode

object Format {

    fun getFormattedNumber(count: Int): String {
        val formatter = java.text.DecimalFormat("###.#")
        formatter.roundingMode = RoundingMode.DOWN

        return when (count) {
            in 0..999 -> count.toString()
            in 1_000..9_999 -> formatter.format(count / 1000.0) + "K"
            in 10_000..999_999 -> (count / 1000).toString() + "K"
            else -> formatter.format(count / 1_000_000.0) + "M"
        }
    }
}