package com.fl0w3r.hammered.utils

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Base64.encodeToString
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.animation.doOnEnd
import java.io.ByteArrayOutputStream


object UiUtils {

    fun hideKeyboard(context: Context, view: View) {
        val inputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun animateError(view: View) {

        val background = view.background

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
        animation.doOnEnd {
            view.background = background
        }
    }

    fun encodeToBase64(bitmap: Bitmap): String {

        val byteStream = ByteArrayOutputStream()
        // Any image format will be converted to jpeg
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStream)

        return "data:image/jpeg;base64,${encodeToString(byteStream.toByteArray(), Base64.DEFAULT)}"
    }

}