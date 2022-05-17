package com.example.tmg_test.utils

import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.doOnTextChange(listener: (newText: String) -> Unit) {
    this.editText?.doOnTextChanged { text, start, before, count -> listener(text.toString()) }
}

fun TextInputLayout.getText() : String {
    return this.editText?.text.toString()
}

fun TextInputLayout.setText(text: String?) {
    this.editText?.setText(text)
}