package com.omsk.railwaymuseum.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun hideKeyboard(view: View) {
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun endIconShow(textInputLayout: TextInputLayout,
                textInputEditText: TextInputEditText
) {
    textInputLayout.addOnEditTextAttachedListener {inputLayout ->
        inputLayout.isEndIconVisible = !textInputEditText.text.isNullOrEmpty()
        textInputEditText.addTextChangedListener {editText ->
            inputLayout.isEndIconVisible = !editText.isNullOrEmpty()
        }
    }
}

fun viewFocus(view: View) {
    view.isFocusable = true
    view.isFocusableInTouchMode = true
    view.requestFocus()
}

fun emptinessCheck(textView: TextView, message: String): Boolean {
    if(!textView.text.isNullOrEmpty()) {
        return false
    }

    Toast.makeText(textView.context, message, Toast.LENGTH_SHORT).show()
    textView.requestFocus()
    return true
}