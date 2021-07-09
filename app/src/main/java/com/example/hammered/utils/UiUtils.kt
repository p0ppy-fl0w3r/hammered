package com.example.hammered.utils

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object UiUtils {

    fun hideKeyboard(context: Context, view: View) {
        val inputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun animateError(view: View) {
        val animation = ObjectAnimator.ofInt(
            view,
            "backgroundColor",
            0x00000000,
            0x55ff0000,
            0x00000000
        )

        animation.duration = 500
        animation.repeatMode = ObjectAnimator.REVERSE
        animation.repeatCount = 1
        animation.setEvaluator(ArgbEvaluator())
        animation.start()
    }

}