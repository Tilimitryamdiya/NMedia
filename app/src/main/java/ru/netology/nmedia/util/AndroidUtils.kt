package ru.netology.nmedia.util

import java.math.RoundingMode
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object AndroidUtils {

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


    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}